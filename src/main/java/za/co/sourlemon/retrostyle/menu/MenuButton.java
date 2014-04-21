package za.co.sourlemon.retrostyle.menu;

import org.newdawn.slick.Image;

public class MenuButton extends MenuItem {
	
	public MenuButton(Image image, Menu parent, MenuManager manager, int id) {
		super(image, parent, manager, id);
	}

	@Override
	public boolean confirm() {
		manager.buttonPressed(this);
		return true;
	}

	@Override
	public boolean cancel() {
		return true;
	}

}
