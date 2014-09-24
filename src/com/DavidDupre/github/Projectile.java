package com.DavidDupre.github;
import java.util.List;

import com.DavidDupre.github.utils.Boundry;
import com.DavidDupre.github.utils.Square;
import com.DavidDupre.github.utils.Vector2D;

public class Projectile {
	public Vector2D position = new Vector2D();
	private List<Boundry> boundries;
	public double size = 50;
	public Square image = new Square(size, "res/rang.png", boundries);
	private double azimuth;
	private double speed = 20;
	boolean distancePassed = false;
	private int projectileDistance;
	
	Projectile(double x, double y, Player entity, List<Boundry> boundries) {
		position.set(x, y);
		azimuth = entity.azimuth;
		System.out.println(azimuth);
	}

	public void update() {
		if (projectileDistance < 30) {
			image.theta = azimuth;
			position.add(new Vector2D(speed * Math.cos(Math.toRadians(azimuth)), speed
					* Math.sin(Math.toRadians(azimuth))));
			projectileDistance++;
			draw();
		}
		
		else {
			distancePassed = true;
		}
	}

	public void draw() {
		image.position.set(this.position);
		image.draw();
	}
}
