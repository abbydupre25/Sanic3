package game;

import game.gui.GUI;
import game.map.MapLoader;
import game.player.Player;
import game.states.ExploreState;
import game.states.InventoryState;
import game.states.MenuState;
import game.states.PausedState;
import game.states.QuestLogState;
import game.util.ItemLoader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
	public Game(String title) throws SlickException{
		super(title);
		ItemLoader.setGame(this);
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		// Initialize here instead of the constructor because the openGL context is available
		Player player = ItemLoader.getPlayer(Defines.PLAYER_PATH);
		GUI gui = new GUI(gc, player);
		ItemLoader.setGUI(gui);
		MapLoader.load(gc, this, player);
		this.addState(new MenuState(Defines.ID_MENU));
		this.addState(new ExploreState(Defines.ID_EXPLORE, player, gui));
		this.addState(new InventoryState(Defines.ID_INV, player));
		this.addState(new PausedState(Defines.ID_PAUSED));
		this.addState(new QuestLogState(Defines.ID_QUEST, player));
		this.enterState(Defines.ID_MENU);
	}
	
	public static void main(String[] arguments) {
		try {
			AppGameContainer app = new AppGameContainer(new Game("Sanic 3"));
			app.setDisplayMode(Defines.WINDOW_WIDTH, Defines.WINDOW_HEIGHT, false);
			//app.setTargetFrameRate(120);
			//app.setFullscreen(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
