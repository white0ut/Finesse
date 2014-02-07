package edu.wmich.gic.finesse.menus;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import org.newdawn.slick.state.transition.*;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class TitleScreen extends BasicGameState {
	
	OscillatingMapGrid map;
	Image titleImage;
	Input input;

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1)
			throws SlickException {
		
		map = OscillatingMapGrid.getInstance();
		try {
			titleImage = new Image("res/images/TitlePageName.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		map.render(g);
		g.drawImage(titleImage, (gc.getWidth()/2)-(titleImage.getWidth()/2), (gc.getHeight() / 2) - (titleImage.getHeight() / 2) );
		
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		
		input = gc.getInput();
		map.update(gc, delta);
		
		if (input.isKeyPressed(Input.KEY_ENTER)){
			game.enterState(FinesseGame.mainMenu, new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return FinesseGame.titleScreen;
	}
}
