package edu.wmich.gic.finesse;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.drawable.GameGrid;

public class Pathfinding {
	Input input;
	final int rows = 30;
	final int columns = 42;
	final static int rowHeight = 15;
	final static int colWidth = 15;
	final static int gridSpacing = 5;
	final static int gridOffset = 40;

	// Tile[][] GameGrid.mapArray;// = new Tile[rows][columns];
	List<Tile> openList;// = new ArrayList<Tile>();
	List<Tile> closedList;// = new ArrayList<Tile>();
	Tile startTile;
	Tile endTile;

	@SuppressWarnings("unused")
	public boolean searchPath(Tile start, Tile end) {
		startTile = start;
		startTile.start = true;
		startTile.walkable = true;
		endTile = end;
		endTile.end = true;
		endTile.walkable = true;

		openList = new ArrayList<Tile>();
		closedList = new ArrayList<Tile>();

		// System.out.println("SearchPath");
		startTile.g = 0;
		startTile.dist = (Math.abs(endTile.row - startTile.row) + Math
				.abs(endTile.col - startTile.col)) * 10;
		startTile.f = startTile.g + startTile.dist;
		openList.add(startTile);
		Tile bestNode = null;
		Tile searchTile = null;
		int bestDistance = 999;
		while (!closedList.contains(endTile) && openList.size() > 0) {
			// System.out.println("Looping");
			bestDistance = 999;
			for (int i = 0; i < openList.size(); i++) {
				if (openList.get(i).dist < bestDistance) {
					// System.out.println("Best Node");
					bestDistance = openList.get(i).dist;
					bestNode = openList.get(i);
					// bestNode.print();
				}
			}
			int index = openList.indexOf(bestNode);
			openList.remove(index);
			if (bestNode.g > GameGrid.maxDist) {
				endTile.end = false;
				endTile = bestNode;
				endTile.end = true;
				// endTile.path = true;
				endTile.furthest = true;
			}
			closedList.add(bestNode);
			if (closedList.contains(endTile)) {
				// Tile oldTile = bestNode;
				bestNode.child.parent = bestNode;
				bestNode = bestNode.child;
				// System.out.println(endTile.child);
				while (bestNode.child != null) {
					bestNode.path = true;
					bestNode.child.parent = bestNode;
					bestNode = bestNode.child;
					// System.out.println(bestNode);
				}
				// System.out.println("Found End Tile");

				return true;
			}
			int aRow = bestNode.row;
			int aCol = bestNode.col;
			searchTile = GameGrid.mapArray[aRow][aCol - 1];
			// searchTile.toString();
			if (searchTile.walkable == true && searchTile.minion == null && !closedList.contains(searchTile)) {
				// System.out.println("First");
				index = openList.indexOf(searchTile);
				if (index > -1) {
					// if(openList[index].g > searchTile.g){
					// openList[index].child = searchTile;
					// openList[index].g = searchTile.child.g + 10;
					// searchTile.f = searchTile.g + searchTile.dist;
					// }
				} else {
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row - searchTile.row) + Math
							.abs(endTile.col - searchTile.col)) * 10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow][aCol + 1];
			// searchTile.toString();
			if (searchTile.walkable == true && searchTile.minion == null && !closedList.contains(searchTile)) {
				// System.out.println("Second");
				index = openList.indexOf(searchTile);
				if (index > -1) {
					// if(openList[index].g > searchTile.g){
					// openList[index].child = searchTile;
					// openList[index].g = searchTile.child.g + 10;
					// searchTile.f = searchTile.g + searchTile.dist;
					// }
				} else {
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row - searchTile.row) + Math
							.abs(endTile.col - searchTile.col)) * 10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow - 1][aCol];
			// searchTile.toString();
			if (searchTile.walkable == true && searchTile.minion == null && !closedList.contains(searchTile)) {
				index = openList.indexOf(searchTile);
				// System.out.println("Third");
				if (index > -1) {
					// if(openList[index].g > searchTile.g){
					// openList[index].child = searchTile;
					// openList[index].g = searchTile.child.g + 10;
					// searchTile.f = searchTile.g + searchTile.dist;
					// }
				} else {
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row - searchTile.row) + Math
							.abs(endTile.col - searchTile.col)) * 10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			searchTile = GameGrid.mapArray[aRow + 1][aCol];
			// searchTile.toString();
			if (searchTile.walkable == true && searchTile.minion == null && !closedList.contains(searchTile)) {
				index = openList.indexOf(searchTile);
				// System.out.println("Fourth");
				if (index > -1) {
					// if(openList[index].g > searchTile.g){
					// openList[index].child = searchTile;
					// openList[index].g = searchTile.child.g + 10;
					// searchTile.f = searchTile.g + searchTile.dist;
					// }
				} else {
					searchTile.child = GameGrid.mapArray[aRow][aCol];
					searchTile.g = searchTile.child.g + 10;
					searchTile.dist = (Math.abs(endTile.row - searchTile.row) + Math
							.abs(endTile.col - searchTile.col)) * 10;
					searchTile.f = searchTile.g + searchTile.dist;
					searchTile.searched = true;
					openList.add(searchTile);
				}
			}
			// System.out.print(openList);
		}
		return false;
	}


	public void resetMap() throws SlickException {
		GameGrid.mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				GameGrid.mapArray[i][j] = new Tile(i, j);
				if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) {
					GameGrid.mapArray[i][j].walkable = false;
				}
			}
		}

		startTile = GameGrid.mapArray[rows - 2][1];
		startTile.start = true;
		startTile.walkable = true;
		endTile = GameGrid.mapArray[1][columns - 2];
		endTile.end = true;
		endTile.walkable = true;

		openList = new ArrayList<Tile>();
		closedList = new ArrayList<Tile>();
	}

	public int getDist(int i) {
		return (Math.abs(endTile.row - openList.get(i).row) + Math
				.abs(endTile.col - openList.get(i).col)) * 10;
	}
}
