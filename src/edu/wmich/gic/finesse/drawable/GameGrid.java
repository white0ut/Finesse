package edu.wmich.gic.finesse.drawable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.entity.Bullet;
import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.Game;
import edu.wmich.gic.finesse.Pathfinding;
import edu.wmich.gic.finesse.Tile;


public class GameGrid {

	// private static final GameGrid INSTANCE = new GameGrid();

	Input input;
	public final int rows = 28;
	public final int columns = 27;
	public final static int rowHeight = 25;
	public final static int colWidth = 25;
	public final static int gridSpacing = 1;
	public final static int gridTopOffset = 0;
	public final static int gridLeftOffset = 300;
	public static int maxDist;
	public static int maxLength;
	public int rowCounter = maxLength * -1;
	public int colCounter = maxLength * -1;

	public Tile currentMinionTile;
	public Tile enemyMinionTile;
	public static Tile[][] mapArray;// = new Tile[rows][columns];

	private Game parentGame;
	private Pathfinding pathfinding;
	private int timeDelta = 0;

	public Bullet bullet = null;

	private boolean moveMinion = false;

	private int oldRow = 0;
	private int oldColumn = 0;
	
	public int playingState = 0;
	private final int MOVING = 0;
	private final int SHOOTING = 1;
	private final int BUYING = 2;
	private final int DEBUGGING = 3;
	public String[] playingStateNames = new String[]{"MOVING","SHOOTING","BUYING","DEBUGGING"};

	// private GameGrid() {
	public GameGrid(Game game) {
		parentGame = game;
		maxDist = 40;
		maxLength = (maxDist / 10) + 1;
		pathfinding = new Pathfinding();
		createGrid();
	}


	public void mouseReleased(int button, int x, int y) {
		if (button == 0) {
			if(playingState == MOVING){
				int row = getRow(y);
				int col = getColumn(x);
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if(currentMinionTile != null){
						if(currentMinionTile == mapArray[row][col]){
							currentMinionTile.minion.selected = false;
							currentMinionTile = null;
							resetGrid(true);
						}
						else{
							if (mapArray[row][col].walkable && mapArray[row][col].minion == null) {
								resetGrid(true);
								pathfinding.searchPath(currentMinionTile,mapArray[row][col]);
								moveMinion = true;
							}
							else{
								if(mapArray[row][col].minion != null){
									currentMinionTile.minion.selected = false;
									currentMinionTile = mapArray[row][col];
									currentMinionTile.minion.selected = true;
									resetGrid(true);
									showFurthest(currentMinionTile);
								}
							}
						}
					} else{
						if(mapArray[row][col].minion != null){
							currentMinionTile = mapArray[row][col];
							currentMinionTile.minion.selected = true;
							showFurthest(currentMinionTile);
						}
					}
				}
			}
			else if(playingState == SHOOTING){
				int row = getRow(y);
				int col = getColumn(x);
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if(currentMinionTile != null){
						if(mapArray[row][col] == currentMinionTile){
							currentMinionTile.minion.selected = false;
							currentMinionTile = null;
							return;
						}
						if(bullet == null){
							bullet = new Bullet(currentMinionTile, x, y);
						}
					}
					else{
						currentMinionTile = mapArray[row][col];
						currentMinionTile.minion.selected = true;
					}
				}
			}
			else if(playingState == DEBUGGING){
				int row = getRow(y);
				int col = getColumn(x);
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					System.out.println(mapArray[row][col].toString());
				}
			}
		} 
	}

	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// System.out.println("Mouse Move");
		if(playingState == MOVING && currentMinionTile != null){
			int row = getRow(newy);
			int col = getColumn(newx);
			if (!moveMinion && (oldRow != row || oldColumn != col) && row > 0
					&& col > 0 && row < rows - 1 && col < columns - 1) {
				oldRow = row;
				oldColumn = col;
				if (mapArray[row][col].walkable && mapArray[row][col].minion == null && currentMinionTile != mapArray[row][col]) {
					resetGrid(false);
					pathfinding.searchPath(currentMinionTile, mapArray[row][col]);
				}
			}
		}
	}

	public void resetGrid(boolean resetFurthest) {
		// System.out.println("Reset Grid");
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				mapArray[i][j].resetTile(resetFurthest);
			}
		}
	}

	public void createGrid() {
		mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				try {
					mapArray[i][j] = new Tile(i, j);
				} catch (SlickException e) {
					e.printStackTrace();
				}
				if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) {
					mapArray[i][j].walkable = false;
				}
			}
		}
		for(int i = 0; i < 100; i++){
			int randRow = FinesseGame.rand.nextInt(rows-2)+1;
			int randCol = FinesseGame.rand.nextInt(columns-2)+1;
			mapArray[randRow][randCol].minion = new Minion(parentGame.players[0]);
			parentGame.players[0].minions.add(mapArray[randRow][randCol].minion);
			mapArray[randRow][randCol].walkable = true;
		}
		

