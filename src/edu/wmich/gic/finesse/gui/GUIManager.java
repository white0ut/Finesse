package edu.wmich.gic.finesse.gui;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.Graphics;

public class GUIManager {
	
	private static  List<Button> registeredButtons = new ArrayList<Button>();
	private static  Button clicked = null;
	
	public GUIManager() {
		
	}
	
	public static List<Button> getRegisteredButtons() {
		return registeredButtons;
	}
	
	public static void registerButton(Button b) {
		registeredButtons.add(b);
	}
	
	public static void renderButtons(Graphics g) {
		for (Button b : registeredButtons) {
			b.render(g);
		}
	}
	
	public static void checkForButtonClicks(int button, int x, int y) {
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
	
	public static void buttonRelease(int button, int x, int y) {
		if(button == 0) {
			if(null != clicked) {
				clicked.releaseClick();
				clicked.getActionHandler().onAction();
				clicked = null;
			}
		}
		
	}

}
