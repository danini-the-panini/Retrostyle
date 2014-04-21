package za.co.sourlemon.retrostyle.particles;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class ParticleSystem {

	public double x, y;
	public static Random r;
	
	public ParticleSystem(double x, double y) {
		this.x = x;
		this.y = y;
		
		r = new Random();
	}
	
	public static double getRandom(double min, double max) {
		return min + (max - min) * r.nextDouble();
	}

	public static Color getRandomColor(Color color1, Color color2) {

		float minRed = Math.min(color1.r, color2.r);
		float maxRed = Math.max(color1.r, color2.r);
		float red = (float) getRandom(minRed, maxRed);

		float minGreen = Math.min(color1.g, color2.g);
		float maxGreen = Math.max(color1.g, color2.g);
		float green = (float) getRandom(minGreen, maxGreen);

		float minBlue = Math.min(color1.b, color2.b);
		float maxBlue = Math.max(color1.b, color2.b);
		float blue = (float) getRandom(minBlue, maxBlue);

		return new Color(red, green, blue);

	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public abstract int update(int delta);
	public abstract void render(Graphics g);
	
}
