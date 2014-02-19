package jxna.audio.modules;

import java.io.IOException;
import java.util.LinkedList;
import jxna.audio.modules.SynthScript.Envelope;
import jxna.audio.modules.SynthScript.ParserState;



class SSParser{
	static ParserState parse(String[] lines) throws IOException{
		ParserState state=new ParserState();
		int line=0;
		for(String s:lines){
			line++;
			
			//parse the line into tokens
			LinkedList<String> tokens=new LinkedList<String>();
			for(int i=0;i<s.length();i++){
				char ch=s.charAt(i);
				if(ch>32 && ch<127){
					if(ch=='/' && tokens.isEmpty()){
						//comment
						if(i+1==s.length() || s.charAt(i+1)!='/') throwMe("syntax error",line,s);
						i=s.length();
					}else if(isVar(ch)){
						//identifier
						StringBuilder sb=new StringBuilder();
						sb.append(ch);
						i++;
						if(ch=='#'){
							if(i==s.length()) throwMe("syntax error",line,s);
							ch=s.charAt(i++);
							if(!isAlpha(ch)) throwMe("syntax error",line,s);
							sb.append(ch);
						}
						while(i<s.length() && isAlphaNum(ch=s.charAt(i))){
							sb.append(ch);
							i++;
						}
						i--;
						tokens.add(sb.toString());
					}else if(isDecimal(ch)){
						//number
						boolean pt=false;
						StringBuilder sb=new StringBuilder();
						while(i<s.length() && isDecimal(ch=s.charAt(i))){
							if(ch=='.'){
								if(pt) throwMe("syntax error",line,s);
								pt=true;
							}
							sb.append(ch);
							i++;
						}
						i--;
						tokens.add(sb.toString());
					}else{
						//symbol
						tokens.add(""+ch);
					}
				}
			}
			
			if(!tokens.isEmpty()){
				String[] tkn=tokens.toArray(new String[tokens.size()]);
				String asgn=tkn.length<3? "":tkn[1];
				boolean fm=asgn.equals("~");
				if(!fm && !asgn.equals("=")) throwMe("syntax error",line,s);
				
				String var=tkn[0];
				char ch=var.charAt(0);
				if(!isVar(ch)) throwMe(var+" is not a valid identifier",line,s);
				
				int[] expr=parseExpr(tkn,2,tkn.length,state,line,s,fm? null:var);
				if(fm){
					if(!state.fm.containsKey(var)) throwMe("no oscillator named "+var,line,s);
					int[] fmnode=state.fm.get(var);
					state.addCode(0,true,fmnode,expr,fmnode);
				}else{
					if(state.vars.containsKey(var)) throwMe("duplicate variable "+var,line,s);
					state.vars.put(var,expr);
					if(ch=='#') state.ostr.put(var,state.ostr.size());
				}
			}
		}
		return state;
	}
	
	private static void throwMe(String msg,int lnum,String line) throws IOException{
		throw new IOException(msg+" on line "+lnum+": "+line);
	}
	
	private static boolean isAlpha(char ch){return ch=='_' || ch>='a' && ch<='z' || ch>='A' && ch<='Z';}
	private static boolean isVar(char ch){return isAlpha(ch) || ch=='#';}
	private static boolean isNum(char ch){return ch>='0' && ch<='9';}
	private static boolean isDecimal(char ch){return isNum(ch) || ch=='.';}
	private static boolean isAlphaNum(char ch){return isAlpha(ch) || isNum(ch);}
	
	private static int[] getConst(String s,ParserState state){
		Integer i=state.nstr.get(s);
		if(i!=null) return new int[]{2,i};
		int ii=state.nstr.size();
		state.nstr.put(s,ii);
		return new int[]{2,ii};
	}
	
	private static int[] parseToken(String s,ParserState state,int lnum,String line) throws IOException{
		char ch=s.charAt(0);
		if(isDecimal(ch)){
			//number
			return getConst(s,state);
		}
		
		if(!isVar(ch)) throwMe(s+" is not an identifier or number",lnum,line);
		
		//variable
		int[] ans=state.vars.get(s);
		if(ans!=null) return ans;
		if(ch!='#') throwMe("undefined variable "+s,lnum,line);
		ans=new int[]{0,state.istr.size()};
		state.istr.put(s,ans[1]);
		state.vars.put(s,ans);
		return ans;
	}
	
