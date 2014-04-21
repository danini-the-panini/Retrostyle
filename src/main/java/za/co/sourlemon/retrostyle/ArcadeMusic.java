package za.co.sourlemon.retrostyle;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import za.co.sourlemon.retrostyle.states.LoadState;

public class ArcadeMusic {
	
	private Music music;
	
	private boolean goingDown = false;
	private float volume = 1.0f;
	private boolean fading = false;
	
	private final int MUSIC_FADE_TIME = 1000;
	
	public ArcadeMusic(String ref) throws SlickException {
		
		music = new Music(ref);
		
	}
	
	public void stop() {
		music.stop();
	}
	
	public void loop() {
		if (!LoadState.data.getMusic()) return;
		music.loop();
	}
	
	public void pause() {
		music.pause();
	}
	
	public void resume() {
		if (!LoadState.data.getMusic()) return;
		music.resume();
	}
	
	public boolean playing() {
		return music.playing();
	}
	
	public void fade() {
		if (!playing())
			resume();
		goingDown = !goingDown;
		fading = true;
	}
	
	public void update(long delta) {
		if (!LoadState.data.getMusic()) {
			stop();
			reset();
			return;
		} else {
			if (fading) {
					float fade = (float) delta / (float) MUSIC_FADE_TIME;
					if (goingDown) {
						volume -= fade;
						if (volume <= 0.0f) {
							volume = 0.0f;
							fading = false;
							pause();
						}
					} else {
						volume += fade;
						if (volume >= 1.0f) {
							volume = 1.0f;
							fading = false;
						}
					}
					music.setVolume(volume);
			}
		}
	}
	
	public void reset() {
		goingDown = false;
		volume = 1.0f;
		fading = false;
	}
	
	public void kill() {
		reset();
		stop();
	}

}
