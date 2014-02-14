package edu.wmich.gic.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.Tile;

public class Minion {
	
	public Image mainImage,selectedImage;
	public int x, y;
	public Player owner;
	private String name;
	public Tile currentTile;
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

	public void action(){
//		System.out.println("Killed Minion");
		owner.points += 100;
	}
	
	public void LoadImages(){
		try {
			mainImage = new Image("res/images/Shy-Minion.png");
			selectedImage = new Image("res/images/Shy-Minion-selected.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
