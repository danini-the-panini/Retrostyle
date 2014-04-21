package za.co.sourlemon.retrostyle.test;

import java.util.Random;
import java.util.Vector;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.entities.Bullet;
import za.co.sourlemon.retrostyle.entities.Gun;
import za.co.sourlemon.retrostyle.entities.Loadout;
import za.co.sourlemon.retrostyle.entities.Star;
import za.co.sourlemon.retrostyle.particles.Explosion;
import za.co.sourlemon.retrostyle.particles.ExplosionParameters;
import za.co.sourlemon.retrostyle.particles.RailgunTrace;
import za.co.sourlemon.retrostyle.particles.Trail;
import za.co.sourlemon.retrostyle.particles.TrailParameters;
import za.co.sourlemon.retrostyle.states.LoadState;

public class Test extends BasicGame {

//	private Music music;
//	private boolean goingDown = false;
//	private float volume = 1.0f;
//	private boolean fading = false;
//	private  final int FADE_TIME = 1000;

	private Sound snd_explosion,snd_railgun,snd_shoot,snd_rocket;
	private Image img_yourBullet, img_rocket, img_ship;
	
	private Vector<Explosion> explosions;
	private Vector<RailgunTrace> railgunTraces;
	
	private Vector<HPEnemy> enemies;
	
	private Trail trail;
	
	private Loadout loadout;
	
	private Vector<Bullet> yourBullets = new Vector<Bullet>();
	private Vector<Bullet> rockets = new Vector<Bullet>();

	private float yourBullet_xacc = 1f;
	private float yourBullet_yacc = 0.05f;
	
	private float rocketAcc = 0.5f;
	
	private Star[] stars = new Star[500];
	private static final float SMIN = 0.1f, SMAX = 5;
	
	int mx = 400, my = 300;
	
	/** The next resource to load */ 
    private DeferredResource nextResource; 
    /** True if we've loaded all the resources and started rendering */ 
    private boolean started; 
    
    Explosion e;
    RailgunTrace r;
    HPEnemy enemy;
    
	public static AppGameContainer container;

	public Test(String title) {
		super(title);
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		container.setMinimumLogicUpdateInterval((int)Arcade.UPDATE_INTERVAL);
		container.setMaximumLogicUpdateInterval((int)Arcade.UPDATE_INTERVAL);
		container.setMouseGrabbed(true);

		LoadingList.setDeferredLoading(true);
		
		snd_explosion = new Sound(LoadState.SOUND_RES + "explosion1.wav");
		snd_railgun = new Sound(LoadState.SOUND_RES + "railgun.wav");
		snd_shoot = new Sound(LoadState.SOUND_RES + "shoot.wav");
		snd_rocket = new Sound(LoadState.SOUND_RES + "rocket.wav");

		img_yourBullet = new Image(LoadState.GUNS_RES + "your_shot.png");
		img_rocket = new Image(LoadState.GUNS_RES + "rocket.png");
		img_ship = new Image(LoadState.SHIPS_RES + "player.png");
		
//		music = new Music(MenuState.MUSIC_RES + "3beersdeep.wav");
		
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Star(SMIN, SMAX, 0, container.getWidth(),
					0, container.getHeight());
		}
		
		explosions = new Vector<Explosion>();
		railgunTraces = new Vector<RailgunTrace>();
		enemies = new Vector<HPEnemy>();
		
		trail = new Trail(400,300,TrailParameters.ENEMY_BULLETS);
		
		loadout = new Loadout();
		
		loadout.switchPrimary(Loadout.BLASTER);
		loadout.switchSecondary(Loadout.ROCKET_LAUNCHER);
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		
		if (nextResource != null) {
			try {
				nextResource.load();
			} catch (Exception e) {
				throw new SlickException("Failed to load: "+nextResource.getDescription(), e);
			}
			
			nextResource = null;
		}
		
