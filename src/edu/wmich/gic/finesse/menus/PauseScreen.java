package edu.wmich.gic.finesse.menus;

import org.lwjgl.input.Mouse;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.drawable.MapGrid;

public class PauseScreen {
	
	MapGrid map = new MapGrid();
	private int BUTTON_WIDTH = 100, BUTTON_HEIGHT = 50,BUTTON_NUM=3;
	private int[] isHover={0,0,0};
	
	private int count=0;
	public PauseScreen(){
		
		
	}
	public void render(Graphics g) {
		//TODO
		map.render(g);
		drawButton(g,Color.gray,0);
		drawButton(g,Color.gray,1);
		drawButton(g,Color.gray,2);
		// Broken
		for (int i=0; i<BUTTON_NUM; i++) {
			if(Mouse.getX() > (FinesseGame.width/5)*(i+1) && (Mouse.getX() < (FinesseGame.width/5)*(i+1)+BUTTON_WIDTH)){
				if(Mouse.getY() > (FinesseGame.height/4)*2 && (Mouse.getY() < (FinesseGame.height/4)*2+BUTTON_HEIGHT)){
					isHover[i]=isHover[i]+10;
					System.out.println("Were here");
				} else isHover[i]=0;
			} else isHover[i]=0;
		}
		for(int i=0;i<BUTTON_NUM; i++){
			if(isHover[i]!=0){
				if(isHover[i]!=256){
					drawButton(g, new Color(isHover[i],isHover[i],isHover[i]),i);
				}

			}
		}
		
	}

	public void update(GameContainer gc, int delta) {
		map.update(gc, delta);
		//TODO
		//Debugging purposes
		count++;
		if(count>100){
		System.out.println(Mouse.getX()+" : "+Mouse.getY());
		System.out.println(FinesseGame.width/5+" : "+FinesseGame.height/5);
		count=0;
		}
		
	}
	
	private void drawButton(Graphics g, Color BColor, int i){
		g.setColor(BColor);
		g.fillRoundRect((FinesseGame.width/5)*(i+1), (FinesseGame.height/4), BUTTON_WIDTH, BUTTON_HEIGHT, 8);
	}
}
