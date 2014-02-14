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
	public Image wall;
	public Image floor;

	public Tile(int _row, int _col) throws SlickException{
		sprites = new SpriteSheet(new Image("res/images/tiles.png"),16,16);
		wall = sprites.getSprite(1,0).getScaledCopy(GameGrid.colWidth, GameGrid.rowHeight);
		floor = sprites.getSprite(6,1).getScaledCopy(GameGrid.colWidth, GameGrid.rowHeight);
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
		//return "Row:"+row+"  Column:"+col+"  Start:"+start+"  End:"+end+"  Searched:"+searched+"  Walkable:"+walkable;//+"  \nParent:"+parent+"\nChild:"+child+"\n";
		return "row"+row+"  col:"+col+"  path:"+path+"  searched:"+searched+"  walkable:"+walkable+"  start:"+start+"  end:"+end;
	}

	public void resetTile(boolean resetFurthest){
		if(resetFurthest){
			furthest = false;
			path = false;
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
		if(!walkable){
			g.drawImage(wall, x, y);
			return;
		}
		g.drawImage(floor, x, y);
		if(minion != null){
			g.setColor(Color.white);
			g.fillRect(x, y, GameGrid.colWidth, GameGrid.rowHeight);
			if(minion.selected){
				g.drawImage(minion.selectedImage, x-5, y-5);
			}else{
				g.drawImage(minion.mainImage, x-5, y-5);
			}
			return;
		}
		g.setColor(Color.blue);
		if(start)
			g.setColor(Color.green);
			else if(path || furthest)
					g.setColor(Color.cyan);
				else if(walkable)
						g.setColor(Color.white);
		
		if(end == true){
			g.setColor(Color.red);
		}
		g.fillRect(x, y, GameGrid.colWidth, GameGrid.rowHeight);
//		if(furthest){
//			g.setColor(Color.red);
//			g.drawRoundRect((float)x+5, (float)y+5, (float)GameGrid.colWidth-10, (float)GameGrid.rowHeight-10,1);
//		}

		//			g.drawString(String.valueOf(f),x+1,y-2);
		//			g.drawString(String.valueOf(g),x+1,y+11);
//		g.drawString(String.valueOf(f),x+1,y);
	}
}

