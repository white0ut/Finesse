package edu.wmich.gic.finesse.gui;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

public class Button {

	//private Shape buttonShape;
	private Image buttonImage, clickedImage, currentImage, hoverImage;
	private int x, y, width, height;
	private ActionHandler actionHandler;
	private String text;

	public Button(int x, int y, int width, int height, Image image,
			ActionHandler action) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		//buttonShape = new Rectangle(x, y, width, height);
		buttonImage = image;
		clickedImage = null;
		hoverImage = null;
		currentImage = buttonImage;
		actionHandler = action;
		text = "";
	}

	public Button(int x, int y, int width, int height, ActionHandler action) {

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		buttonImage = null;
		clickedImage = null;
		hoverImage = null;
		currentImage = buttonImage;
		actionHandler = action;
		text = "";
		//buttonShape = new Rectangle(x, y, width, height);

	}

	public boolean checkClick(int clickX, int clickY) {
		return (clickX > x && clickX < x + width &&
					clickY > y && clickY < y + height);
	}
	
	public void drawDownClick() {
		currentImage = clickedImage;
	}
	
	public void releaseClick() {
		currentImage = buttonImage;
	}

	public void render(Graphics g) {
		if (null != buttonImage) {
			g.drawImage(currentImage, x, y);
			g.drawString(text, x, y);
		} else {
			g.drawRect(x, y, width, height);
			g.drawString(text, x, y);
		}

	}
	
	public void enableHoverImage() {
		currentImage = hoverImage;
	}
	
	public boolean isHovered() {
		return currentImage == hoverImage;
	}
	
	public void setHoverImage(Image image) {
		hoverImage = image;
	}
	
	public boolean hasHoverImage() {
		return null != hoverImage ? true : false;
	}
	
	public void setClickedImage(Image image) {
		clickedImage = image;
	}
	
	public boolean hasClickedImage() {
		return null != clickedImage ? true : false;
	}
	
	public void setText(String t) {
		text = t;
	}

	public ActionHandler getActionHandler() {
		return actionHandler;
	}
	
	public boolean hasImage() {
		return null != buttonImage ? true : false;
	}
}
