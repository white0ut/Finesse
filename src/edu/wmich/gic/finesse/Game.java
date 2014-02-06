package edu.wmich.gic.finesse;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Game {

	Input input;

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.drawString("Welcome to our amazing game 0_0", 200, 200);
	}

	public void update(GameContainer gc, int delta) {
		input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
			FinesseGame.isGame = false;
			FinesseGame.isPaused = true;
		}

	}

}