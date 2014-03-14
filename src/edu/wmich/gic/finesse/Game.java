package edu.wmich.gic.finesse;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import edu.wmich.gic.entity.Player;
import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.drawable.GameGrid;
import edu.wmich.gic.finesse.gui.ActionHandler;
import edu.wmich.gic.finesse.gui.Button;
import edu.wmich.gic.finesse.gui.GUIManager;
import edu.wmich.gic.finesse.network.Network;

public class Game extends BasicGameState {

	private GameGrid gameGrid;
	
	public Player[] players;
	
	private boolean isInitialized;
	
	private GUIManager guiManager;
	private Network network;
	
	Button test;
	

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		// map = GameGrid.getInstance();
		//Temporary fix to the creation of this w/out use of the intermediate window
		guiManager = new GUIManager();

		isInitialized = false;
		initPlayers(MainFinesse.numPlayersConfig,MainFinesse.playerNamesConfig);
		
		network = new Network(this);
		gameGrid = new GameGrid(this,network);
		
		test = new Button(0, MainFinesse.height/2, 200, 202, new ActionHandler() {
			public void onAction() {
				System.out.println("CLICKED");
			}
		});
		
		guiManager.registerButton(test);
		
		
		
		
	}

	public void initPlayers(int numPlayers, String[] playerNames) {
		if (numPlayers > 4 || playerNames.length != numPlayers){
			System.err.print("ERROR INITIATING PLAYERS, INCORRECT INPUT: error at initPlayers(int, String[]) in Game.java");
			System.exit(0);
		}
		players = new Player[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			players[i] = new Player(playerNames[i]);
		}
		isInitialized = true;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) {
		g.setColor(Color.blue);
		for (int i=0; i<players.length; i++) {
			g.drawString(players[i].name+" Score: " + players[i].points, 30, 140+(20*i));
		}
		gameGrid.render(g);
		
		guiManager.render(gc, g);
		
		
//		g.setColor(Color.white);
//		g.drawString("CLICK to move the Minion. Use ENTER to reset the map", 20, 50);
//		g.drawString("Press ESC to quit", 20, 100);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		gameGrid.mouseReleased(button, x, y);
		guiManager.buttonRelease(button, x, y);
		
	}
	
	@Override 
	public void mousePressed(int button, int x, int y) {
		guiManager.checkForButtonClicks(button, x, y);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
		gameGrid.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		gameGrid.update(gc, delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		} else if (gc.getInput().isKeyPressed(Input.KEY_TAB)) {
			gameGrid.createGrid();
		}
	}

	public void receiveNetwork(String data){
		
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}

}
