package edu.wmich.gic.finesse.menus;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.TextField;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;

import edu.wmich.gic.finesse.FinesseGame.ScreenType;
import edu.wmich.gic.finesse.gui.ActionHandler;
import edu.wmich.gic.finesse.gui.Button;
import edu.wmich.gic.finesse.gui.GUIManager;

public class SetupMenu extends BasicGameState {

	private GUIManager guiManager;
	
	private Image title;
	private TextField test;
	private Button finishButton;
	

	
	@Override
	public void init(GameContainer gc, final StateBasedGame game)
			throws SlickException {
		
		guiManager = new GUIManager();
		
		title = new Image("res/images/FinesseTitle.png");
		
		test = new TextField(gc, gc.getDefaultFont(), gc.getWidth()/4, 200, 200, 50);
		test.setText("THIS IS A HUGE TEST");
		test.setBorderColor(Color.green);
		test.setTextColor(Color.white);
		guiManager.registerTextField(test);
		
		finishButton = new Button(200, 500, 100, 50, new ActionHandler() {

			@Override
			public void onAction() {
				game.enterState(ScreenType.GAME.getValue(), null,
						new FadeInTransition());
				
			}
		});
		finishButton.setText("Finished");
		guiManager.registerButton(finishButton);
		
		
	}
	

	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g)
			throws SlickException {
		
		g.drawImage(title, (gc.getWidth() / 2)- (title.getWidth() / 2), 10);
		guiManager.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame game, int delta)
			throws SlickException {
		
		Input input = gc.getInput();
		if (input.isKeyPressed(Input.KEY_ENTER)) {
			game.enterState(ScreenType.GAME.getValue(), null,
					new FadeInTransition());
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		guiManager.buttonRelease(button, x, y);
		
	}
	
	@Override 
	public void mousePressed(int button, int x, int y) {
		guiManager.checkForButtonClicks(button, x, y);
	}

	@Override
	public int getID() {
		return ScreenType.SETUPMENU.getValue();
	}

}
