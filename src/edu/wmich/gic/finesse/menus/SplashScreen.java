package edu.wmich.gic.finesse.menus;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class SplashScreen extends BasicGameState {

	OscillatingMapGrid map;
	Image splashImage;
	private int timer;

	@Override
	public void init(GameContainer gc, StateBasedGame game)
			throws SlickException {
		
		try {
			splashImage = new Image("res/images/GICLogo.png");
		} catch (SlickException e) {
			e.printStackTrace();
		}
		map = OscillatingMapGrid.getInstance();
		timer = 0;
	}

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		map.render(g);
		g.drawImage(splashImage, 
				(gc.getWidth() / 2) - (splashImage.getWidth() / 2), 
				(gc.getHeight() / 2) - (splashImage.getHeight() / 2));
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		map.update(gc, delta);
		Input input = gc.getInput();
		if(input.isKeyDown(Input.KEY_ENTER))
			game.enterState(ScreenType.TITLESCREEN.getValue());
		if ((timer += 0.75 * delta) > 2000) {
			
			game.enterState(ScreenType.TITLESCREEN.getValue(),
					new FadeOutTransition(), new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return ScreenType.SPLASHSCREEN.getValue();
	}

}
