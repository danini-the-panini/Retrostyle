package za.co.sourlemon.retrostyle.menu;

import org.newdawn.slick.Image;

public abstract class MenuItem {

	public Menu parent;
	public MenuManager manager;
	public Image image;
	public int id;

	public MenuItem(Image image, Menu parent, MenuManager manager, int id) {
		this.image = image;
		this.parent = parent;
		this.manager = manager;
		this.id = id;
	}
	
	public abstract boolean confirm();
	public abstract boolean cancel();
	
}
