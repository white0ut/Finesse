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
import edu.wmich.gic.finesse.drawable.GameGrid;


public class Pathfinding {
	
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
	
//	Tile[][] GameGrid.mapArray;// = new Tile[rows][columns];
	List<Tile> openList;// = new ArrayList<Tile>();
	List<Tile> closedList;// = new ArrayList<Tile>();
	Tile startTile;
	Tile endTile;

	
//	@Override
//	public void init(GameContainer gc, StateBasedGame game) throws SlickException {
//		resetMap();
////		searchPath();
//	}
	
	@SuppressWarnings("unused")
	public boolean searchPath(Tile start, Tile end){
		startTile = start;
		startTile.start = true;
		startTile.walkable = true;
		endTile = end;
		endTile.end = true;
		endTile.walkable = true;
		
		openList = new ArrayList<Tile>();
		closedList = new ArrayList<Tile>();
		
//		System.out.println("SearchPath");
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
			if(bestNode.g > GameGrid.maxDist){
				endTile.end = false;
				endTile = bestNode;
				endTile.end = true;
//				endTile.path = true;
				endTile.furthest = true;
			}
			closedList.add(bestNode);
			if(closedList.contains(endTile)){
//				Tile oldTile = bestNode;
				bestNode.child.parent = bestNode;
				bestNode = bestNode.child;
//				System.out.println(endTile.child);
				while(bestNode.child != null){
					bestNode.path = true;
					bestNode.child.parent = bestNode;
					bestNode = bestNode.child;
//					System.out.println(bestNode);
				}
				//System.out.println("Found End Tile");
				
				return true;
			}
			int aRow = bestNode.row;
			int aCol = bestNode.col;
			searchTile = GameGrid.mapArray[aRow][aCol-1];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
//				System.out.println("First");
				index = openList.indexOf(searchTile);
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].child = searchTile;
					// 	openList[index].g = searchTile.child.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow][aCol+1];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
//				System.out.println("Second");
				index = openList.indexOf(searchTile);
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].child = searchTile;
					// 	openList[index].g = searchTile.child.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow-1][aCol];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
				index = openList.indexOf(searchTile);
//				System.out.println("Third");
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].child = searchTile;
					// 	openList[index].g = searchTile.child.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow+1][aCol];
//			searchTile.toString();
			if(searchTile.walkable == true && !closedList.contains(searchTile)){
				index = openList.indexOf(searchTile);
//				System.out.println("Fourth");
				if(index > -1){
					// if(openList[index].g > searchTile.g){
					// 	openList[index].child = searchTile;
					// 	openList[index].g = searchTile.child.g + 10;
					// 	searchTile.f = searchTile.g + searchTile.dist;
					// }
				}
				else{
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row-searchTile.row) + Math.abs(endTile.col-searchTile.col))*10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
//			System.out.print(openList);
		}
		return false;
	}
	
//	@Override
//	public void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
//		input = gc.getInput();
//		if (input.isKeyPressed(Input.KEY_ESCAPE)) {
////			FinesseGame.isGame = false;
////			FinesseGame.isPaused = true;
//			gc.exit();
////			System.out.println("Escape");
//		}
//		else if(input.isKeyPressed(Input.KEY_ENTER)){
////			System.out.println(GameGrid.mapArray[0][0]);
//			resetMap();
////			searchPath();
//		}
//	}

//	@Override
//	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
//		g.setColor(Color.blue);
//		g.fillRect(0, 0, MainFinesse.width, MainFinesse.height);
//		g.setColor(Color.white);
//		g.drawString("Change the starting state in the bottom of FinesseGame.java\nBrodie might have forgotten to comment it out when committing",150,0);
//		for (int i = 0; i < rows; i ++){
//		    for (int j = 0; j < columns; j++){
//		    	GameGrid.mapArray[i][j].render(g);
//		    }
//		}
//	}
	
	public void resetMap(){
		GameGrid.mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i ++){
		    for (int j = 0; j < columns; j++){
		    	GameGrid.mapArray[i][j] = new Tile(i,j);
				if(i == 0 || i == rows-1 || j == 0 || j == columns-1){
					GameGrid.mapArray[i][j].walkable = false;
				}
		    }
		}
		
		GameGrid.mapArray[0][13].walkable = false;
		GameGrid.mapArray[1][13].walkable = false;
		GameGrid.mapArray[2][13].walkable = false;
		GameGrid.mapArray[3][13].walkable = false;
		GameGrid.mapArray[4][13].walkable = false;
		GameGrid.mapArray[5][13].walkable = false;
		GameGrid.mapArray[6][13].walkable = false;
		GameGrid.mapArray[7][13].walkable = false;

		GameGrid.mapArray[14][7].walkable = false;
		GameGrid.mapArray[13][7].walkable = false;
		GameGrid.mapArray[12][7].walkable = false;
		GameGrid.mapArray[11][7].walkable = false;
		GameGrid.mapArray[10][7].walkable = false;
		GameGrid.mapArray[9][7].walkable = false;
		GameGrid.mapArray[8][7].walkable = false;
		GameGrid.mapArray[7][7].walkable = false;
		
		startTile = GameGrid.mapArray[rows-2][1];
		startTile.start = true;
		startTile.walkable = true;
		endTile = GameGrid.mapArray[1][columns-2];
		endTile.end = true;
		endTile.walkable = true;
		
		openList = new ArrayList<Tile>();
		closedList = new ArrayList<Tile>();
	}
	
	public int getDist(int i){
		return (Math.abs(endTile.row-openList.get(i).row) + Math.abs(endTile.col-openList.get(i).col))*10;
	}

//	@Override
//	public int getID() {
//		return ScreenType.PATHFINDING.getValue();
//	}
}





