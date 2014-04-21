package za.co.sourlemon.retrostyle.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

import za.co.sourlemon.retrostyle.Arcade;

/**
 * When you enter your name for hi score
 * 
 * @author Daniel Smith
 */
public class EnterScoreState extends ArcadeGameState {
	public static final int ID = 6;

	private int score = 0;
	private String name = "";

	private int score1 = 0;
	private int score2 = 0;
	private boolean one = false, two = false;
	private int entering = 0;

	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		container.setVSync(false);
		container.setMouseGrabbed(true);
	}

	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		String message = "";
		g.drawImage(LoadState.img_enterscore_title, LoadState.enterscore_title_x,
				LoadState.enterscore_title_y);
		g.drawImage(LoadState.img_enterscore,
				LoadState.enterscore_x,
				LoadState.enterscore_y);
		message = "." + score + ".";
		LoadState.font.drawString(320 - LoadState.font.getWidth(message) / 2,
				130, message);
		message = "." + name + ".";
		LoadState.font.drawString(320 - LoadState.font.getWidth(message) / 2,
				310, message);
	}

	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		LoadState.updateMusic(delta);
	}

	public void setValues(int score) {
		this.score = score;
		entering = 0;
	}

	public void setValues(int score1, int score2) {
		this.score1 = score1;
		this.score2 = score2;
		if (score1 > LoadState.data.getScores()[8]
				&& score2 <= LoadState.data.getScores()[8]) {
			entering = 1;
		} else if (score2 > LoadState.data.getScores()[8]
				&& score1 <= LoadState.data.getScores()[8]) {
			entering = 2;
		} else if (score2 > LoadState.data.getScores()[8]
				&& score1 > LoadState.data.getScores()[8]) {
			if (score2 > score1) {
				entering = 2;
			} else {
				entering = 1;
			}
		}
		one = false;
		two = false;
	}

	public void keyReleased(int key, char c) {
		super.keyPressed(key, c);

		if (key == Input.KEY_DELETE) {
			if (name.length() > 0) {
				name = name.substring(1, name.length());
			}
		} else if (!(key == Input.KEY_BACK || key == Input.KEY_ENTER)){
			String string = c + "";
			if (name.length() < 10)
				name = name + string.toUpperCase();
		}
	}
	
	public void upPressed() {}
	public void downPressed() {}
	
	public void confirmPressed() {
		if (!name.equals("")) {
			if (entering == 0) {
				LoadState.data.addScore(name, score);
				name = "";
				Arcade.game.notifyEntered();
			} else if (entering == 1) {
				one = true;
				LoadState.data.addScore(name, score1);
				if (score2 > LoadState.data.getScores()[8] && !two) {
					name = "";
					entering = 2;
				} else {
					name = "";
					Arcade.game.notifyEntered();
				}
			} else if (entering == 2) {
				two = true;
				LoadState.data.addScore(name, score2);
				if (score1 > LoadState.data.getScores()[8] && !one) {
					name = "";
					entering = 1;
				} else {
					name = "";
					Arcade.game.notifyEntered();
				}
			}
		}
	}
	@Override
	public void cancelPressed() {}
	
	@Override
	public void keyPressed(int key, char c) {
		switch (key) {
		case Input.KEY_BACK:
			if (name.length() > 0) {
				name = name.substring(0, name.length() - 1);
			}
		break;
		}
	}

	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		name = "";
	}

	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
	}
}