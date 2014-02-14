package edu.wmich.gic.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Minion {
	
	public Image image;
	public int x, y;
	public Player owner;
	private String name;
	
	public Minion(Player _owner){
		x=1;
		y=1;
		owner =  _owner;
		name = "Fred";
		try {
			image = new Image("res/images/Shy-Minion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	//FFS obviously our minions need names
	public Minion(Player _owner, String _name){
		x=1;
		y=1;
		name = _name;
		owner =  _owner;
		try {
			image = new Image("res/images/Shy-Minion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	private String getName(){return name;}

	public void action(){
		System.out.println("Killed Minion");
		owner.points += 100;
	}
}
