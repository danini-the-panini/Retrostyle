package za.co.sourlemon.retrostyle.particles;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Explosion extends ParticleSystem {

	public static final int PLAYER_DESTROYED = 0, SHIELD_HIT = 1, ROCKET_EXPLODED = 2,
		FIRE_PICKUP = 3, SHIELD_PICKUP = 4, LIFE_PICKUP = 5, BULLET_PICKUP = 6,
		SPEED_PICKUP = 7, ENEMY_DESTROYED_0 = 8, ENEMY_DESTROYED_1 = 9,
		ENEMY_DESTROYED_2 = 10, BULLET_DESTROYED = 11,
		RAILGUN_TRACE = 12, RAILGUN_FLARE = 13, ENEMY_HIT = 14,
		PLAYER_HIT = 15;
	
	private Particle[] particles;
	public int type;
	
	Color color;

	public Explosion(int x, int y, ExplosionParameters param) {
		this(x, y, param.numParticles, param.minSpeed, param.maxSpeed,
				param.minSize, param.maxSize, param.minLife, param.maxLife,
				param.color1, param.color2, param.type);
	}

	public Explosion(int x, int y, int numParticles, double minSpeed,
			double maxSpeed, int minSize, int maxSize, long minLife,
			long maxLife, Color color1, Color color2, int type) {
		super(x,y);
		
		this.type = type;

		particles = new Particle[numParticles];
		for (int i = 0; i < particles.length; i++) {
			double speed = getRandom(minSpeed, maxSpeed);
			long life = (long) getRandom(minLife, maxLife);
			int size = (int) getRandom(minSize, maxSize);
			color = getRandomColor(color1, color2);
			particles[i] = new Particle(x, y, speed, Math.PI * 2.0d
					* r.nextDouble(), size, 0, life, color);
		}
	}

	@Override
	public int update(int delta) {
		int numAlive = 0;
		for (int i = 0; i < particles.length; i++) {
			numAlive += particles[i].update(delta);
		}
		return numAlive;
	}

	public void render(Graphics g) {

		for (int i = 0; i < particles.length; i++) {
			if (particles[i].alive)
				particles[i].render(g);
		}

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

}
