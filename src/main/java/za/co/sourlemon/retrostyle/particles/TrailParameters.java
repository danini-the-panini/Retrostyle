package za.co.sourlemon.retrostyle.particles;

import org.newdawn.slick.Color;

public class TrailParameters {

	public static final TrailParameters PLAYER_BULLETS = new TrailParameters(
			0.5d, 2d, Math.PI, 10, 15, -5, 100, 200, 10,
			new Color(0xff5a00), new Color(0xff0000));
	public static final TrailParameters PLAYER = new TrailParameters(
			1d, 2d, Math.PI, 10, 15, -10, 100, 200, 10,
			new Color(0xff5a00), new Color(0xff0000));
	public static final TrailParameters ENEMY_BULLETS= new TrailParameters(
			0.5d, 2d, 0d, 10, 15, -5, 100, 200, 20,
			new Color(0x00dd00), new Color(0x00ff00));
	public static final TrailParameters ENEMY= new TrailParameters(
			0.5d, 2d, 0d, 10, 15, 30, 200, 400, 40,
			new Color(0xff5a00), new Color(0xff0000));
	
	double minSpeed, maxSpeed;
	double direction;
	int minSize, maxSize;
	int sizeChange; // can be negative
	long minLife, maxLife, timeGap;
	Color color1, color2;

	public TrailParameters(double minSpeed,
			double maxSpeed, double direction, int minSize, int maxSize, int sizeChange, long minLife,
			long maxLife, long timeGap, Color color1, Color color2) {
		
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.direction = direction;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.sizeChange = sizeChange;
		this.minLife = minLife;
		this.maxLife = maxLife;
		this.timeGap = timeGap;
		this.color1 = color1;
		this.color2 = color2;

	}
	
}
