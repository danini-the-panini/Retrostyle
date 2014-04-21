package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.*;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.particles.Trail;
import za.co.sourlemon.retrostyle.particles.TrailParameters;

/**
 * The class which defines each bullet in the game,
 * Both player and Enemy
 *
 * @author Daniel Smith
 */
public class Bullet {
	
	/** The x position of this bullet on the screen */
	private float x;
	/** The y position of this bullet on the screen */
	private float y;
	
	private float lastX, lastY;
	public int damage;
	
	/** The x speed of this bullet */
	private float xSpeed;
	/** The y speed of this bullet */
	private float ySpeed;
	/** The acceleration of this bullet */
	private float xacc;
	private float yacc;
	/** This bullet's sprite Image */
	private Image sprite;
	/** The enemy this bullet came from (if any) */
	private Enemy enemy;
	/** the nice fancy trail the bullet leaves behind :) */
	Trail trail;
	
	Gun gun;
	
	/**
	 * Creates a new bullet with the specified x, y, x speed, y speed, and Image
	 *
	 * @param x The x position this bullet is created at
	 * @param y The y position this bullet is created at
	 * @param speed The initial speed of this bullet
	 * @param sprite The sprite Image this bullet has
	 */
	public Bullet(float x, float y, float xSpeed, float ySpeed, float xacc, float yacc,
			int damage, Image sprite, Enemy enemy, Gun gun) {
		this.x = lastX = x;
		this.y = lastY = y;
		this.xSpeed = xSpeed;
		this.ySpeed = ySpeed;
		this.xacc = xacc;
		this.yacc = yacc;
		this.damage = damage;
		this.sprite = sprite;
		this.enemy = enemy;
		this.gun = gun;
		
		trail = new Trail(x,y,(enemy == null) ? TrailParameters.PLAYER_BULLETS : TrailParameters.ENEMY_BULLETS);
	}
	
	/**
	 * Moves this bullet according to its speed
	 */
	public void update(int delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		xSpeed += xacc*ratio;
		ySpeed += yacc*ratio;
		lastX = x;
		lastY = y;
		x += xSpeed*ratio;
		y += ySpeed*ratio;
		
		// update the trail
		trail.setX(x);
		trail.setY(y);
		trail.update(delta);
	}
	
	/**
	 * Gets the current x position of this bullet
	 *
	 * @return The current x position of this bullet
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Gets the current y position of this bullet
	 *
	 * @return The current y position of this bullet
	 */
	public float getY() {
		return y;
	}
	
	public float getLastX() {
		return lastX;
	}
	public float getLastY() {
		return lastY;
	}
	
	/**
	 * Gets the current x speed of this bullet
	 *
	 * @return The current x speed of this bullet
	 */
	public float getXSpeed() {
		return xSpeed;
	}
	
	/**
	 * Gets the current y speed of this bullet
	 *
	 * @return The current y speed of this bullet
	 */
	public float getYSpeed() {
		return ySpeed;
	}
	
	/**
	 * Gets the current acceleration of this bullet
	 *
	 * @return The current acceleration of this bullet
	 */
	public float getXAcc() {
		return xacc;
	}
	public float getYAcc() {
		return yacc;
	}
	
	/**
	 * Gets the enemy this bullet came from (if any)
	 *
	 * @return The enemy this bullet came from (if any)
	 */
	public Enemy getEnemy() {
		return enemy;
	}
	
	public Gun getGun() {
		return gun;
	}

	public void render(Graphics g) {
		g.drawImage(sprite,(int)(x-6),(int)(y-sprite.getHeight()/2));
		trail.render(g);
	}
	
}