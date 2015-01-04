package game.map;

import game.Defines;
import game.gui.GUI;
import game.player.Player;

import java.io.File;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;

public class MapManager {
	private HashMap<String, Map> maps;
	private Map map;

	public MapManager(GameContainer gc, StateBasedGame sbg, GUI gui)
			throws SlickException {
		maps = new HashMap<String, Map>();

		// Find and load maps
		File[] files = new File("res/maps/").listFiles();
		for (File file : files) {
			if (file.isFile() && file.getName().endsWith("tmx")) {
				String name = file.getName();
				maps.put(name, new Map("res/maps/" + name, gc, sbg));
			}
		}
		for (Map m : maps.values()) {
			m.initPortalMap(maps);
		}

		File file = new File(Defines.MAP_CONFIG_PATH);
		Document doc = null;
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String firstMap = doc.getElementsByTagName("init").item(0)
				.getAttributes().getNamedItem("file").getTextContent();
		map = maps.get(firstMap);
	}

	public HashMap<String, Map> getMaps() {
		return maps;
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public void addPlayer(Player player) {
		for (Map m : maps.values()) {
			m.getGameObjects().add(player);
		}
	}
}
