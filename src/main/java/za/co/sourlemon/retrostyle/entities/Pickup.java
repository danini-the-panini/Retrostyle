package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.*;

import za.co.sourlemon.retrostyle.Arcade;

/**
 * Any pickup, shield or firepower
 *
 * @author Daniel Smith
 */
public class Pickup {
	
	public static final int
	SHIELD = 0,
	FIRE = 1,
	SPEED = 2,
	BULLET = 3,
	ROCKET = 4,
	LIFE = 5,
	UBER = 6,
	RAILGUN = 7;
	
	/** The x position of this pickup on the screen */
	private float x;
	/** The y position of this pickup on the screen */
	private float y;
	/** The type of pickup this is */
	private int type;
	/** The speed of this pickup */
	private float speed;
	/** This pickup's sprite Image */
	private Image sprite;
	/** The collision shape of this pickup */
	private Polygon collision;
	
	/**
	 * Creates a new pickup with the specified x, y, speed, type, and Image
	 *
	 * @param x The x position this pickup is created at
	 * @param y The y position this pickup is created at
	 * @param speed The speed of this pickup
	 * @param type The type of pickup this is
	 * @param sprite The sprite Image this pickup has
	 */
	public Pickup (float x, float y, float speed, int type, Image sprite) {
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.type = type;
		this.sprite = sprite;
		float[] points = {0,23,6,8,23,0,40,7,46,23,39,40,23,46,6,39};
		collision = new Polygon(points);
		collision.setX(x-23);
		collision.setY(y-23);
	}
	
	public void render(Graphics g) {
		g.drawImage(
			sprite,
			x-sprite.getWidth()/2,
			y-sprite.getHeight()/2);
//		g.setColor(Color.white);
//		String m = "";
//		Color c;
//		switch (type) {
//		case BULLET:
//			c = Color.white;
//			break;
//		case LIFE:
//			c = Color.pink;
//			break;
//		case RAILGUN:
//			m = "R";
//		case SHIELD:
//			c = Color.cyan;
//			break;
//		case SPEED:
//			c = Color.green;
//			break;
//		case UBER:
//			m = "U";
//		case FIRE:
//			c = Color.red;
//			break;
//		case ROCKET:
//			m = "R";
//			c = Color.red;
//			break;
//		default:
//			c = Color.white;
//			break;
//		}
//		
//		g.setColor(c);
//		g.drawOval(collision.getX(), collision.getY(), collision.getWidth(), collision.getHeight());
//		g.drawString(m, collision.getCenterX(), collision.getCenterY());
	}
	
	public void update(int delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		this.x += speed*ratio;
		collision.setX(x-23);
		collision.setY(y-23);
	}
	
	/**
	 * Gets the current x position of this pickup
	 *
	 * @return The current x position of this pickup
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Gets the current y position of this pickup
	 *
	 * @return The current y position of this pickup
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Gets the current speed of this pickup
	 *
	 * @return The current speed of this pickup
	 */
	public float getSpeed() {
		return speed;
	}
	
	/*
	 * Gets the type of pickup this is
	 *
	 * @return The type of pickup this is
	 */
	public int getType() {
		return type;
	}
	
	/**
	 * Gets the current sprite of this pickup
	 *
	 * @return The current sprite of this pickup
	 */
	public Image getSprite() {
		return sprite;
	}
	
	/**
	 * Gets the collision polygon of this pickup
	 *
	 * @return The collision polygon of this pickup
	 */
	public Polygon getPolygon() {
		return collision;
	}
	
}