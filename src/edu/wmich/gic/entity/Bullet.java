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
	int width = GameGrid.colWidth*3/4;
	int height = GameGrid.rowHeight*3/4;
	Tile shooter;
	Tile collisionTile;
	
	public Bullet(Tile _shooter, int _mouseX, int _mouseY){
		shooter = _shooter;
		x = shooter.x+shooter.width/2-width/2;
		y = shooter.y+shooter.height/2-height/2;
//		velX = _velX;
//		velY = _velY;
		mouseX = _mouseX-width/2;
		mouseY = _mouseY-height/2;
		startX = (int)x;
		startY = (int)y;
		double deltaX = mouseX-x;
		double deltaY = mouseY-y;
		double distance = Math.sqrt(deltaX*deltaX + deltaY*deltaY);
		velX = deltaX * 4 / distance;
		velY = deltaY * 4 / distance;
		endX = Math.abs((int)(deltaX * GameGrid.shootingDiameter / 2 / distance));
		endY = Math.abs((int)(deltaY * GameGrid.shootingDiameter / 2 / distance));
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
		g.setColor(Color.red);
		g.fillOval((float)x, (float)y, width, height);
	}
	
	public boolean update(GameContainer gc, int delta){
		x += velX;
		y += velY;
		if(x > startX + endX || x < startX - endX || y > startY + endY || y < startY - endY){
//			System.out.println(startX +" - "+ endX);
			return false;
		}
		row = GameGrid.getRow((int)y);
		column = GameGrid.getColumn((int)x);
		for(int i = -1; i <= 1;i++){
			for(int j = -1; j <= 1; j++){
				collisionTile = GameGrid.mapArray[row-i][column-j];
//				collisionTile.path = true;
				double vX = (x + (width / 2)+velX) - (collisionTile.x + (collisionTile.width / 2));
				double vY = (y + (height / 2)+velY) - (collisionTile.y + (collisionTile.height / 2));
				int hWidths = (width / 2) + (collisionTile.width / 2);
				int hHeights = (height / 2) + (collisionTile.height / 2);
				if (Math.abs(vX) < hWidths && Math.abs(vY) < hHeights){
					if(GameGrid.mapArray[row-i][column-j].minion != null && GameGrid.mapArray[row-i][column-j] != shooter){
						GameGrid.mapArray[row-i][column-j].minion.action();
						GameGrid.mapArray[row-i][column-j].minion = null;
						return false;
					}
					if(!collisionTile.walkable){
						return false;
					}
				}
			}
		}
		return true;
	}
}
