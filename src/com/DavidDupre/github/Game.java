package com.DavidDupre.github;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import com.DavidDupre.github.utils.Boundry;

public class Game {
	private static List<Boundry> boundries = new ArrayList<Boundry>();
	private static List<Player> players = new ArrayList<Player>();
	private static List<Enemy> enemies = new ArrayList<Enemy>();

	private static int width = 640;
	private static int height = 480;

	public static TrueTypeFont font;

	public static void main(String[] args) throws SlickException {
		init();
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			pollInput();

			for (int i = 0; i < players.size(); i++) {
				players.get(i).update();
				players.get(i).draw();
			}

			for (int i = 0; i < enemies.size(); i++) {
				if (enemies.get(i).deadFlag() == -1) {
					enemies.get(i).update();
					enemies.get(i).draw();
				} else {
					enemies.remove(enemies.get(i).deadFlag());
				}
			}
			Display.update();
			Display.sync(60);
		}
		Display.destroy();
		System.exit(0);
	}

	public static void init() throws SlickException {
		//Put the display inside a swing canvas
		GUI gui = new GUI(width, height);
		gui.setDialogue("res/exampleDialogue.xml");

		try {
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

		// Loading Screen stuff
		glClear(GL_COLOR_BUFFER_BIT);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		Texture splash = null;
		try {
			splash = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("res/sanicIII.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		GL11.glLoadIdentity();
		Color.white.bind();
		splash.bind();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0,0);
		GL11.glVertex2f(0,0);
		GL11.glTexCoord2f(1,0);
		GL11.glVertex2f(splash.getTextureWidth(),0);
		GL11.glTexCoord2f(1,1);
		GL11.glVertex2f(splash.getTextureWidth(),splash.getTextureHeight());
		GL11.glTexCoord2f(0,1);
		GL11.glVertex2f(0,splash.getTextureHeight());
		GL11.glEnd();
		Display.update();

		Boundry mapEdge = new Boundry(0, width, 0, height);
		boundries.add(mapEdge);

		players.add(new Player(width / 2, height / 2, 50, "res/sanic.png",
				boundries, enemies));
		players.add(new Player(width / 2 - 50, height / 2, 40, "res/tails.png",
				boundries, enemies));

		Random random = new Random();
		enemies.add(new Enemy((int) (random.nextDouble() * width),
				(int) (random.nextDouble() * height), 50, "res/shaedow.png",
				boundries, players));

		Sounds.load();
		Sounds.music.loop();
		Sounds.music.setVolume(.05f); //watch your ears m8
	}

	public static void pollInput() {
		// Handles keyboard and mouse input
		boolean booped = false;
		boolean m_booped = false;
		boolean rollBooped = false;
		boolean fireBooped = false;

		for (Player p : players) {
			p.azimuth = Math.toDegrees(Math.atan2(
					height - Mouse.getY() - p.position.y,
					Mouse.getX() - p.position.x));
			if (Mouse.getX() - p.position.x < 0) {
				p.v_azimuth = 180;
			} else {
				p.v_azimuth = 0;
			}
			
			if (p.equals(players.get(0))) {
				if (Keyboard.isKeyDown(Keyboard.KEY_W)
						|| Keyboard.isKeyDown(Keyboard.KEY_A)
						|| Keyboard.isKeyDown(Keyboard.KEY_S)
						|| Keyboard.isKeyDown(Keyboard.KEY_D)) {
					p.wobble++;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
					p.moveForward();
				} else if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
					p.moveBackward();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
					p.moveLeft();
				} else if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
					p.moveRight();
				}
			} else if (p.equals(players.get(1))) {
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)
						|| Keyboard.isKeyDown(Keyboard.KEY_LEFT)
						|| Keyboard.isKeyDown(Keyboard.KEY_RIGHT)
						|| Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					p.wobble++;
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
					p.moveForward();
				} else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
					p.moveBackward();
				}
				if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
					p.moveLeft();
				} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
					p.moveRight();
				}
			}
			while (Keyboard.next() || booped) {
				booped = true;
				if (Keyboard.getEventKeyState()) {
					if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
						Display.destroy();
						System.exit(0);
					}
				} else {
					if (Keyboard.getEventKey() == Keyboard.KEY_SPACE
							|| rollBooped) {
						rollBooped = true;
						p.azimuth = Math.toDegrees(Math.atan2(
								height - Mouse.getY() - p.position.y,
								Mouse.getX() - p.position.x));
						p.roll();
						p.azimuth = 0;
						break;
					}
				}
				break;
			}
			while (Mouse.next() || m_booped) {
				m_booped = true;
				if (!Mouse.getEventButtonState() || fireBooped) {
					if (Mouse.getEventButton() == 0 || fireBooped) { // Detect mouse movement
						fireBooped = true;
						p.fire();
					}
				}
				break;
			}
		}
	}
}

