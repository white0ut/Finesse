package jxna.audio.modules;

import java.io.*;
import java.util.*;
import jxna.audio.*;



public class SynthScript{
	private static double[] powmap,sinmap;
	
	private static double exp2(double x){
		int res=48;
		if(powmap==null){
			powmap=new double[res+1];
			for(int i=0;i<=res;i++) powmap[i]=Math.pow(2,(double)i/res);
		}
		if(x<0) return 1/exp2(-x);
		int squares=0;
		while(x>=1){x/=2;squares++;}
		x*=res;
		int y=(int)x;
		x-=y;
		x=powmap[y]+(powmap[y+1]-powmap[y])*x;
		for(int i=0;i<squares;i++) x*=x;
		return x;
	}
	
	private static double sinpi(double x){
		int res=48;
		if(sinmap==null){
			sinmap=new double[res+1];
			for(int i=0;i<=res;i++) sinmap[i]=Math.sin(Math.PI*i/res/2);
		}
		x-=2*Math.floor(x/2);
		int mul=1;
		if(x>=1){mul=-1;x-=1;}
		if(x>.5){x=1-x;}
		if(x>=.5) return mul;
		if(x<=0) return 0;
		x*=2*res;
		int y=(int)x;
		x-=y;
		return mul*(sinmap[y]+(sinmap[y+1]-sinmap[y])*x);
	}
	
	//call one of the two constructors depending on whether it's a text or binary file
	public static SynthScript load(File f) throws IOException{
		InputStream in=new FileInputStream(f);
		try{readLabel(in);}
		catch(IOException e){
			in.close();
			final Scanner scan=new Scanner(f);
			List<String> lines=new LinkedList<String>();
			while(scan.hasNextLine()) lines.add(scan.nextLine());
			scan.close();
			return new SynthScript(lines.toArray(new String[lines.size()]));
		}
		SynthScript ans=new SynthScript(in,false);
		in.close();
		return ans;
	}
	
	private static void readLabel(InputStream in) throws IOException{
		if(in.read()!='S') throw new IOException("not a SynthScript instrument file");
		if(in.read()!='S') throw new IOException("not a SynthScript instrument file");
		if(in.read()!='I') throw new IOException("not a SynthScript instrument file");
		if(in.read()!=0)   throw new IOException("not a SynthScript instrument file");
	}
	
	private String[] inputNames,outputNames;
	private HashMap<String,Integer> outputMap;
	private int[] outputs;
	private Envelope[] envs;
	private double[] consts;
	private int[][] codes;
	private int noiseGens,codeMem;
	
	//for debugging, to ensure correctness of the synth setup
	/*public void report(){
		System.out.println(inputNames.length+" inputs, "+envs.length+" envelopes");
		System.out.println("constants:");
		for(double x:consts) System.out.println("  "+x);
		System.out.println("codes:");
		String[] ops={"add","sub","mul","div","sin","exp","osc","pwm","xxx","yyy","nze","cut","fms"};
		for(int[] code:codes) System.out.println("  "+ops[code[0]]+" "+code[1]+" "+code[2]+" "+code[3]);
	}*/
	
	//parse a SynthScript text file
	public SynthScript(String[] lines) throws IOException{
		ParserState state=SSParser.parse(lines);
		
		noiseGens=state.noiseGens;
		codeMem=state.memCount;
		inputNames=toArr(state.istr);
		outputNames=toArr(outputMap=state.ostr);
		
		envs=state.envs.toArray(new Envelope[state.envs.size()]);
		
		String[] scons=toArr(state.nstr);
		consts=new double[scons.length];
		for(int i=0;i<consts.length;i++) consts[i]=Double.parseDouble(scons[i]);
		
		int[] sizes={inputNames.length,envs.length,consts.length};
		
		codes=new int[state.tcodes.size()][4];
		int code=0;
		for(TempCode tc:state.tcodes){
			codes[code][0]=tc.type;
			int i=1;
			for(int[] child:tc.children) codes[code][i++]=toAddr(child,sizes);
			if(tc.dest==null) codes[code][3]=codes[code][i-1];
			else codes[code][3]=toAddr(tc.dest,sizes);
			code++;
		}
		
		outputs=new int[outputNames.length];
		for(int i=0;i<outputs.length;i++) outputs[i]=toAddr(state.vars.get(outputNames[i]),sizes);
	}
	
