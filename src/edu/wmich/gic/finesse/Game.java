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

public class Game extends BasicGameState {

	private GameGrid map;
	
	public static Player[] players;

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		// map = GameGrid.getInstance();
		//Temporary fix to the creation of this w/out use of the intermediate window
		initPlayers(MainFinesse.numPlayersConfig,MainFinesse.playerNamesConfig);
		map = new GameGrid(this);
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
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.blue);
		for (int i=0; i<players.length; i++) {
			g.drawString(players[i].name+" Score: " + players[i].points, 30, 40+(30*i));
		}
		map.render(g);
		
//		g.setColor(Color.white);
//		g.drawString("CLICK to move the Minion. Use ENTER to reset the map", 20, 50);
//		g.drawString("Press ESC to quit", 20, 100);
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		map.mouseReleased(button, x, y);
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy){
		map.mouseMoved(oldx, oldy, newx, newy);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		map.update(gc, delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		} else if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			// int rand1 = (int) Math.floor((Math.random()*(map.rows-2))+1);
			// int rand2 = (int) Math.floor((Math.random()*(map.rows-2))+1);
			// System.out.println(rand1+" - "+map.rows);
			// map.resetGrid();
			// pathfinding.searchPath(map.mapArray[rand1][1],
			// map.mapArray[rand2][map.columns-2]);
			map.createGrid();
		}
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}
}
