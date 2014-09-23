import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

import utils.Vector2D;

public class Game {
	public static Player player;

	private static int width = 640;
	private static int height = 480;

	public static void main(String[] args) {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
			Display.setTitle("RPG");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}

		player = new Player();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, 640, 480, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);

			player.draw();

			pollInput();

			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	public static void pollInput() {
		// Handles keyboard and mouse input

		player.azimuth = Math.toDegrees(Math.atan2(height - Mouse.getY()
				- player.position.y, Mouse.getX() - player.position.x));

		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			player.moveForward();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			player.moveBackward();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			player.moveLeft();
		} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			player.moveRight();
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					Display.destroy();
					System.exit(0);
				}
			} else {
				/*
				 * switch (Keyboard.getEventKey()) { case Keyboard.KEY_W:
				 * player.moveUp(); break; case Keyboard.KEY_A:
				 * player.moveLeft(); break; case Keyboard.KEY_S:
				 * player.moveDown(); break; case Keyboard.KEY_D:
				 * player.moveRight(); break; }
				 */
			}
		}
	}
}
