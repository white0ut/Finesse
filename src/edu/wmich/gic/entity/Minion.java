package edu.wmich.gic.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Minion {
	
	public Image image;
	public int x, y;
	public Player opponent;
	
	public Minion(Player _opponent){
		opponent = _opponent;
		x=1;
		y=1;
		try {
			image = new Image("res/images/Shy-Minion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void action(){
		System.out.println("Killed Minion");
		opponent.points += 100;
	}
}
