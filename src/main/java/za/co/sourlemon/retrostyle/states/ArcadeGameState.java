package za.co.sourlemon.retrostyle.states;

import org.newdawn.slick.state.BasicGameState;

public abstract class ArcadeGameState extends BasicGameState {

	public abstract void upPressed();
	public abstract void downPressed();
	public abstract void confirmPressed();
	public abstract void cancelPressed();
	
}
