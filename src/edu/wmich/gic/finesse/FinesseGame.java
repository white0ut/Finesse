package edu.wmich.gic.finesse;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.finesse.menus.TitleScreen;
import edu.wmich.gic.finesse.menus.MainMenu;

public class FinesseGame extends StateBasedGame {
	public static enum ScreenType {
		TITLESCREEN,
		MAINMENU,
		GAME;

		public int getValue() {
			return this.ordinal();
		}
	}

	// PUBLIC VALUES
	public static Random rand = new Random();

	public FinesseGame(String title) {
		super(title);

		this.addState(new TitleScreen());
		this.addState(new MainMenu());
		this.addState(new Game());

	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		this.getState(ScreenType.TITLESCREEN.getValue()).init(gc, this);
		this.getState(ScreenType.MAINMENU.getValue()).init(gc, this);
		this.getState(ScreenType.GAME.getValue()).init(gc, this);
		//		System.out.println(MainFinesse.commandLineArgs[0]);
		if(MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
				this.enterState(ScreenType.TITLESCREEN.getValue());
			}else{
				this.enterState(ScreenType.TITLESCREEN.getValue()); // Main Starting State
			}
		}
	}
}
