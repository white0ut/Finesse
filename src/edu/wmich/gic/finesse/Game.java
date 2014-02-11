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

import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.drawable.GameGrid;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class Game extends BasicGameState {
	
	private GameGrid map;
	private Pathfinding pathfinding;
	private int timeDelta = 0;
	
	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		map = GameGrid.getInstance();
		pathfinding = new Pathfinding();
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.render(g);
		
		g.setColor(Color.white);
		g.drawString("CLICK to move the Minion. Use ENTER to reset the map", 20, 50);
		g.drawString("Press ESC to quit", 20, 100);
	}

	public void mouseReleased(int button, int x, int y) {
		if (button == 0) {
//			System.out.println("Release "+((x-GameGrid.gridOffset)/(GameGrid.colWidth+GameGrid.gridSpacing)));
//			System.out.println("Release "+((y-GameGrid.gridOffset)/(GameGrid.rowHeight+GameGrid.gridSpacing)));
			int row = (y-GameGrid.gridTopOffset)/(GameGrid.rowHeight+GameGrid.gridSpacing);
			int col = (x-GameGrid.gridLeftOffset)/(GameGrid.colWidth+GameGrid.gridSpacing);
			if(row > 0 && col > 0 && row < map.rows-1 && col < map.columns-1){
				if(map.mapArray[row][col].walkable && map.currentMinionTile != map.mapArray[row][col]){
					map.resetGrid();
					if(pathfinding.searchPath(map.currentMinionTile,map.mapArray[row][col])){
//						while(map.currentMinionTile.parent != null){
//							map.currentMinionTile.parent.minion = map.currentMinionTile.minion;
//							map.currentMinionTile.minion = null;
//							map.currentMinionTile = map.currentMinionTile.parent;
//						}
					}
//					map.mapArray[row][col].minion = new Minion();
				}
			}
		}
	}
	
	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		timeDelta += delta;
		if(timeDelta > 250){
//			System.out.println("Update");
			if(map.currentMinionTile.parent != null){
				map.currentMinionTile.parent.minion = map.currentMinionTile.minion;
				map.currentMinionTile.minion = null;
				map.currentMinionTile = map.currentMinionTile.parent;
			}
			timeDelta = 0;
		}
		map.update(gc, delta);

		if (gc.getInput().isKeyPressed(Input.KEY_ESCAPE)) {
			gc.exit();
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
//			int rand1 = (int) Math.floor((Math.random()*(map.rows-2))+1); 
//			int rand2 = (int) Math.floor((Math.random()*(map.rows-2))+1); 
//			System.out.println(rand1+" - "+map.rows);
//			map.resetGrid();
//			pathfinding.searchPath(map.mapArray[rand1][1], map.mapArray[rand2][map.columns-2]);
			map.createGrid();
		}
//		if(gc.getInput().isMouseButtonDown(0)){
//			System.out.println("click");
//		}
	}

	@Override
	public int getID() {
		return ScreenType.GAME.getValue();
	}
}