	//load a compiled SynthScript file
	public SynthScript(InputStream in,boolean readLabel) throws IOException{
		if(readLabel) readLabel(in);
		
		//inputs
		inputNames=readNames(in);
		
		//outputs
		outputNames=readNames(in);
		outputMap=new HashMap<String,Integer>();
		for(int i=0;i<outputNames.length;i++) outputMap.put(outputNames[i],i);
		outputs=new int[outputNames.length];
		for(int i=0;i<outputs.length;i++) outputs[i]=(int)readInt(in,3);
		
		//envelopes
		envs=new Envelope[(int)readInt(in,1)];
		for(int i=0;i<envs.length;i++) envs[i]=new Envelope(in);
		
		//samples
		if(in.read()!=0) throw new IOException("Samples not yet supported");
		
		//constants
		consts=new double[(int)readInt(in,3)];
		for(int i=0;i<consts.length;i++) consts[i]=readDbl(in);
		
		//calculations
		codes=new int[(int)readInt(in,3)][4];
		codeMem=(int)readInt(in,3);
		noiseGens=0;
		for(int[] code:codes){
			for(int i=0;i<4;i++) code[i]=(int)readInt(in,i>0? 3:2);
			if(code[0]==10) noiseGens++;
		}
	}
	
	//save a compiled SynthScript file
	public void save(OutputStream out,boolean writeLabel) throws IOException{
		if(writeLabel){out.write('S');out.write('S');out.write('I');out.write(0);}
		
		//inputs
		writeNames(out,inputNames);
		
		//outputs
		writeNames(out,outputNames);
		for(int x:outputs) writeInt(out,x,3);
		
		//envelopes
		out.write(envs.length);
		for(Envelope env:envs) env.save(out);
		
		//samples
		out.write(0);
		
		//constants
		writeInt(out,consts.length,3);
		for(double x:consts) writeDbl(out,x);
		
		//calculations
		writeInt(out,codes.length,3);
		writeInt(out,codeMem,3);
		for(int[] code:codes) for(int i=0;i<4;i++) writeInt(out,code[i],i>0? 3:2);
	}
	
	private String[] readNames(InputStream in) throws IOException{
		String[] ans=new String[(int)readInt(in,1)];
		for(int i=0;i<ans.length;i++){
			StringBuilder sb=new StringBuilder("#");
			int len=(int)readInt(in,1);
			for(int j=0;j<len;j++) sb.append((char)readInt(in,1));
			ans[i]=sb.toString();
		}
		return ans;
	}
	
	private static double readDbl(InputStream in) throws IOException{
		return Double.longBitsToDouble(readInt(in,8));
	}
	
	private static long readInt(InputStream in,int bytes) throws IOException{
		long ans=0;
		for(int i=0;i<bytes;i++){
			ans<<=8;
			int x=in.read();
			if(x<0) throw new EOFException();
			ans|=x;
		}
		return ans;
	}
	
	private void writeNames(OutputStream out,String[] names) throws IOException{
		out.write(names.length);
		for(String s:names){
			out.write(s.length()-1);
			for(int i=1;i<s.length();i++) out.write(s.charAt(i));
		}
	}
	
	private static void writeDbl(OutputStream out,double x) throws IOException{
		writeInt(out,Double.doubleToLongBits(x),8);
	}
	
	private static void writeInt(OutputStream out,long x,int bytes) throws IOException{
		for(int i=bytes-1;i>=0;i--) out.write((int)(x>>i*8));
	}
	
	private int toAddr(int[] addr,int[] sizes){
		int ans=addr[1];
		if(addr[0]>=0) ans+=2;
		for(int i=0;i<addr[0];i++) ans+=sizes[i];
		return ans;
	}
	
	private String[] toArr(HashMap<String,Integer> map){
		String[] ans=new String[map.size()];
		for(Map.Entry<String,Integer> entry:map.entrySet()) ans[entry.getValue()]=entry.getKey();
		return ans;
	}
	
	public boolean terminates(){return envs.length>0;}
	
	private static class Noise{
		private static final Random rnd=new Random();
		private double l,r,t;
		public Noise(){l=t=0;setR();}
		
		public double getValue(double cycles){
			t+=Math.abs(cycles);
			while(t>=1){l=r;setR();t-=1;}
			return l+(r-l)*t;
		}
		
		private void setR(){r=rnd.nextDouble()*2-1;}
	}
	
