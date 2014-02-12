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
	
	// TODO: MAKE THIS FUNCTIONAL WITH AN ARRAY
	public Player human;
	public Player computer;
	Player[] players;

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		// map = GameGrid.getInstance();
		map = new GameGrid(this);

		human = new Player("Human");
		computer = new Player("Computer");
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
	
	public void initPlayers(){
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		g.setColor(Color.blue);
		g.drawString("Player Score: " + human.points, 100, 30);
		g.drawString("Computer Score: " + computer.points, 100, 60);
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
