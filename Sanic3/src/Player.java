
import utils.Square;
import utils.Vector2D;

public class Player {
	public Vector2D position = new Vector2D();
	public Square image = new Square(30);
	public double azimuth = 0;
	public double speed = 5;

	public Player() {
		
	}

	public void draw() {
		image.azimuth = azimuth;
		if (azimuth > 90 || azimuth < -90) {
			image.isFlipped = true;
		} else {
			image.isFlipped = false;
		}
		image.position.set(this.position);
		image.draw();
	}

	public void moveForward() {
		position.add(new Vector2D(speed * Math.cos(Math.toRadians(azimuth)),
				speed * Math.sin(Math.toRadians(azimuth))));
	}

	public void moveBackward() {
		position.add(new Vector2D(speed * -Math.cos(Math.toRadians(azimuth)),
				speed * -Math.sin(Math.toRadians(azimuth))));
	}

	public void moveLeft() {
		position.add(new Vector2D(speed * Math.sin(Math.toRadians(azimuth)),
				speed * -Math.cos(Math.toRadians(azimuth))));
	}

	public void moveRight() {
		position.add(new Vector2D(speed * -Math.sin(Math.toRadians(azimuth)),
				speed * Math.cos(Math.toRadians(azimuth))));
	}
}
