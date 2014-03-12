package edu.wmich.gic.finesse.menus;


import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.gui.ComponentListener;
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
	private TextField p1TextField;
	private TextField p2TextField;
	private Button finishButton;
	

	
	@Override
	public void init(GameContainer gc, final StateBasedGame game)
			throws SlickException {
		
		
		
		guiManager = new GUIManager();
		
		title = new Image("res/images/FinesseTitle.png");
		
		p1TextField = new TextField(gc, gc.getDefaultFont(), gc.getWidth()/4, 200, 200, 25);
		p1TextField.setText("Player 1 Name");
		p1TextField.setBorderColor(Color.green);
		p1TextField.setTextColor(Color.white);
		guiManager.registerTextField(p1TextField);
		
		
		p2TextField = new TextField(gc, gc.getDefaultFont(), gc.getWidth()/4 *2, 200, 200, 25);
		p2TextField.setText("Player 2 Name");
		p2TextField.setBorderColor(Color.green);
		p2TextField.setTextColor(Color.white);
		guiManager.registerTextField(p2TextField);
		
		
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
		
		if(p2TextField.hasFocus() && p2TextField.getText().equals("Player 2 Name")) {
			p2TextField.setText("");
		} else if (!p2TextField.hasFocus() && p2TextField.getText().equals("")) {
			p2TextField.setText("Player 2 Name");
		}
		
		
		if(p1TextField.hasFocus() && p1TextField.getText().equals("Player 1 Name")) {
			p1TextField.setText("");
		} else if (!p1TextField.hasFocus() && p1TextField.getText().equals("")) {
			p1TextField.setText("Player 1 Name");
		}
//		Input input = gc.getInput();
//		if (input.isKeyPressed(Input.KEY_ENTER)) {
//			game.enterState(ScreenType.GAME.getValue(), null,
//					new FadeInTransition());
//		}
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
