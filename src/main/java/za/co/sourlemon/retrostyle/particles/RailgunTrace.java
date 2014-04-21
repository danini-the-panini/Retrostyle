package za.co.sourlemon.retrostyle.particles;

import org.newdawn.slick.Graphics;

public class RailgunTrace {

	private Explosion[] trace = new Explosion[50];
	private Explosion flare;
	
	public boolean finished = false;
	
	public RailgunTrace(int x, int y) {
		
		for (int i = 0; i < trace.length; i++) {
			trace[i] = new Explosion(x + i*20, y, ExplosionParameters.RAILGUN_TRACE);
		}
		
		flare = new Explosion(x, y, ExplosionParameters.RAILGUN_FLARE);
		
	}
	
	public int update(int delta) {
		int done = 0;
		for (int i = 0; i < trace.length; i++) {
			done += trace[i].update(delta);
		}
		
		done += flare.update(delta);
		return done;
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < trace.length; i++) {
			trace[i].render(g);
		}
		
		flare.render(g);
	}

}
