package edu.wmich.gic.finesse.drawable;

import java.awt.Font;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.TrueTypeFont;

import edu.wmich.gic.entity.Bullet;
import edu.wmich.gic.entity.Minion;
import edu.wmich.gic.entity.Player;
import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.Game;
import edu.wmich.gic.finesse.MainFinesse;
import edu.wmich.gic.finesse.Pathfinding;
import edu.wmich.gic.finesse.Tile;
import edu.wmich.gic.finesse.network.Network;


public class GameGrid {

	// private static final GameGrid INSTANCE = new GameGrid();

	Input input;
	private Network network = null;
	
	public final static int rowHeight = 25;
	public final static int colWidth = 25;
	public final static int gridSpacing = 1;
	public final static int rows = MainFinesse.height / (rowHeight + gridSpacing);
	public final static int columns = MainFinesse.width * 4 / 5 / (colWidth + gridSpacing);
	public final static int gridTopOffset = (MainFinesse.height - (rows * (rowHeight + gridSpacing)))/2;
	public final static int gridLeftOffset = MainFinesse.width - (columns*colWidth + columns*gridSpacing) - gridTopOffset;
	public static int maxDist;
	public static int maxLength;
	public int rowCounter = maxLength * -1;
	public int colCounter = maxLength * -1;
	public int startingMinions = 3;
	public int buyingZoneWidth = 6;
	public int buyingZoneHeight = 6;
	public int minionPurchaseCost = 50;
	public static SpriteSheet sprites;

	public Tile currentMinionTile;
	public Tile enemyMinionTile;
	public static Tile[][] mapArray;// = new Tile[rows][columns];

	private Game parentGame;
	private Pathfinding pathfinding;
	private int timeDelta = 0;

	public Bullet bullet = null;

	private boolean moveMinion = false;
	
	public static Player currentPlayer;

	private int oldRow = 0;
	private int oldColumn = 0;
	
	private static String popupMessage = "";
	private int popupX = MainFinesse.width / 5;
	private int popupY = MainFinesse.height/5;
	private int popupWidth = MainFinesse.width * 3 / 5;
	private int popupHeight = MainFinesse.height * 3 / 5;
	private Font awtBigFont;
	private TrueTypeFont bigFont;
	
	public static int playingState = 0;
	public static int previousState = 0;
	private final int MOVING = 0;
	private final int SHOOTING = 1;
	private final int BUYING = 2;
	private final int DEBUGGING = 3;
	private final int POPUP = 4;
	public String[] playingStateNames = new String[]{"MOVING","SHOOTING","BUYING","DEBUGGING","POPUP"};
	
//	double deltaX = 100;
//	double distance = Math.sqrt(deltaX*deltaX);
	public static int shootingDiameter = 300;

	// private GameGrid() {
	public GameGrid(Game game, Network _network) {
		try {
			sprites = new SpriteSheet(new Image("res/images/tiles.png"),16,16);
		} catch (SlickException e) {
			e.printStackTrace();
		}
		network = _network;
		awtBigFont = new Font("Arial",Font.BOLD, 30);
	    bigFont = new TrueTypeFont(awtBigFont, false);
		parentGame = game;
		maxDist = 40;
		maxLength = (maxDist / 10) + 1;
		pathfinding = new Pathfinding();
		createGrid();
	}


