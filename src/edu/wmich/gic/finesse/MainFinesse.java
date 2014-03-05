package edu.wmich.gic.finesse;

import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.FinesseGame.ScreenType;


public class MainFinesse{
	private static AppGameContainer app;
	public static String[] commandLineArgs = new String[0];
	public static String[] playerNamesConfig;
	public static int numPlayersConfig;
	public final static int width = 1280;
	public final static int height = 1024;
	/* BUGS! 0-0
	 * Post bugs here
	 * 
	 *FIXED:confront BUG where minions can eat each other... Intentional?  I like it... -----
	 * 
	 */
	/*
	 * ##############For the Meeting February 12, 2014#################
	 * Quote of the day:
	 * 		�Programming is like sex. One mistake and you have to support it for the rest of your life.�
			- Michael Sinz
	 * 
	 * Set a projected finish date for board game night
	 * We need a page before the game starts that accepts input for the follow
	 * 		FIXED:1: Amount of players
	 * 		FIXED:2: What their names are
	 * 		FIXED:3: Max of 4 players
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
		commandLineArgs = args;
//		System.out.println(args[0]);
		if(MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
				playerNamesConfig = new String[]{"Tom","Jerry"};
				numPlayersConfig = 2;
			}else{
				ConfigPopUp();
			}
		}else{
			ConfigPopUp();
		}
		startGame();
	}

	public static void startGame() throws SlickException{
		app = new AppGameContainer(new FinesseGame("Finesse"));
		app.setTargetFrameRate(60); 
		app.setMinimumLogicUpdateInterval(20);
		app.setDisplayMode(width, height, false);
		app.start();
	}
	
	public static void ConfigPopUp(){
		String s = (String)JOptionPane.showInputDialog(null,"# of Players,Name#1,Name#2,etc...","Configs",
				JOptionPane.PLAIN_MESSAGE,null,null,"2,Tom,Jerry");
		
		String[] options = s.split(",");
		numPlayersConfig = Integer.parseInt(options[0]);
		if(numPlayersConfig < 2){
			numPlayersConfig = 2;
		}else if(numPlayersConfig > 4){
			numPlayersConfig = 4;
		}
		if(options.length-1 < numPlayersConfig){
			options = new String[numPlayersConfig+1];
			options[0] = String.valueOf(numPlayersConfig);
			for(int i = 1; i < numPlayersConfig+1; i++){
				options[i] = "John Doe";
			}
		}
		playerNamesConfig = new String[numPlayersConfig];
		for(int i = 0; i < numPlayersConfig; i++){
			playerNamesConfig[i] = options[i+1];
		}
//		for(int i = 0; i < options.length; i++){
//			System.out.println(options[i]);
//		}
	}
}
