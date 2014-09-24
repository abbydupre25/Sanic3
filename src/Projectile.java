import utils.Square;
import utils.Vector2D;

public class Projectile {
	public Vector2D position = new Vector2D();
	public double size = 50;
	public Square image = new Square(size, "res/rang.png");
	private double azimuth;
	private double speed = 20;

	boolean distancePassed = false;
	private int projectileDistance;
	
	Projectile(double x, double y, Player entity) {
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
