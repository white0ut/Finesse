package jxna.audio;

import java.util.LinkedList;



public abstract class DSP{
	protected final double[] output;
	private Port mainPort=null;
	private boolean playing=true;
	private LinkedList<Port> blackHole=new LinkedList<Port>();
	
	public DSP(int outChannels){output=new double[outChannels];}
	
	public final int getChannelCount(){return output.length;}
	public final void stop(){playing=false;}
	public final void blackHole(DSP dsp){blackHole.add(dsp.new Port());}
	
	protected abstract void advance(double smpSecondsElapsed,double seqSecondsElapsed);
	
	protected final class Port{
		public Port(){synchronized(DSP.this){if(mainPort==null) mainPort=this;}}
		
		public boolean isMain(){return this==mainPort;}
		
		public double[] getSignal(double smpSecondsElapsed,double seqSecondsElapsed){
			if(!playing) return null;
			if(isMain()){
				advance(smpSecondsElapsed,seqSecondsElapsed);
				for(Port port:blackHole) port.getSignal(smpSecondsElapsed,seqSecondsElapsed);
			}
			double[] ans=new double[output.length];
			for(int i=0;i<ans.length;i++) ans[i]=output[i];
			return ans;
		}
	}
}
