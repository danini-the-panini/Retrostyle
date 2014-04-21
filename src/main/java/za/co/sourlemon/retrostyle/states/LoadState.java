package za.co.sourlemon.retrostyle.states;

import org.newdawn.slick.*;
import org.newdawn.slick.loading.DeferredResource;
import org.newdawn.slick.loading.LoadingList;
import org.newdawn.slick.state.*;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.ArcadeMusic;
import za.co.sourlemon.retrostyle.SaveData;

import java.io.*;


/**
 * MenuState is the state the game is in when you are in the main menu
 *
 * @author Daniel Smith
 */
public class LoadState extends ArcadeGameState {
	public static final int ID = 1;
static public SaveData data;

static public String RES = "res/";
static public final String
	FONT_RES = "res/font/",
	IMAGES_RES = "res/images/",
	ENEMIES_RES = "res/images/enemies/",
	GUNS_RES = "res/images/guns/",
	HUD_RES = "res/images/hud/",
	PICKUPS_RES = "res/images/pickups/",
	SHIPS_RES = "res/images/ships/",
	INTRO_RES = "res/intro/",
	MENU_RES = "res/menu/",
	MUSIC_RES = "res/music/",
	SOUND_RES = "res/sound/";
	
// All the stuff to load!
	static public Image
	img_resume,
	img_endGame,
	img_gameOver,
	img_areyousure,
	img_areyousure2,
	img_ship,
	img_invincible,
	img_enemy,
	img_enemy_50,
	img_enemy1,
	img_enemy1_60,
	img_enemy1_30,
	img_enemy2,
	img_enemy2_75,
	img_enemy2_50,
	img_enemy2_25,
	img_yourBullet,
	img_theirBullet,
	img_rocket,
	img_shieldPickup,
	img_firePickup,
	img_uberPickup,
	img_railgunPickup,
	img_speedPickup,
	img_bulletPickup,
	img_lifePickup,
	img_rocketPickup,
	img_heat,
	img_blob,
	img_overheat,
	
	img_main_title,
	img_main_newGame,
	img_main_options,
	img_opt_controls,
	img_main_hiScores,
	img_hs_reset,
	img_menu_yes,
	img_menu_no,
	img_main_exit,
	img_menu_back,
	
	img_numbers[],
	img_numbersb[],
	logos[];
	static public AngelCodeFont font;
	
	// in-game overlays
	static public Image img_ingame_top;
	static public Image img_ingame_bottom;

	static public final int ingame_top_x = 4;
	static public final int ingame_top_y = 1;

	static public final int ingame_bottom_x = 7;
	static public final int ingame_bottom_y = 434;

	// enter score stuff
	static public Image img_enterscore_title;
	static public Image img_enterscore;

	static public final int enterscore_title_x = 75;
	static public final int enterscore_title_y = 39;

	static public final int enterscore_x = 72;
	static public final int enterscore_y = 220;

	// some new help pages... yeah I said PAGES not one new page...
	// seriously you're going to like this!
	static public Image[] img_help;
		
	static public Sound
		snd_playerFire,
		snd_railgun,
		snd_overheat,
		snd_explosion,
		snd_playerDeath,
		snd_shieldDestroyed,
		snd_pickup,
		snd_rocket,
		snd_select1,
		snd_select2,
		snd_gameOver;
	
	static public ArcadeMusic
		msc_inGame,
		msc_menu;
			
	private String loadingMessage = "Loading lots of cool stuff!";
// end of loading list
	
	/** The next resource to load */ 
    private DeferredResource nextResource; 
    /** True if we've loaded all the resources and started rendering */ 
    private boolean started; 
	
	private int selected = 1;
	
	private boolean isLogo = true;
	private boolean skipLogo = false;
	private final float logoFadeSpeed = 0.01f;
	private int logo = 0;
	private float logoAlpha = 0.99999f;
	private boolean alphaIncreasing = false;
	private boolean isMenuFade = false;
	private boolean logoDone = false;
	
	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setVSync(false);
		container.setMouseGrabbed(false);
		
//Load Resources
		LoadingList.setDeferredLoading(true);
		
