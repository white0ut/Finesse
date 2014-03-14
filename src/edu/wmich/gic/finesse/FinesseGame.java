package edu.wmich.gic.finesse;

import java.util.Random;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.finesse.menus.MainMenu;
import edu.wmich.gic.finesse.menus.SetupMenu;
import edu.wmich.gic.finesse.menus.SplashScreen;
import edu.wmich.gic.finesse.menus.TitleScreen;
import edu.wmich.gic.finesse.network.Network;

public class FinesseGame extends StateBasedGame {
	public static enum ScreenType {
		SPLASHSCREEN,
		TITLESCREEN,
		MAINMENU,
		SETUPMENU,
		GAME;

		public int getValue() {
			return this.ordinal();
		}
	}

	// PUBLIC VALUES
	public static Random rand = new Random();

	public FinesseGame(String title) {
		super(title);

		this.addState(new SplashScreen());
		this.addState(new TitleScreen());
		this.addState(new MainMenu());
		this.addState(new SetupMenu());
		this.addState(new Game());
	}

	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		//this.getState(ScreenType.SPLASHSCREEN.getValue()).init(gc, this);
//		this.getState(ScreenType.SPLASHSCREEN.getValue());
//		this.getState(ScreenType.TITLESCREEN.getValue());
//		this.getState(ScreenType.MAINMENU.getValue());
//		this.getState(ScreenType.SETUPMENU.getValue());
//		this.getState(ScreenType.GAME.getValue());
		//		System.out.println(MainFinesse.commandLineArgs[0]);
		
		if(MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
				this.enterState(ScreenType.GAME.getValue());
			}else{
				this.enterState(ScreenType.TITLESCREEN.getValue()); // Main Starting State
			}
		} else {
			enterState(ScreenType.SPLASHSCREEN.getValue());
		}
	}
}
