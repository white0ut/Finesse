package edu.wmich.gic.finesse;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;


public class Pathfinding extends BasicGameState {
	
	private static final Pathfinding INSTANCE = new Pathfinding();

	public static Pathfinding getInstance() {
			return INSTANCE;
	}

	Input input;
	final int rows = 15;
	final int columns = 20;
	final static int rowHeight = 40;
	final static int colWidth = 40;
	final static int gridSpacing = 5;
	final static int gridOffset = 40;
	
	Tile[][] mapArray = new Tile[rows][columns];
	List<Tile> openList = new ArrayList<Tile>();
	List<Tile> closedList = new ArrayList<Tile>();
	

//	var closedList = [];
//	var startTile = null;
//	var endTile = null;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	mapArray[i][j] = new Tile(i,j);
				if(i == 0 || i == rows-1 || j == 0 || j == columns-1){
					mapArray[i][j].walkable = false;
				}
		    }
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.blue);
		g.fillRect(0, 0, MainFinesse.width, MainFinesse.height);
		g.setColor(Color.white);
		g.drawString("Change the starting state in the bottom of FinesseGame.java\nBrodie might have forgotten to comment it out when committing",150,0);
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	if(mapArray[i][j].start == true){
		    		g.setColor(Color.green);
				}else{
					if(mapArray[i][j].end == true){
						g.setColor(Color.red);
					}
					else{
						if(mapArray[i][j].searched == true){
							g.setColor(Color.cyan);
						}
						else{
							if(mapArray[i][j].walkable == true){
								g.setColor(Color.white);
							}
							else{
								g.setColor(Color.black);
							}
						}
					}
				}
		    	if(mapArray[i][j].path == true){
		    		g.setColor(Color.blue);
				}
		    	g.fillRect(mapArray[i][j].x, mapArray[i][j].y, colWidth, rowHeight);
		    }
		}
		for(int i=0; i < rows; i++){
			for(int j=0; j < columns; j++){
				g.drawString(String.valueOf(mapArray[i][j].f),mapArray[i][j].x+1,mapArray[i][j].y-2);
				g.drawString(String.valueOf(mapArray[i][j].g),mapArray[i][j].x+1,mapArray[i][j].y+11);
				g.drawString(String.valueOf(mapArray[i][j].dist),mapArray[i][j].x+1,mapArray[i][j].y+24);
			}
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
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

	@Override
	public int getID() {
		return FinesseGame.pathfinding;
	}

}

@SuppressWarnings("unused")
class Tile {
	public int x = 0;
	public int y = 0;
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
		x = Pathfinding.gridOffset+col*(Pathfinding.colWidth+Pathfinding.gridSpacing);
		y = Pathfinding.gridOffset+row*(Pathfinding.rowHeight+Pathfinding.gridSpacing);
	}
}




