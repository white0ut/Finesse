package edu.wmich.gic.finesse;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.drawable.GameGrid;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class Game extends BasicGameState {
	
	private GameGrid map;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		map = GameGrid.getInstance();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.render(g);
		
//		g.setColor(Color.white);
//		g.drawString("CLICK to move the Minion. Use ENTER to reset the map", 20, 50);
//		g.drawString("Press ESC to quit", 20, 100);
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		map.mouseReleased(button, x, y);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
		map.mouseMoved(oldx, oldy, newx, newy);
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		map.update(gc, delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
//			int rand1 = (int) Math.floor((Math.random()*(map.rows-2))+1); 
//			int rand2 = (int) Math.floor((Math.random()*(map.rows-2))+1); 
//			System.out.println(rand1+" - "+map.rows);
//			map.resetGrid();
//			pathfinding.searchPath(map.mapArray[rand1][1], map.mapArray[rand2][map.columns-2]);
			map.createGrid();
		}
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}
}
