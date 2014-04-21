package za.co.sourlemon.retrostyle.entities;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import za.co.sourlemon.retrostyle.Arcade;

public class Star {

	public double x, y, xx;
	public double maxSpeedX, speedx, speedxx, speedy;
	private double min, max;
	private int bx1, bx2, by1, by2;
	private final double ACCELERATION = 0.1f, DECELERATION = 0.05f, FACTOR = 1000.0f;
	private boolean negx = false, negy = false;
	private float cval;
	
	public Star(double min, double max, int bx1, int bx2, int by1, int by2) {

		this.min = min;
		this.max = max;
		
		this.bx1 = bx1;
		this.bx2 = bx2;
		
		this.by1 = by1;
		this.by2 = by2;
		
		begin();
		
		randomX();
		
	}
	
	public void begin() {
		speedxx = 0;
		speedy = 0;
		
		Random r = new Random();
		x = bx2;
		int i = by2-by1;
		y = by1 + r.nextInt(i);
		double j = max - min;
		speedx = maxSpeedX = min + r.nextFloat()*j;
		cval = (float) ((maxSpeedX-min)/(max-min));
	}
	
	private void randomX() {
		Random r = new Random();
		int i = bx2-bx1;
		x = bx1 + r.nextInt(i);
	}
	
	public void update(int delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;

		if (speedx < maxSpeedX) speedx += ACCELERATION*ratio;
		x -= speedx;
		
		x -= speedxx;
		y -= speedy;
		
		double dec = DECELERATION*ratio;
		
		if (speedxx > 0) {
			if (negx) speedxx = 0;
			else speedxx -= dec;
		}
		else if (speedxx < 0) {
			if (negx) speedxx += dec;
			else speedxx = 0;
		}
		if (speedy > 0) {
			if (negy) speedy = 0;
			else speedy -= dec;
		}
		else if (speedy < 0) {
			if (negy) speedy += dec;
			else speedy = 0;
		}
		
		if (x <= bx1 || x >= bx2 || y <= by1 || y >= by2) {
			begin();
		}
	}
	
	public void render(Graphics g) {
		g.setColor(new Color(1,1,1,cval));
		g.fillRect((int)x, (int)y, 2+3*(cval), 2);
	}
	
	public void explode(double centerx, double centery, double distx, double disty, int magnitude) {
		
		if (maxSpeedX < min+(max-min)/4) return;
		
		double dist = Math.sqrt(distx*distx+disty*disty);
		double newx = ((magnitude-dist)/dist)*distx;
		double newy = ((magnitude-dist)/dist)*disty;
		
		speedxx = newx*magnitude/FACTOR*(maxSpeedX/(max-min));
		negx = speedxx < 0;
		
		speedy = newy*magnitude/FACTOR*(maxSpeedX/(max-min));
		negy = speedy < 0;

		speedx = 0;
		
	}
	
	public void shiftVert(float speedx, float speedy) {
		if (cval>0.5f) {
			this.speedy = speedy*(cval);
			negy = speedy < 0;
			this.speedxx = speedx*(cval);
			negx = speedx < 0;
		}
	}

}
