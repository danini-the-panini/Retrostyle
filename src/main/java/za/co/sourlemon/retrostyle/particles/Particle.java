package za.co.sourlemon.retrostyle.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import za.co.sourlemon.retrostyle.Arcade;

public class Particle {

	double x, y;
	long age = 0;
	long life;
	long lastUpdate = 0;
	double speedx, speedy;
	boolean alive = true;
	int size, startSize, sizeChange;
	Color color;
	
	public Particle(double x, double y, double speed, double direction, int startSize, int sizeChange, long life, Color color) {
		this.x = x;
		this.y = y;
		this.life = life;
		this.size = this.startSize = startSize;
		this.sizeChange = sizeChange;
		this.color = color;
		
		speedx = speed*Math.cos(direction);
		speedy = speed*Math.sin(direction);
		
		lastUpdate = System.currentTimeMillis();
	}
	
	public int update(long delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		x += speedx*ratio;
		y += speedy*ratio;
		size = (int) (startSize + (sizeChange*age)/life);
		age += System.currentTimeMillis()-lastUpdate;
		lastUpdate = System.currentTimeMillis();
		return (alive = age < life) ? 1 : 0;
	}
	
	public void render(Graphics g) {
		color.a = 1f-((float)age/(float)life);
		g.setColor(color);
		g.fillRect((float)x-size/2, (float)y-size/2, size, size);
	}
	
}
