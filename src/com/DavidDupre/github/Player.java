package com.DavidDupre.github;

import java.util.ArrayList;
import java.util.List;

import com.DavidDupre.github.utils.Boundry;
import com.DavidDupre.github.utils.Square;
import com.DavidDupre.github.utils.Vector2D;

public class Player {
	public Vector2D position = new Vector2D();
	public int wobble = 0;
	public double azimuth = 0;
	
	private double size = 50;
	private Square image;
	
	private double speed = 5;
	private List<Boundry> boundries;
	
	//Terrible variables that should probably go away
	private int action = Actions.IDLE;
	private boolean movementLocked = false;
	private int rollCount = 0;
	private double rollAzimuth;
	private List<Projectile> projectiles = new ArrayList<Projectile>();

	public Player(int x, int y, int size, String imageUrl, List<Boundry> boundries) {
		this.boundries = boundries;
		this.size = size;
		position.x = x;
		position.y = y;
		this.image = new Square(size, imageUrl, boundries);
	}
	
	public void update() {
		//Collision detection...sort of
		image.boundryDetection(position);
		
		this.image.theta = azimuth;
		this.image.theta += Math.toDegrees(Math.sin(wobble))*.1;
		switch (action) {
		case Actions.ROLL:
			if (rollCount < 20){
				movementLocked = true;
				position.add(new Vector2D(2 * speed * Math.cos(Math.toRadians(rollAzimuth)),
						2 * speed * Math.sin(Math.toRadians(rollAzimuth))));
				rollCount++;
				this.image.theta += Math.toDegrees(Math.sin(rollCount))*2;
			} else {
				rollCount = 0;
				movementLocked = false;
				action = Actions.IDLE;
			}
		}
		
		for (int i = 0; i < projectiles.size(); i++) { // Needs to have the for loop to reference the list and delete elements
			if (projectiles.get(i).distancePassed) {
				projectiles.remove(i);
			}
			
			else {
				projectiles.get(i).update();
			}
		}
	}

	public void fire() {
		projectiles.add(new Projectile(position.x, position.y, this, boundries));
	}
	
	public void draw() {
		if (azimuth > 90 || azimuth < -90) {
			this.image.isFlipped = true;
		} else {
			this.image.isFlipped = false;
		}
		this.image.position.set(this.position);
		this.image.draw();
	}
	
	public void roll() {
		action = Actions.ROLL;
		rollAzimuth = azimuth;
	}

	public void moveForward() {
		if (!movementLocked) {
			position.add(new Vector2D(0, -speed));
		}
	}

	public void moveBackward() {
		if (!movementLocked) {
			position.add(new Vector2D(0, speed));
		}
	}

	public void moveLeft() {
		if (!movementLocked) {
			position.add(new Vector2D(-speed, 0));
		}
	}

	public void moveRight() {
		if (!movementLocked) {
			position.add(new Vector2D(speed, 0));
		}
	}
}