	public void mouseReleased(int button, int x, int y) {
//		send("mouseclick "+button);
		int row = getRow(y);
		int col = getColumn(x);
		if (button == 0) {
			//Moving state logic
			if(playingState == MOVING){
				//only check inside the walls
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if(currentMinionTile != null){ //if a minion is selected
						if(currentMinionTile == mapArray[row][col]){ 
							//if your selected minion is where you clicked, de-select it
							currentMinionTile.minion.selected = false;
							currentMinionTile = null;
							resetGrid(true);
						}
						else{ //if you click on a walkable terrain and there is no minion in the way
							//  then find path
							if (!moveMinion && mapArray[row][col].walkable && mapArray[row][col].minion == null) {
								send(new Object[]{"move",currentMinionTile.row,currentMinionTile.col,row,col});
								resetGrid(true);
								pathfinding.searchPath(currentMinionTile,mapArray[row][col]);
								moveMinion = true;
							}
							else{//if you own the minion you clicked on, select that one and unselect the old one
								if(mapArray[row][col].minion != null && mapArray[row][col].minion.owner == currentPlayer){
									currentMinionTile.minion.selected = false;
									currentMinionTile = mapArray[row][col];
									currentMinionTile.minion.selected = true;
									resetGrid(true);
									showFurthest(currentMinionTile);
								}
							}
						}
					} else{//select the minion you clicked on if you own it
						if(mapArray[row][col].minion != null && mapArray[row][col].minion.owner == currentPlayer){
							currentMinionTile = mapArray[row][col];
							currentMinionTile.minion.selected = true;
							showFurthest(currentMinionTile);
						}
					}
				}
			}//shooting state logic
			else if(playingState == SHOOTING){
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					if(currentMinionTile != null){//if you have a minion selected
						if(mapArray[row][col] == currentMinionTile){
							//deselect minion
							currentMinionTile.minion.selected = false;
							currentMinionTile = null;
							return;
						}
						if(bullet == null){//if there is no bullet, create a new one
							send(new Object[]{"bullet",currentMinionTile.row,currentMinionTile.col,x,y});
							bullet = new Bullet(currentMinionTile, x, y);
						}
					}
					else{//select a minion if it is yours
						if(mapArray[row][col].minion != null && mapArray[row][col].minion.owner == currentPlayer){
							currentMinionTile = mapArray[row][col];
							currentMinionTile.minion.selected = true;
						}
					}
				}
			}//buying state logic
			else if(playingState == BUYING){
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					//if there is no minion where you are clicking
					//then if you are clicking in a buyingzone
					//then if you are the zone owner
					if(mapArray[row][col].minion == null && mapArray[row][col].buyingZone && mapArray[row][col].buyingZoneOwner == currentPlayer){
						//check points vs price
						if(currentPlayer.points >= minionPurchaseCost){
							//create new minion and add to player list
							mapArray[row][col].minion = new Minion(currentPlayer);
							currentPlayer.minions.add(mapArray[row][col].minion);
							currentPlayer.points -= minionPurchaseCost;
							send(new Object[]{"purchase",row,col});
						}
						else{//start popup state and set message
							previousState = playingState;
							popupMessage = "Not Enough Money!";
							playingState = POPUP;
						}
					}
				}
			}
			else if(playingState == DEBUGGING){
				if (row > 0 && col > 0 && row < rows - 1 && col < columns - 1) {
					System.out.println(mapArray[row][col].toString()); //prints out stats for clicked tile
				}
			}
			else if(playingState == POPUP){
				//close popup
				if(x > popupX && x < popupX + popupWidth && y > popupY && y < popupY + popupHeight){
					playingState = previousState;
					popupMessage = "";
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
				oldRow = row; //only perform logic if they move to new tile
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
				mapArray[i][j].resetTile(resetFurthest); //resets pathfinding information
			}
		}
	}

	public void createGrid() { //creates new grid from scratch
		currentMinionTile = null;
		currentPlayer = parentGame.players[0];
		parentGame.players[0].minions.clear();
		parentGame.players[1].minions.clear();
		mapArray = new Tile[rows][columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				try {
					mapArray[i][j] = new Tile(i, j);
				} catch (SlickException e) {
					e.printStackTrace();
				}
				// Check for edges, in which case you can't walk
				if (i == 0 || i == rows - 1 || j == 0 || j == columns - 1) {
					mapArray[i][j].walkable = false;
				}
				//set buying zones for players 0 and 1
				if(j <= buyingZoneWidth && j > 0 && i >= rows - buyingZoneHeight - 1 && i < rows){
					mapArray[i][j].buyingZone = true;
					mapArray[i][j].buyingZoneOwner = parentGame.players[0];
				}
				if(j < columns && j >= columns - buyingZoneWidth - 1 && i > 0 && i <= buyingZoneHeight){
					mapArray[i][j].buyingZone = true;
					mapArray[i][j].buyingZoneOwner = parentGame.players[1];
				}
			}
		}
		//add minions for players 0 and 1
		for(int i = 1; i <= startingMinions; i++){
			int randRow = rows - i - 2;//FinesseGame.rand.nextInt(rows-2)+1;
			int randCol = startingMinions - i + 2;//FinesseGame.rand.nextInt(columns-2)+1;
			mapArray[randRow][randCol].minion = new Minion(parentGame.players[0]);
			parentGame.players[0].minions.add(mapArray[randRow][randCol].minion);
			mapArray[randRow][randCol].walkable = true;
		}
		for(int i = 1; i <= startingMinions; i++){
			int randRow = startingMinions - i + 2;//FinesseGame.rand.nextInt(rows-2)+1;
			int randCol = columns - i - 2;//FinesseGame.rand.nextInt(columns-2)+1;
			mapArray[randRow][randCol].minion = new Minion(parentGame.players[1]);
			parentGame.players[1].minions.add(mapArray[randRow][randCol].minion);
			mapArray[randRow][randCol].walkable = true;
		}
		currentPlayer = parentGame.players[0];

