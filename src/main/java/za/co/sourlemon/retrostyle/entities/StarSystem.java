package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.Graphics;

public class StarSystem {

	Star stars[];
	
	public StarSystem(int numberOfStars, float minSpeed, float maxSpeed,
			int screenWidth, int screenHeight) {
		
		stars = new Star[numberOfStars];
		
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star(minSpeed, maxSpeed, 0, screenWidth,
					0, screenHeight);
		}
		
	}
	
	public void update(int delta) {
		for (int i = 0; i < stars.length; i++) {
			stars[i].update(delta);
		}
	}
	
	public void render(Graphics g) {
		for (int i = 0; i < stars.length; i++) {
			stars[i].render(g);
		}
	}

	public void explode(int ex, int ey, int magnitude) {
		for (int i = 0; i < stars.length; i++) {
			
			int distx = ex-(int)stars[i].x;
			int disty = ey-(int)stars[i].y;
			
			if (distx == 0 && disty == 0) {
				stars[i].begin();
				continue;
			}
			if ((Math.sqrt(distx*distx+disty*disty) < magnitude))
				stars[i].explode(ex, ey, distx, disty, magnitude);
			
		}
	}
	
	public void shift(int x1, int y1, int x2, int y2, float speedx, float speedy) {
		
		if (x1==x2 || y1==y2) return;
		
		int maxx = Math.max(x1,x2);
		int minx = Math.min(x1,x2);
		int midx = (maxx+minx)/2;
		int halfx = maxx-midx;
		
		float svalx = 1.0f;
		
		int maxy = Math.max(y1,y2);
		int miny = Math.min(y1,y2);
		int midy = (maxy+miny)/2;
		int halfy = maxy-midy;
		
		float svaly = 1.0f;
		
		for (int i = 0; i < stars.length; i++) {
			if (stars[i].x > minx && stars[i].x < maxx
					&& stars[i].y > miny && stars[i].y < maxy) {
				svalx = 1.0f-(float)(Math.abs(stars[i].x-midx)/halfx);
				svaly = 1.0f-(float)(Math.abs(stars[i].y-midy)/halfy);
				stars[i].shiftVert(speedx*svaly,speedy*svalx);
			}
		}
	}
	
}
