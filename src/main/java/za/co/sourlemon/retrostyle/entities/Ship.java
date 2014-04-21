package za.co.sourlemon.retrostyle.entities;

import java.awt.Point;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;

import za.co.sourlemon.retrostyle.states.InGameState;

public abstract class Ship {

	public static final float DEFAULT_DAMAGE = 10f;
	
	float x, y;
	Polygon hitBox;
	
	int maxHealth;
	int health;
	int maxShield;
	int shield;
	
	Image sprite;
	
	/** The game state this enemy is in */
	InGameState game;
	
	Vector2f v;
	Hit hit;
	
	public Ship(float x, float y, int maxHealth, int maxShield, InGameState game) {
		this.x = x;
		this.y = y;
		this.game = game;
		this.health = this.maxHealth = maxHealth;
                this.maxShield = maxShield;
                this.shield = 0;
	}
	
	public abstract void render(Graphics g);
	
	public abstract void update(int delta);
	
	public Polygon getHitBox() {
		return hitBox;
	}
	
	public Vector2f hit(Line line) {
		Line l;
		float[] p1;
		float[] p2;
		if (line.intersects(hitBox)) {
			for (int i = 1; i < hitBox.getPointCount(); i++) {
				p1 = hitBox.getPoint(i - 1);
				p2 = hitBox.getPoint(i);
				l = new Line(p1[0], p1[1], p2[0], p2[1]);
				return l.intersect(line);
			}
		} else if (hitBox.contains(line)) {
			return new Vector2f(hitBox.getCenter());
		}
		return null;
	}
	public Vector2f hit(Line line, Gun gun) {
		v = hit(line);
		if (v!=null) {
			hit(gun, v);
			return v;
		}
		return null;
	}
	public Vector2f hit(Bullet bullet, boolean hit) {
		if (hit) return hit(new Line(
			bullet.getX(),
			bullet.getY(),
			bullet.getLastX(),
			bullet.getLastY()),
			bullet.gun);
		else return hit(new Line(
				bullet.getX(),
				bullet.getY(),
				bullet.getLastX(),
				bullet.getLastY()));
	}
	
	public Hit hit(Gun gun, Vector2f v) {
		return hit(gun.damage, v);
	}
	
	public Hit hit(float damage, Vector2f v) {
		float oh = health;
		shield -= damage;
		if (shield < 0) {
			health += shield;
			shield = 0;
		}
		hit = new Hit(v,health!=oh ? Hit.HEALTH : Hit.SHIELD);
		hit(hit);
		return hit;
	}
	
	public Hit hit() {
		return hit(DEFAULT_DAMAGE, null);
	}
	
	public abstract boolean hit(Hit hit);
	
        public int getShields() { return shield; }
        public int getLives() { return health; }
}
