package edu.wmich.gic.finesse;

import javax.swing.JOptionPane;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;


public class MainFinesse{
	private static AppGameContainer app;
	public static String[] commandLineArgs;// = new String[0];
	public static String[] playerNamesConfig;
	public static int numPlayersConfig;
	public static int width = 1280;
	public static int height = 1024;
	public static boolean useNetwork = false;

	public static void main(String[] args) throws SlickException {
		commandLineArgs = args;
//		commandLineArgs = new String[]{"brodie"};
		if(MainFinesse.commandLineArgs.length > 0){
			if(MainFinesse.commandLineArgs[0].compareTo("brodie") == 0){
//				width = 800;height = 600;
				width = 1000;height = 800;
//				width = 1440;height = 800;
				playerNamesConfig = new String[]{"Tom","Jerry"};
				numPlayersConfig = 2;
//				useNetwork = true;
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
		app.setShowFPS(false);
		app.start();
	}
	
	public static void ConfigPopUp(){
		String s = (String)JOptionPane.showInputDialog(null,"# of Players,Name#1,Name#2,etc...","Configs",
				JOptionPane.PLAIN_MESSAGE,null,null,"2,Tom,Jerry");
		
		String[] options = s.split(",");
		numPlayersConfig = Integer.parseInt(options[0]);
		if(numPlayersConfig < 2){
			numPlayersConfig = 2;
		}else if(numPlayersConfig > 2){
			numPlayersConfig = 2;
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
