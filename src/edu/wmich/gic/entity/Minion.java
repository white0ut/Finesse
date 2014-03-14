package edu.wmich.gic.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.Tile;
import edu.wmich.gic.finesse.drawable.GameGrid;

public class Minion {
	
	public Image mainImage,selectedImage;
	public int x, y;
	public Player owner;
	private String name;
	public Tile currentTile;
	public int width = GameGrid.colWidth*5/4;
	public int height = GameGrid.rowHeight*5/4;
	public boolean selected = false;
	
	public Minion(Player _owner){
		x=1;
		y=1;
		owner =  _owner;
		name = "Fred";
		LoadImages();
	}
	//FFS obviously our minions need names
	public Minion(Player _owner, String _name){
		x=1;
		y=1;
		name = _name;
		owner =  _owner;
		LoadImages();
	}
	private String getName(){return name;}

	public void death(Player killer){
		System.out.println("Killed Minion");
		owner.minions.remove(this);
		killer.points += 100;
	}
	
	public void render(Graphics g, int x, int y){
		int offsetX = x - (width-GameGrid.colWidth)/2;
		int offsetY = y - (height-GameGrid.rowHeight)/2;
		if(selected){
			g.drawImage(selectedImage, offsetX, offsetY);
		}else{
			g.drawImage(mainImage, offsetX, offsetY);
		}
	}
	
	public void LoadImages(){
		try {
			mainImage = new Image("res/images/Shy-Minion.png").getScaledCopy(width, height);
			selectedImage = new Image("res/images/Shy-Minion-selected.png").getScaledCopy(width, height);
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
