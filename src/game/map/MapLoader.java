package game.map;

import game.Defines;
import game.player.Player;
import game.util.ItemLoader;

import java.io.File;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;

public class MapLoader {
	public static void load(GameContainer gc, StateBasedGame sbg, Player player) throws SlickException {
		HashMap<String, Map> maps = new HashMap<String, Map>();

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

		Document doc = ItemLoader.getDoc(Defines.MAP_CONFIG_PATH);
		String firstMap = doc.getElementsByTagName("init").item(0)
				.getAttributes().getNamedItem("file").getTextContent();
		Map map = maps.get(firstMap);
		
		for (Map m : maps.values()) {
			m.getGameObjects().add(player);
		}
		
		player.setMap(map);
	}
}
