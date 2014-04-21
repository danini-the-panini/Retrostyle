package za.co.sourlemon.retrostyle.states;

import java.util.*;

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.*;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.entities.Bullet;
import za.co.sourlemon.retrostyle.entities.Enemy;
import za.co.sourlemon.retrostyle.entities.Gun;
import za.co.sourlemon.retrostyle.entities.Loadout;
import za.co.sourlemon.retrostyle.entities.Pickup;
import za.co.sourlemon.retrostyle.entities.Player;
import za.co.sourlemon.retrostyle.entities.StarSystem;
import za.co.sourlemon.retrostyle.particles.Explosion;
import za.co.sourlemon.retrostyle.particles.ExplosionParameters;

/**
 * InGameState is the state the game is in when you are playing the game
 *
 * @author Daniel Smith
 */
public class InGameState extends ArcadeGameState {
	public static final int ID = 2;
	
	StarSystem stars;
	
	/** The Player */
	private Player player;
	
	/** The next higher score when the enemy will get faster */
	private int nextLevel;
	
	private Loadout loadout;
	
	/** The acceleration of the bullets */
	private float yourBullet_xacc;
	private float yourBullet_yacc;
	
	/** The acceleration of the rockets */
	private float rocketAcc;
	
	/** The player's current score */
	private int tscore;
	private int score;
	private boolean scoreUp;
	public static final int MULTIPLIER = 738;
	
	/** The time at which an enemy last appeared */
	private long lastEnemy;
	/** The time interval between enemies */
	private int enemyInterval;
	/** The speed of the enemies */
	private float enemySpeed;
	/** The chance of an enemy appearing on screen (0-1)*/
	private float chanceOfEnemy;
	/** The chance of a dead enemy leaving a pickup */
	private float chanceOfPickup;
//	private float chanceOfPickup2;
	
	/** Your bullets */
	private Vector<Bullet> yourBullets;
	/** The Rockets */
	private Vector<Bullet> rockets;
	/** The Enemies */
	public Vector<Enemy> enemies;
	/** The Enemy's bullets */
	private Vector<Bullet> enemyBullets;
	/** The ParticleSystems for explosions*/
	private Vector<Explosion> explosions;
	/** The Pickups */
	private Vector<Pickup> pickups;
	
	/** True if space was pressed when game was over */
	private boolean spacePressedWhenGameOver;
	
	/** Time at which this game was paused */
	private long pauseTime;
	/** True if the game is paused */
	private boolean paused;
	/** True if the game just ended */
	public boolean gameOver;
	
	private boolean isParticles;

	// the score is stored in this string
	private String str_score;
	// individual digits from the score are stored in here for drawing
	private int scoreNumber = 0;
	
	// pointers to use when updating/rendering
	private Enemy enemy;
	private Bullet bullet;
	private Pickup pickup;
	private Explosion explosion;
	private Vector<Enemy> hitEnemies = new Vector<Enemy>();
	private Vector<Bullet> hitBullets = new Vector<Bullet>();
	private Image gunImage;
	
	private Vector2f vector;
//	private Hit hit;
	
	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setVSync(false);
		container.setMouseGrabbed(true);
		
		loadout = new Loadout();
		
		enemies = new Vector<Enemy>();
		enemyBullets = new Vector<Bullet>();
		explosions = new Vector<Explosion>();
		pickups = new Vector<Pickup>();
		
		stars = new StarSystem(500, 0.1f, 5, Arcade.WIDTH, Arcade.HEIGHT);
		
		resetValues();

