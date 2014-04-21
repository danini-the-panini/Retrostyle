package za.co.sourlemon.retrostyle.states;

import org.newdawn.slick.*;
import org.newdawn.slick.state.*;

/**
 * Game Paused... you see this
 *
 * @author Daniel Smith
 */
public class PausedState extends ArcadeGameState {
	public static final int ID = 4;
	
	int selected = 1;
	
	public int getID() {
		return ID;
	}

	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		container.setVSync(true);
		container.setMouseGrabbed(true);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		if (selected == 1) g.drawImage(LoadState.img_resume,0,0);
		else if (selected == 2) g.drawImage(LoadState.img_endGame,0,0);
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		LoadState.updateMusic(delta);
	}
	
	public int getSelected() {
		return selected;
	}
	
	public void setSelected(int selected) {
		this.selected = selected;
	}
	
	public void upPressed() {
			if (selected > 1) selected--;
			else selected = 2;
			LoadState.snd_select2.play();
	}
	public void downPressed() {
			if (selected < 2) selected++;
			else selected = 1;
			LoadState.snd_select2.play();
	}
	
	public void confirmPressed() {}
	public void cancelPressed() {}
	
	public void enter(GameContainer container, StateBasedGame game) throws SlickException {
	}
	
	public void leave(GameContainer container, StateBasedGame game) throws SlickException {
	}
}