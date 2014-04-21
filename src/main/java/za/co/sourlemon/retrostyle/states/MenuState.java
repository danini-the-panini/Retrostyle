package za.co.sourlemon.retrostyle.states;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import za.co.sourlemon.retrostyle.Arcade;
import za.co.sourlemon.retrostyle.entities.StarSystem;
import za.co.sourlemon.retrostyle.menu.Menu;
import za.co.sourlemon.retrostyle.menu.MenuButton;
import za.co.sourlemon.retrostyle.menu.MenuItem;
import za.co.sourlemon.retrostyle.menu.MenuManagerState;

public class MenuState extends MenuManagerState {
	public static final int ID = 11;
	
	public static final float SHIFT = 5f;

	StarSystem stars;
	
	MenuItem
	btn_newGame;
	Menu menu_options;
		MenuItem
		btn_controls,	
		btn_back;
	public Menu menu_hiScores,
		menu_reset;
			MenuItem
			btn_yes,
			btn_no;
		//btn_back
	Menu menu_exit;
//		MenuItem
//		btn_yes
//		btn_no

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		stars = new StarSystem(500, 0.1f, 5, Arcade.WIDTH, Arcade.HEIGHT);
		
		menu_main = new Menu(LoadState.img_main_title,
				null,this,container,true,0);
		
		btn_newGame = new MenuButton(LoadState.img_main_newGame,
				menu_main,this,0);
		
		{
			menu_options = new Menu(LoadState.img_main_options,
					menu_main,this,container,true,1);
			
			btn_controls = new MenuButton(LoadState.img_opt_controls,
					menu_options,this,0);
			
			btn_back = new MenuButton(LoadState.img_menu_back,
					menu_options,this,1);
			
			MenuItem[] items = {btn_controls,btn_back};
			
			menu_options.setItems(items);
		}
		
		{
			menu_hiScores = new Menu(LoadState.img_main_hiScores,
					menu_main,this,container,false,2);
			
			{
				menu_reset = new Menu(LoadState.img_hs_reset,
						menu_hiScores,this,container,true,0);
				
				btn_yes = new MenuButton(LoadState.img_menu_yes,
						menu_options,this,0);
				
				btn_no = new MenuButton(LoadState.img_menu_no,
						menu_options,this,1);
				
				MenuItem[] resetItems = {btn_yes,btn_no};
				
				menu_reset.setItems(resetItems);
			}
			
			//btn_back
			
			MenuItem[] items = {menu_reset,btn_back};
			
			menu_hiScores.setItems(items);
		}
		
		{
			menu_exit = new Menu(LoadState.img_main_exit,
					menu_main,this,container,true,3);
			
			MenuItem[] items = {btn_yes,btn_no};
			
			menu_exit.setItems(items);
		}
		
		MenuItem[] mainItems = {btn_newGame, menu_options, menu_hiScores, menu_exit};
		
		menu_main.setItems(mainItems);
		
		menu_current = menu_main;
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		stars.render(g);
		g.setColor(new Color(0xff9600));
		g.fillRect(container.getWidth()/2-3, 0, 5, container.getHeight());
		super.render(container, game, g);
		
		if (menu_current.equals(menu_hiScores)
				|| (menu_current.transition != null
				&& menu_current.transition.equals(menu_hiScores))) {
			int offset = (int) (menu_current.transition == null ? 0 : -500
					*(menu_current.transition == menu_hiScores
							? 1.0f-menu_current.transRatio
							: menu_current.transRatio));
			for (int i = 0; i < LoadState.data.getNames().length; i++) {
				LoadState.font.drawString(
						offset,
						-5+50*i,(i+1)+". " + LoadState.data.getNames()[i]
				+ "..." + LoadState.data.getScores()[i]);
			}
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		stars.update(delta);
		super.update(container, game, delta);
		LoadState.updateMusic(delta);
	}

	@Override
	public void buttonPressed(MenuButton button) {
		if (button.equals(btn_yes)) {
			if (menu_current.equals(menu_exit)) Arcade.game.exitGame();
			else if (menu_current.equals(menu_reset)) {
				LoadState.data.clearScores();
				menu_current.changeMenu(menu_current.parent);
			}
		}
		else if (button.equals(btn_back) || button.equals(btn_no))
			menu_current.changeMenu(menu_current.parent);
		else if (button.equals(btn_newGame)) {
			Arcade.game.startGame();
		}
	}
	
	@Override
	public void confirmPressed(Menu menu) {}
	@Override
	public void cancelPressed(Menu menu) {
		if (menu.equals(menu_main)) System.exit(0);
	}

	@Override
	public void cancelPressed() {
		if (menu_current.cancel())
			itemSelected();
	}

	@Override
	public void confirmPressed() {
		if (menu_current.confirm())
			itemSelected();
	}

	@Override
	public void downPressed() {
		int shift = menu_current.moveDown();
		if (shift != 0) itemMoved(SHIFT*((shift<0) ? -1 : 1));
	}

	@Override
	public void upPressed() {
		int shift = menu_current.moveUp();
		if (shift != 0) itemMoved(SHIFT*((shift<0) ? -1 : 1));
	}
	
	public void itemMoved(float shift) {
		stars.shift(
			menu_current.optionX,
			menu_current.optionY-menu_current.selection*Menu.ITEM_SEPARATION,
			menu_current.optionX+menu_current.maxWidth+50,
			menu_current.optionY+
				(menu_current.items.length-menu_current.selection)*Menu.ITEM_SEPARATION,
			0f,shift);
	}
	
	public void itemSelected() {
		stars.explode(
			menu_current.optionX+menu_current.items[menu_current.selection].image.getWidth()/2,
			menu_current.optionY+Menu.ITEM_HEIGHT, 200);
	}

}