//		showFurthest(currentMinionTile);
	}

	// public static GameGrid getInstance() {
	// return INSTANCE;
	// }

	public void render(Graphics g) {
		g.setColor(Color.red);
		g.drawString(currentPlayer.name+"'s Turn", 30, 30);
		g.setColor(Color.blue);
		g.drawString("Playing State: "+playingStateNames[playingState], 30, 50);
		//g.drawString("Change State\nM = Moving\nS = Shooting\nB = Buying", 30, 50);
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
		if(currentMinionTile != null && playingState == SHOOTING){//Shooting
			g.setColor(Color.red);
			g.setLineWidth(5);
			int circleX = currentMinionTile.x + currentMinionTile.width / 2 - shootingDiameter / 2;
			int circleY = currentMinionTile.y + currentMinionTile.height / 2 - shootingDiameter / 2;
			g.drawOval(circleX, circleY, shootingDiameter, shootingDiameter);
		}
		if (bullet != null) {
			bullet.render(g);
		}
		if(playingState == POPUP){
			g.setColor(new Color(0,0,200,0.9f));
			g.fillRect(popupX,popupY,popupWidth,popupHeight);
			g.setFont(bigFont);
			g.setColor(Color.yellow);
			int stringWidth = bigFont.getWidth(popupMessage);
			g.drawString(popupMessage, popupX + (popupWidth - stringWidth) / 2, popupY+50);
			stringWidth = bigFont.getWidth("This is an alert, click to remove");
			g.drawString("This is an alert, click to remove", popupX + (popupWidth - stringWidth) / 2, popupY+300);
		}
	}

	public void update(GameContainer gc, int delta) {
		timeDelta += delta;
		//moving minion logic and delay
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
		//move bullet logic
		if (bullet != null) {
			if (!bullet.update(gc, delta)) {
				bullet = null;
			}
		}
		//change state only when there are no flying bullets and minions are done moving
		if(bullet == null && moveMinion == false){
			if(gc.getInput().isKeyPressed(Input.KEY_M)){
				playingState = 0;
				if(currentMinionTile != null){
					resetGrid(true);
					showFurthest(currentMinionTile);
				}
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
			else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
				if(currentMinionTile != null){
					currentMinionTile.minion.selected = false;
				}
				currentMinionTile = null;
				resetGrid(true);
				send(new Object[]{"turnend"});
				if(currentPlayer == parentGame.players[0]){
					currentPlayer = parentGame.players[1];
				}
				else if(currentPlayer == parentGame.players[1]){
					currentPlayer = parentGame.players[0];
				}
			}
		}
//		else if(gc.getInput().isKeyPressed(Input.KEY_ENTER)){
//			
//		}
	}

	public void showFurthest(Tile startingTile) { //show the minions possible movements
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

	static public int getRow(int y) { //translates coords to rows
		return (y - GameGrid.gridTopOffset)
				/ (GameGrid.rowHeight + GameGrid.gridSpacing);
	}

	static public int getColumn(int x) { //translates coords to columns
		return (x - GameGrid.gridLeftOffset)
				/ (GameGrid.colWidth + GameGrid.gridSpacing);
	}
	
	private void send(Object[] data){
		String output = "";
		for (Object item : data) {
			output += item.toString() + " ";
		}
		System.out.println(output);
		if(network.state == "connected"){
			network.writer.println(output);
		}
	}
}