		LoadState.msc_menu.kill();
		LoadState.msc_inGame.reset();
		LoadState.msc_inGame.loop();
	}
	public void resetValues() {
		
		player = new Player(60,Arcade.HEIGHT/2,3,3,this);
		
		yourBullets = new Vector<Bullet>();
		rockets = new Vector<Bullet>();
		
		yourBullets = new Vector<Bullet>();
		rockets = new Vector<Bullet>();
		
		loadout.reset();
		loadout.switchPrimary(Loadout.BLASTER);
		loadout.switchSecondary(Loadout.ROCKET_LAUNCHER);
		
		score = 0;
		tscore = 0;
		scoreUp = false;
		
		enemies.clear();
		enemyBullets.clear();
		explosions.clear();
		pickups.clear();
		
		nextLevel = 5*MULTIPLIER;
		
		yourBullet_xacc = 1f;
		yourBullet_yacc = 0.05f;
		rocketAcc = 0.5f;
		
		lastEnemy = 0;
		enemyInterval = 2000;
		enemySpeed = 2;
		chanceOfEnemy = 0.5f;
		chanceOfPickup = 0.7f;
		chanceOfPickup = 0.5f;
		
		pauseTime = System.currentTimeMillis();
		paused = false;
		gameOver = false;
		
		spacePressedWhenGameOver = false;
		
		isParticles = LoadState.data.getParticles();
	}
	
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (!paused && !gameOver) {
			//draw the background image
			g.drawImage(LoadState.img_ingame_top,
					0,
					0);
			g.drawImage(LoadState.img_ingame_bottom,
					0,
					(Arcade.HEIGHT-LoadState.img_ingame_bottom.getHeight())-6);
			
			stars.render(g);
			
			renderScore(g);
			
			renderCurrentPickups(g);
			
			renderGunHeat(g);
                        
                        renderShields(g);
                        
                        renderLives(g);
							
			renderProjectiles(g);
			
			// draw the enemies
			if (!enemies.isEmpty()) {
				for (int i = 0; i < enemies.size();i++) {
					//get the enemy
					enemy = (Enemy)enemies.get(i);
					enemy.render(g);
				}
			}
			
			// render pickups (if any)
			if (!pickups.isEmpty()) {
				for (int i = 0; i < pickups.size(); i++) {
					pickup = (Pickup)pickups.get(i);
					pickup.render(g);
				}
			}
			
			player.render(g);
			
			// render explosions (if any)
			if (isParticles && !explosions.isEmpty()) {
				for (int i = 0; i < explosions.size(); i++) {
					explosion = explosions.get(i);
					explosion.render(g);
				}
			}
			
//			g.setColor(loadout.primary.isTooHot() ? Color.red : Color.white);
//			g.drawString("Gun Heat: " + loadout.primary.getHeat(), 50, 50);
			
		} else if (gameOver) {
			g.drawImage(LoadState.img_gameOver,0,0);
		} 
			
	}
	private void renderScore(Graphics g) {
		str_score = ""+score;
		int gap = 0;
		for (int i = 0; i < str_score.length(); i++) {
			try {
				scoreNumber = Integer.parseInt(str_score.charAt(i)+"");}
			catch (NumberFormatException e) {
				scoreNumber = 0;
			}
			// creates a gap every 3 digits from the right
			if ((str_score.length()-i)%3==0)gap+=10;
			
			// paints it red when the score is decreasing
			if (score > tscore)
				g.drawImage(LoadState.img_numbers[scoreNumber],5+25*i+gap,Arcade.HEIGHT-45, Color.red);
			else
				g.drawImage(LoadState.img_numbers[scoreNumber],5+25*i+gap,Arcade.HEIGHT-45);
		}
//		g.setColor(Color.red);
//		g.drawString(str_score, 5, Arcade.HEIGHT-20);
	}
	private void renderCurrentPickups(Graphics g) {
		
		switch (loadout.primary.getType()) {
		case Loadout.MINIGUN:
			gunImage = LoadState.img_firePickup;
			break;
		case Loadout.UBERGUN:
			gunImage = LoadState.img_uberPickup;
			break;
		case Loadout.RAILGUN:
			gunImage = LoadState.img_railgunPickup;
			break;
		default:
			gunImage = null;
			break;
		}
		if (gunImage != null)
			g.drawImage(gunImage,403,1);
		
		//draw rockets you have
		if (loadout.secondary.getType() == Loadout.ROCKET_LAUNCHER) {
			for (int i = 0; i < loadout.secondary.getAmmo(); i++) {
				g.drawImage(LoadState.img_rocketPickup,463+60*i,1);
			}
		}
	}
	
	private void renderGunHeat(Graphics g) {
		float heat = loadout.primary.getHeat();
		int img_height = LoadState.img_heat.getHeight();
		int img_width = LoadState.img_heat.getWidth();
		if (heat > 0) {
			if (heat < 1f/3f) {
				float p = heat*3;
				g.drawImage(LoadState.img_heat, 24, 1, img_width*p+24 ,img_height+1, 0, 0, img_width*p ,img_height);
			} else if (heat < 2f/3f) {
				g.drawImage(LoadState.img_heat, 24, 1);
				float p = (heat-1f/3f)*3;
				g.drawImage(LoadState.img_heat, 85, 1, img_width*p+85 ,img_height+1, 0, 0, img_width*p ,img_height);
			} else if (heat < 1f) {
				g.drawImage(LoadState.img_heat, 24, 1);
				g.drawImage(LoadState.img_heat, 85, 1);
				float p = (heat-2f/3f)*3;
				g.drawImage(LoadState.img_heat, 145, 1, img_width*p+145 ,img_height+1, 0, 0, img_width*p ,img_height);
			} else {
				g.drawImage(LoadState.img_heat, 24, 1);
				g.drawImage(LoadState.img_heat, 85, 1);
				g.drawImage(LoadState.img_heat, 145, 1);
			}
			if (loadout.primary.isTooHot()) {
				g.drawImage(LoadState.img_overheat, 0, 1, new Color(1.0f,0f,0f,heat));
				g.drawImage(LoadState.img_overheat, 194, 1, new Color(1.0f,0f,0f,heat));
			}
		}
	}
        private void renderShields(Graphics g)
        {
            for (int i = 0; i < player.getShields(); i++)
            {
                g.drawImage(LoadState.img_shieldPickup, 222+i*60, 1);
            }
            
        }
        private void renderLives(Graphics g)
        {
            for (int i = 0; i < player.getLives(); i++)
            {
                g.drawImage(LoadState.img_ship, 855+i*58, Arcade.HEIGHT-40);
            }
        }
	private void renderProjectiles(Graphics g) {
		// draw the bullets
		if (!yourBullets.isEmpty()) {
			for (int i = 0; i < yourBullets.size(); i++) {
				yourBullets.get(i).render(g);
			}
		}
		
		// draw the rockets
		if (!rockets.isEmpty()) {
			for (int i = 0; i < rockets.size(); i++) {
				rockets.get(i).render(g);
			}
		}
		
		//draw enemy bullets
		if (!enemyBullets.isEmpty()) {
			for (int i = 0; i < enemyBullets.size(); i++) {
				enemyBullets.get(i).render(g);
			}
		}
	}
	
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (!paused && !gameOver) {
			
			updateScore();
			
			stars.update(delta);
			
			player.update(delta);
			
			if (!player.dead() && !gameOver) {
				if (loadout.primary != null && loadout.primary.firing) tryToFire(loadout.primary);
				if (loadout.secondary != null && loadout.secondary.firing) tryToFire(loadout.secondary);
			}
			
			if (loadout.primary.getType() == Loadout.UBERGUN && loadout.primary.isTooHot()) {
				loadout.switchPrimary(Loadout.MINIGUN);
			}
			
			loadout.update(delta);
			
			// attempt to make an enemy appear
			tryToMakeEnemy();
			
			updateBullets(yourBullets, delta);
			updateBullets(rockets, delta);
			
			//move enemy bullets
			if (!enemyBullets.isEmpty()) {
				for (int i = 0; i < enemyBullets.size(); i++) {
					// get the bullet
					bullet = (Bullet)enemyBullets.get(i);
					// move the bullet
					bullet.update(delta);
					// remove the bullet if it is off the screen
					if (bullet.getX()<-100)
						enemyBullets.remove(bullet);
				}
			}
			
			// move the enemies
			if (!enemies.isEmpty()) {
				for (int i = 0; i < enemies.size();i++) {
					//get the enemy
					enemy = (Enemy)enemies.get(i);
					//move the enemy
					enemy.update(delta);
					//remove the enemy if it is off the screen
					//and take off points for letting him pass!
					if (enemy.getX()<-155) {
						enemies.remove(enemy);
						if (score>0) incScore(-1);
					}
				}
			}
			
			// enemy destruction
			if (!enemies.isEmpty()) {
				for (int i = 0; i < enemies.size(); i++) {
					enemy = (Enemy)enemies.get(i);
//					int type = enemy.getType();
					if (!player.dead() && enemy.getHitBox().intersects(player.getHitBox())) {
//							explode(
//									(int)enemy.getX()+25,
//									(int)enemy.getY(),
//									ExplosionParameters.ENEMY_DESTROYED[enemy.getType()]
//								);
						enemyHitPlayer(enemy);
					}
				}
			}
			
			// player destruction
			if ( !enemyBullets.isEmpty() ) {
				for ( int i = 0; i < enemyBullets.size(); i++ ) {
					bullet = (Bullet)enemyBullets.get(i);
					enemy = bullet.getEnemy();
					if ( player.getHitBox().contains(bullet.getX(),bullet.getY() )
						&& player.getX()<enemy.getX() && !player.dead() ) {
						enemyHitPlayer(bullet);
						enemyBullets.remove(bullet);
					}
				}
			}
			
			updatePickups(delta);
			
			// update explosions (if any)
			if (isParticles && !explosions.isEmpty()) {
				for (int i = 0; i < explosions.size(); i++) {
					explosion = explosions.get(i);
					if (explosion.update(delta) == 0) explosions.remove(explosion);
				}
			}
		}

		LoadState.updateMusic(delta);
		
	}
	private void updateBullets(Vector<Bullet> bullets, int delta) {
		if (!bullets.isEmpty()) {
			for (int i = 0; i < bullets.size(); i++) {
				// get the bullet
				bullet = (Bullet)bullets.get(i);
				// move the bullet
				bullet.update(delta);
				// remove the bullet if it is off the screen
				if (bullet.getX()>Arcade.WIDTH+500 || bullet.getX() < -200) {
					bullets.remove(bullet);
				} else {
					if (enemies.size() > 0) {
						for (int j = 0; j < enemies.size(); j++) {
							enemy = enemies.get(j);
							vector = enemy.hit(bullet,true);
							if (vector != null) {
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
	private void updatePickups(int delta) {
		if (!pickups.isEmpty()) {
			for (int i = 0; i < pickups.size(); i++) {
				pickup = (Pickup)pickups.get(i);
				pickup.update(delta);
				if (pickup.getX()<-50) pickups.remove(pickup);
				if ( pickup.getPolygon().intersects(player.getHitBox()) && !player.dead()) {
					if ( pickup.getType() == Pickup.FIRE) {
						loadout.switchPrimary(Loadout.MINIGUN);
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.FIRE_PICKUP
								);
					} else if ( pickup.getType() == Pickup.UBER) {
						loadout.switchPrimary(Loadout.UBERGUN);
						loadout.levelUp();
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.FIRE_PICKUP
								);
					} else if ( pickup.getType() == Pickup.RAILGUN) {
						loadout.switchPrimary(Loadout.RAILGUN);
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.SHIELD_PICKUP
								);
					} else if ( pickup.getType() == Pickup.SHIELD) {
//						if (shield < 3) shield++;
						// TODO: implement new shield system.
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.SHIELD_PICKUP
								);
					} else if ( pickup.getType() == Pickup.SPEED) {
						enemySpeed /= 2;
						if (enemySpeed < 2) enemySpeed = 2;
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.SPEED_PICKUP
								);
					} else if ( pickup.getType() == Pickup.BULLET) {
						loadout.levelUp();
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.BULLET_PICKUP
								);
					} else if ( pickup.getType() == Pickup.ROCKET) {
						loadout.secondary.reload(1);
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.FIRE_PICKUP
								);
					} else if ( pickup.getType() == Pickup.LIFE) {
//						if (lives < 3) lives++;
						// TODO: implement new health system
							explode(
									(int)pickup.getX(),
									(int)pickup.getY(),
									ExplosionParameters.LIFE_PICKUP
								);
					}
					incScore(3);
					pickups.remove(pickup);
				}
			}
		}
	}
	
	
	public void explode(int x, int y, ExplosionParameters param) {
		
		boolean moveStars = false;
		
		switch (param.getType()) {
		case Explosion.ENEMY_DESTROYED_0:
		case Explosion.ENEMY_DESTROYED_1:
		case Explosion.ENEMY_DESTROYED_2:
			moveStars = true;
		case Explosion.PLAYER_HIT:
			Random r = new Random();
			LoadState.snd_explosion.play(r.nextFloat()+0.5f,2.0f);
			break;
		case Explosion.FIRE_PICKUP:
		case Explosion.SHIELD_PICKUP:
		case Explosion.LIFE_PICKUP:
		case Explosion.BULLET_PICKUP:
		case Explosion.SPEED_PICKUP:
			LoadState.snd_pickup.play();
			break;
		case Explosion.PLAYER_DESTROYED:
			LoadState.snd_playerDeath.play();
			moveStars = true;
			break;
		case Explosion.SHIELD_HIT:
			LoadState.snd_shieldDestroyed.play();
			break;
		}
		
		if (!isParticles) return;
		
		Explosion explosion = new Explosion (x,y,param);
		
		explosions.add(explosion);
		
		if (!moveStars) return;
		
		// TODO: implement variable magnitude
		// base it on given explosion size
		// calculated from various information: speed, life, etc.
		int MAGNITUDE = 150;
		
		int ex = (int) explosion.getX();
		int ey = (int) explosion.getY();
		
		stars.explode(ex, ey, MAGNITUDE);
		
	}
	private void rocketExplosion(Bullet bullet) {
		explode((int)bullet.getX(), (int)bullet.getY(), ExplosionParameters.ROCKET_EXPLODED);
		
		Enemy deadEnemy;
		for (int k = 0; k < enemies.size(); k++) {
			deadEnemy = (Enemy)enemies.get(k);
//			makePickup(deadEnemy);
				explode(
						(int)deadEnemy.getX()+25,
						(int)deadEnemy.getY(),
						ExplosionParameters.ENEMY_DESTROYED[deadEnemy.getType()]
					);
			incScore(1);
		}
		enemies.clear();
		for (int k = 0; k < enemyBullets.size(); k++) {
			bullet = (Bullet)enemyBullets.get(k);
				explode(
						(int)bullet.getX(),
						(int)bullet.getY(),
						ExplosionParameters.BULLET_DESTROYED
					);
		}
		enemyBullets.clear();
	}
	
	public void enemyHitPlayer(Bullet bullet) {
		if (player.invincible()) {
				explode(
						(int)player.getX()-25,
						(int)player.getY(),
						ExplosionParameters.SHIELD_HIT
					);
		} else {
			vector = player.hit(bullet,false);
			if (vector != null)
				player.hit(bullet.damage, vector);
		}
	}
	
	public void enemyHitPlayer(Enemy enemy) {
		if (player.invincible()) {
				explode(
						(int)player.getX()-25,
						(int)player.getY(),
						ExplosionParameters.SHIELD_HIT
					);
		} else {
			enemy.hitByPlayer();
			player.hit();
		}
	}
	
	public void gameOver() {
		gameOver = true;
		LoadState.snd_gameOver.play();
		LoadState.msc_inGame.fade();
	}
	
	private void tryToFire(Gun gun) {
		if (!gun.fire()) return;
		
		switch (gun.getType()) {
		case Loadout.BLASTER:
		case Loadout.MINIGUN:
		case Loadout.UBERGUN:
			switch (loadout.getLevel()) {
			case 4:
				yourBullets.add(new Bullet(player.getX(), player.getY() - 3, 0, -2,
						yourBullet_xacc, -yourBullet_yacc * 2, gun.damage,
						LoadState.img_yourBullet, null, gun));
				yourBullets.add(new Bullet(player.getX(), player.getY() + 3, 0, 2,
						yourBullet_xacc, yourBullet_yacc * 2, gun.damage,
						LoadState.img_yourBullet, null, gun));
			case 2:
				yourBullets.add(new Bullet(player.getX(), player.getY() - 3, 0, -1,
						yourBullet_xacc, -yourBullet_yacc, gun.damage,
						LoadState.img_yourBullet, null, gun));
				yourBullets.add(new Bullet(player.getX(), player.getY() + 3, 0, 1,
						yourBullet_xacc, yourBullet_yacc, gun.damage,
						LoadState.img_yourBullet, null, gun));
			case 0:
				yourBullets.add(new Bullet(player.getX(), player.getY(), 0, 0,
						yourBullet_xacc, 0, gun.damage, LoadState.img_yourBullet, null, gun));
				break;
			case 3:
				yourBullets.add(new Bullet(player.getX(), player.getY() - 7, 0, -1,
						yourBullet_xacc, -yourBullet_yacc, gun.damage,
						LoadState.img_yourBullet, null, gun));
				yourBullets.add(new Bullet(player.getX(), player.getY() + 7, 0, 1,
						yourBullet_xacc, yourBullet_yacc, gun.damage,
						LoadState.img_yourBullet, null, gun));
			case 1:
				yourBullets.add(new Bullet(player.getX(), player.getY() - 7f, 0, 0,
						yourBullet_xacc, 0, gun.damage, LoadState.img_yourBullet, null, gun));
				yourBullets.add(new Bullet(player.getX(), player.getY() + 7, 0, 0,
						yourBullet_xacc, 0, gun.damage, LoadState.img_yourBullet, null, gun));
				break;
			}
			break;
		case Loadout.RAILGUN:
			switch (loadout.getLevel()) {
			case 4:
			case 3:
			case 2:
				fireRailgun((int)player.getX(),(int)player.getY(),gun);
				fireRailgun((int)player.getX()-5,(int)player.getY()-10,gun);
				fireRailgun((int)player.getX()-5,(int)player.getY()+10,gun);
				break;
			case 1:
				fireRailgun((int)player.getX()-5,(int)player.getY()-5,gun);
				fireRailgun((int)player.getX()-5,(int)player.getY()+5,gun);
				break;
			case 0:
				fireRailgun((int)player.getX(),(int)player.getY(),gun);
				break;
			}
			break;
		case Loadout.ROCKET_LAUNCHER:
			LoadState.snd_rocket.play();
			rockets.add(new Bullet(player.getX(), player.getY(),
					0, 0, rocketAcc, 0, loadout.secondary.damage, LoadState.img_rocket, null, gun));
		}
	}
	
	private void fireRailgun(int x, int y, Gun gun) {
		
		for (int i = 0; i < 50; i++) {
			explosions.add(new Explosion(x + i*20, y, ExplosionParameters.RAILGUN_TRACE));
		}
		
		explosions.add(new Explosion(x, y, ExplosionParameters.RAILGUN_FLARE));
		
		railgunHitEnemy(x, y, gun);
	}

	private void railgunHitEnemy(int x, int y, Gun gun) {
		if (enemies.size() > 0) {
			for (int i = 0; i < enemies.size(); i++) {
				enemy = enemies.get(i);
				vector = enemy.hit(new Line(x, y, 1000, y));
				if (vector != null) {
					explode((int)vector.x,(int)vector.y,ExplosionParameters.RAILGUN_FLARE);
					hitEnemies.add(enemy);
				}
				
			}
			if (hitEnemies.size() > 0) {
				for (int i = 0; i < hitEnemies.size(); i++) {
					hitEnemies.get(i).hit(gun,null);
				}
			}
			hitEnemies.clear();
		}
		
		if (enemyBullets.size() > 0) {
			for (int i = 0; i < enemyBullets.size(); i++) {
				bullet = enemyBullets.get(i);
				if (bullet.getX() > x && bullet.getY() < y + 5 && bullet.getY() > y - 5) {
					hitBullets.add(bullet);
					explode((int)bullet.getX(),(int)bullet.getY(),ExplosionParameters.BULLET_DESTROYED);
				}
			}
			if (hitBullets.size() > 0) {
				for (int i = 0; i < hitBullets.size(); i++) {
					enemyBullets.remove(hitBullets.get(i));
				}
			}
			hitBullets.clear();
		}
	}
	
	public void enemyFire(int x, int y,float speed, Enemy enemy) {
		bullet = new Bullet(x, y, -2-speed, 0, 0, 0, 1, LoadState.img_theirBullet, enemy, null);
		enemyBullets.add(bullet);
	}
	
	public void tryToMakeEnemy() {
		if (System.currentTimeMillis() - lastEnemy < enemyInterval) {
			return;
		}
		
		double random = Math.random();
		if (random < chanceOfEnemy) {
			lastEnemy = System.currentTimeMillis();
			random = Math.random();
			int enemy_y = (int)(70+random*(Arcade.HEIGHT-140));
			int score1 = 0;
			score1 = score; // WTF?
			if (score1 < 300*MULTIPLIER) {
				enemy = new Enemy(enemy_y,enemySpeed,0,this);
			} else if (score1 < 600*MULTIPLIER) {
				random = Math.random();
				if (random <= 0.6)
					enemy = new Enemy(enemy_y,enemySpeed,0,this);
				else
					enemy = new Enemy(enemy_y,enemySpeed,1,this);
			} else if (score1 < 1000*MULTIPLIER) {
				random = Math.random();
				if (random <= 0.3)
					enemy = new Enemy(enemy_y,enemySpeed,0,this);
				else if (random <= 0.8)
					enemy = new Enemy(enemy_y,enemySpeed,1,this);
				else
					enemy = new Enemy(enemy_y,enemySpeed,2,this);
			} else if (score1 < 1500*MULTIPLIER) {
				random = Math.random();
				if (random <= 0.1)
					enemy = new Enemy(enemy_y,enemySpeed,0,this);
				else if (random <= 0.3)
					enemy = new Enemy(enemy_y,enemySpeed,1,this);
				else
					enemy = new Enemy(enemy_y,enemySpeed,2,this);
			} else {
				random = Math.random();
				if (random <= 0.1)
					enemy = new Enemy(enemy_y,enemySpeed,1,this);
				else
					enemy = new Enemy(enemy_y,enemySpeed,2,this);
			}
			enemies.add(enemy);
		}
	}
	
	public void makePickup(Enemy enemy) {
		// make pickup...
		int spickups = 0,epickups = 0,rpickups = 0,gpickups = 0,rcktpickups = 0,lifepickups = 0, upickups = 0, railpickups = 0;
		for (int k = 0; k < pickups.size(); k++) {
			pickup = (Pickup)pickups.get(k);
			if (pickup.getType() == Pickup.SHIELD) spickups++;
			if (pickup.getType() == Pickup.SPEED) epickups++;
			if (pickup.getType() == Pickup.FIRE) rpickups++;
			if (pickup.getType() == Pickup.BULLET) gpickups++;
			if (pickup.getType() == Pickup.ROCKET) rcktpickups++;
			if (pickup.getType() == Pickup.LIFE) lifepickups++;
			if (pickup.getType() == Pickup.UBER) upickups++;
			if (pickup.getType() == Pickup.RAILGUN) railpickups++;
		}
		float random = (float)Math.random();
		if (random < chanceOfPickup && pickups.size()<=4) {
			random = (float)Math.random();
			if (random < (1.0/12)) {
				if (upickups == 0
						&& loadout.primary.getType() == Loadout.MINIGUN
						&& score > 500*MULTIPLIER &&
						Math.random()<0.1) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.UBER,
						LoadState.img_uberPickup
					);
					pickups.add(pickup);
				}
			} else if (random < (1.0/7)) {
				if (rpickups == 0
						&& loadout.primary.getType() != Loadout.MINIGUN
						&&  loadout.primary.getType() != Loadout.UBERGUN
						&& score > 50*MULTIPLIER &&
						Math.random()<0.3) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.FIRE,
						LoadState.img_firePickup
					);
					pickups.add(pickup);
				}
			} else if (random < (1.0/7)*2) {
				if (railpickups == 0 &&
						loadout.primary.getType() != Loadout.RAILGUN &&
						score > 300*MULTIPLIER &&
						Math.random()<0.3) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.RAILGUN,
						LoadState.img_railgunPickup
					);
					pickups.add(pickup);
				}
			} else if (random < (1.0/7)*3) {
				// TODO: new requirements for shield pickup.
//				if (spickups == 0
//					&& ( shield < 3)
//					&& score > 10*multiplier) {
//					pickup = new Pickup(
//						enemy.getX()+25,
//						enemy.getY(),
//						-0.5f,
//						Pickup.SHIELD,
//						LoadState.img_shieldPickup
//					);
//					pickups.add(pickup);
//				}
			} else if (random < (1.0/7)*4) {
				if (epickups == 0 &&
						enemySpeed > 2
						&& score < 1000*MULTIPLIER) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.SPEED,
						LoadState.img_speedPickup
					);
					pickups.add(pickup);
				}
			} else if (random < (1.0/7)*5) {
				if (gpickups == 0
						&& (loadout.getLevel() < Loadout.MAX_LEVEL)
						&& score > 100*MULTIPLIER) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.BULLET,
						LoadState.img_bulletPickup
					);
					pickups.add(pickup);
				}
			} else if (random < (1.0/7)*6) {
				if (rcktpickups == 0
						&& !loadout.secondary.ammoFull()
						&& score > 200*MULTIPLIER) {
					pickup = new Pickup(
						enemy.getX()+25,
						enemy.getY(),
						-0.5f,
						Pickup.ROCKET,
						LoadState.img_rocketPickup
					);
					pickups.add(pickup);
				}
			} else if (random <= 1.0) {
				// TODO: new requirements for health/lives
//				if (lifepickups == 0 && (lives < 3)
//					&& score > 0
//					&& Math.random()<0.1) {
//					pickup = new Pickup(
//						enemy.getX()+25,
//						enemy.getY(),
//						-0.5f,
//						Pickup.LIFE,
//						LoadState.img_lifePickup
//					);
//					pickups.add(pickup);
//				}
			}
		}
	}
	
	public void incScore(int amount) {
		Random r = new Random();
		int m = r.nextInt(100)-50;
		tscore+=amount*(MULTIPLIER+m);
		if (tscore <= 0) tscore = 0;
		scoreUp = tscore > score;
	}
	public void updateScore() {
		int gap = Math.abs(tscore-score)/6;
		if (gap < 1) gap = 1;
		if (score != tscore) {
			if (scoreUp && tscore > score) {
				score+=gap;
				if (score >= tscore) score = tscore;
			} else if (tscore < score) {
				score-=gap;
				if (score <= tscore) score = tscore;
			}
		}
		if (score > nextLevel) {
			nextLevel += 20*MULTIPLIER;
			enemyInterval /= 1.2;
			if (enemyInterval < 300) enemyInterval = 300;
			enemySpeed += 1;
			if (enemySpeed > 10) enemySpeed = 10;
			chanceOfEnemy *= 1.3;
			if (chanceOfEnemy > 1.0f) chanceOfEnemy = 1.0f;
		}
	}
	
	public void pause() {
		pauseTime = System.currentTimeMillis();
		paused = true;
		LoadState.msc_inGame.fade();
	}	
 	public void unPause() {
		int delta = (int)(System.currentTimeMillis() - pauseTime);
		player.unPause(delta);
		// TODO see if we need to do something about the guns...
//		lastFire += delta;
//		lastRocket += delta;
		lastEnemy += delta;
		if (!enemies.isEmpty()) {
			for (int i = 0; i < enemies.size(); i++) {
				enemy = (Enemy)enemies.get(i);
				long time = enemy.getLastFire();
				time += delta;
				enemy.setLastFire(time);
			}
		}
		player.reset();
		loadout.primary.firing = false;
		loadout.secondary.firing = false;
		paused = false;
		LoadState.msc_inGame.fade();
	}

	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		
		if (!gameOver) {
			if (key == LoadState.data.getControls()[0]) {
				player.up = true;
			} else if (key == LoadState.data.getControls()[1]) {
				player.down = true;
			} else if (key == LoadState.data.getControls()[2]) {
				player.left = true;
			} else if (key == LoadState.data.getControls()[3]) {
				player.right = true;
			} else if (key == LoadState.data.getControls()[4]) {
				loadout.primary.firing = true;
			} else if (key == LoadState.data.getControls()[5]) {
				loadout.secondary.firing = true;
			} else if (key == Input.KEY_F3) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.SHIELD,
						LoadState.img_shieldPickup));
			} else if (key == Input.KEY_F4) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.SPEED,
						LoadState.img_speedPickup));
			} else if (key == Input.KEY_F5) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.BULLET,
						LoadState.img_bulletPickup));
			} else if (key == Input.KEY_F6) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.ROCKET,
						LoadState.img_rocketPickup));
			} else if (key == Input.KEY_F7) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.FIRE,
						LoadState.img_firePickup));
			} else if (key == Input.KEY_F8) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.RAILGUN,
						LoadState.img_railgunPickup));
			} else if (key == Input.KEY_F9) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.LIFE,
						LoadState.img_lifePickup));
			} else if (key == Input.KEY_F10) {
				pickups.add(new Pickup(
						player.getX(),
						player.getY(),
						-1f, Pickup.UBER,
						LoadState.img_uberPickup));
			} else if (key == Input.KEY_F11) {
				incScore(-100);
			} else if (key == Input.KEY_F12) {
				incScore(100);
			}
		} else if (key == Input.KEY_ENTER) {
			spacePressedWhenGameOver = true;
		}
	}
	@Override
	public void keyReleased(int key, char c) {
		super.keyPressed(key, c);
		
		if (!gameOver) {
			if (key == LoadState.data.getControls()[0]) {
				player.up = false;
			} else if (key == LoadState.data.getControls()[1]) {
				player.down = false;
			} else if (key == LoadState.data.getControls()[2]) {
				player.left = false;
			} else if (key == LoadState.data.getControls()[3]) {
				player.right = false;
			} else if (key == LoadState.data.getControls()[4]) {
				loadout.primary.firing = false;
			} else if (key == LoadState.data.getControls()[5]) {
				loadout.secondary.firing = false;
			}
		} else if (key == Input.KEY_ENTER && spacePressedWhenGameOver) {
			Arcade.game.gameOver(score);
		}
	}
	
	@Override
	public void controllerButtonPressed(int controller, int button) {
		if (!gameOver) {
			if (button == 1) {
				loadout.primary.firing = true;
			} else if (button == 2) {
				loadout.secondary.firing = true;
			}
		}
	}
	@Override
	public void controllerButtonReleased(int controller, int button) {
		if (!gameOver) {
			if (button == 1) {
				loadout.primary.firing = false;
			} else if (button == 2) {
				loadout.secondary.firing = false;
			} else if (button == 3) {
				pause();
			}
		}
	}
	@Override
	public void controllerDownPressed(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.down = true;
			}
		}
	}
	@Override
	public void controllerDownReleased(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.down = false;
			}
		}
	}
	@Override
	public void controllerLeftPressed(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.left = true;
			}
		}
	}
	@Override
	public void controllerLeftReleased(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.left = false;
			}
		}
	}
	@Override
	public void controllerRightPressed(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.right = true;
			}
		}
	}
	@Override
	public void controllerRightReleased(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.right = false;
			}
		}
	}
	@Override
	public void controllerUpPressed(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.up = true;
			}
		}
	}
	@Override
	public void controllerUpReleased(int controller) {
		if (!gameOver) {
			if (controller == 0) {
				player.up = false;
			}
		}
	}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		player.reset();
		loadout.resetKeys();
	}
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
		player.reset();
		loadout.resetKeys();
	}

	@Override
	public void cancelPressed() {}
	@Override
	public void confirmPressed() {}
	@Override
	public void downPressed() {}
	@Override
	public void upPressed() {}
}