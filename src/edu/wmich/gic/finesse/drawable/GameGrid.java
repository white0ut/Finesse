package edu.wmich.gic.finesse.drawable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import edu.wmich.gic.finesse.MainFinesse;

/*
 * The intent of this Class is to provide a class with a render() and update() method 
 * When you want to draw this grid you simply instantiate is and call these methods.
 */
public class GameGrid {

	private static final GameGrid INSTANCE = new GameGrid();

	private GameGrid() {

	}
	
	public static GameGrid getInstance() {
		return INSTANCE;
	}


	public void render(Graphics g) {

		g.setColor(Color.blue);
		for (int x = 7; x < MainFinesse.width / 32 + 1; x++) {
			for (int y = 0; y < (MainFinesse.height / 32) + 1; y++) {
				g.fillRect(x * 32, y * 32, 31, 31);
			}
		}
	}

	public void update(GameContainer gc, int delta) {

	}
}
