package edu.wmich.gic.finesse.menus;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.drawable.OscillatingMapGrid;

public class SetupMenu extends BasicGameState {

	OscillatingMapGrid map;
	private int temp;
	TextField test;
	@Override
	public void init(GameContainer gc, StateBasedGame arg1)
			throws SlickException {
		map = OscillatingMapGrid.getInstance();
		temp = 0;
	}
	

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		map.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		map.update(gc, delta);
		if ((temp += 0.5 * delta) > 1000) {
			game.enterState(ScreenType.GAME.getValue(), null,
					new FadeInTransition());
		}
	}

	@Override
	public int getID() {
		return ScreenType.SETUPMENU.getValue();
	}

}