	public class Note extends DSP{
		private Port ctrl;
		private int[] inputs,outputs;
		private Envelope.Tracker[] ets;
		private double[] mem;
		private Noise[] noise;
		
		public Note(DSP controller,String[] inputs,String[] outputs){
			super(outputs.length);
			ctrl=controller.new Port();
			
			//link inputs and outputs
			HashMap<String,Integer> inputMap=new HashMap<String,Integer>();
			for(int i=0;i<inputs.length;i++) inputMap.put("#"+inputs[i],i);
			this.inputs=link(inputMap,inputNames,"");
			this.outputs=link(outputMap,outputs,"#");
			for(int i=0;i<this.outputs.length;i++) this.outputs[i]=SynthScript.this.outputs[this.outputs[i]];
			
			//initialize envelopes
			ets=new Envelope.Tracker[envs.length];
			for(int i=0;i<ets.length;i++) ets[i]=envs[i].new Tracker();
			
			//initialize memory
			int s1=inputNames.length,s2=ets.length,s3=consts.length,s4=codeMem;
			mem=new double[2+s1+s2+s3+s4];
			for(int i=0;i<s3;i++) mem[2+s1+s2+i]=consts[i];
			
			//initialize noise generators
			noise=new Noise[noiseGens];
			for(int i=0;i<noiseGens;i++) noise[i]=new Noise();
		}
		
		private int[] link(HashMap<String,Integer> src,String[] dest,String destPrefix){
			int[] ans=new int[dest.length];
			for(int i=0;i<ans.length;i++){
				Integer ii=src.get(destPrefix+dest[i]);
				ans[i]=ii==null? -1:ii;
			}
			return ans;
		}
		
		public synchronized void release(){for(Envelope.Tracker track:ets) track.release();}

		@Override
		protected void advance(double smpSecondsElapsed,double seqSecondsElapsed){
			int s1=inputNames.length,s2=ets.length;
			mem[0]=seqSecondsElapsed;
			mem[1]=smpSecondsElapsed;
			
			//get inputs
			double[] signal=ctrl.getSignal(smpSecondsElapsed,seqSecondsElapsed);
			for(int i=0;i<s1;i++) mem[2+i]=inputs[i]<0? 0:signal[inputs[i]];
			
			//advance envelopes
			boolean done=terminates();
			synchronized(this){
				for(int i=0;i<s2;i++){
					mem[2+s1+i]=ets[i].getValue(seqSecondsElapsed);
					if(!ets[i].finished()) done=false;
				}
			}
			if(done) stop();
			
			//perform calculations
			for(int i=0;i<codes.length;i++){
				int type=codes[i][0],a=codes[i][1],b=codes[i][2],c=codes[i][3];
				if     (type==0) mem[c]=mem[a]+mem[b];
				else if(type==1) mem[c]=mem[a]-mem[b];
				else if(type==2) mem[c]=mem[a]*mem[b];
				else if(type==3) mem[c]=mem[a]/mem[b];
				else if(type==4) mem[c]=sinpi(mem[a]);
				else if(type==5) mem[c]=440*exp2(mem[a]/12);
				else if(type==6){
					mem[c]+=mem[a]*mem[b];
					mem[c]-=Math.floor(mem[c]);
				}else if(type==7) mem[c]=mem[a]<mem[b]? mem[a]/mem[b]:(mem[b]-mem[a])/(1-mem[b]);
				else if(type==8){
					boolean neg=mem[a]<0;
					double x=Math.abs(mem[a]),y=mem[b];
					mem[c]=(x<y? x/(2*y):.5+(x-y)/(2*(1-y)))*(neg? -1:1);
				}else if(type==9){
					boolean neg=mem[a]<0;
					double x=Math.abs(mem[a]),y=mem[b];
					x=x<.5? 2*x*y:2*(x-.5)*(1-y)+y;
					mem[c]=neg? 1-2*x:2*x-1;
				}else if(type==10) mem[c]=noise[b].getValue(smpSecondsElapsed*mem[a]);
				else if(type==11){
					double cut=Math.max(mem[b],0);
					mem[c]=Math.max(-cut,Math.min(cut,mem[a]));
				}else if(type==12){
					mem[c]=mem[a]*(mem[b]+1);
					mem[b]=0;
				}
				else throw new RuntimeException("SynthScript operation "+type+" not yet supported");
			}
			
			//set outputs
			for(int i=0;i<outputs.length;i++) output[i]=outputs[i]<0? 0:mem[outputs[i]];
		}
	}
	