//		showFurthest(currentMinionTile);
	}

	// public static GameGrid getInstance() {
	// return INSTANCE;
	// }

	public void render(Graphics g) {
		g.setColor(Color.blue);
		g.drawString("Playing State: "+playingStateNames[playingState], 30, 30);
		g.drawString("Change State\nM = Moving\nS = Shooting\nB = Buying", 30, 50);
		// for (int x = 7; x < MainFinesse.width / 32 + 1; x++) {
		// for (int y = 0; y < (MainFinesse.height / 32) + 1; y++) {
		// g.fillRect(x * 32, y * 32, 31, 31);
		// }
		// }

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				mapArray[i][j].render(g);
			}
		}
		if (bullet != null) {
			bullet.render(g);
		}
	}

	public void update(GameContainer gc, int delta) {
		timeDelta += delta;
		// TODO: We need to work this through to work with different minions
		if (moveMinion && timeDelta > 100) {
			// System.out.println("Update");
			if (currentMinionTile.parent != null) {
				currentMinionTile.parent.minion = currentMinionTile.minion;
				currentMinionTile.minion = null;
				currentMinionTile = currentMinionTile.parent;
			} else {
				moveMinion = false;
				resetGrid(true);
				showFurthest(currentMinionTile);
			}
			timeDelta = 0;
		}

		if (bullet != null) {
			if (!bullet.update(gc, delta)) {
				bullet = null;
			}
		}
		if(gc.getInput().isKeyPressed(Input.KEY_M)){
			playingState = 0;
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_S)){
			playingState = 1;
			resetGrid(true);
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_B)){
			playingState = 2;
			resetGrid(true);
		}
		else if(gc.getInput().isKeyPressed(Input.KEY_D)){
			playingState = 3;
			resetGrid(true);
		}
//		else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
//			
//		}
	}

	public void showFurthest(Tile startingTile) {
		// System.out.println("Show Furthest");
		// System.out.println(maxLength);
		int negMaxLength = -1 * maxLength;
		// System.out.println(negMaxLength);
		for (int i = negMaxLength; i <= maxLength; i++) {
			for (int j = negMaxLength; j <= maxLength; j++) {
				// System.out.println("blah");
				int row = startingTile.row + i;
				int col = startingTile.col + j;
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if (mapArray[row][col].walkable && startingTile != mapArray[row][col]) {
						resetGrid(false);
						pathfinding.searchPath(startingTile,mapArray[row][col]);
					}
				}
			}
		}
		pathfinding.endTile.end = false;
//		pathfinding.startTile.start = false;
	}

	static public int getRow(int y) {
		return (y - GameGrid.gridTopOffset)
				/ (GameGrid.rowHeight + GameGrid.gridSpacing);
	}

	static public int getColumn(int x) {
		return (x - GameGrid.gridLeftOffset)
				/ (GameGrid.colWidth + GameGrid.gridSpacing);
	}
}
