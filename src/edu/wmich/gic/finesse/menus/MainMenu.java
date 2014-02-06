package edu.wmich.gic.finesse.menus;


import java.awt.Font;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.*;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.Color;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class MainMenu extends BasicGameState {
	OscillatingMapGrid map;
	TrueTypeFont menuFont;
	String title = "Main Menu";
	String[] menuOptions = { "Start Game", "Options", "Exit" };
	int selection = 0;
	
	final int TEXT_Y = 40;
	
	
	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		
		map = OscillatingMapGrid.getInstance();
		
		Font awtFont = new Font("Lucida Console", Font.PLAIN, 48);
	    menuFont = new TrueTypeFont(awtFont, false);
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.render(g);
		
		g.setFont(menuFont);
		g.setColor(Color.white);
		g.drawString("Main Menu", (gc.getWidth()/2) - (menuFont.getWidth(title) / 2), TEXT_Y);
		
		g.drawLine(40, 100, gc.getWidth() - 40, 100);
		
		for (int i = 0; i < menuOptions.length; i++) {
			g.setColor((this.selection == i ? Color.yellow : Color.white));
			g.drawString(menuOptions[i], (gc.getWidth()/2) - (menuFont.getWidth(menuOptions[i]) / 2), TEXT_Y + ((i+1) * 100));
		}
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		
		map.update(gc, delta);
		
		if (gc.getInput().isKeyPressed(Input.KEY_UP)) {
			selection--;
			if (selection < 0) selection = 2;
		} else if (gc.getInput().isKeyPressed(Input.KEY_DOWN)) {
			selection++;
			if (selection > 2) selection = 0;
		}
		
		if (gc.getInput().isKeyPressed(Input.KEY_ENTER)) {
			switch (selection) {
				case 0:
					game.enterState(FinesseGame.game, new FadeOutTransition(), new FadeInTransition());
					break;
				case 1:
					// enterState(optionsMenu);
					break;
				case 2:
					gc.exit();
					break;
			}
		}
	}

	@Override
	public int getID() {
		return FinesseGame.mainMenu;
	}
}
