package za.co.sourlemon.retrostyle.particles;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Trail extends ParticleSystem {
	
	private Vector<Particle> particles = new Vector<Particle>(), removable = new Vector<Particle>();
	private double minSpeed, maxSpeed, direction;
	private int maxSize, minSize, sizeChange;
	private long minLife, maxLife, timeGap, last = 0;
	private Color color1,color2;
	
	Particle particle;
	Color color;
	
	public Trail(double x, double y, TrailParameters param) {
		this(x, y, param.minSpeed, param.maxSpeed, param.direction,
				param.minSize, param.maxSize, param.sizeChange, param.minLife, param.maxLife,
				param.timeGap, param.color1, param.color2);
	}
	
	public Trail(double x, double y, double minSpeed,
			double maxSpeed, double direction, int minSize, int maxSize, int sizeChange, long minLife,
			long maxLife, long timeGap, Color color1, Color color2) {
		super(x,y);
		
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
	
	public int update(int delta) {
		if (particles.size() > 0) {
			for (int i = 0; i < particles.size(); i++) {
				particle = particles.get(i);
				if (particle.update(delta) == 0)
					removable.add(particle);
			}
		}
		
		if (removable.size() > 0) {
			for (int i = 0; i < removable.size(); i++) {
				particles.remove(removable.get(i));
			}
			removable.clear();
		}
		
		if (System.currentTimeMillis()-last > timeGap) {
			last = System.currentTimeMillis();
			double speed = getRandom(minSpeed, maxSpeed);
			long life = (long) getRandom(minLife, maxLife);
			int size = (int) getRandom(minSize, maxSize);
			color = getRandomColor(color1, color2);
			particles.add(new Particle(x,y,speed,direction,size,sizeChange,life,color));
		}
		
		return 0;
	}
	
	public void render(Graphics g) {
		if (particles.size() > 0) {
			for (int i = 0; i < particles.size(); i++) {
				particles.get(i).render(g);
			}
		}
	}
	
}