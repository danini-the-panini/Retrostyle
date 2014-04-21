package za.co.sourlemon.retrostyle.menu;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

public class Menu extends MenuItem {

	public static final int SELECTION_TRANSITION_TIME = 300, MENU_TRANSITION_TIME = 400;
	public static final int CENTER_SEPARATION = 40, ITEM_SEPARATION = 80, ITEM_HEIGHT = 65;
	public static final float IMAGE_ALPHA = 0.2f, IMAGE_ALPHA_INV = 1.0f - IMAGE_ALPHA;
	
	public int titleX, titleY;
	public int optionX, optionY;
	public float cOptionY;
	public int maxWidth = 0;
	
	public MenuItem[] items;
	int previousSelection = -1;
	public int selection = 0;
	boolean up = false;
	int transDistance = 0;
	public Menu transition;
	int transTime = 0;
	public float transRatio = 0.0f;
	
	boolean showLabel = true;
	
	public Menu(Image image, Menu parent, MenuManager manager,
			GameContainer container, boolean showLabel, int id) {
		super(image, parent, manager, id);
		
		this.showLabel = showLabel;
		
		optionX = container.getWidth()/2+CENTER_SEPARATION;
		cOptionY = optionY = container.getHeight()/2-ITEM_HEIGHT/2;

		titleX = container.getWidth()/2-CENTER_SEPARATION-image.getWidth();
		if (image.getHeight() != ITEM_HEIGHT) {
			titleY = container.getHeight()/2-image.getHeight()/2;
		} else {
			titleY = optionY;
		}
	}
	
	public void setItems(MenuItem[] items) {
		
		this.items = items;
		for (int i = 0; i < items.length; i++) {
			if (i != selection) items[i].image.setAlpha(IMAGE_ALPHA);
			if (items[i].image.getWidth()>maxWidth) maxWidth=items[i].image.getWidth();
		}
		
	}
	
	public void reset() {
		items[selection].image.setAlpha(1.0f);
		previousSelection = -1;
		transRatio = 0.0f;
		cOptionY = optionY-ITEM_SEPARATION*selection;
	}
	
	public void update(GameContainer container, long delta) {
		
		// if we are not moving to a new menu
		if (transition == null) {
			
			// if we are moving the selection
			if (previousSelection != -1) {
				
				transRatio = 1.0f-((optionY-ITEM_SEPARATION*selection-cOptionY)/(float)transDistance);
				
				float moveBy = (transDistance)
					*((float)delta/(float)SELECTION_TRANSITION_TIME);
				
				cOptionY += moveBy;
				
				// the transition is finished
				if (up	? (cOptionY > optionY-ITEM_SEPARATION*selection)
							: (cOptionY < optionY-ITEM_SEPARATION*selection)) {
					
					// reset the alpha of the selection
					items[previousSelection].image.setAlpha(IMAGE_ALPHA);
					reset();
				
				// the transition is happening
				} else {
					
					// update the alpha of the selected items
					items[previousSelection].image.setAlpha
						((1.0f-transRatio)*IMAGE_ALPHA_INV+IMAGE_ALPHA);
					items[selection].image.setAlpha
						(transRatio*IMAGE_ALPHA_INV+IMAGE_ALPHA);
							
				}
				
			}
			
		// if we are moving to a new menu
		} else {
			
			transTime += delta;
			
			// the transition is finished
			if (transTime > MENU_TRANSITION_TIME) {
				
				// reset the menus
				transTime = 0;
				transition.image.setAlpha(1.0f);
				
				for (int i = 0; i < items.length; i++) {
					items[i].image.setAlpha(i == selection ? 1.0f : 0.0f);
				}
				
				image.setAlpha(1.0f);
				manager.setMenu(transition);
				transition = null;
				transRatio = 0.0f;
				
			// the transition is happening
			} else {
				
				// update the alpha of the menus and items
				transRatio = (float)transTime/(float)MENU_TRANSITION_TIME;
				
				float alpha = 1.0f-transRatio;
				for (int i = 0; i < items.length; i++) {
					if (!(i == selection && transition != parent))
						items[i].image.setAlpha(alpha*IMAGE_ALPHA);
				}
				
				for (int i = 0; i < transition.items.length; i++) {
					if (!showLabel || !(i == transition.selection && transition == parent))
						transition.items[i].image.setAlpha(
								(1.0f-alpha)*((i==transition.selection) ? 1.0f : IMAGE_ALPHA));
				}
				
				if (transition == parent) {
					items[selection].image.setAlpha(alpha);
					if (transition.showLabel) transition.image.setAlpha(1.0f-alpha);
				} else {
					if (showLabel) image.setAlpha(transition.showLabel ? alpha : alpha);
					if (!transition.showLabel) transition.image.setAlpha(alpha);
				}
			}
		}
	}
	