	private static double readNum(String[] tokens,int[] st,int end,boolean allowNeg,int lnum,String line)
	throws IOException{
		if(st[0]==end) throwMe("syntax error",lnum,line);
		String s=tokens[st[0]++];
		boolean neg=s.equals("-");
		if(neg){
			if(!allowNeg) throwMe("envelope durations must be positive",lnum,line);
			if(st[0]==end) throwMe("syntax error",lnum,line);
			s=tokens[st[0]++];
		}
		if(!isDecimal(s.charAt(0))) throwMe("syntax error",lnum,line);
		return Double.parseDouble(s)*(neg? -1:1);
	}
	
	private static Envelope
	parseEnvelope(String[] tokens,int start,int end,int lnum,String line) throws IOException{
		double[][] segs; //segs[seg][0=start, 1=end, 2=duration]
		int mainSegs=0;
		double mainEnd,minRel=0,maxRel=0,relTarget=0;
		boolean jumpOnRelease=false;
		
		int[] st={start};
		LinkedList<double[]> seglist=new LinkedList<double[]>();
		boolean release=false;
		mainEnd=readNum(tokens,st,end,true,lnum,line);
		while(st[0]<end){
			if(jumpOnRelease) throwMe("syntax error",lnum,line);
			
			//get the y-value of the previous point
			double prev=release? seglist.getLast()[1]:mainEnd;
			
			//check for a colon (indicating the release portion of the envelope)
			String s=tokens[st[0]++];
			if(jumpOnRelease=s.equals(":")){
				if(release) throwMe("syntax error",lnum,line);
				release=true;
			}else if(!s.equals(",")) throwMe("syntax error",lnum,line);
			
			//if we're not yet in the release portion, increment the main segment count
			if(!release) mainSegs++;
			
			//read the duration of this segment
			double dur=readNum(tokens,st,end,false,lnum,line);
			if(st[0]==end) throwMe("syntax error",lnum,line);
			s=tokens[st[0]++];
			if(s.equals(",")){
				//read the next y-value
				jumpOnRelease=false;
				double next=readNum(tokens,st,end,true,lnum,line);
				if(!release) mainEnd=next;
				seglist.add(new double[]{prev,next,dur});
			}else if(s.equals("-") && jumpOnRelease){
				//read jump-on-release information
				minRel=dur;
				maxRel=readNum(tokens,st,end,false,lnum,line);
				if(st[0]==end) throwMe("syntax error",lnum,line);
				if(!tokens[st[0]++].equals(",")) throwMe("syntax error",lnum,line);
				relTarget=readNum(tokens,st,end,true,lnum,line);
			}else throwMe("syntax error",lnum,line);
		}
		segs=seglist.toArray(new double[seglist.size()][]);
		
		return new Envelope(segs,mainSegs,mainEnd,minRel,maxRel,relTarget,jumpOnRelease);
	}
	
