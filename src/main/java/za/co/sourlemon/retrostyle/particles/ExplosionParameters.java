package za.co.sourlemon.retrostyle.particles;

import org.newdawn.slick.Color;

public class ExplosionParameters {

	public static final ExplosionParameters[] ENEMY_DESTROYED = {
		new ExplosionParameters(
			50, 0d, 5.0d, 5, 20, 300, 500,
			new Color(0x00dd00), new Color(0x00ff00), Explosion.ENEMY_DESTROYED_0),
		new ExplosionParameters(
				100, 0d, 7.0d, 7, 25, 500, 700,
				new Color(0x00dd00), new Color(0x00ff00), Explosion.ENEMY_DESTROYED_1),
		new ExplosionParameters(
				150, 0d, 10.0d, 10, 30, 700, 1000,
				new Color(0x00dd00), new Color(0x00ff00), Explosion.ENEMY_DESTROYED_2)
		};
	
	public static final ExplosionParameters ENEMY_HIT = new ExplosionParameters(
			10, 0d, 4.0d, 5, 10, 300, 500,
			new Color(0x00dd00), new Color(0x00ff00), Explosion.ENEMY_HIT);
	
	public static final ExplosionParameters PLAYER_DESTROYED = new ExplosionParameters(
			100, 0d, 5.0d, 5, 20, 300, 500,
			new Color(0xff5a00), new Color(0xff0000), Explosion.PLAYER_DESTROYED);
	
	public static final ExplosionParameters SHIELD_HIT = new ExplosionParameters(
			20, 0d, 4.0d, 5, 20, 300, 500,
			new Color(0.2f,0.5f,0.5f), new Color(0.4f,1.0f,1.0f), Explosion.SHIELD_HIT);
	
	public static final ExplosionParameters PLAYER_HIT = new ExplosionParameters(
			20, 0d, 4.0d, 5, 20, 300, 500,
			new Color(0xff5a00), new Color(0xff0000), Explosion.PLAYER_HIT);
	
	public static final ExplosionParameters ROCKET_EXPLODED = new ExplosionParameters(
			300, 0d, 10.0d, 1, 30, 700, 3000,
			new Color(0xff5a00), new Color(0xff0000), Explosion.ROCKET_EXPLODED);
	
	public static final ExplosionParameters BULLET_DESTROYED = new ExplosionParameters(
			10, 0d, 4.0d, 5, 20, 300, 500,
			new Color(0xff5a00), new Color(0xff0000), Explosion.BULLET_DESTROYED);
	
	public static final ExplosionParameters FIRE_PICKUP = new ExplosionParameters(
			100, 0d, 50.0d, 5, 10, 400, 500,
			new Color(1.0f,0.6f,0.2f), new Color(1.0f,0.6f,0.2f), Explosion.FIRE_PICKUP);

	public static final ExplosionParameters SHIELD_PICKUP = new ExplosionParameters(
			100, 0d, 50.0d, 5, 10, 400, 500,
			new Color(0.4f,1.0f,1.0f), new Color(0.4f,1.0f,1.0f), Explosion.SHIELD_PICKUP);

	public static ExplosionParameters LIFE_PICKUP = new ExplosionParameters(
			100, 0d, 50.0d, 5, 10, 400, 500,
			new Color(1.0f,0.4f,1.0f), new Color(1.0f,0.4f,1.0f), Explosion.LIFE_PICKUP);

	public static ExplosionParameters BULLET_PICKUP = new ExplosionParameters(
			100, 0d, 50.0d, 5, 10, 400, 500,
			Color.white, Color.white, Explosion.BULLET_PICKUP);

	public static final ExplosionParameters SPEED_PICKUP = new ExplosionParameters(
			100, 0d, 50.0d, 5, 10, 400, 500,
			Color.green, Color.green, Explosion.SPEED_PICKUP);
	
	public static final ExplosionParameters RAILGUN_TRACE = new ExplosionParameters(
			1, 0.02d, 0.05d, 5, 6, 300, 500,
			new Color(0x0084ff), new Color(0x0084ff), Explosion.RAILGUN_TRACE);
	
	public static final ExplosionParameters RAILGUN_FLARE = new ExplosionParameters(
			10, 0d, 4.0d, 5, 10, 300, 500,
			new Color(0x0084ff), new Color(0x0084ff), Explosion.RAILGUN_FLARE);

	protected int numParticles, type;
	protected double minSpeed, maxSpeed;
	protected int minSize, maxSize;
	protected long minLife, maxLife;
	protected Color color1, color2;

	public ExplosionParameters(int numParticles, double minSpeed,
			double maxSpeed, int minSize, int maxSize, long minLife,
			long maxLife, Color color1, Color color2, int type) {

		this.numParticles = numParticles;
		this.minSpeed = minSpeed;
		this.maxSpeed = maxSpeed;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.minLife = minLife;
		this.maxLife = maxLife;
		this.color1 = color1;
		this.color2 = color2;
		this.type = type;

	}

	public int getType() {
		return type;
	}

}