	public void render(GameContainer container, Graphics g) {
		
		// if we are not moving to a different menu
		if (transition == null) {
			
			// if we want to show the label on the left side, draw it
			if (showLabel) g.drawImage(image, titleX, titleY);
			
			
			for (int i = 0; i < items.length; i++) {
				g.drawImage(items[i].image, optionX,
						cOptionY + i*ITEM_SEPARATION);
			}
			
		// if we are moving to a different menu
		} else {
			
			// draw the outgoing menu items
			for (int i = 0; i < items.length; i++) {
				if (!(i == selection && transition != parent))
					g.drawImage(items[i].image, optionX,
						optionY + i*ITEM_SEPARATION
						- selection*ITEM_SEPARATION);
			}
			
			// draw the incoming menu items
			for (int i = 0; i < transition.items.length; i++) {
				if (!(i == transition.selection && transition == parent))
					g.drawImage(transition.items[i].image, transition.optionX,
						transition.optionY + i*ITEM_SEPARATION
						- transition.selection*ITEM_SEPARATION);
			}
			
			// draw the incoming and outgoing menu items
			if (transition == parent) {
				if (transition.showLabel) g.drawImage(transition.image, transition.titleX
						- (optionX-titleX)*(1.0f-transRatio), transition.titleY);
				g.drawImage(image, titleX + (optionX-titleX)*transRatio, titleY);
			} else {
				if (showLabel) g.drawImage(image, titleX
						-(optionX-transition.titleX)*(transRatio), titleY);
				g.drawImage(transition.image, transition.titleX
						+(optionX-transition.titleX)*(1.0f-transRatio), transition.titleY);
			}
		}
	}
	
	// TODO: implement transition
	public int moveUp(){
		if (previousSelection == -1 && transition == null) {
			previousSelection = selection;
			if (selection == 0) selection = items.length-1;
			else selection--;
			return moveItem();
		}
		return 0;
	}
	
	public int moveDown() {
		if (previousSelection == -1 && transition == null) {
			previousSelection = selection;
			if (selection == items.length-1) selection = 0;
			else selection++;
			return moveItem();
		}
		return 0;
	}
	
	public int moveItem() {
		items[previousSelection].image.setAlpha(1.0f);
		items[selection].image.setAlpha(IMAGE_ALPHA);
		up = cOptionY < optionY-ITEM_SEPARATION*selection;
		transDistance = (int) (optionY-ITEM_SEPARATION*selection-cOptionY);
		
		return selection-previousSelection;
	}
	
	public boolean changeMenu(Menu menu) {
		if (previousSelection == -1 && transition == null) {
			this.transition = menu;
			return true;
		}
		return false;
	}
	
	@Override
	public boolean confirm() {
		if (items[selection] instanceof MenuButton) {
			return items[selection].confirm();
		}
		else if (items[selection] instanceof Menu) {
			return changeMenu((Menu)items[selection]);
		}
		return false;
	}
	
	@Override
	public boolean cancel() {
		if (parent == null) {
			manager.cancelPressed(this);
			return true;
		}
		return changeMenu(parent);
	}
	
}
