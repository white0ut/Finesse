package edu.wmich.gic.finesse;

import java.io.*;
import java.util.HashMap;
import jxna.audio.*;
import jxna.audio.modules.*;



public class AudioManager{
	public static volatile double musVol=.5,sfxVol=.7;
	
	private static final String dir="res"+File.separator+"audio";
	private static final String path=dir+File.separator;
	
	private static volatile boolean musicSync=true;
	
	private static DSP main;
	private static SoundMixer sfxMixer;
	
	private static HashMap<String,SynthScript> sfxmap=new HashMap<String,SynthScript>();
	private static Organya[] musList;
	private static String[] musNameList;
	private static int music=-1;
	
	private static int sampleRate;
	private static double bufferLen;
	
	public static boolean setParams(int sampleRate,double bufferLen){
		AudioManager.sampleRate=sampleRate;
		AudioManager.bufferLen=bufferLen;
		
		if(main==null){
			//configure signal flow
			sfxMixer=new SoundMixer();
			main=new DSP(2){
				private Port sfxPort=sfxMixer.new Port(),musPort;
				@Override
				protected void advance(double smpSecondsElapsed,double seqSecondsElapsed){
					double[] sfx=sfxPort.getSignal(smpSecondsElapsed,seqSecondsElapsed);
					output[0]=sfx[0]*sfxVol;
					output[1]=sfx[1]*sfxVol;
					synchronized(AudioManager.class){
						if(!musicSync){
							Organya org=musList==null || music<0? null:musList[music];
							musPort=org==null? null:org.getSound(true,true).new Port();
							musicSync=true;
						}
					}
					if(musPort!=null){
						double[] mus=musPort.getSignal(smpSecondsElapsed,seqSecondsElapsed);
						output[0]+=mus[0]*musVol;
						output[1]+=mus[1]*musVol;
					}
				}
			};
		}
		
		if(musList==null){
			//load music
			boolean go=true;
			try{Organya.loadSamples(new BufferedInputStream(new FileInputStream(path+"org instruments")));}
			catch(IOException e){go=false;}
			if(go){
				musList=new Organya[32];
				musNameList=new String[32];
				for(File f:new File(dir).listFiles()){
					String s=f.getName();
					if(s.endsWith(".org")){
						char c1=s.charAt(0),c2=s.charAt(1);
						if(c1>='0' && c1<='9' && c2>='0' && c2<='9'){
							int id=(10*(c1-'0')+c2-'0');
							if(id<32){
								try{
									musList[id]=new Organya(new FileInputStream(f));
									musNameList[id]=s.substring(0,s.length()-4);
								}catch(IOException e){}
							}
						}
					}
				}
			}
		}
		
		try{AudioSystem.start(main,sampleRate,bufferLen);}
		catch(Exception e){return false;}
		return true;
	}
	
	public static int getSampleRate(){return sampleRate;}
	public static double getBufferLen(){return bufferLen;}
	
	public static String getMusName(int mus){return musNameList[mus];}
	
	public static synchronized void playMus(int mus){
		if(mus!=music){
			music=mus;
			musicSync=false;
		}
	}
	
	public static void playSfx(String sfx,final double... params){
		//System.out.println(sfx);
		try{
			boolean cached=sfxmap.containsKey(sfx);
			SynthScript toPlay=cached? sfxmap.get(sfx):SynthScript.load(new File(path+sfx+".txt"));
			if(!cached) sfxmap.put(sfx,toPlay);
			String[] in=new String[params.length];
			for(int i=0;i<params.length;i++) in[i]="in"+i;
			sfxMixer.add(
				toPlay.new Note(
					new DSP(params.length){public void advance(double x,double y){
						for(int i=0;i<params.length;i++) output[i]=params[i];
					}},
					in,
					new String[]{"out"}
				)
			);
		}catch(IOException e){System.err.println("SFX error: "+e.getMessage());}
	}
}
