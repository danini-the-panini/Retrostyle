package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.geom.Vector2f;

public class Hit {

	public static final int HEALTH = 0, SHIELD = 1;
	
	Vector2f v;
	int type;
	
	public Hit(float x, float y, int type) {
		this(new Vector2f(x,y),type);
	}
	
	public Hit(Vector2f v, int type) {
		this.v = v;
		this.type = type;
	}
	
}
