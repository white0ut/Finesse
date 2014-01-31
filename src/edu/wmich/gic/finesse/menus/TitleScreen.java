package edu.wmich.gic.finesse.menus;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import edu.wmich.gic.finesse.drawable.MapGrid;

public class TitleScreen {
	
	MapGrid map;
	public TitleScreen(){
		map = new MapGrid();
	}
	
	public void render(Graphics g) {
		map.render(g);
	}
	

	public void update(GameContainer gc, int delta) {
		map.update(gc, delta);
	}
	

}
