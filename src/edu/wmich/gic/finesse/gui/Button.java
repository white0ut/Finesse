package edu.wmich.gic.finesse.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Button {

	private Shape buttonShape;
	private Image image;
	private int x, y, width, height;
	private ActionHandler actionHandler;

	public Button(int x, int y, int width, int height, Image image,
			ActionHandler action) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		buttonShape = new Rectangle(x, y, width, height);
		this.image = image;
		this.actionHandler = action;
	}

	public Button(int x, int y, int width, int height, ActionHandler action) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.image = null;
		this.actionHandler = action;
		buttonShape = new Rectangle(x, y, width, height);

	}

	public boolean checkClick(int x, int y) {
		System.out.println("Checking for inclusion"+buttonShape.includes(x, y));
		return buttonShape.includes(x, y);
	}

	public void render(Graphics g) {
		if (null != image) {
			g.drawImage(image, buttonShape.getX(), buttonShape.getY());
		} else {
			g.drawRect(x, y, width, height);
		}

	}

	public ActionHandler getActionHandler() {
		return actionHandler;
	}
}
