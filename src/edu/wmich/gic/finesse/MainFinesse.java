package edu.wmich.gic.finesse;


import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class MainFinesse {
	private static AppGameContainer app;
	
	public static void main(String[] args) throws SlickException {
		startGame();
	}

	public static void startGame() throws SlickException{
		FinesseGame.setTitleScreen(true);
		app = new AppGameContainer(new FinesseGame("Finesse"));
		app.setTargetFrameRate(200);
		app.setDisplayMode(1680, 1280, false);
		app.start();
	}
}