	private static int[]
	parseAtom(String[] tokens,int[] start,int end,ParserState state,int lnum,String line,String varname)
	throws IOException{
		String s=tokens[start[0]++];
		if(s.equals("[")){
			//envelope
			int st=start[0];
			while(start[0]<end && !tokens[start[0]].equals("]")) start[0]++;
			if(!tokens[start[0]].equals("]")) throwMe("unmatched open bracket",lnum,line);
			Envelope e=parseEnvelope(tokens,st,start[0],lnum,line);
			start[0]++;
			int[] ans={1,state.envs.size()};
			state.envs.add(e);
			return ans;
		}
		
		boolean func=start[0]<end && tokens[start[0]].equals("(");
		boolean paren=func || s.equals("(");
		if(!paren) return parseToken(s,state,lnum,line);
		
		int args=0,rawfunc=10;
		boolean high=false;
		if(func){
			//determine which function is being called
			start[0]++;
			if(s.equals("noise")) args=1;
			else if(s.equals("sinpi")){args=1;rawfunc=4;}
			else if(s.equals("freq")){args=1;rawfunc=5;}
			else if(s.equals("cut")) args=2;
			else if(s.equals("svf")) args=6;
			else if(s.length()==3 && s.endsWith("fo")){
				char ch=s.charAt(0);
				high=ch=='h';
				if(high || ch=='l') args=4;
			}
			if(args==0) throwMe(s+" is not a valid function name",lnum,line);
		}
		
		//find the matching close paren
		int depth=0;
		LinkedList<int[]> arglist=null;
		if(func) arglist=new LinkedList<int[]>();
		int x=start[0];
		while(start[0]<end && depth>=0){
			String ss=tokens[start[0]++];
			if(ss.equals("(") || ss.equals("[")) depth++;
			if(depth==0 && ss.equals(",")){
				if(!func) throwMe("syntax error",lnum,line);
				arglist.add(parseExpr(tokens,x,start[0]-1,state,lnum,line,null));
				x=start[0];
			}
			if(ss.equals(")") || ss.equals("]")) depth--;
			if(depth<0 && ss.equals("]")) throwMe("syntax error",lnum,line);
		}
		if(depth>=0) throwMe("unmatched open paren",lnum,line);
		int[] last=parseExpr(tokens,x,start[0]-1,state,lnum,line,null);
		if(!func) return last;
		arglist.add(last);
		if(arglist.size()!=args)
			throwMe(args+" args expected for function "+s+", "+arglist.size()+" given",lnum,line);
		
		//apply the function
		if(args==1){
			//noise
			if(rawfunc==10) return state.addCode(10,arglist.get(0),new int[]{-1,state.noiseGens++});
			
			//sin, exp
			else return state.addCode(rawfunc,arglist.get(0));
		}else if(args==2){
			//cut
			int[] signal=arglist.get(0),cut=arglist.get(1);
			return state.addCode(11,signal,cut);
		}else if(args==4){
			//oscillator
			int[] fm=state.getDummy();
			if(varname!=null) state.fm.put(varname,fm);
			int[] freq=state.addCode(12,arglist.get(0),fm);
			int[] osc=state.addCode(6,freq,new int[]{-1,high? 1:0});
			int[] out=state.getDummy();
			for(int i=1;i<4;i++) state.addCode(i+6,true,i>1? out:osc,arglist.get(i),out);
			return out;
		}else{
			//svf
			int[] one=getConst("1",state),two=getConst("2",state);
			int[] f=state.addCode(2,arglist.get(1),new int[]{-1,1}),q=state.addCode(1,one,arglist.get(2));
			state.addCode(4,true,f,f);
			state.addCode(2,true,f,two,f);
			state.addCode(2,true,q,two,q);
			int[] lo=state.getDummy();
			int[] ba=state.getDummy();
			int[] hi=state.getDummy();
			
			//lowpass output
			int[] fba=state.addCode(2,f,ba);
			state.addCode(0,true,lo,fba,lo);
			state.addCode(11,true,lo,two,lo);
			
			//highpass output
			int[] qba=state.addCode(2,q,ba);
			state.addCode(0,true,qba,lo,qba);
			state.addCode(1,true,arglist.get(0),qba,hi);
			state.addCode(11,true,hi,two,hi);
			
			//bandpass output
			int[] fhi=state.addCode(2,f,hi);
			state.addCode(0,true,ba,fhi,ba);
			state.addCode(11,true,ba,two,ba);
			
			//mix
			int[][] sources={lo,ba,hi};
			int[] temp=state.getDummy();
			int[] out=state.getDummy();
			for(int i=0;i<3;i++){
				state.addCode(2,true,sources[i],arglist.get(i+3),i>0? temp:out);
				if(i>0) state.addCode(0,true,out,temp,out);
			}
			return out;
		}
	}
	
	private static int[] parseExpr
	(String[] tokens,int start,int end,ParserState state,int lnum,String line,String varname) throws IOException{
		int[] first=null,second=null;
		int op1=0,op2=0;
		while(start<end){
			//get the next atom
			int[] st={start};
			int[] next=parseAtom(tokens,st,end,state,lnum,line,varname);
			varname=null;
			start=st[0];
			
			//consolidate it as much as possible with the buffered atoms and operators
			if(first==null) first=next;
			else if(second==null){
				if(op1>1) first=state.addCode(op1,first,next);
				else second=next;
			}else{
				if(op2<2){
					first=state.addCode(op1,first,second);
					op1=op2;
					second=next;
				}else second=state.addCode(op2,second,next);
			}
			
			//add the next operator
			if(start<end){
				String op=tokens[start++];
				if(start==end) throwMe("syntax error",lnum,line);
				int opi=-1;
				if(op.equals("+")) opi=0;
				if(op.equals("-")) opi=1;
				if(op.equals("*")) opi=2;
				if(op.equals("/")) opi=3;
				if(opi<0) throwMe(op+" is not a valid mathematical operator",lnum,line);
				if(second==null) op1=opi;
				else op2=opi;
			}
		}
		if(first==null) throwMe("syntax error",lnum,line);
		return second==null? first:state.addCode(op1,first,second);
	}
}