		img_resume = new Image(MENU_RES + "paused_resume.png");
		img_endGame = new Image(MENU_RES + "paused_end.png");
		img_gameOver = new Image(MENU_RES + "game_over.png");
		img_areyousure = new Image(MENU_RES + "areyousure.png");
		img_areyousure2 = new Image(MENU_RES + "areyousure2.png");
		img_ship = new Image(SHIPS_RES + "player.png");
		img_invincible = new Image(SHIPS_RES + "player_invincible.png");
		img_enemy = new Image(ENEMIES_RES + "enemy.png");
		img_enemy_50 = new Image(ENEMIES_RES + "enemy_50.png");
		img_enemy1 = new Image(ENEMIES_RES + "enemy1.png");
		img_enemy1_60 = new Image(ENEMIES_RES + "enemy1_60.png");
		img_enemy1_30 = new Image(ENEMIES_RES + "enemy1_30.png");
		img_enemy2 = new Image(ENEMIES_RES + "enemy2.png");
		img_enemy2_75 = new Image(ENEMIES_RES + "enemy2_75.png");
		img_enemy2_50 = new Image(ENEMIES_RES + "enemy2_50.png");
		img_enemy2_25 = new Image(ENEMIES_RES + "enemy2_25.png");
		img_yourBullet = new Image(GUNS_RES + "your_shot.png");
		img_theirBullet = new Image(GUNS_RES + "their_shot.png");
		img_rocket = new Image(GUNS_RES + "rocket.png");
		img_shieldPickup = new Image(PICKUPS_RES + "pickup_shield.png");
		img_firePickup = new Image(PICKUPS_RES + "pickup_firepower.png");
		img_uberPickup = new Image(PICKUPS_RES + "pickup_uberrapid.png");
		img_railgunPickup = new Image(PICKUPS_RES + "pickup_railgun.png");
		img_speedPickup = new Image(PICKUPS_RES + "pickup_speed.png");
		img_bulletPickup = new Image(PICKUPS_RES + "pickup_bullet.png");
		img_lifePickup = new Image(PICKUPS_RES + "pickup_life.png");
		img_rocketPickup = new Image(PICKUPS_RES + "pickup_rocket.png");
		img_heat = new Image(HUD_RES + "heat.png");
		img_blob = new Image(HUD_RES + "blob.png");
		img_overheat = new Image(HUD_RES + "overheat.png");
		img_numbers = new Image[10];
		img_numbersb = new Image[10];
		font = new AngelCodeFont(FONT_RES + "font.fnt", FONT_RES + "font_00.png");
		for (int i = 0; i < img_numbers.length; i++) {
			img_numbers[i] = new Image(FONT_RES + ""+i+".png");
		}
		for (int i = 0; i < img_numbersb.length; i++) {
			img_numbersb[i] = new Image(FONT_RES + ""+i+"b.png");
		}
		logos = new Image[3];
		for (int i = 0; i < logos.length; i++) {
			logos[i] = new Image(INTRO_RES + "logo" + i + ".png");
		}
			
		// in-game overlays
		img_ingame_top = new Image(HUD_RES + "ingame-top.png");
		img_ingame_bottom = new Image(HUD_RES + "ingame-bottom.png");

		// enter hi score stuff
		img_enterscore_title = new Image(MENU_RES + "enterscore-title.png");
		img_enterscore = new Image(MENU_RES + "enterscore-one-player.png");
		
		// new menu system
		img_main_title = new Image(LoadState.MENU_RES + "new/main-title.png");
		img_main_newGame = new Image(LoadState.MENU_RES + "new/main-newGame.png");
		img_main_options = new Image(LoadState.MENU_RES + "new/main-options.png");
		img_opt_controls = new Image(LoadState.MENU_RES + "new/opt-controls.png");
		img_main_hiScores = new Image(LoadState.MENU_RES + "new/main-hiScores.png");
		img_hs_reset = new Image(LoadState.MENU_RES + "new/hs-reset.png");
		img_menu_yes = new Image(LoadState.MENU_RES + "new/yes.png");
		img_menu_no = new Image(LoadState.MENU_RES + "new/no.png");
		img_main_exit = new Image(LoadState.MENU_RES + "new/main-exit.png");
		img_menu_back = new Image(LoadState.MENU_RES + "new/back.png");

		// the new help images
		img_help = new Image[6];
		for (int i = 0; i < img_help.length; i++) {
			img_help[i] = new Image(MENU_RES + "help_" + i + ".png");
		}
				
