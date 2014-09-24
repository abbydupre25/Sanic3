
import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;

public class Game {
	public static Player player;

	private static int width = 640;
	private static int height = 480;
	
	private static Music music;
	public static TrueTypeFont font;

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
				
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity(); // Resets any previous projection matrices
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glClearColor(0f, 1f, 0f, 0f);

		//Loading Screen stuff
		Font awtFont = new Font("Comic Sans", Font.BOLD, 24);
		font = new TrueTypeFont(awtFont, false);
		glClear(GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		font.drawString(width/2 - 50, height/2 - 20, "Loading...", Color.yellow);
		Display.update();
		
		player = new Player();
		player.position.set(width/2, height/2);
		music = new Music("res/sanicTheme.ogg");
		music.loop();
	}

	public static void pollInput() {
		// Handles keyboard and mouse input

		player.azimuth = Math.toDegrees(Math.atan2(height - Mouse.getY()
				- player.position.y, Mouse.getX() - player.position.x));
		
		if (Keyboard.isKeyDown(Keyboard.KEY_W) 
				|| Keyboard.isKeyDown(Keyboard.KEY_A)
				|| Keyboard.isKeyDown(Keyboard.KEY_S)
				|| Keyboard.isKeyDown(Keyboard.KEY_D)){
			player.wobble++;
		}
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
		
		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
			player.roll(); 
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
					Display.destroy();
					System.exit(0);
				}
			} else {
				/*switch (Keyboard.getEventKey()) { 
				case Keyboard.KEY_SPACE:
					player.roll(); 
					break; 
				}*/
			}
		}
	}
}
