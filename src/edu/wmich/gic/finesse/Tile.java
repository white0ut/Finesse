package edu.wmich.gic.finesse;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;

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
	public boolean furthest = false;
	public int f = 0;
	public int g = 0;
	public int dist = 0;
	public Tile parent = null;
	public Tile child = null;
	public Minion minion = null;
	public SpriteSheet sprites;
	public Image img;

	public Tile(int _row, int _col) throws SlickException{
		sprites = new SpriteSheet(new Image("res/images/tiles.png"),17,17);
		img = sprites.getSprite(1, 0).getSubImage(1, 1, 17, 17).getScaledCopy(1.6f);
		int rand = (int) Math.floor((Math.random()*10)+1);
		if(rand == 1){
			walkable = false;
		}
		row = _row;
		col = _col;
		//TODO: Offset the grid to an edge of the screen, leaving room for buttons
		x = GameGrid.gridLeftOffset+col*(GameGrid.colWidth+GameGrid.gridSpacing);
		y = GameGrid.gridTopOffset+row*(GameGrid.rowHeight+GameGrid.gridSpacing);
	}

	public String toString(){
		return "Row:"+row+"  Column:"+col+"  Start:"+start+"  End:"+end+"  Searched:"+searched+"  Walkable:"+walkable;//+"  \nParent:"+parent+"\nChild:"+child+"\n";
	}

	public void resetTile(boolean resetFurthest){
		if(resetFurthest){
			furthest = false;
		}
		start = false;
		end = false;
		searched = false;
//		path = false;
		f = 0;
		g = 0;
		dist = 0;
		parent = null;
		child = null;
		// minion = null;
	}

	public void render(Graphics g){
		if(minion != null){
			//g.setColor(Color.white);
			g.fillRect(x, y, GameGrid.colWidth, GameGrid.rowHeight);
			g.drawImage(minion.image, x-5, y-5);
			return;
		}
		if(!walkable){
			g.drawImage(img, x, y);
			return;
		}
		g.setColor(Color.blue);
		if(start)
			g.setColor(Color.green);
			else if(path || furthest)
					g.setColor(Color.cyan);
				else if(walkable)
						g.setColor(Color.white);
					else{
	//					if(searched == true){
	//						g.setColor(Color.magenta);
	//					}
	//					else{
	//						g.setColor(Color.gray);
	//					}
					}
		
		if(end == true){
			g.setColor(Color.red);
		}
		//    	if(minion != null){
		//    		g.drawImage(minion.image, x-5, y-5);
		//    	}
		//    	else{
		g.fillRect(x, y, GameGrid.colWidth, GameGrid.rowHeight);
		if(furthest){
			g.setColor(Color.red);
			g.drawRoundRect((float)x+5, (float)y+5, (float)GameGrid.colWidth-10, (float)GameGrid.rowHeight-10,1);
		}
		//    	}


		//			g.drawString(String.valueOf(f),x+1,y-2);
		//			g.drawString(String.valueOf(g),x+1,y+11);
		//			g.drawString(String.valueOf(dist),x+1,y+24);
	}
}

