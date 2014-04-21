package za.co.sourlemon.retrostyle.entities;

import za.co.sourlemon.retrostyle.states.LoadState;

public class Loadout {

	public static final int BLASTER = 0, MINIGUN = 1, UBERGUN = 2, RAILGUN = 3, ROCKET_LAUNCHER = 4;
	public static final int MAX_LEVEL = 2;
	
	private int level = 0;
	
	private Gun[] guns = new Gun[5];
	
	public Gun primary;
	public Gun secondary;
	
	public Loadout() {
		guns[BLASTER] = new Gun(5, 300, 2000, 0.3f, -1, false, BLASTER, LoadState.snd_playerFire);
		guns[MINIGUN] = new Gun(5, 100, 1500, 0.17f, -1, false, MINIGUN, LoadState.snd_playerFire);
		guns[UBERGUN] = new Gun(5, 100, 50000, 0.04f, -1, false, UBERGUN, LoadState.snd_playerFire);
		guns[RAILGUN] = new Gun(5, 150, 2000, 0.2f, -1, false, RAILGUN, LoadState.snd_railgun);
		guns[ROCKET_LAUNCHER] = new Gun(100, 1000, 10000, 0f, 3, false, ROCKET_LAUNCHER, LoadState.snd_rocket);
		
		primary = guns[BLASTER];
		secondary = guns[ROCKET_LAUNCHER];
	}
	
	public void switchPrimary(int gunID) {
		if (primary != null) {
			if (gunID == primary.type) return;
			primary.reset();
		}
		primary = guns[gunID];
	}
	
	public void switchSecondary(int gunID) {
		if (secondary != null) {
			if (gunID == secondary.type) return;
			secondary.reset();
		}
		secondary = guns[gunID];
	}
	
	public void levelUp() {
		if (level < MAX_LEVEL) level++;
	}
	
	public void levelDown() {
		if (level > 0) level--;
	}
	
	public int getLevel() {
		return level;
	}
	
	public void update(int delta) {
		if (primary != null) primary.update(delta);
		if (secondary != null) secondary.update(delta);
	}
	
	public void reset() {
		level = 0;
		for (int i = 0; i < guns.length; i++) {
			guns[i].emptyClip();
		}
		primary.reset();
		secondary.reset();
		resetKeys();
	}
	
	public void resetKeys() {
		primary.firing = false;
		secondary.firing = false;
	}
}
