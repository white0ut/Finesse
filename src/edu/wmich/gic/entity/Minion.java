package edu.wmich.gic.entity;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Minion {
	
	public Image image;
	public int x, y;
	
	public Minion(){
		x=1;
		y=1;
		try {
			image = new Image("res/images/Shy-Minion.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
