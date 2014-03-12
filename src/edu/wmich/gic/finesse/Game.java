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
import edu.wmich.gic.finesse.gui.GUIWindow;

public class Game extends BasicGameState implements GUIWindow{

	private GameGrid gameGrid;
	
	public static Player[] players;
	
	private boolean isInitialized;
	
	Button test;
	

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		// map = GameGrid.getInstance();
		//Temporary fix to the creation of this w/out use of the intermediate window
		isInitialized = false;
		initPlayers(MainFinesse.numPlayersConfig,MainFinesse.playerNamesConfig);
		gameGrid = new GameGrid(this);
		
		test = new Button(0, MainFinesse.height/2, 200, 202, null, new ActionHandler() {
			public void onAction() {
				System.out.println("CLICKED");
			}
		});
		
		GUIManager.registerButton(test);
		
		
		
		
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
		for(Button b : GUIManager.getRegisteredButtons()) {
			b.render(g);
		}
		
		
//		g.setColor(Color.white);
//		g.drawString("CLICK to move the Minion. Use ENTER to reset the map", 20, 50);
//		g.drawString("Press ESC to quit", 20, 100);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		System.out.println("Mouse released");
		gameGrid.mouseReleased(button, x, y);
		checkForButtonInteractions(button, x, y);
		
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
		} else if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			// int rand1 = (int) Math.floor((Math.random()*(map.rows-2))+1);
			// int rand2 = (int) Math.floor((Math.random()*(map.rows-2))+1);
			// System.out.println(rand1+" - "+map.rows);
			// map.resetGrid();
			// pathfinding.searchPath(map.mapArray[rand1][1],
			// map.mapArray[rand2][map.columns-2]);
			gameGrid.createGrid();
		}
	}
	
	public void checkForButtonInteractions(int button, int mouseX, int mouseY) {
		if(button == 0) {
			for(Button b : GUIManager.getRegisteredButtons()) {
				if (b.checkClick(mouseX, mouseY)) {
					b.getActionHandler().onAction();
				}
			}
		}
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}

}
