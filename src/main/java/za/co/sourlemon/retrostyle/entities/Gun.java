package za.co.sourlemon.retrostyle.entities;

import org.newdawn.slick.Sound;

import za.co.sourlemon.retrostyle.states.LoadState;

public class Gun {
	
	public int damage;
	private float heat = 0.0f;
	private float heatPerShot;
	private long cooldownTime, refireTime, lastShot = 0;
	private boolean tooHot = false;
	public boolean firing = false;
	private int ammo, maxAmmo;
	protected int type;
	
	Sound firingSound;
	
	public Gun(int damage, long refireTime, long cooldownTime, float heatPerShot, int maxAmmo, boolean startFull, int type, Sound firingSound) {
		this.damage = damage;
		this.refireTime = refireTime;
		this.cooldownTime = cooldownTime;
		this.heatPerShot = heatPerShot;
		this.maxAmmo = maxAmmo;
		ammo = startFull ?  maxAmmo : 0;
		this.type = type;
		this.firingSound = firingSound;
	}
	
	public Gun(Gun gun) {
		this.refireTime = gun.refireTime;
		this.cooldownTime = gun.cooldownTime;
		this.heatPerShot = gun.heatPerShot;
		this.type = gun.type;
		this.maxAmmo = gun.maxAmmo;
		this.ammo = gun.ammo;
		this.firingSound = gun.firingSound;
	}
	
	public void reload() {
		if (maxAmmo <= 0) return;
		ammo = maxAmmo;
	}
	
	public void reload(int ammo) {
		if (maxAmmo <= 0) return;
		this.ammo += ammo;
		if (this.ammo > maxAmmo) this.ammo = maxAmmo;
	}
	
	public void emptyClip() {
		if (maxAmmo <= 0) return;
		ammo = 0;
	}
	
	public int getAmmo() {
		return ammo;
	}
	
	public boolean ammoFull() {
		return ammo == maxAmmo;
	}
	
	public float getHeat() {
		return heat;
	}
	
	public void setRefireTime(long time) {
		this.refireTime = time;
	}
	
	public boolean fire() {
		if (System.currentTimeMillis() - lastShot < refireTime || tooHot) return false;
		if (maxAmmo > 0 && ammo <= 0) return false;
		lastShot = System.currentTimeMillis();
		if (firingSound != null) firingSound.play();
		heat += heatPerShot;
		if (heat >= 1.0f) {
			heat = 1.0f;
			tooHot = true;
			if (LoadState.snd_overheat != null) LoadState.snd_overheat.play();
		}
		if (maxAmmo > 0) ammo -= 1;
		return true;
	}
	
	public void update(int delta) {
		heat -= (float)delta/(float)cooldownTime;
		if (heat < 0) {
			heat = 0;
			tooHot = false;
		}
	}
	
	public boolean isTooHot() {
		return tooHot;
	}
	
	public void reset() {
		heat = 0.0f;
		lastShot = 0;
	}
	
	public int getType() {
		return type;
	}
}
