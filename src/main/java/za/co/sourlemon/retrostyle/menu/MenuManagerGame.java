package za.co.sourlemon.retrostyle.menu;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class MenuManagerGame extends BasicGame implements MenuManager {

	public Menu menu_current;
	public Menu menu_main;
	
	public MenuManagerGame(String title) {
		super(title);
	}
	
	@Override
	public void setMenu(Menu menu) {
		this.menu_current = menu;
	}
	
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		menu_current.update(container, delta);
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		menu_current.render(container, g);
	}
	
}