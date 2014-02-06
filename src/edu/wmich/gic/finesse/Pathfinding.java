package edu.wmich.gic.finesse;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

public class Pathfinding {

	Input input;
	final int rows = 15;
	final int columns = 20;
	final int rowHeight = 40;
	final int colWidth = 40;
	final int gridSpacing = 5;
	final int gridOffset = 40;
	
	Tile[][] mapArray = new Tile[rows][columns];
	List<Tile> openList = new ArrayList<Tile>();
	List<Tile> closedList = new ArrayList<Tile>();
	

//	var closedList = [];
//	var startTile = null;
//	var endTile = null;
	
	
	public Pathfinding(){
		for (int row = 0; row < rows; row ++){
		    for (int col = 0; col < columns; col++){
		    	mapArray[row][col] = new Tile(row,col);
		    }
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.fillRect(0, 0, MainFinesse.width, MainFinesse.height);
		g.setColor(Color.white);
		for (int row = 0; row < rows; row ++){
		    for (int col = 0; col < columns; col++){
		    	g.fillRect(gridOffset+mapArray[row][col].col*(colWidth+gridSpacing), gridOffset+mapArray[row][col].row*(rowHeight+gridSpacing), colWidth, rowHeight);
		    }
		}
		//g.drawString("Welcome to our amazing game 0_0", 200, 200);
	}

	public void update(GameContainer gc, int delta) {
		input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
//			FinesseGame.isGame = false;
//			FinesseGame.isPaused = true;
			gc.exit();
//			System.out.println("Escape");
		}
		else if(input.isKeyPressed(Input.KEY_ENTER)){
//			System.out.println(mapArray[0][0]);
		}
	}

}

@SuppressWarnings("unused")
class Tile {
	public int row = 0;
	public int col = 0;
	public boolean start = false;
	public boolean end = false;
	public boolean wall = false;
	public boolean searched = false;
	public boolean walkable = true;
	public boolean path = false;
	public int f = 0;
	public int g = 0;
	public int dist = 0;
	public Tile parent = null;
	
	public Tile(int _row, int _col){
		row = _row;
		col = _col;
	}
}




