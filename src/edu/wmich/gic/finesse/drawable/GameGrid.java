package edu.wmich.gic.finesse.drawable;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.entity.Bullet;
import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.finesse.Game;
import edu.wmich.gic.finesse.Pathfinding;
import edu.wmich.gic.finesse.Tile;

/*
 * The intent of this Class is to provide a class with a render() and update() method 
 * When you want to draw this grid you simply instantiate is and call these methods.
 */
public class GameGrid {

	// private static final GameGrid INSTANCE = new GameGrid();

	Input input;
	public final int rows = 28;
	public final int columns = 27;
	public final static int rowHeight = 25;
	public final static int colWidth = 25;
	public final static int gridSpacing = 2;
	public final static int gridTopOffset = 0;
	public final static int gridLeftOffset = 300;
	public static int maxDist;
	public static int maxLength;

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

	// private GameGrid() {
	public GameGrid(Game game) {
		parentGame = game;

		maxDist = 40;
		maxLength = (maxDist / 10) + 1;
		pathfinding = new Pathfinding();
		createGrid();
	}

	// public void randomSearch(){
	// pathfinding.searchPath(mapArray[4][4], mapArray[10][10]);
	// }

	public void mouseReleased(int button, int x, int y) {
		// TODO: There is a bug where you can walk over an enemy if the enemy is
		// to the right of you
		if (button == 1) {
			// System.out.println("Release "+((x-GameGrid.gridOffset)/(GameGrid.colWidth+GameGrid.gridSpacing)));
			// System.out.println("Release "+((y-GameGrid.gridOffset)/(GameGrid.rowHeight+GameGrid.gridSpacing)));
			int row = getRow(y);
			int col = getColumn(x);
			if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
				if (mapArray[row][col].walkable
						&& currentMinionTile != mapArray[row][col]) {
					resetGrid(true);
					pathfinding.searchPath(currentMinionTile,
							mapArray[row][col]);
					moveMinion = true;
				}
			}
		} else if (button == 0) {
			bullet = new Bullet(currentMinionTile, x, y);
		}
	}

	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		// System.out.println("Mouse Move");
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
		// TODO: Remove, see below
		
		mapArray[10][10].minion = new Minion(parentGame.computer);
		currentMinionTile = mapArray[10][10];

		mapArray[10][15].minion = new Minion(parentGame.human);
		enemyMinionTile = mapArray[10][15];

		/*
		 * TODO:This will be handled via a loop that draws the minions per
		 * player...we need to be have a minion be "highlighted" when the minion
		 * is clicked...We will then showFurthest() on the currently highlighted
		 * minion
		 */

		showFurthest();
	}

	// public static GameGrid getInstance() {
	// return INSTANCE;
	// }

	public void render(Graphics g) {
		g.setColor(Color.blue);

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
				showFurthest();
			}
			timeDelta = 0;
		}

		if (bullet != null) {
			if (!bullet.update(gc, delta)) {
				bullet = null;
			}
		}
	}

	public void showFurthest() {
		// System.out.println("Show Furthest");
		// System.out.println(maxLength);
		int negMaxLength = -1 * maxLength;
		// System.out.println(negMaxLength);
		for (int i = negMaxLength; i <= maxLength; i++) {
			for (int j = negMaxLength; j <= maxLength; j++) {
				// System.out.println("blah");
				int row = currentMinionTile.row + i;
				int col = currentMinionTile.col + j;
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if (mapArray[row][col].walkable
							&& currentMinionTile != mapArray[row][col]) {
						resetGrid(false);
						pathfinding.searchPath(currentMinionTile,
								mapArray[row][col]);
					}
				}
			}
		}
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
