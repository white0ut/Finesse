package edu.wmich.gic.finesse;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.drawable.GameGrid;

@SuppressWarnings("unused")
public class Tile {
	public int x = 0;
	public int y = 0;
	public int row = 0;
	public int col = 0;
	public boolean start = false;
	public boolean end = false;
	public boolean searched = false;
	public boolean walkable = true;
	public boolean path = false;
	public int f = 0;
	public int g = 0;
	public int dist = 0;
	public Tile parent = null;
	public Tile child = null;
	public Minion minion = null;
	
	public Tile(int _row, int _col){
		int rand = (int) Math.floor((Math.random()*4)+1);
		if(rand == 1){
			this.walkable = false;
		}
		row = _row;
		col = _col;
		x = GameGrid.gridLeftOffset+col*(GameGrid.colWidth+GameGrid.gridSpacing);
		y = GameGrid.gridTopOffset+row*(GameGrid.rowHeight+GameGrid.gridSpacing);
	}
	
	public String toString(){
		return "Row:"+row+"  Column:"+col+"  Start:"+start+"  End:"+end+"  Searched:"+searched+"  Walkable:"+walkable;//+"  \nParent:"+parent+"\nChild:"+child+"\n";
	}
	
	public void resetTile(){
		this.start = false;
		this.end = false;
		this.searched = false;
		this.path = false;
		this.f = 0;
		this.g = 0;
		this.dist = 0;
		this.parent = null;
		this.child = null;
//		this.minion = null;
	}
	
	public void render(Graphics g){
		g.setColor(Color.blue);
		if(this.start == true){
    		g.setColor(Color.green);
		}else{
			if(this.end == true){
				g.setColor(Color.red);
			}
			else{
				if(this.searched == true){
					g.setColor(Color.magenta);
				}
				else{
					if(this.walkable == true){
						g.setColor(Color.white);
					}
					else{
						g.setColor(Color.gray);
					}
				}
			}
		}
    	if(this.path == true){
    		g.setColor(Color.cyan);
		}
//    	if(this.minion != null){
//    		g.drawImage(this.minion.image, this.x-5, this.y-5);
//    	}
//    	else{
    		g.fillRect(this.x, this.y, GameGrid.colWidth, GameGrid.rowHeight);
//    	}
    		if(this.minion != null){
        		g.drawImage(this.minion.image, this.x-5, this.y-5);
        	}
//			g.drawString(String.valueOf(this.f),this.x+1,this.y-2);
//			g.drawString(String.valueOf(this.g),this.x+1,this.y+11);
//			g.drawString(String.valueOf(this.dist),this.x+1,this.y+24);
	}
}

