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

import edu.wmich.gic.finesse.FinesseGame.ScreenType;


public class Pathfinding extends BasicGameState {
	
//	private static final Pathfinding INSTANCE = new Pathfinding();
//
//	public static Pathfinding getInstance() {
//			return INSTANCE;
//	}

	Input input;
	final int rows = 30;
	final int columns = 42;
	final static int rowHeight = 15;
	final static int colWidth = 15;
	final static int gridSpacing = 5;
	final static int gridOffset = 40;
	
	Tile[][] mapArray;// = new Tile[rows][columns];
	List<Tile> openList;// = new ArrayList<Tile>();
	List<Tile> closedList;// = new ArrayList<Tile>();
	Tile startTile;
	Tile endTile;

	
	@Override
	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
		resetMap();
		searchPath();
	}
	
	@SuppressWarnings("unused")
	public void searchPath(){
		System.out.println("SearchPath");
		startTile.g = 0;
		startTile.dist = (Math.abs(endTile.row-startTile.row) + Math.abs(endTile.col-startTile.col))*10;
		startTile.f = startTile.g + startTile.dist;
		openList.add(startTile);
		Tile bestNode = null;
		Tile searchTile = null;
		int bestDistance = 999;
		while(!closedList.contains(endTile) && openList.size() > 0){
//			System.out.println("Looping");
			bestDistance = 999;
			for(int i = 0; i < openList.size(); i++){
				if(openList.get(i).dist < bestDistance){
//					System.out.println("Best Node");
					bestDistance = openList.get(i).dist;
					bestNode = openList.get(i);
//					bestNode.print();
				}
			}
			int index = openList.indexOf(bestNode);
			openList.remove(index);
			closedList.add(bestNode);
			if(closedList.contains(endTile)){
				bestNode = bestNode.parent;
				while(bestNode.parent != null){
					bestNode.path = true;
					bestNode = bestNode.parent;
					// console.log("hello");
				}
				System.out.println("Found End Tile");
				break;
			}
			int aRow = bestNode.row;
			int aCol = bestNode.col;
			searchTile = mapArray[aRow][aCol-1];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
//				System.out.println("First");
				index = openList.indexOf(searchTile);
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].parent = searchTile;
					// 	openList[index].g = searchTile.parent.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.parent = mapArray[aRow][aCol];
					searchTile.g = searchTile.parent.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = mapArray[aRow][aCol+1];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
//				System.out.println("Second");
				index = openList.indexOf(searchTile);
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].parent = searchTile;
					// 	openList[index].g = searchTile.parent.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.parent = mapArray[aRow][aCol];
					searchTile.g = searchTile.parent.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = mapArray[aRow-1][aCol];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
				index = openList.indexOf(searchTile);
//				System.out.println("Third");
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].parent = searchTile;
					// 	openList[index].g = searchTile.parent.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.parent = mapArray[aRow][aCol];
					searchTile.g = searchTile.parent.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = mapArray[aRow+1][aCol];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
				index = openList.indexOf(searchTile);
//				System.out.println("Fourth");
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].parent = searchTile;
					// 	openList[index].g = searchTile.parent.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.parent = mapArray[aRow][aCol];
					searchTile.g = searchTile.parent.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
//			System.out.print(openList);
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
			resetMap();
			searchPath();
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
							g.setColor(Color.gray);
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
		    		g.setColor(Color.cyan);
				}
		    	g.fillRect(mapArray[i][j].x, mapArray[i][j].y, colWidth, rowHeight);
		    }
		}
//		for(int i=0; i < rows; i++){
//			for(int j=0; j < columns; j++){
//				g.drawString(String.valueOf(mapArray[i][j].f),mapArray[i][j].x+1,mapArray[i][j].y-2);
//				g.drawString(String.valueOf(mapArray[i][j].g),mapArray[i][j].x+1,mapArray[i][j].y+11);
//				g.drawString(String.valueOf(mapArray[i][j].dist),mapArray[i][j].x+1,mapArray[i][j].y+24);
//			}
//		}
	}
	
	public void resetMap(){
		mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	mapArray[i][j] = new Tile(i,j);
				if(i == 0 || i == rows-1 || j == 0 || j == columns-1){
					mapArray[i][j].walkable = false;
				}
		    }
		}
		
		mapArray[0][13].walkable = false;
		mapArray[1][13].walkable = false;
		mapArray[2][13].walkable = false;
		mapArray[3][13].walkable = false;
		mapArray[4][13].walkable = false;
		mapArray[5][13].walkable = false;
		mapArray[6][13].walkable = false;
		mapArray[7][13].walkable = false;

		mapArray[14][7].walkable = false;
		mapArray[13][7].walkable = false;
		mapArray[12][7].walkable = false;
		mapArray[11][7].walkable = false;
		mapArray[10][7].walkable = false;
		mapArray[9][7].walkable = false;
		mapArray[8][7].walkable = false;
		mapArray[7][7].walkable = false;
		
		startTile = mapArray[rows-2][1];
		startTile.start = true;
		startTile.walkable = true;
		endTile = mapArray[1][columns-2];
		endTile.end = true;
		endTile.walkable = true;
		
		openList = new ArrayList<Tile>();
		closedList = new ArrayList<Tile>();
	}
	
	public int getDist(int i){
		return (Math.abs(endTile.row-openList.get(i).row) + Math.abs(endTile.col-openList.get(i).col))*10;
	}

	@Override
	public int getID() {
		return ScreenType.PATHFINDING.getValue();
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
	public boolean searched = false;
	public boolean walkable = true;
	public boolean path = false;
	public int f = 0;
	public int g = 0;
	public int dist = 0;
	public Tile parent = null;
	
	public Tile(int _row, int _col){
		int rand = (int) Math.floor((Math.random()*4)+1);
		if(rand == 1){
			this.walkable = false;
		}
		row = _row;
		col = _col;
		x = Pathfinding.gridOffset+col*(Pathfinding.colWidth+Pathfinding.gridSpacing);
		y = Pathfinding.gridOffset+row*(Pathfinding.rowHeight+Pathfinding.gridSpacing);
	}
	
	public String toString(){
		return "Row:"+row+"  Column:"+col+"  Start:"+start+"  End:"+end+"  Searched:"+searched+"  Walkable:"+walkable+"  Path:"+path;
	}
}




