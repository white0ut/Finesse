package edu.wmich.gic.finesse.menus;


import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class TitleScreen {
	
	OscillatingMapGrid map;
	Image titleImage;
	Input input;
	
	public TitleScreen(){
		map = new OscillatingMapGrid();
		try {
			titleImage = new Image("res/images/TitlePageName.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	public void render(Graphics g) {
		map.render(g);
		g.drawImage(titleImage, 80, 0);
	}
	

	public void update(GameContainer gc, int delta) {
		input = gc.getInput();
		if(input.isKeyPressed(Input.KEY_ENTER)){
			FinesseGame.isTitle = false;
			FinesseGame.isGame = true;
		}
		map.update(gc, delta);
	}
	

}
