package edu.wmich.gic.finesse.drawable;

import java.util.PriorityQueue;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Circle;

import edu.wmich.gic.finesse.FinesseGame;
import edu.wmich.gic.finesse.MainFinesse;

public class OscillatingMapGrid {

	// I am fascinated right now... --Kenny
	private static final OscillatingMapGrid INSTANCE = new OscillatingMapGrid();
	// 0_0 ^^^^^^^^^^^^^^^^^^^^^^^^^^ 0_0 THIS IS GENIUS 0_0

	public static OscillatingMapGrid getInstance() {
		return INSTANCE;
	}

	private Control[] color = { new Control(10, 14, false),
			new Control(10, 14, false), new Control(10, 14, false) };
	private Control[] pulses = new Control[20];
	private static final double defaultBrightness = .5;
	private static final int numCircles = 7, radius = 85;

	// Private constructor prevents instantiation from other classes
	private OscillatingMapGrid() {
		for (int i = 0; i < pulses.length; i++)
			pulses[i] = new Control(10, 14, true);
	}

	public void render(Graphics g) {
		g.setColor(getColor(0, defaultBrightness));
		g.fillRect(0, 0, MainFinesse.width, MainFinesse.height);
		
		PriorityQueue<Circ> heap=new PriorityQueue<Circ>();
		for (int circ = 0; circ < numCircles; circ++) {
			for (int i = 0; i < pulses.length; i++) {
				pulses[i].drawPulse(g,(MainFinesse.width * (i + 1) / (pulses.length + 1)),circ,heap);
			}
		}
		while(!heap.isEmpty()) heap.remove().draw(g);
		
		g.setColor(Color.black);
		for (int x = 0; x < MainFinesse.width / 32 + 1; x++) {
			for (int y = 0; y < (MainFinesse.height / 32) + 1; y++) {
				g.fillRect(x * 32, y * 32, 31, 31);
			}
		}
	}

	public void update(GameContainer gc, int delta) {
		double time = delta / 1000.0;

		// Update background color
		for (Control c : color)
			c.update(time);

		// Update background pulses
		for (Control c : pulses)
			c.update(time);
	}

	public Color getColor(double min, double max) {
		return new Color(color[0].getColor(min, max), color[1].getColor(min,
				max), color[2].getColor(min, max));
	}

	private class Control {
		double start, end, len, time, minLen, maxLen;
		boolean jump;

		Control(double minlen, double maxLen, boolean jump) {
			time = 0;
			this.minLen = minlen;
			this.maxLen = maxLen;
			this.jump = jump;
			change();
		}

		void change() {
			if (jump)
				start = FinesseGame.rand.nextDouble();
			else
				start = end;
			end = FinesseGame.rand.nextDouble();
			len = minLen + (maxLen - minLen) * FinesseGame.rand.nextDouble();
		}

		void update(double seconds) {
			time += seconds;
			while (time >= len) {
				time -= len;
				change();
			}
		}

		double getValue() {
			return start + (end - start) * time / len;
		}

		int getColor(double min, double max) {
			double x = min + (max - min) * getValue();
			return Math.max(0, Math.min(255, (int) (x * 255 + .5)));
		}

		void drawPulse(Graphics g, int pos, int i, PriorityQueue<Circ> heap) {
			double bright1 = .3;
			double bright2 = .5;

			OscillatingMapGrid mg = OscillatingMapGrid.this;

			int coord = (int) (getValue() * (MainFinesse.height - 1) + .5);
			double brightness = 1 - Math.cos(2 * Math.PI * time / len);
			double intensity = (i + 1.0) / numCircles * brightness;
			int r = radius * (numCircles - i) / numCircles;

			heap.add(new Circ(pos,coord,r,intensity));
		}
	}
	
	private class Circ implements Comparable<Circ>{
		int x,y,r;
		double bright,bright1=.3,bright2=.5;
		Circ(int x,int y,int r,double bright){this.x=x;this.y=y;this.r=r;this.bright=bright;}
		
		void draw(Graphics g){
			g.setColor(OscillatingMapGrid.this.getColor(
				bright1*bright,
				defaultBrightness+bright2*bright
			));
			g.draw(new Circle(x,y,r));
			g.fill(new Circle(x,y,r));
		}

		@Override
		public int compareTo(Circ arg0){
			double ob=arg0.bright;
			if(bright>ob) return 1;
			if(bright<ob) return -1;
			return 0;
		}
	}
}
