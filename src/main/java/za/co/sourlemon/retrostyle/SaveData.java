package za.co.sourlemon.retrostyle;

import java.io.*;

import org.newdawn.slick.*;

import za.co.sourlemon.retrostyle.states.LoadState;

public class SaveData implements Serializable {
	private static final long serialVersionUID = 7898673474474160324L;
	
	private boolean particles;
	private boolean music;
	private String[] names = new String[9];
	private int[] scores = new int[9];
	private int[] controls = new int[6];
	
	public SaveData(boolean particles, boolean music, String[] names, int[] scores, int[] controls) {
		this.particles = particles;
		this.music = music;
		this.names = names;
		this.scores = scores;
		this.controls = controls;
	}
        
        public SaveData()
        {
            this(
                    true, true,
                    new String[]{"THE","BEST","ARCADE","PLAYERS","OF","ALL","TIME","BY","FAR"},
                    new int[]{0,0,0,0,0,0,0,0,0},
                    new int[]{
					Input.KEY_UP,
					Input.KEY_DOWN,
					Input.KEY_LEFT,
					Input.KEY_RIGHT,
					Input.KEY_SPACE,
					Input.KEY_LSHIFT}
            );
        }
	
	public void addScore(String name, int score) {
		String[] names1 = new String[names.length];
		int[] scores1 = new int[scores.length];
		boolean found = false;
		for (int i = 0; i < scores.length; i++) {
			if (score>scores[i] && !found) {
				for (int j = 0; j < i; j++) {
					names1[j] = names[j];
					scores1[j] = scores[j];
				}
				names1[i] = name;
				scores1[i] = score;
				for (int j = i; j < scores.length-1; j++) {
					names1[j+1] = names[j];
					scores1[j+1] = scores[j];
				}
				found = true;
			}
		}
		this.names = names1;
		this.scores = scores1;
	}
	
	public void clearScores() {
		String[] names1 = {"THE","BEST","ARCADE","PLAYERS","OF","ALL","TIME","BY","FAR"};
		this.names = names1;
		int[] scores1 = {0,0,0,0,0,0,0,0,0};
		this.scores = scores1;
	}
	
	public void setParticles(boolean particles) {
		this.particles = particles;
	}
	
	public void setMusic(boolean music) {
		this.music = music;
	}
	
	public void setNames(String[] names) {
		this.names = names;
	}
	
	public void setScores(int[] scores) {
		this.scores = scores;
	}
	
	public void setControls(int[] controls) {
		this.controls = controls;
	}
	
	public boolean getParticles() {
		return particles;
	}
	
	public boolean getMusic() {
		return music;
	}
	
	public String[] getNames() {
		return names;
	}
	
	public int[] getScores() {
		return scores;
	}
	
	public int[] getControls() {
		return controls;
	}
	
	public static void main(String[] args) {
		try {
			String[] newNames = {"THE","BEST","ARCADE","PLAYERS","OF","ALL","TIME","BY","FAR"};
			int[] newScores = {0,0,0,0,0,0,0,0,0};
			int[] newControls = {0,0,0,0,0,0,0,0,0,0,0,0};
			boolean particles1 = false;
			boolean music1 = false;
			
				particles1 = true;
				music1 = true;
				String[] newNames1 = {"THE","BEST","ARCADE","PLAYERS","OF","ALL","TIME","BY","FAR"};
				newNames = newNames1;
				int[] newScores1 = {0,0,0,0,0,0,0,0,0};
				newScores = newScores1;
				int[] newControls1 = {
					Input.KEY_UP,
					Input.KEY_DOWN,
					Input.KEY_LEFT,
					Input.KEY_RIGHT,
					Input.KEY_SPACE,
					Input.KEY_LSHIFT
				};
				newControls = newControls1;
				
			SaveData data = new SaveData(
				particles1,
				music1,
				newNames,
				newScores,
				newControls
			);
			FileOutputStream fo = new FileOutputStream(LoadState.RES + "data");
			ObjectOutputStream oo = new ObjectOutputStream(fo);
			oo.writeObject(data);
			oo.close();
			System.out.println("done.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}