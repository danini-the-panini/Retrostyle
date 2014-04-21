package za.co.sourlemon.retrostyle.test;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.entities.Bullet;

public class HPEnemy {

	private float x;
	private float y;
	private float speed;
	
	private float maxHitPoints;
	private float hitPoints;
	
	Shape hitbox;
	
	private int size = 100;
	
	Test game;
	
	public HPEnemy(float y, float speed, float hitpoints, int size, Test game) {
		this.x = Test.container.getWidth();
		this.y = y;
		this.speed = speed;
		this.hitPoints = maxHitPoints = hitpoints;
		this.size = size;
		this.game = game;
		
		hitbox = new Rectangle(x, y-size/2, size, size);
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public int update(int delta) {
		float ratio = (float)delta/Arcade.UPDATE_INTERVAL;
		x -= speed*ratio;
		hitbox.setX(x);
		return x < -size ? 0 : 1;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.green);
		g.fillRect(x, y-size/2, size, size);
		g.setColor(Color.black);
		g.drawString("" + (int)((hitPoints/maxHitPoints)*100) + "%", x, y-size/2);
	}
	
	public boolean hit(Line line) {
		return line.intersects(hitbox) || hitbox.contains(line);
	}
	public boolean hit(Line line, float damage) {
		boolean hit = hit(line);
		if (hit) {
			hit(damage);
		}
		return hit;
	}
	
	public boolean hit(Bullet bullet) {
		return hit(
				new Line(bullet.getX(),
					bullet.getY(),
					bullet.getLastX(),
					bullet.getLastY()),
					bullet.damage);
	}
	
	public void hit(float damage) {
		hitPoints -= damage;
		if (hitPoints <= 0) {
			game.notifyEnemyDeath(this);
		}
	}
	
}
