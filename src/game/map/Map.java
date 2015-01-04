package game.map;

import game.Defines;
import game.GameObject;
import game.util.ItemLoader;
import game.util.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;

public class Map extends TiledMap {
	private ArrayList<Rectangle> blocks;
	private ArrayList<Portal> portals;
	private ArrayList<GameObject> gameObjects;
	private Camera camera;

	public Map(String ref, GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		super(ref);
		this.gameObjects = new ArrayList<GameObject>();
		
		for (int xAxis = 0; xAxis < getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < getHeight(); yAxis++) {
				int layerID = getLayerIndex("objects");
				if (layerID != -1) {
					int tileID = getTileId(xAxis, yAxis, layerID);
					String theValue = getTileProperty(tileID, "file", "null");
					if (!"null".equals(theValue)) {
						createObject(tileID, xAxis, yAxis, sbg);
					}
				}
			}
		}

		// Make collision map
		blocks = new ArrayList<Rectangle>();
		ArrayList<int[]> blockPositions = getObjectPositions("blocked",
				"blocked", "true");
		for (int[] p : blockPositions) {
			blocks.add(new Rectangle((float) p[0] * Defines.SIZE, (float) p[1]
					* Defines.SIZE, Defines.SIZE, Defines.SIZE));
		}

		for (GameObject go : gameObjects) {
			go.setMap(this);
		}
		camera = new Camera(gc, this);
	}
	
	/** Add objects to map using ItemLoader */
	private void createObject(int tileID, int x, int y, StateBasedGame sbg) throws SlickException {
		String filePath = this.getTileProperty(tileID, "file", "null");
		Vector2D pos = new Vector2D(x*Defines.SIZE, y*Defines.SIZE);
		GameObject go = ItemLoader.getGameObjectFromFile(filePath);
		go.setPos(pos);
		gameObjects.add(go);
	}

	/** Gets the positions of all objects on the map with given properties */
	private ArrayList<int[]> getObjectPositions(String layer, String property,
			String value) {
		ArrayList<int[]> positions = new ArrayList<int[]>();
		for (int xAxis = 0; xAxis < getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < getHeight(); yAxis++) {
				int layerID = getLayerIndex(layer);
				int tileID = getTileId(xAxis, yAxis, layerID);
				String theValue = getTileProperty(tileID, property, "null");
				if (value.equals(theValue)) {
					positions.add(new int[] { xAxis, yAxis });
				}
			}
		}
		return positions;
	}

	public void initPortalMap(HashMap<String, Map> mapList) {
		portals = new ArrayList<Portal>();
		for (int xAxis = 0; xAxis < getWidth(); xAxis++) {
			for (int yAxis = 0; yAxis < getHeight(); yAxis++) {
				int layerID = getLayerIndex("portal");
				int tileID = getTileId(xAxis, yAxis, layerID);
				String target = getTileProperty(tileID, "target", "null");
				if (!target.equals("null")) {
					float posX = Float.parseFloat(getTileProperty(tileID, "x",
							"null"));
					float posY = Float.parseFloat(getTileProperty(tileID, "y",
							"null"));
					target += ".tmx";
					Portal newPortal = new Portal(new Rectangle((float) xAxis
							* Defines.SIZE, (float) yAxis * Defines.SIZE,
							Defines.SIZE, Defines.SIZE), mapList.get(target),
							posX, posY);
					portals.add(newPortal);
				}
			}
		}
	}

	public void renderObjects(GameContainer gc, StateBasedGame sbg, Graphics g) {
		camera.drawMap();
		camera.translateGraphics();
		for (GameObject go : gameObjects) {
			if (go.isOnMap()) {
				go.render();
			}
		}
		camera.untranslateGraphics();
	}

	public void updateObjects(GameContainer gc, StateBasedGame sbg, int delta) {
		if (!gc.isPaused()) {
			// Make or kill gameObjects per their request
			ArrayList<GameObject> newGameObjects = new ArrayList<GameObject>();
			ArrayList<GameObject> dyingGameObjects = new ArrayList<GameObject>();
			for (GameObject go : gameObjects) {
				if (!go.isOnMap()) {
					dyingGameObjects.add(go);
				} else {
					go.update(gc, delta);
					for (int i = 0; i < go.getRequests().size(); i++) {
						newGameObjects.add(go.getRequests().get(i));
					}
					go.setRequests(new ArrayList<GameObject>());
				}
			}
			gameObjects.addAll(newGameObjects);
			gameObjects.removeAll(dyingGameObjects);
		}
	}

	public boolean isInCollision(Rectangle rekt) {
		for (Rectangle r : blocks) {
			if (rekt.intersects(r)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isBeingPortaled(Rectangle rekt) {
		for (Portal p : portals) {
			if (rekt.intersects(p.getRect())) {
				return true;
			}
		}
		return false;
	}

	public Portal getPortal(Rectangle rectangle) {
		for (Portal p : portals) {
			if (rectangle.intersects(p.getRect())) {
				return p;
			}
		}
		return null;
	}

	public Camera getCamera() {
		return camera;
	}

	public ArrayList<GameObject> getGameObjects() {
		return gameObjects;
	}
}
