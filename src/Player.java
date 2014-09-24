
import utils.Square;
import utils.Vector2D;

public class Player {
	public Vector2D position = new Vector2D();
	public double size = 50;
	public Square image = new Square(size);
	public double azimuth = 0;
	public double speed = 5;
	private int wobble = 0;

	public Player() {
		
	}

	public void draw() {
		image.theta = azimuth;
		image.theta += Math.toDegrees(Math.sin(wobble))*.1;
		if (azimuth > 90 || azimuth < -90) {
			image.isFlipped = true;
		} else {
			image.isFlipped = false;
		}
		image.position.set(this.position);
		image.draw();
	}

	public void moveForward() {
		wobble+=1;
		position.add(new Vector2D(speed * Math.cos(Math.toRadians(azimuth)),
				speed * Math.sin(Math.toRadians(azimuth))));
	}

	public void moveBackward() {
		wobble-=1;
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
