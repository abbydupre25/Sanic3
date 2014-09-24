
import java.util.ArrayList;
import java.util.List;

import utils.Boundry;
import utils.Square;
import utils.Vector2D;

public class Player {
	public Vector2D position = new Vector2D();
	public double size = 50;
	public Square image;
	public double azimuth = 0;
	public double speed = 5;
	public int wobble = 0;
	public List<Boundry> boundries = new ArrayList<Boundry>();
	
	//Terrible variables that should probably go away
	public int action = Actions.IDLE;
	private boolean movementLocked = false;
	private int rollCount = 0;
	private double rollAzimuth;

	public Player(String imageUrl) {
		this.image = new Square(size, imageUrl);
	}
	
	public void update() {
		//Collision detection...sort of
		for (Boundry b : boundries) {
			if (position.x > b.maxX-size){
				position.x = b.maxX-size;
			} else if (position.x < b.minX+size){
				position.x = b.minX+size;
			}
			if (position.y > b.maxY-size) {
				position.y = b.maxY-size;
			} else if (position.y < b.minY+size) {
				position.y = b.minY+size; 
			}
		}
		
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