	static class Envelope{
		private double[][] segs; //segs[seg][0=start, 1=end, 2=duration]
		private int mainSegs=0;
		private double mainEnd,minRel,maxRel,relTarget;
		private boolean jumpOnRelease=false;
		
		Envelope
		(double[][] segs,int mainSegs,double mainEnd,double minRel,double maxRel,double relTarget,boolean jor){
			this.segs=segs;
			this.mainSegs=mainSegs;
			this.mainEnd=mainEnd;
			this.minRel=minRel;
			this.maxRel=maxRel;
			this.relTarget=relTarget;
			jumpOnRelease=jor;
		}
		
		Envelope(InputStream in) throws IOException{
			mainSegs=(int)readInt(in,1);
			if(jumpOnRelease=mainSegs>=128) mainSegs-=128;
			int totalSegs=mainSegs;
			if(!jumpOnRelease) totalSegs+=(int)readInt(in,1);
			segs=new double[totalSegs][3];
			mainEnd=readDbl(in);
			for(int i=0;i<totalSegs;i++){
				segs[i][0]=i>0? segs[i-1][1]:mainEnd;
				segs[i][2]=readDbl(in);
				segs[i][1]=readDbl(in);
				if(i<mainSegs) mainEnd=segs[i][1];
			}
			if(jumpOnRelease){
				minRel=readDbl(in);
				maxRel=readDbl(in);
				relTarget=readDbl(in);
			}
		}
		
		public void save(OutputStream out) throws IOException{
			out.write(mainSegs+(jumpOnRelease? 128:0));
			if(!jumpOnRelease) out.write(segs.length-mainSegs);
			for(double[] seg:segs){writeDbl(out,seg[0]);writeDbl(out,seg[2]);}
			writeDbl(out,segs.length>0? segs[segs.length-1][1]:mainEnd);
			if(jumpOnRelease){
				writeDbl(out,minRel);
				writeDbl(out,maxRel);
				writeDbl(out,relTarget);
			}
		}
		
		class Tracker{
			private int seg=0;
			private double time=0,relFrom,relTime;
			private boolean released=false;
			
			public double getValue(double secondsElapsed){
				time+=secondsElapsed;
				if(jumpOnRelease && released){
					time=Math.min(time,relTime);
					return interp(relFrom,relTarget,time,relTime);
				}
				int segct=released? segs.length:mainSegs;
				double end=segct>0? segs[segct-1][1]:mainEnd;
				while(seg<segct && time>=segs[seg][2]) time-=segs[seg++][2];
				if(seg==segct){time=0;return end;}
				return interp();
			}
			
			public boolean finished(){return jumpOnRelease? released && time>=relTime:seg==segs.length;}
			
			public void release(){
				released=true;
				if(jumpOnRelease){
					time=0;
					relFrom=getValue();
					relTime=interp(minRel,maxRel,Math.abs(relFrom-relTarget),1);
					//System.out.println(relFrom);
					//System.out.println(relTime);
				}
			}
			
			private double getValue(){
				return seg<mainSegs? interp():mainEnd;
			}
			
			private double interp(){return interp(segs[seg][0],segs[seg][1],time,segs[seg][2]);}
			private double interp(double y1,double y2,double x,double max){return y1+(y2-y1)*x/max;}
		}
	}
	
	static class TempCode{
		public final int type;
		public final int[][] children;
		public final int[] dest;
		public TempCode(int type,int[] dest,int[]... children){
			this.type=type;
			this.dest=dest;
			this.children=children;
		}
	}
	
	static class ParserState{
		public final HashMap<String,Integer>
			nstr=new HashMap<String,Integer>(),
			istr=new HashMap<String,Integer>(),
			ostr=new HashMap<String,Integer>();
		public final HashMap<String,int[]> vars=new HashMap<String,int[]>(),fm=new HashMap<String,int[]>();
		public final LinkedList<Envelope> envs=new LinkedList<Envelope>();
		public final LinkedList<TempCode> tcodes=new LinkedList<TempCode>();
		public int noiseGens=0;
		private int memCount=0;
		
		public int[] addCode(int type,int[]... children){return addCode(type,false,children);}
		public int[] addCode(int type,boolean feedback,int[]... children){
			int[] ans=feedback? null:getDummy();
			tcodes.add(new TempCode(type,ans,children));
			return ans;
		}
		
		public int[] getDummy(){return new int[]{3,memCount++};}
	}
}
