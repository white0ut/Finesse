package edu.wmich.gic.finesse;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

public class MainFinesse {
	private static AppGameContainer app;
	public final static int width = 1024;
	public final static int height = 768;
	/* BUGS! 0-0
	 * Post bugs here
	 * 
	 *TODO:confront BUG where minions can eat each other... Intentional?  I like it... -----
	 * 
	 */
	/*
	 * ##############For the Meeting February 12, 2014#################
	 * Quote of the day:
	 * 		ÒProgramming is like sex. One mistake and you have to support it for the rest of your life.Ó
			- Michael Sinz
	 * 
	 * Set a projected finish date for board game night
	 * We need a page before the game starts that accepts input for the follow
	 * 		TODO:1: Amount of players
	 * 		TODO:2: What their names are
	 * 		TODO:3: Max of 4 players
	 * 		TODO:4: Optional game settings? (get creative, idc)
	 * 		If anyone is interested in doing the GUI this page has potential to look cool
	 * 
	 * 		TODO:What I call 'modularize' the Players and the minions
	 * 			All player control needs to be done in the game class
	 *			GameGrid simply prints it to the screen and updates the following
	 * 		TODO:if 'highlighted (AKA if clicked)' then tell them max range of movement
	 * 		
	 * 
	 * ##################### BIG STEPS ############################
	 * 
	 * TODO:Implement "turns", don't forget amount of turns = Game.players.length
	 * TODO:On a specific turn you can only control your minions
	 * TODO:Highlight Player of CurrentTurn's minions
	 * 
	 * TODO:GUI layout of the Game needs to be implement, this includes
	 * 			Displaying information on the screen
	 * 		Buttons such as 
	 * 			TODO'Save' (Do not implement yet)
	 * 			TODO'Finish' (When you complete your moves)
	 * 			TODO'Shoot' (Just set a boolean to true for now)
	 * 			TODO'New Minion' (Logic is self explanatory)
	 * 
	 */

	public static void main(String[] args) throws SlickException {
		startGame();
	}

	public static void startGame() throws SlickException{
		app = new AppGameContainer(new FinesseGame("Finesse"));
		app.setTargetFrameRate(60); 
		app.setMinimumLogicUpdateInterval(20);
		app.setDisplayMode(width, height, false);
		app.start();
	}
	//boo
}
