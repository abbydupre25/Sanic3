package com.DavidDupre.github.Player;

import org.lwjgl.input.Mouse;

import com.DavidDupre.github.Actions;
import com.DavidDupre.github.Defines;
import com.DavidDupre.github.Entity;
import com.DavidDupre.github.Physics;
import com.DavidDupre.github.utils.Vector2D;

public class PlayerPhysics extends Physics {
	Player p;
	int action = Actions.IDLE;
	int rollCount = 0;
	boolean movementLocked = false;
	Vector2D position;
	double azimuth = 0;
	double speed = 5;

	PlayerPhysics(Player p, int x, int y, double speed) {
		this.p = p;
		position.set(x, y);
		this.speed = speed; 
	}

	public Vector2D getPosition() {
		return new Vector2D();
	}

	public void addPosition(Vector2D i) {
		if (!movementLocked) {
			position.add(i);
		}
	}

	@Override
	public void update() {
		azimuth = Math.toDegrees(Math.atan2(Defines.HEIGHT - Mouse.getY()
				- position.y, Mouse.getX() - position.x));

		switch (action) {
		case Actions.ROLL:
			if (rollCount < 20) {
				movementLocked = true;
				position.add(new Vector2D(2 * speed
						* Math.cos(Math.toRadians(azimuth)), 2 * speed
						* Math.sin(Math.toRadians(azimuth))));
				rollCount++;
				this.image.theta += Math.toDegrees(Math.sin(rollCount)) * 2;
			} else {
				rollCount = 0;
				movementLocked = false;
				action = Actions.IDLE;
			}
		}
	}
}
