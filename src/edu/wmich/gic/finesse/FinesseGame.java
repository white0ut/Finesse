package edu.wmich.gic.finesse;

import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.menus.PauseScreen;
import edu.wmich.gic.finesse.menus.TitleScreen;


public class FinesseGame extends BasicGame {
	
	private PauseScreen pauseScreen;
	private TitleScreen titleScreen;
	private Game game;

	public static boolean isTitle, isGame, isPaused;
	
	//PUBLIC VALUES
	public static Random rand = new Random();
	
	public FinesseGame(String title) {
		super(title);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		pauseScreen = new PauseScreen();
		titleScreen = new TitleScreen();
		game = new Game();
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException {
		
		
		//TODO: Game stuff goes here
		if(isGame){
			game.render(g);
		}

		//TODO: Title stuff goes here
		else if(isTitle){
			titleScreen.render(g);
		}

		//TODO: Paused stuff goes here
		else if(isPaused){
			pauseScreen.render(g);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) throws SlickException {
		//TODO: Game stuff goes here
		if(isGame){
			game.update(gc, delta);
		}

		//TODO: Title stuff goes here
		else if(isTitle){
			titleScreen.update(gc, delta);
		}

		//TODO: Paused stuff goes here
		else if(isPaused){
			pauseScreen.update(gc, delta);
		}
	}

}
