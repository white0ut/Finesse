package jxna.audio;

import javax.sound.sampled.*;



public class AudioSystem{
	private static AudioFormat fmt;
	private static SourceDataLine line;
	private static DSP.Port sound;
	private static volatile boolean playing;
	private static volatile long bufferPos,playPos;
	
	public static void start(DSP sound,int sampleRate,double bufferLen) throws LineUnavailableException{
		playing=false;
		syncstart(sound,sampleRate,(int)(sampleRate*bufferLen+.5));
	}
	
	private synchronized static void syncstart(DSP sound,int sampleRate,int bufferLen)
	throws LineUnavailableException{
		fmt=new AudioFormat(sampleRate,16,2,true,false);
		AudioSystem.sound=sound.new Port();
		new Thread(new Runnable(){
			private int bufferLen,bufferPos;
			private double frameLen;
			
			public Runnable init(int bufferLen,double frameLen) throws LineUnavailableException{
				this.bufferLen=bufferLen;
				this.frameLen=frameLen;
				line=(SourceDataLine)
					javax.sound.sampled.AudioSystem.getLine(new DataLine.Info(SourceDataLine.class,fmt));
				line.open(fmt);
				playing=true;
				return this;
			}
			
			public void run(){
				synchronized(AudioSystem.class){
					byte[] buffer=new byte[(bufferLen/2)*4];
					line.start();
					while(playing){
						double[] s=AudioSystem.sound.getSignal(frameLen,frameLen);
						if(s==null) playing=false;
						else{
							buffer(buffer,bufferPos*4,s[0]);
							buffer(buffer,bufferPos*4+2,s[s.length>1? 1:0]);
							if(++bufferPos==bufferLen/2){
								line.write(buffer,0,4*bufferPos);
								bufferPos=0;
							}
							AudioSystem.bufferPos++;
							while((playPos=line.getLongFramePosition())+bufferLen<AudioSystem.bufferPos){
								Object lock=new Object();
								try{
									synchronized(lock){lock.wait(0,10000);}
								}catch(InterruptedException e){}
							}
						}
					}
					line.drain();
					line.close();
					playPos=AudioSystem.bufferPos=0;
					AudioSystem.sound=null;
				}
			}
			
			private void buffer(byte[] buffer,int index,double value){
				int v=(int)(value*32768);
				if(v<-32768) v=-32768;
				if(v>=32768) v=32767;
				if(v<0) v+=65536;
				buffer[index]=(byte)(v%256);
				buffer[index+1]=(byte)(v/256);
			}
		}.init(bufferLen,1.0/sampleRate)).start();
	}
	
	public static void stop(){playing=false;}
	public static boolean isPlaying(){return playing;}
	public static long getBufferPos(){return bufferPos;}
	public static long getPlayPos(){return playPos;}
}