		if (LoadingList.get().getRemainingResources() > 0) {
			nextResource = LoadingList.get().getNext();
		} else {
			if (!started) {
				started = true;
//				music.loop();
			} else {
				loadedUpdate(container, delta);
			}
		}
		
	}

	public void loadedUpdate(GameContainer container, int delta) {
		
		for (int i = 0; i < stars.length; i++) {
			stars[i].update(delta);
		}
		
		if (explosions.size() > 0) {
			for (int i = 0; i < explosions.size(); i++) {
				e = explosions.get(i);
				if (e.update(delta) == 0) explosions.remove(e);
			}
		}
		
		if (railgunTraces.size() > 0) {
			for (int i = 0; i < railgunTraces.size(); i++) {
				r = railgunTraces.get(i);
				if (r.update(delta) == 0) railgunTraces.remove(r);
			}
		}
		
		if (enemies.size() > 0) {
			for (int i = 0; i < enemies.size(); i++) {
				enemy = enemies.get(i);
				if (enemy.update(delta) == 0) enemies.remove(enemy);
			}
		}
		
		if (loadout.primary != null && loadout.primary.firing) tryToFire(loadout.primary);
		if (loadout.secondary != null && loadout.secondary.firing) tryToFire(loadout.secondary);
		loadout.update(delta);
		
		// move the bullets
		updateBullets(yourBullets, delta);
		updateBullets(rockets, delta);

		trail.setX(mx);
		trail.setY(my);
		
//		trail.update(delta);
		
//		if (fading) {
//			float fade = (float)delta/(float)FADE_TIME;
//			if (goingDown) {
//				volume-=fade;
//				if (volume <= 0.0f) {
//					volume = 0.0f;
//					fading = false;
//					music.pause();
//				}
//			} else {
//				volume+=fade;
//				if (volume >= 1.0f) {
//					volume = 1.0f;
//					fading = false;
//				}
//			}
//			music.setVolume(volume);
//		}
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		if (!started) {
			g.drawString("Loading", 200, 200);
		} else {
			for (int i = 0; i < stars.length; i++) {
				float cval = (float) ((stars[i].speedx-SMIN)/(SMAX-SMIN));
				Color c = new Color(1,1,1,cval);
				g.setColor(c);
				g.fillRect((int)stars[i].x, (int)stars[i].y, 2+3*(cval), 2);
			}
			
			if (explosions.size() > 0) {
				for (int i = 0; i < explosions.size(); i++) {
					Explosion e = explosions.get(i);
					e.render(g);
				}
			}
			
			if (railgunTraces.size() > 0) {
				for (int i = 0; i < railgunTraces.size(); i++) {
					RailgunTrace r = railgunTraces.get(i);
					r.render(g);
				}
			}
			
			if (enemies.size() > 0) {
				for (int i = 0; i < enemies.size(); i++) {
					HPEnemy enemy = enemies.get(i);
					enemy.render(g);
				}
			}
			
			renderBullets(yourBullets, g);
			renderBullets(rockets, g);
			
//			trail.render(g);
			
			g.drawImage(img_ship, mx-img_ship.getWidth(), my-img_ship.getHeight()/2);
			
			g.setColor(loadout.primary.isTooHot() ? Color.red : Color.white);
			g.drawString("Gun Heat: " + loadout.primary.getHeat(), 50, 50);
			g.setColor(Color.white);
			g.drawString("Gun Level: " + loadout.getLevel(), 50, 75);
			g.drawString("Rockets: " + loadout.secondary.getAmmo(), 50, 100);
		}
	}
	
	private void tryToFire(Gun gun) {
		if (!gun.fire()) return;
		switch (gun.getType()) {
		case Loadout.BLASTER:
		case Loadout.MINIGUN:
		case Loadout.UBERGUN:
			switch (loadout.getLevel()) {
			case 4:
				yourBullets.add(new Bullet(mx, my - 3, 0, -2,
						yourBullet_xacc, -yourBullet_yacc * 2, gun.damage,
						img_yourBullet, null, gun));
				yourBullets.add(new Bullet(mx, my + 3, 0, 2,
						yourBullet_xacc, yourBullet_yacc * 2, gun.damage,
						img_yourBullet, null, gun));
			case 2:
				yourBullets.add(new Bullet(mx, my - 3, 0, -1,
						yourBullet_xacc, -yourBullet_yacc, gun.damage,
						img_yourBullet, null, gun));
				yourBullets.add(new Bullet(mx, my + 3, 0, 1,
						yourBullet_xacc, yourBullet_yacc, gun.damage,
						img_yourBullet, null, gun));
			case 0:
				yourBullets.add(new Bullet(mx, my, 0, 0,
						yourBullet_xacc, 0, gun.damage, img_yourBullet, null, gun));
				break;
			case 3:
				yourBullets.add(new Bullet(mx, my - 7, 0, -1,
						yourBullet_xacc, -yourBullet_yacc, gun.damage,
						img_yourBullet, null, gun));
				yourBullets.add(new Bullet(mx, my + 7, 0, 1,
						yourBullet_xacc, yourBullet_yacc, gun.damage,
						img_yourBullet, null, gun));
			case 1:
				yourBullets.add(new Bullet(mx, my - 7f, 0, 0,
						yourBullet_xacc, 0, gun.damage, img_yourBullet, null, gun));
				yourBullets.add(new Bullet(mx, my + 7, 0, 0,
						yourBullet_xacc, 0, gun.damage, img_yourBullet, null, gun));
				break;
			}
			break;
		case Loadout.RAILGUN:
			switch (loadout.getLevel()) {
			case 4:
			case 3:
			case 2:
				railgunTraces.add(new RailgunTrace(mx,my));
				railgunHitEnemy(mx, my, gun);
				railgunTraces.add(new RailgunTrace(mx-10,my-10));
				railgunHitEnemy(mx, my, gun);
				railgunTraces.add(new RailgunTrace(mx-10,my+10));
				railgunHitEnemy(mx, my, gun);
				break;
			case 1:
				railgunTraces.add(new RailgunTrace(mx-5,my-5));
				railgunHitEnemy(mx, my, gun);
				railgunTraces.add(new RailgunTrace(mx-5,my+5));
				railgunHitEnemy(mx, my, gun);
				break;
			case 0:
				railgunTraces.add(new RailgunTrace(mx,my));
				railgunHitEnemy(mx, my, gun);
				break;
			}
			break;
		case Loadout.ROCKET_LAUNCHER:
			rockets.add(new Bullet(mx, my,
					0, 0, rocketAcc, 0, loadout.secondary.damage, img_rocket, null, gun));
			break;
		default:
			break;
		}
	}
	
	private void updateBullets(Vector<Bullet> bullets, int delta) {
		if (!bullets.isEmpty()) {
			for (int i = 0; i < bullets.size(); i++) {
				// get the bullet
				Bullet bullet = (Bullet)bullets.get(i);
				// move the bullet
				bullet.update(delta);
				// remove the bullet if it is off the screen
				if (bullet.getX()>container.getWidth()+200 || bullet.getX() < -200) {
					bullets.remove(bullet);
				} else {
					if (enemies.size() > 0) {
						for (int j = 0; j < enemies.size(); j++) {
							HPEnemy e = enemies.get(j);
							if (e.hit(bullet)) {
								if (bullet.getGun().getType() == Loadout.ROCKET_LAUNCHER) {
									rocketExplosion(bullet);
								}
								bullets.remove(bullet);
							}
						}
					}
				}
			}
		}
	}
	
	private void renderBullets(Vector<Bullet> bullets, Graphics g) {
		if (!bullets.isEmpty()) {
			for (int i = 0; i < bullets.size(); i++) {
				// get the bullet
				bullets.get(i).render(g);
			}
		}
	}
	
	private void makeEnemy(int mx, int my) {
		enemies.add(new HPEnemy(my, 1f, 50f, 50, this));
	}
	
	public void notifyEnemyDeath(HPEnemy enemy) {
		enemies.remove(enemy);
		explode((int)enemy.getX(), (int)enemy.getY(), ExplosionParameters.ENEMY_DESTROYED[0]);
	}
	
	private void railgunHitEnemy(int x, int y, Gun gun) {
		if (enemies.size() > 0) {
			Vector<HPEnemy> hitEnemies = new Vector<HPEnemy>();
			for (int i = 0; i < enemies.size(); i++) {
				HPEnemy e = enemies.get(i);
				if (e.hit(new Line(x, y, container.getWidth(), y))) {
					explode((int)e.getX(),y,ExplosionParameters.RAILGUN_FLARE);
					hitEnemies.add(e);
				}
			}
			if (hitEnemies.size() > 0) {
				for (int i = 0; i < hitEnemies.size(); i++) {
					hitEnemies.get(i).hit(gun.damage);
				}
			}
		}
	}
	
	private void rocketExplosion(Bullet bullet) {
		// TODO: magnitude based on damage done
		explode((int)bullet.getX(), (int)bullet.getY(), ExplosionParameters.ROCKET_EXPLODED);
	}
	
	public void explode(int x, int y, ExplosionParameters param) {
		
		Random r = new Random();
		
		switch (param.getType()) {
		case Explosion.ENEMY_DESTROYED_0:
		case Explosion.ENEMY_DESTROYED_1:
		case Explosion.ENEMY_DESTROYED_2:
			snd_explosion.play(r.nextFloat()+0.5f,2.0f);
			break;
		}
		
		Explosion explosion = new Explosion (x,y,param);
		
		explosions.add(explosion);
		
		// TODO: implement variable magnitude
		int MAGNITUDE = 150;
		
		int ex = (int) explosion.getX();
		int ey = (int) explosion.getY();
		
		for (int i = 0; i < stars.length; i++) {
			
			int distx = ex-(int)stars[i].x;
			int disty = ey-(int)stars[i].y;
			
			if (distx == 0 && disty == 0) {
				stars[i].begin();
				continue;
			}
			if ((Math.sqrt(distx*distx+disty*disty) < MAGNITUDE))
				stars[i].explode(ex, ey, distx, disty, MAGNITUDE);
			
		}
		
	}

	public static void main(String[] args) {
		try {
			container = new AppGameContainer(new Test("Test"), 800, 600, false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == 0) {
			loadout.primary.firing = true;
		} else if (button == 1) {
			loadout.secondary.firing = true;
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		
		if (button == 0) {
//			// TODO: implement variable magnitude
//			int MAGNITUDE = 100;
//			
//			for (int i = 0; i < stars.length; i++) {
//				
//				double distx = x-(int)stars[i].x;
//				double disty = y-(int)stars[i].y;
//				
//				if (distx == 0 && disty == 0) {
//					stars[i].begin();
//					continue;
//				}
//				if ((Math.sqrt(distx*distx+disty*disty) < MAGNITUDE))
//					stars[i].explode((double)x, (double)y, distx, disty, MAGNITUDE);
//				
//			}
//			
//			explosions.add(new Explosion(x, y, 200, 0d, 5.0d, 5, 20, 300, 500, new Color(0x00dd00), new Color(0x00ff00))); // small green explosion (small enemy destroyed)
//			explosions.add(new Explosion(x, y, 500, 0d, 10.0d, 1, 50, 700, 1200, new Color(0xff5a00), new Color(0xff0000))); // large red/orange explosion (rocket)
//			explosions.add(new Explosion(x, y, 200, 0d, 50.0d, 5, 10, 400, 500, Color.blue, Color.blue)); // blue pickup
//			explosions.add(new Explosion(x, y, 200, 0d, 7.0d, 7, 25, 500, 700, new Color(0x00dd00), new Color(0x00ff00)));
//			explosions.add(new Explosion(x, y, 200, 0d, 10.0d, 10, 30, 700, 1000, new Color(0x00dd00), new Color(0x00ff00), 0));
//			rguns.add(new RailGunTrace(x,y));
//			snd_railgun.play();
			loadout.primary.firing = false;
		} else if (button == 1) {
			loadout.secondary.firing = false;
		}
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		
		mx = newx;
		my = newy;
		
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		mouseMoved(oldx, oldy, newx, newy);
	}
	
	@Override
	public void keyPressed(int key, char c) {
		
		if (key == Input.KEY_R) {
			for (int i = 0; i < stars.length; i++) {
				stars[i] = new Star(SMIN, SMAX, 0, container.getWidth(),
						0, container.getHeight());
			}
		} else if (key == Input.KEY_SPACE) {
//			if (!music.playing()) music.resume();
//			goingDown = !goingDown;
//			fading = true;
			
			makeEnemy(mx,my);
			
		} else if (key == Input.KEY_1) {
			loadout.switchPrimary(Loadout.BLASTER);
		} else if (key == Input.KEY_2) {
			loadout.switchPrimary(Loadout.MINIGUN);
		} else if (key == Input.KEY_3) {
			loadout.switchPrimary(Loadout.UBERGUN);
		} else if (key == Input.KEY_4) {
			loadout.switchPrimary(Loadout.RAILGUN);
		} else if (key == Input.KEY_ADD) {
			loadout.levelUp();
		} else if (key == Input.KEY_SUBTRACT) {
			loadout.levelDown();
		} else if (key == Input.KEY_MULTIPLY) {
			loadout.secondary.reload();
		} else if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
	}

}
