package edu.wmich.gic.finesse.menus;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.MainFinesse;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class PauseScreen {

	OscillatingMapGrid map = new OscillatingMapGrid();
	private Input input;

	public PauseScreen() {

	}

	public void render(Graphics g) {
		// TODO
		map.render(g);

		g.setColor(Color.blue);
		g.drawString("Pause Screen is here 'Enter' to return to game", 200, 200);
		// Broken

	}

	public void update(GameContainer gc, int delta) {
		input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ENTER)){
			FinesseGame.isPaused = false;
			FinesseGame.isGame = true;
		}
		map.update(gc, delta);


	}

}
