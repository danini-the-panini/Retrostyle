package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.particles.ExplosionParameters;
import za.co.sourlemon.retrostyle.particles.Trail;
import za.co.sourlemon.retrostyle.particles.TrailParameters;
import za.co.sourlemon.retrostyle.states.InGameState;
import za.co.sourlemon.retrostyle.states.LoadState;

/**
 * The player's ship
 *
 * @author Daniel Smith
 */
public class Player extends Ship {
	
	public boolean up, down, left, right;
	
	/** The x speed of the player */
	private int speed = 0;
	/** The Acceleration of the player when an arrow key is pressed */
	private final float ACC = 1f;
	/** The Deceleration of the player when an arrow key is released */
	private final float DEC = 0.5f;
	/** The maximum speed of the player */
	private final float MAX = 5f;
	/** The x speed of the player */
	private float x_speed;
	/** The y speed of the player */
	private float y_speed;
	/** True if player has just been destroyed in some way */
	private boolean dead, invincible;
	
	/** The time at which the player last regenerated */
	private long lastRegen;
	/** The interval between regenerations */
	private long regenInterval;
	
	private Trail trail;
	private Image sprite, isprite;

	private long timeDead;
	private final long deathLingerTime = 2000;
	
	/**
	 * Creates a new Player with the specified y position
	 *
	 * @param x The location the player will be on the x axis
	 * @param y The location the player will be on the y axis
	 */	
	public Player (int x, int y, int maxHealth, int maxShield, InGameState game) {
		super(x,y,maxHealth,maxShield,game);
		float[] points = {
				23f,0f,
				48f,19f,
				23f,39f,
				23f,30f,
				-5f,30f,
				-5f,8f,
				23f,8f};
		hitBox = new Polygon(points);
		sprite = LoadState.img_ship;
		isprite = LoadState.img_invincible;
		trail = new Trail(x-sprite.getWidth(), y, TrailParameters.PLAYER);
		
		dead = false;
		invincible = true;

		regenInterval = 2500;
		lastRegen = System.currentTimeMillis();
		
		x_speed = 0f;
		y_speed = 0f;
	}
	
	public void render(Graphics g) {
		if (dead) return;
		trail.render(g);
		if (invincible) g.drawImage(isprite,x-59,y-34);
		g.drawImage(sprite,x-sprite.getWidth(),y-sprite.getHeight()/2);
//		g.setColor(Color.red);
//		g.draw(hitBox);
	}
	
	/**
	 * Moves this player
	 *
	 * @param direction The direction that it should move
	 */
	public void update(int delta) {
		if (dead) {
			timeDead += delta;
			if (timeDead >= deathLingerTime) game.gameOver();
			return;
		}
		if (invincible()) tryToRegen();
		trail.set(x-sprite.getWidth(), y);
		trail.update(delta);
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		// if up_pressed then accelerate to maximum speed in the negative y direction
		float acc = ACC*ratio;
		float dec = DEC*ratio;
		if (up && !down) {
			if (y_speed >= -MAX) y_speed-=acc;
		// if down_pressed then accelerate to maximum speed in the positive y direction
		} else if (down && !up) {
			if (y_speed <= MAX) y_speed+=acc;
		// otherwise decelerate to a standstill
		} else {
			if (y_speed < 0) y_speed+=dec;
			else if (y_speed > 0) y_speed-=dec;
		}
		
		// if right_pressed then accelerate to maximum speed in the negative x direction
		if (left && !right) {
			if (x_speed >= -MAX) x_speed-=acc;
		// if left_pressed then accelerate to maximum speed in the positive x direction
		} else if (right && !left) {
			if (x_speed <= MAX) x_speed+=acc;
		// otherwise decelerate to a standstill
		} else {
			if (x_speed < 0) x_speed+=dec;
			else if (x_speed > 0) x_speed-=dec;
		}
		
				// make sure the ship doesn't go off the screen
		// left border
		if (x_speed < 0 && x >= 60) x += x_speed*ratio;
		// right border
		else if (x_speed > 0 && x <= Arcade.WIDTH-5) x += x_speed*ratio;
		// top border
		if (y_speed < 0 && y >= 70) y += y_speed*ratio;
		// bottom border
		else if (y_speed > 0 && y <= Arcade.HEIGHT-70) y += y_speed*ratio;
		
		hitBox.setX(x-53);
		hitBox.setY(y-19);
	}
	
	/**
	 * Gets the current x position of the player
	 *
	 * @return The current x position of the player
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Gets the current y position of the player
	 *
	 * @return The current y position of the player
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Gets the current speed of the player
	 *
	 * @return The current speed of the player
	 */
	public int getSpeed() {
		return speed;
	}
	
	public void reset() {
		up = false;
		down = false;
		left = false;
		right = false;
	}
	
	public boolean dead() {
		return dead;
	}
	
	public void kill() {
		dead = true;
		game.explode(
				(int)x-25,
				(int)y,
				ExplosionParameters.PLAYER_DESTROYED
			);
	}
	
	public boolean invincible() {
		return invincible;
	}
	
	public void tryToRegen() {
		// check that we have waiting long enough to regen
		if (System.currentTimeMillis() - lastRegen < regenInterval) {
			return;
		}
		
		// if we waited long enough...
		invincible = false;
	}
	
	public void unPause(int delta) {
		lastRegen += delta;
	}

	@Override
	public boolean hit(Hit hit) {
		int hx = (int)x-25, hy = (int)y;
		if (hit.v != null) {
			hx = (int) hit.v.x;
			hy = (int) hit.v.y;
		}
		game.explode(hx,hy,
			hit.type==Hit.SHIELD ?
				ExplosionParameters.SHIELD_HIT :
				ExplosionParameters.BULLET_DESTROYED);
		if (health <= 0) {
			kill();
			return false;
		}
		return true;
	}
}