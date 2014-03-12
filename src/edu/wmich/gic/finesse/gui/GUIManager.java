package edu.wmich.gic.finesse.gui;

import java.util.ArrayList;
import java.util.List;
import edu.wmich.gic.finesse.gui.Button;

public class GUIManager {
	
	private static  List<Button> registeredButtons = new ArrayList<Button>();
	
	public GUIManager() {
		
	}
	
	public static List<Button> getRegisteredButtons() {
		return registeredButtons;
	}
	
	public static void registerButton(Button b) {
		registeredButtons.add(b);
	}

}
