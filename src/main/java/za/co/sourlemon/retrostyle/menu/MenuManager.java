package za.co.sourlemon.retrostyle.menu;

public interface MenuManager {
	
	public void setMenu(Menu menu);
	public void buttonPressed(MenuButton button);
	public void confirmPressed(Menu menu);
	public void cancelPressed(Menu menu);
	
}
