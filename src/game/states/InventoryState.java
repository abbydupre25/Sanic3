package game.states;

import game.Defines;
import game.Inventory;
import game.gui.InventoryPanel;
import game.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class InventoryState extends BasicGameState {
	private int id;
	private Inventory inv;
	private InventoryPanel ip;
	
	public InventoryState(int id, Player player) {
		this.id = id;
		this.inv = player.getInv();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);
		TrueTypeFont font = new TrueTypeFont(awtFont, false);
		ip = new InventoryPanel(20, 20, Defines.WINDOW_WIDTH-40, Defines.WINDOW_HEIGHT-40, inv, font);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		ip.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		ip.update(gc,  sbg,  delta);
	}

	@Override
	public int getID() {
		return id;
	}
}
