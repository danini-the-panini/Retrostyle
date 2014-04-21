package za.co.sourlemon.retrostyle.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import za.co.sourlemon.retrostyle.states.ArcadeGameState;

public abstract class MenuManagerState extends ArcadeGameState implements MenuManager {

	public Menu menu_current;
	public Menu menu_main;
	
	@Override
	public void setMenu(Menu menu) {
		this.menu_current = menu;
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		menu_current.update(container, delta);
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		menu_current.render(container, g);
	}
	
}