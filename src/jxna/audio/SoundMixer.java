package jxna.audio;



public class SoundMixer extends DSP{
	private Node head;
	private int childCount=0;
	private boolean terminate;
	
	public SoundMixer(int outChannels,boolean terminate,DSP... sounds){
		super(outChannels);
		this.terminate=terminate;
		removeAll();
		for(DSP sound:sounds) add(sound);
	}
	public SoundMixer(DSP... sounds){this(2,false,sounds);}
	public SoundMixer(int outChannels,DSP... sounds){this(outChannels,false,sounds);}
	public SoundMixer(boolean terminate,DSP... sounds){this(2,terminate,sounds);}
	
	public synchronized void removeAll(){
		head=new Node(null,null,null);
		head.next=new Node(head,null,null);
	}
	
	public synchronized void add(DSP sound){
		head.next=head.next.prev=new Node(head,head.next,sound.new Port());
		childCount++;
	}

	@Override
	public synchronized void advance(double smpSecondsElapsed,double seqSecondsElapsed){
		int chan=output.length;
		for(int i=0;i<chan;i++) output[i]=0;
		boolean stop=terminate;
		Node n=head.next;
		while(n.next!=null){
			double[] signal=n.sound.getSignal(smpSecondsElapsed,seqSecondsElapsed);
			if(signal==null){
				n.next.prev=n.prev;
				n=n.prev.next=n.next;
				childCount--;
			}else{
				stop=false;
				int j=0;
				for(int i=0;i<chan;i++){
					output[i]+=signal[j];
					if(++j==signal.length) j=0;
				}
				n=n.next;
			}
		}
		if(stop) stop();
	}
	
	public int getChildCount(){return childCount;}
	
	private class Node{
		public Node prev,next;
		public DSP.Port sound;
		public Node(Node prev,Node next,DSP.Port sound){this.prev=prev;this.next=next;this.sound=sound;}
	}
}
