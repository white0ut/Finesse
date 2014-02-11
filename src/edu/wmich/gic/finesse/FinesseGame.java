package edu.wmich.gic.finesse;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.finesse.menus.TitleScreen;
import edu.wmich.gic.finesse.menus.MainMenu;



public class FinesseGame extends StateBasedGame {
	public static final int titleScreen = 0, mainMenu = 1, game = 2, pathfinding = 3;

	//PUBLIC VALUES
	public static Random rand = new Random();
	
	public FinesseGame(String title) {
		super(title);

		this.addState(new TitleScreen());
		this.addState(new MainMenu());
		this.addState(new Game());
		this.addState(new Pathfinding());
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(titleScreen).init(gc, this);
		this.getState(mainMenu).init(gc, this);
		this.getState(game).init(gc, this);
		this.getState(pathfinding).init(gc, this);

//		this.enterState(titleScreen); //Main Starting State
		this.enterState(pathfinding); //Brodie is using this state for easy testing
	}
}
