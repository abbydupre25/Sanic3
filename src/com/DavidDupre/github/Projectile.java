package com.DavidDupre.github;
import java.util.List;

import com.DavidDupre.github.utils.Boundry;
import com.DavidDupre.github.utils.Square;
import com.DavidDupre.github.utils.Vector2D;

public class Projectile {
	public Vector2D position = new Vector2D();
	public double size = 30;
	public Square image;
	
	private List<Enemy> enemies;
	
	private double azimuth;
	private double speed = 20;
	private boolean distancePassed = false;
	private boolean collided = false; 
	private int projectileDistance;
	
	Projectile(double x, double y, Player entity, List<Boundry> boundries, List<Enemy> enemies) {
		position.set(x, y);
		azimuth = entity.azimuth;
		this.enemies = enemies;
		this.image = new Square(size, "res/rang.png", boundries);
	}

	public void update() {
		image.boundryDetection(position);
		endConditions();
		if (!isFinished()) {
			image.theta = azimuth;
			position.add(new Vector2D(speed * Math.cos(Math.toRadians(azimuth)), speed
					* Math.sin(Math.toRadians(azimuth))));
		}
	}
	
	private void endConditions() {
		if (projectileDistance < 20) {
			projectileDistance++;
		}
		
		else {
			distancePassed = true;
		}
	
		int nullEnemy = -1;
		for (Enemy e: enemies) {
			if(image.collided(e.image)) {
				collided = true;
				e.killNotify();
				nullEnemy = enemies.indexOf(e);
			}
		}
		if (nullEnemy != -1) {
			enemies.remove(nullEnemy);
		}
	}
	
	public boolean isFinished() {
		System.out.println(distancePassed + ", " + collided);
		return distancePassed || collided;
	}

	public void draw() {
		image.position.set(this.position);
		image.draw();
	}
}
