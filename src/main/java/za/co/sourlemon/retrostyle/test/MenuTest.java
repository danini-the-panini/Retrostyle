package za.co.sourlemon.retrostyle.test;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.entities.StarSystem;
import za.co.sourlemon.retrostyle.menu.Menu;
import za.co.sourlemon.retrostyle.menu.MenuButton;
import za.co.sourlemon.retrostyle.menu.MenuManagerGame;
import za.co.sourlemon.retrostyle.menu.MenuItem;
import za.co.sourlemon.retrostyle.states.LoadState;

public class MenuTest extends MenuManagerGame {
	
	StarSystem stars;
	
	MenuItem
	btn_newGame;
	Menu menu_options;
		MenuItem
		btn_controls,	
		btn_back;
	Menu menu_hiScores,
		menu_reset;
			MenuItem
			btn_yes,
			btn_no;
		//btn_back
	Menu menu_exit;
//		MenuItem
//		btn_yes
//		btn_no
	
	public MenuTest(String title) {
		super(title);
	}

	public static void main(String[] args) {
		try {
			AppGameContainer container = new AppGameContainer(new MenuTest("Menu Test"),1024,600,false);
			container.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {

		stars = new StarSystem(500, 0.1f, 5, container.getWidth(), container.getHeight());
		
		menu_main = new Menu(new Image(LoadState.MENU_RES + "new/main-title.png"),
				null,this,container,true,0);
		
		btn_newGame = new MenuButton(new Image(LoadState.MENU_RES + "new/main-newGame.png"),
				menu_main,this,0);
		
		{
			menu_options = new Menu(new Image(LoadState.MENU_RES + "new/main-options.png"),
					menu_main,this,container,true,1);
			
			btn_controls = new MenuButton(new Image(LoadState.MENU_RES + "new/opt-controls.png"),
					menu_options,this,0);
			
			btn_back = new MenuButton(new Image(LoadState.MENU_RES + "new/back.png"),
					menu_options,this,1);
			
			MenuItem[] items = {btn_controls,btn_back};
			
			menu_options.setItems(items);
		}
		
		{
			menu_hiScores = new Menu(new Image(LoadState.MENU_RES + "new/main-hiScores.png"),
					menu_main,this,container,false,2);
			
			{
				menu_reset = new Menu(new Image(LoadState.MENU_RES + "new/hs-reset.png"),
						menu_hiScores,this,container,true,0);
				
				btn_yes = new MenuButton(new Image(LoadState.MENU_RES + "new/yes.png"),
						menu_options,this,0);
				
				btn_no = new MenuButton(new Image(LoadState.MENU_RES + "new/no.png"),
						menu_options,this,1);
				
				MenuItem[] resetItems = {btn_yes,btn_no};
				
				menu_reset.setItems(resetItems);
			}
			
			//btn_back
			
			MenuItem[] items = {menu_reset,btn_back};
			
			menu_hiScores.setItems(items);
		}
		
		{
			menu_exit = new Menu(new Image(LoadState.MENU_RES + "new/main-exit.png"),
					menu_main,this,container,true,3);
			
			MenuItem[] items = {btn_yes,btn_no};
			
			menu_exit.setItems(items);
		}
		
		MenuItem[] mainItems = {btn_newGame, menu_options, menu_hiScores, menu_exit};
		
		menu_main.setItems(mainItems);
		
		menu_current = menu_main;
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		stars.update(delta);
		super.update(container, delta);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		stars.render(g);
		g.setColor(new Color(0xff9600));
		g.fillRect(container.getWidth()/2-3, 0, 5, container.getHeight());
		super.render(container, g);
	}
	
	@Override
	public void keyReleased(int key, char c) {
		float shift = 5;
		boolean explode = true;
		switch (key) {
		case Input.KEY_UP:
			shift *= (menu_current.moveUp()<0) ? -1 : 1;
			explode = false;
			break;
		case Input.KEY_DOWN:
			shift *= (menu_current.moveDown()<0) ? -1 : 1;
			explode = false;
			break;
		case Input.KEY_ENTER:
		case Input.KEY_NUMPADENTER:
			menu_current.confirm();
			break;
		case Input.KEY_ESCAPE:
		case Input.KEY_BACK:
			menu_current.cancel();
			break;
		}

		if (explode) stars.explode(
				menu_current.optionX+menu_current.items[menu_current.selection].image.getWidth()/2,
				menu_current.optionY+Menu.ITEM_HEIGHT, 200);
		else stars.shift(
				menu_current.optionX,
				menu_current.optionY-menu_current.selection*Menu.ITEM_SEPARATION,
				menu_current.optionX+menu_current.maxWidth,
				menu_current.optionY+
					(menu_current.items.length-menu_current.selection)*Menu.ITEM_SEPARATION,
				0f,shift);
	}

	
	@Override
	public void buttonPressed(MenuButton button) {
		if (button.equals(btn_yes)) {
			if (menu_current.equals(menu_exit)) System.exit(0);
		}
		if (button.equals(btn_back) || button.equals(btn_no))
			menu_current.changeMenu(menu_current.parent);
	}

	@Override
	public void confirmPressed(Menu menu) {}
	@Override
	public void cancelPressed(Menu menu) {
		if (menu.equals(menu_main)) System.exit(0);
	}
	
}
