package game;

import org.lwjgl.input.Keyboard;

public class Defines {
	public static final int SIZE = 32;
	public static final int WINDOW_WIDTH = 640;// 1366;
	public static final int WINDOW_HEIGHT = 480;//768;
	public static final int TILE_WIDTH = WINDOW_WIDTH/SIZE;
	public static final int TILE_HEIGHT = WINDOW_HEIGHT/SIZE;
	
	public static final int ID_MENU = 0;
	public static final int ID_EXPLORE = 1;
	public static final int ID_INV = 2;
	public static final int ID_PAUSED = 3;
	public static final int ID_QUEST = 4;
		
	public enum MoveDir {
		MOVE_NULL, MOVE_UP, MOVE_DOWN, MOVE_LEFT, MOVE_RIGHT
	}
	
	public enum MoveKey {
		MOVE_NULL(0), MOVE_UP(Keyboard.KEY_W), MOVE_DOWN(Keyboard.KEY_S), MOVE_LEFT(
				Keyboard.KEY_A), MOVE_RIGHT(Keyboard.KEY_D), INTERACT(
				Keyboard.KEY_E), INVENTORY(Keyboard.KEY_TAB), PAUSE(Keyboard.KEY_ESCAPE),
				RUN(Keyboard.KEY_LSHIFT), QUEST(Keyboard.KEY_Z);

		private int i;

		MoveKey(int i) {
			this.i = i;
		}

		public int get() {
			return i;
		}
	}
	
	public enum Action {
		ACTION_INTERACT, ACTION_NULL, ACTION_INVENTORY, ACTION_PAUSE, ACTION_RUN, ACTION_QUEST
	}
	
	public enum ComponentType {
		RENDER, PHYSICS, INPUT, INTERACT
	}
	
	public static final String[] invisibleLayers = new String[]{
			"objects", "portal", "blocked"
	};
	
	public static final String[] gearTypes = new String[]{
		"weapon", "shoes", "hat"
	};
	
	public static final String MAP_CONFIG_PATH = "res/maps/mapLoader.xml";
	public static final String PLAYER_PATH = "res/player.xml";
	
	// TODO these need to match types in the xml files. Not seeing a way around this
	public static final String PLAYER = "player";
	public static final String MOB = "mob";
	public static final String ITEM = "item";
	public static final String CURRENCY_NAME = "rang";
}
