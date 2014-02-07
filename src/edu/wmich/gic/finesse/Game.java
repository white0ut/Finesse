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
import edu.wmich.gic.finesse.drawable.GameGrid;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class Game extends BasicGameState {
	
	private GameGrid map;
	private Pathfinding pathfinding;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		map = GameGrid.getInstance();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.render(g);
		
		g.setColor(Color.white);
		g.drawString("You are in the game state", 20, 50);
		g.drawString("Press ESC to quit", 20, 100);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		
		map.update(gc, delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
	}

	@Override
	public int getID() {
		return FinesseGame.game;
	}
}
