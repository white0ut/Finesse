package edu.wmich.gic.finesse.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.gui.TextField;

public class GUIManager {
	
	private  List<Button> registeredButtons = new ArrayList<Button>();
	private List<TextField> registeredTextFields = new ArrayList<TextField>();
	private  Button clicked = null;
	
	
	public GUIManager() {
		
	}
	
	public List<Button> getRegisteredButtons() {
		return registeredButtons;
	}
	
	public void registerButton(Button b) {
		registeredButtons.add(b);
	}
	
	
	public void checkForButtonClicks(int button, int x, int y) {
		if(button == 0) {
			for(Button b : getRegisteredButtons()) {
				if (b.checkClick(x, y)) {
					if(b.hasImage()){
						b.drawDownClick();
					}
					clicked = b;
					
				}
			}
		}
		
	}
	
	public void buttonRelease(int button, int x, int y) {
		if(button == 0) {
			if(null != clicked) {
				clicked.releaseClick();
				clicked.getActionHandler().onAction();
				clicked = null;
			}
		}
		
	}
	
	public void registerTextField(TextField t) {
		registeredTextFields.add(t);
	}
	
	public List<TextField> getRegisterTextFields() {
		return registeredTextFields;
	}

	
	public void render(GameContainer gc, Graphics g) {
		for(Button b : registeredButtons) {
			b.render(g);
		}
		for(TextField t : registeredTextFields) {
			t.render(gc, g);
		}
	}
}
