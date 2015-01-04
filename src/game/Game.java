package game;

import game.states.ExploreState;
import game.states.InventoryState;
import game.states.MenuState;
import game.states.PausedState;
import game.util.ItemLoader;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Game extends StateBasedGame {
	public Game(String title) throws SlickException{
		super(title);
		ItemLoader.setGame(this);
		Inventory inv = new Inventory(); // shared by multiple states
		History history = new History();
		this.addState(new MenuState(Defines.ID_MENU));
		this.addState(new ExploreState(Defines.ID_EXPLORE, inv, history));
		this.addState(new InventoryState(Defines.ID_INV, inv));
		this.addState(new PausedState(Defines.ID_PAUSED));
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
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
