
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

public class Game {
	public static Player player;

	private static int width = 640;
	private static int height = 480;
	
	private static Music music;

	public static void main(String[] args) throws SlickException {
		init();
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
	
	public static void init() throws SlickException{		
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("Sanic 3");
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
				
		player = new Player();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		GL11.glClearColor(0f, 1f, 0f, 0f);

		music = new Music("res/sanicTheme.ogg");
		music.loop();
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
