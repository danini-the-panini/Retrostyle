package za.co.sourlemon.retrostyle.entities;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.*;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.particles.Explosion;
import za.co.sourlemon.retrostyle.particles.ExplosionParameters;
import za.co.sourlemon.retrostyle.particles.Trail;
import za.co.sourlemon.retrostyle.particles.TrailParameters;
import za.co.sourlemon.retrostyle.states.InGameState;
import za.co.sourlemon.retrostyle.states.LoadState;

/**
 * The class describing an Enemy you must shoot at
 *
 * @author Daniel Smith
 */
public class Enemy extends Ship{
	
	public float speed;
	/** The collision shape of this enemy */
	private Polygon hitBox0;
	private Polygon hitBox1;
	private Polygon hitBox2;
	/** The time at which this enemy last fired a bullet */
	private long lastFire = 0;
	/** The firing interval of this enemy */
	private int firingInterval = 800;
	
	private Trail[] trail;
	private Explosion explosion;
	
	private int type = 0;
	
	public Enemy (float y, float speed, int type, int maxHealth, InGameState state) {
		super(Arcade.WIDTH, y, maxHealth, 0, state);
		this.speed = speed;
		this.type = type;
		
		float[] points = {0,19,47,0,47,38};
		hitBox0 = new Polygon(points);
		
		float[] points1 = {5,39,100,0,100,78};
		hitBox1 = new Polygon(points1);
		
		float[] points2 = {0,59,145,3,145,115};
		hitBox2 = new Polygon(points2);
		
		switch (type) {
		case 0:
			hitBox =  hitBox0;
			break;
		case 1:
			hitBox =  hitBox1;
			break;
		case 2:
		default:
			hitBox =  hitBox2;
			break;
		}
		
		switch (type) {
		case 0:
			sprite = LoadState.img_enemy;
			break;
		case 1:
			sprite = LoadState.img_enemy1;
			break;
		case 2:
			sprite = LoadState.img_enemy2;
			break;
		default:
			sprite = LoadState.img_enemy;
			break;
		}
		
		trail = new Trail[type + 1];
		explosion = new Explosion((int)x, (int)y, ExplosionParameters.ENEMY_DESTROYED[type]);
	}
	
	public Enemy (float y, float speed, int type, InGameState state) {
		this(y,speed,type,type == 0 ? 5 : (type == 1 ? 20 : 30), state);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, x-2, y-sprite.getHeight()/2);
//		g.setColor(Color.white);
//		g.drawString(""+health, x,y);
		for (int i = 0; i < trail.length; i++) if (trail[i] != null) trail[i].render(g);
//		g.setColor(Color.green);
//		g.draw(hitBox);
	}
	
	public void update(int delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		
		this.x -= speed*ratio;
		hitBox0.setX(x-2);
		hitBox0.setY(y-19);
		
		hitBox1.setX(x);
		hitBox1.setY(y-38);
		
		hitBox2.setX(x-3);
		hitBox2.setY(y-58);
		
		tryToFire();
		for (int i = 0; i < trail.length; i++)
			if (trail[i] != null) {
				trail[i].x -= speed*ratio;
				trail[i].update(delta);
			}
		explosion.x -= speed*ratio;
	}
	
	public void tryToFire() {
		if (System.currentTimeMillis() - lastFire < firingInterval) {
			return;
		}
		
		lastFire = System.currentTimeMillis();
		if (Math.random() < 0.3) {
			game.enemyFire((int)x,(int)y,speed,this);
		}
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Gets the current x position of this enemy
	 *
	 * @return The current x position of this enemy
	 */
	public float getX() {
		return x;
	}
	
	/**
	 * Gets the current y position of this enemy
	 *
	 * @return The current y position of this enemy
	 */
	public float getY() {
		return y;
	}
	
	/**
	 * Gets the current speed of this enemy
	 *
	 * @return The current speed of this enemy
	 */
	public float getSpeed() {
		return speed;
	}
	
	public long getLastFire() {
		return lastFire;
	}
	
	public void setLastFire(long time) {
		this.lastFire = time;
	}
	
	@Override
	public Hit hit(Gun gun, Vector2f v) {
		float oldHP = health;
		hit = super.hit(gun, v);
		if (health <= 0) {
			death();
		} else {
			switch (type) {
			case 0:
				if (oldHP > maxHealth/2f && health <= maxHealth/2f) {
					sprite = LoadState.img_enemy_50;
					addTrail(0);
				}
				break;
			case 1:
				if (oldHP > maxHealth/3f && health <= maxHealth/3f) {
					sprite = LoadState.img_enemy1_30;
					addTrail(0);
					explosion = new Explosion((int)x, (int)y, ExplosionParameters.ENEMY_DESTROYED[type-1]);
				} if (oldHP > maxHealth/1.67f && health <= maxHealth/1.67f) {
					sprite = LoadState.img_enemy1_60;
					addTrail(1);
				}
				break;
			case 2:
				if (oldHP > maxHealth/4f && health <= maxHealth/4f) {
					sprite = LoadState.img_enemy2_25;
					addTrail(0);
					explosion = new Explosion((int)x, (int)y, ExplosionParameters.ENEMY_DESTROYED[type-2]);
				} if (oldHP > maxHealth/2f && health <= maxHealth/2f) {
					sprite = LoadState.img_enemy2_50;
					addTrail(1);
					explosion = new Explosion((int)x, (int)y, ExplosionParameters.ENEMY_DESTROYED[type-1]);
				} if (oldHP > maxHealth/1.33f && health <= maxHealth/1.33f) {
					sprite = LoadState.img_enemy2_75;
					addTrail(2);
				}
				break;
			}
			int extra = 0;
			if (type == 3) extra = 25;
			switch (gun.getType()) {
			default:
				game.incScore(1);
			case Loadout.BLASTER:
			case Loadout.MINIGUN:
			case Loadout.UBERGUN:
				game.explode(
						(int)x+25+extra,
						(int)y,
						ExplosionParameters.BULLET_DESTROYED
					);
				break;
			case Loadout.RAILGUN:
				break;
			}
		}
		return hit;
	}
	
	public void hitByPlayer() {
		hit();
		if (health <= 0) death();
	}
	
	public void death() {
		game.explode(
				(int)x+25,
				(int)y,
				ExplosionParameters.ENEMY_DESTROYED[type]
			);
		game.enemies.remove(this);
		game.incScore(2);
		game.makePickup(this);
	}
	
	public void addTrail(int i) {
		if (trail[i] != null) return;
		Random r = new Random();
		trail[i] = new Trail(r.nextInt(sprite.getWidth()-20)+20+x,
				r.nextInt(sprite.getHeight()-30)-sprite.getHeight()/2+15+y,TrailParameters.ENEMY);
	}

	@Override
	public boolean hit(Hit hit) {
		return health <= 0;
	}
}