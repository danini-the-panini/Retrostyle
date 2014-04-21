package za.co.sourlemon.retrostyle.states;

import java.util.Vector;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class HelpState extends ArcadeGameState {
	public static final int ID = 10;
	
	private static final int STAYING_PUT = 0, GOING_UP = 1, GOING_DOWN = 2, Y_INC = 5;
	
	public int currentPage = 0;
	private int whatsItDoing = STAYING_PUT;
	
	private int[] y;

	public HelpState() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public int getID() {
		return ID;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		container.setVSync(false);
		container.setMouseGrabbed(true);
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		Vector<Image> imageList = new Vector<Image>();
		for (int i = 0; i < getY().length; i++) {
			if (getY()[i] < container.getHeight() && getY()[i] > -container.getHeight()) {
//				imageList.add(LoadState.img_help[i]);
			}
		}
		if (imageList.size() > 1) {
			if (whatsItDoing == GOING_UP) {
				float alpha = (float)-getY()[currentPage]/(float)container.getHeight();
				
				g.setColor(new Color(0,0,0,alpha));
				g.drawImage(imageList.get(0), 0, getY()[currentPage]);
				g.fillRect(0, getY()[currentPage], container.getWidth(), container.getHeight());
				
				g.setColor(new Color(0,0,0,1-alpha));
				g.drawImage(imageList.get(1), 0, getY()[currentPage+1]);
				g.fillRect(0, getY()[currentPage+1], container.getWidth(), container.getHeight());
				
				g.setColor(Color.white);
				
			} else if (whatsItDoing == GOING_DOWN) {
				float alpha = (float)-getY()[currentPage-1]/(float)container.getHeight();
				
				g.setColor(new Color(0,0,0,alpha));
				g.drawImage(imageList.get(0), 0, getY()[currentPage-1]);
				g.fillRect(0, getY()[currentPage-1], container.getWidth(), container.getHeight());
				
				g.setColor(new Color(0,0,0,1-alpha));
				g.drawImage(imageList.get(1), 0, getY()[currentPage]);
				g.fillRect(0, getY()[currentPage], container.getWidth(), container.getHeight());
				
				g.setColor(Color.white);
			}
		} else {
			g.drawImage(imageList.get(0), 0, 0);
		}

	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		
		switch (whatsItDoing) {
		case GOING_UP:
			boolean found1 = false;
			for (int i = 0; i < getY().length; i++) {
				getY()[i]-=Y_INC;
				if (getY()[i] == 0) {
					currentPage = i;
					found1 = true;
				}
				if (found1) {
					whatsItDoing = STAYING_PUT;
				}
			}
			break;
		case GOING_DOWN:
			boolean found2 = false;
			for (int i = 0; i < getY().length; i++) {
				getY()[i]+=Y_INC;
				if (getY()[i] == 0) {
					currentPage = i;
					found2 = true;
				}
				if (found2) {
					whatsItDoing = STAYING_PUT;
				}
			}
			break;
			
		}
		
	}

	public void setY(int[] y) {
		this.y = y;
	}

	public int[] getY() {
		return y;
	}

	public void cancelPressed() {}
	public void confirmPressed() {}

	public void upPressed() {
		if (whatsItDoing == STAYING_PUT) {
			if (currentPage > 0) {
				whatsItDoing = GOING_DOWN;
			}
		}
	}
	
	public void downPressed() {
		if (whatsItDoing == STAYING_PUT) {
			if (currentPage < getY().length-1) {
				whatsItDoing = GOING_UP;
			}
		}
	}

}
