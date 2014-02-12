package edu.wmich.gic.entity;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import edu.wmich.gic.finesse.Tile;
import edu.wmich.gic.finesse.drawable.GameGrid;

public class Bullet {
	double x = 0;
	double y = 0;
	double velX = 0;
	double velY = 0;
	int mouseX = 0;
	int mouseY = 0;
	int startX = 0;
	int startY = 0;
	int endX = 0;
	int endY = 0;
	int row = 0;
	int column = 0;
	int width = GameGrid.colWidth;
	int height = GameGrid.rowHeight;
	Tile shooter;
	
	public Bullet(Tile _shooter, int _mouseX, int _mouseY){
		shooter = _shooter;
		x = shooter.x;
		y = shooter.y;
//		velX = _velX;
//		velY = _velY;
		mouseX = _mouseX-width/2;
		mouseY = _mouseY-height/2;
		startX = (int)x;
		startY = (int)y;
		double deltaX = mouseX-x;
		double deltaY = mouseY-y;
		double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		velX = deltaX / distance * 4;
		velY = deltaY / distance * 4;
		endX = Math.abs((int)(deltaX * 200 / distance));
		endY = Math.abs((int)(deltaY * 200 / distance));
//		delta_x = float(end_x-self.start_x)
//	            delta_y = float(end_y-self.start_y)
//
//	            distance = math.sqrt(delta_x*delta_x + delta_y*delta_y)
//	            if distance == 0:
//	                distance = 1
//	            self.rect.centerx = self.start_x + delta_x / distance *self.step
//	            self.rect.centery = self.start_y + delta_y / distance *self.step
	}
	
	public void render(Graphics g){
		g.setColor(Color.pink);
		g.fillOval((float)x, (float)y, width, height);
	}
	
	public boolean update(GameContainer gc, int delta){
		x += velX;
		y += velY;
		if(x > startX + endX || x < startX - endX){
//			System.out.println(startX +" - "+ endX);
			return false;
		}
		row = GameGrid.getRow((int)y);
		column = GameGrid.getColumn((int)x);
		if(!GameGrid.mapArray[row][column].walkable){
			return false;
		}
		if(GameGrid.mapArray[row][column].minion != null && GameGrid.mapArray[row][column] != shooter){
			GameGrid.mapArray[row][column].minion.action();
			GameGrid.mapArray[row][column].minion = null;
			return false;
		}
		row = GameGrid.getRow((int)y+height);
		column = GameGrid.getColumn((int)x+width);
		if(!GameGrid.mapArray[row][column].walkable){
			return false;
		}
		if(GameGrid.mapArray[row][column].minion != null && GameGrid.mapArray[row][column] != shooter){
			GameGrid.mapArray[row][column].minion.action();
			GameGrid.mapArray[row][column].minion = null;
			return false;
		}
		return true;
	}
}
