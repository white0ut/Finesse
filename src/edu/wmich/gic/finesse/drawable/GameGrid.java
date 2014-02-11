package edu.wmich.gic.finesse.drawable;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.MainFinesse;
import edu.wmich.gic.finesse.Tile;

/*
 * The intent of this Class is to provide a class with a render() and update() method 
 * When you want to draw this grid you simply instantiate is and call these methods.
 */
public class GameGrid {

	private static final GameGrid INSTANCE = new GameGrid();
	
	Input input;
	public final int rows = 20;
	public final int columns = 25;
	public final static int rowHeight = 25;
	public final static int colWidth = 25;
	public final static int gridSpacing = 5;
	public final static int gridOffset = 100;
	
	public Tile currentMinionTile;
	public static Tile[][] mapArray;// = new Tile[rows][columns];

	private GameGrid() {
		createGrid();
	}
	
//	public void randomSearch(){
//		pathfinding.searchPath(mapArray[4][4], mapArray[10][10]);
//	}
	
	public void resetGrid(){
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	mapArray[i][j].resetTile();
		    }
		}
	}
	
	public void createGrid(){
		mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	mapArray[i][j] = new Tile(i,j);
				if(i == 0 || i == rows-1 || j == 0 || j == columns-1){
					mapArray[i][j].walkable = false;
				}
		    }
		}
		mapArray[2][2].minion = new Minion();
		currentMinionTile = mapArray[2][2];
	}
	
	public static GameGrid getInstance() {
		return INSTANCE;
	}


	public void render(Graphics g) {

		g.setColor(Color.blue);
//		for (int x = 7; x < MainFinesse.width / 32 + 1; x++) {
//			for (int y = 0; y < (MainFinesse.height / 32) + 1; y++) {
//				g.fillRect(x * 32, y * 32, 31, 31);
//			}
//		}
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	mapArray[i][j].render(g);
		    }
		}
	}

	public void update(GameContainer gc, int delta) {

	}
}
