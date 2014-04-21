package za.co.sourlemon.retrostyle;

import java.io.*;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.util.*;

import za.co.sourlemon.retrostyle.states.ArcadeGameState;
import za.co.sourlemon.retrostyle.states.EnterScoreState;
import za.co.sourlemon.retrostyle.states.HelpState;
import za.co.sourlemon.retrostyle.states.InGameState;
import za.co.sourlemon.retrostyle.states.LoadState;
import za.co.sourlemon.retrostyle.states.MenuState;
import za.co.sourlemon.retrostyle.states.PausedState;

public class Arcade extends StateBasedGame {

	public static Arcade game;
	
	public static int WIDTH, HEIGHT;
	public static final float UPDATE_INTERVAL = 30;

	public Transition fadeOut = new FadeOutTransition();
	public Transition fadeIn = new FadeInTransition();

	public static final String GAME_NAME = "Arcade";

	protected InGameState ingame;
	protected LoadState load;
	protected MenuState menu;
	protected HelpState help;
	protected PausedState paused;
	protected EnterScoreState enterScore;
	protected AppGameContainer container;

	private boolean loaded;

	public Arcade() {
		super(GAME_NAME);
	}

	public void initStatesList(GameContainer container) throws SlickException {
		if (container instanceof AppGameContainer) {
			this.container = (AppGameContainer) container;
		}
		container.setShowFPS(false);
		container.setMinimumLogicUpdateInterval((int)UPDATE_INTERVAL);
		container.setMaximumLogicUpdateInterval((int)UPDATE_INTERVAL);
		
		WIDTH = container.getWidth();
		HEIGHT = container.getHeight();
		
		ingame = new InGameState();
		load = new LoadState();
		menu = new MenuState();
		help = new HelpState();
		paused = new PausedState();
		enterScore = new EnterScoreState();
		
		addState(load);
		enterState(load.getID());
	}

	public void gameOver(int score) {
		if (score > LoadState.data.getScores()[8]) {
			enterScore.setValues(score);
			enterStateTransition(enterScore.getID());
		} else
			enterStateTransition(menu.getID());
	}

	public void gameOver(int score1, int score2) {
		if (score1 > LoadState.data.getScores()[8]
				|| score2 > LoadState.data.getScores()[8]) {
			enterScore.setValues(score1, score2);
			enterStateTransition(enterScore.getID());
		} else {
			enterStateTransition(menu.getID());
		}
	}

	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);

		// if the resources have successfully loaded...
		if (loaded) {

			// F2 toggles full screen
			switch (key) {
			case Input.KEY_F2:
				if (container != null) {
					try {
						container.setFullscreen(!container.isFullscreen());
					} catch (SlickException e) {

						// log? isn't that piece of wood? I mean wtf?
						Log.error(e);
					}
				}

			break;
			case Input.KEY_ESCAPE:

				cancelButtonPressed();

				break;
			case Input.KEY_ENTER:

				confirmButtonPressed();

				break;
			case Input.KEY_UP:
				((ArcadeGameState) getCurrentState()).upPressed();

				break;
			case Input.KEY_DOWN:
				((ArcadeGameState) getCurrentState()).downPressed();

				break;
				
			case Input.KEY_F1:
				// in the pure main menu (ie. really seriously)
				if (getCurrentState() == load) {

					if (!load.isLogo()) {
						help.setY(new int[LoadState.img_help.length]);
						for (int i = 0; i < help.getY().length; i++) {
							help.getY()[i] = 480 * i;
						}
						help.currentPage = 0;
						enterStateTransition(help.getID());
					}
				}
			}
		}
	}
	
	@Override
	public void controllerButtonPressed(int controller, int button) {
		if (loaded) {
			switch (button) {
			case (0):
				confirmButtonPressed();
				break;
			case (1):
				cancelButtonPressed();
				break;
			}
		}
	}
	
	@Override
	public void controllerDownPressed(int controller) {
		if (loaded) {
			((ArcadeGameState) getCurrentState()).downPressed();
		}
	}
	
	@Override
	public void controllerUpPressed(int controller) {
		if (loaded) {
			((ArcadeGameState) getCurrentState()).upPressed();
		}
	}
	
	private void confirmButtonPressed() {
		
		ArcadeGameState currentState = (ArcadeGameState) getCurrentState();
		
		// in the pure main menu (ie. really seriously)
		if (currentState == paused) {
			if (paused.getSelected() == 1) {
				ingame.unPause();
				enterStateTransition(ingame.getID());
			} else if (paused.getSelected() == 2) {
				enterStateTransition(load.getID());
			}
		}
		
		currentState.confirmPressed();
	}
	
	private void cancelButtonPressed() {
		
		ArcadeGameState currentState = (ArcadeGameState) getCurrentState();
	
		// in the menu it asks to exit the game =(
		if (currentState == ingame) {
			ingame.pause();
			paused.setSelected(1);
			enterStateTransition(paused.getID());
		}
		
		currentState.cancelPressed();
	}
	
	public void exitGame() {
		// save the data
		try {
			FileOutputStream fo = new FileOutputStream(
					LoadState.RES + "data");
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(LoadState.data);
			oo.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// and kill the game
		System.exit(0);
	}

	// after we definitely know everything is loaded
	// we can add all the game's states
	public void notifyLoaded() {
		loaded = true;

		addState(menu);
		addState(help);
		addState(ingame);
		addState(paused);
		addState(enterScore);
	}

	// called after the user has entered his/her name into the hi scores list 
	// TODO: put this in a better place!?
	public void notifyEntered() {
		menu.menu_current = menu.menu_hiScores;
		menu.menu_main.selection = menu.menu_hiScores.id;
		menu.menu_main.reset();
		enterStateTransition(menu.getID());
	}

	public void enterStateTransition(int id) {
		fadeOut = new FadeOutTransition();
		fadeIn = new FadeInTransition();
		enterState(id, fadeOut, fadeIn);

	}

	public void goToMenu() {
		try {
			menu.init(container, game);
		} catch (SlickException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		enterStateTransition(menu.getID());
	}
	
	public void startGame() {
		try {
			ingame.init(container, this);
		} catch (SlickException e) {
		}
		enterStateTransition(ingame.getID());
	}
	
	public static void main(String[] args) {
		try {
			game = new Arcade();
			AppGameContainer container = new AppGameContainer(game);
			container.setDisplayMode(1024, 600, false);
			container.setIcon(LoadState.RES + "/icon.png");
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
