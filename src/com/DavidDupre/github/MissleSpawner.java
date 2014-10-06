package com.DavidDupre.github;

import java.util.ArrayList;
import java.util.List;

public class MissleSpawner {
	private List<Projectile> projectiles = new ArrayList<Projectile>();
	Entity e;
	
	public MissleSpawner(Entity e) {
		this.e = e;
	}
	
	public void update() {
		for (int i = 0; i < projectiles.size(); i++) {
			if (projectiles.get(i).isFinished()) {
				projectiles.remove(i);
			}
			
			else {
				projectiles.get(i).update();
				projectiles.get(i).draw();
			}
		}
	}
	
	public void fire() {
		projectiles.add(new Projectile(e.position.x, e.position.y, e, e.boundries, e.enemies));
	}
}
