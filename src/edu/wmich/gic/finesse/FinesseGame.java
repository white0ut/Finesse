package edu.wmich.gic.finesse;

import java.util.Random;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import edu.wmich.gic.finesse.menus.PauseScreen;
import edu.wmich.gic.finesse.menus.TitleScreen;


public class FinesseGame extends BasicGame {
	
	private PauseScreen pauseScreen;
	private TitleScreen titleScreen;
	private Game game;
	private Input input;

	private static boolean isTitle, isGame, isPaused;
	
	//PUBLIC VALUES
	public static Random rand = new Random();
	public static int height, width;
	
	public FinesseGame(String title) {
		super(title);
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(GameContainer gc) throws SlickException {
		pauseScreen = new PauseScreen();
		titleScreen = new TitleScreen();
		height = gc.getHeight();
		width = gc.getWidth();
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
		input = gc.getInput();
		//TODO: Game stuff goes here
		if(isGame){
			if(input.isKeyPressed(Input.KEY_ESCAPE)){ 
				isGame = false;
				isPaused = true;
			}
			game.update(gc, delta);
		}

		//TODO: Title stuff goes here
		else if(isTitle){
			if(input.isKeyPressed(Input.KEY_ESCAPE)){
				isTitle = false;
				isPaused = true;
			}
			titleScreen.update(gc, delta);
		}

		//TODO: Paused stuff goes here
		else if(isPaused){
			pauseScreen.update(gc, delta);
		}
	}

	public static void setTitleScreen(boolean b){isTitle = b;}
	public static void setPaused(boolean b){isPaused = b;}
	public static void setGame(boolean b){isGame = b;}
}
