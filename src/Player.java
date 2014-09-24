
import utils.Square;
import utils.Vector2D;

public class Player {
	public Vector2D position = new Vector2D();
	public double size = 50;
	public Square image = new Square(size);
	public double azimuth = 0;
	public double speed = 5;
	public int wobble = 0;
	
	//Terrible variables that should probably go away
	public int action = Actions.IDLE;
	private boolean movementLocked = false;
	private int rollCount = 0;
	private double rollAzimuth;

	public Player() {
		
	}

	public void draw() {
		image.theta = azimuth;
		image.theta += Math.toDegrees(Math.sin(wobble))*.1;
		switch (action) {
		case Actions.ROLL:
			if (rollCount < 10){
				movementLocked = true;
				position.add(new Vector2D(2 * speed * Math.cos(Math.toRadians(rollAzimuth)),
						2 * speed * Math.sin(Math.toRadians(rollAzimuth))));
				rollCount++;
				image.theta += Math.toDegrees(Math.sin(rollCount))*2;
			} else {
				rollCount = 0;
				movementLocked = false;
				action = Actions.IDLE;
			}
		}
		if (azimuth > 90 || azimuth < -90) {
			image.isFlipped = true;
		} else {
			image.isFlipped = false;
		}
		image.position.set(this.position);
		image.draw();
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