		snd_playerFire = new Sound(SOUND_RES + "shoot.wav");
		snd_railgun = new Sound(SOUND_RES + "railgun.wav");
		snd_explosion = new Sound(SOUND_RES + "explosion1.wav");
		snd_overheat = new Sound(SOUND_RES + "overheat.wav");
		snd_playerDeath = new Sound(SOUND_RES + "explosion2.wav");
		snd_shieldDestroyed = new Sound(SOUND_RES + "explosion3.wav");
		snd_pickup = new Sound(SOUND_RES + "powerup.wav");
		snd_rocket = new Sound(SOUND_RES + "rocket.wav");
		snd_select1 = new Sound(SOUND_RES + "select1.wav");
		snd_select2 = new Sound(SOUND_RES + "select2.wav");
		snd_gameOver = new Sound(SOUND_RES + "gameover.wav");
		
		msc_inGame = new ArcadeMusic(MUSIC_RES + "space2.ogg");
		msc_menu = new ArcadeMusic(MUSIC_RES + "atestofislandcourage.ogg");
		
		try {
			FileInputStream fi = new FileInputStream(RES + "data");
			ObjectInputStream oi = new ObjectInputStream(fi);
			data = (SaveData)oi.readObject();
		} catch (Exception e) {
                    data = new SaveData();
		}
		
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (!started) {
			
			int total = LoadingList.get().getTotalResources();
			int loaded = LoadingList.get().getTotalResources()
				-LoadingList.get().getRemainingResources();

			g.setColor(new Color(1f,0.5f,0f));
			g.drawString(loadingMessage, 35, container.getHeight()-150);
			g.fillRect(35,container.getHeight()-130, container.getWidth()-70, 40);
			g.setColor(Color.black);
			int loadingBarWidth = ((container.getWidth()-90)/total)*loaded;
			g.fillRect(45,container.getHeight()-120,loadingBarWidth,20);
		
		} else {
			
			if (isLogo) {
				g.drawImage(logos[logo], 0, 0);
				g.setColor(new Color(0,0,0,logoAlpha));
				g.fillRect(0, 0, container.getScreenWidth(), container.getScreenWidth());
			}
			
		}
		
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		if (nextResource != null) {
			try {
//				System.out.println("Loading: " + nextResource.getDescription());
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
                Arcade.game.notifyLoaded();
    			msc_menu.reset();
    			msc_menu.loop();
			} else {
			
				if (isLogo) {
					if (alphaIncreasing) {
						logoAlpha += skipLogo ? logoFadeSpeed*5 : logoFadeSpeed;
						if (logoAlpha >= 1f) {
							alphaIncreasing = false;
							if (skipLogo) {
								isLogo = false;
								logoDone = true;
								isMenuFade = true;
								logoAlpha = 1f;
							} else {
								if (logo < logos.length-1) {
									logo ++;
									logoAlpha = 0.9999f;
								} else {
									isLogo = false;
									logoDone = true;
									
								}
							}
						}
					} else {
						logoAlpha -= logoFadeSpeed;
						if (logoAlpha <= 0) {
							alphaIncreasing = true;
						}
					}
				} else if (logoDone) {
					Arcade.game.goToMenu();
				}
				
			}
			updateMusic(delta);
		}
	}
	
	public static void updateMusic(int delta) {
		msc_inGame.update(delta);
		msc_menu.update(delta);
	}

	public boolean isLogo() {
		return isLogo;
	}

	public void skipLogo() {
		skipLogo = true;
		alphaIncreasing = true;
	}
	
	public int getSelected() {
		return selected;
	}
	
	public void upPressed() {}
	public void downPressed() {}
	
	public void confirmPressed() {
		if (isLogo) 
			skipLogo();
	}
	public void cancelPressed() {
		if (isLogo)
			skipLogo();
	}
	
		public void enter(GameContainer container, StateBasedGame game) throws SlickException {
		if (started) {
			if (msc_inGame.playing())
				msc_inGame.kill();
			msc_menu.reset();
			if (!msc_menu.playing())
				msc_menu.loop();
		}
		if (logoDone) {
			isLogo = false;
			logoAlpha = 0f;
		}
	}
	
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
	}

}