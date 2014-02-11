package edu.wmich.gic.finesse;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class MainFinesse {
	private static AppGameContainer app;
	public final static int width = 1024;
	public final static int height = 768;
	
	public static void main(String[] args) throws SlickException {
		startGame();
	}

	public static void startGame() throws SlickException{
		app = new AppGameContainer(new FinesseGame("Finesse"));
		app.setTargetFrameRate(120); //200 was sucking a lot of cpu, fans kept turning on haha 
		app.setMinimumLogicUpdateInterval(25);
		app.setDisplayMode(width, height, false);
		app.start();
	}
}
