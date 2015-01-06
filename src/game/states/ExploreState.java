package game.states;

import game.Defines;
import game.Defines.ComponentType;
import game.History;
import game.Inventory;
import game.component.Component;
import game.gui.GUI;
import game.map.MapLoader;
import game.player.Player;
import game.player.PlayerInput;
import game.player.PlayerInteract;
import game.player.PlayerPhysics;
import game.player.PlayerRender;
import game.util.ItemLoader;
import game.util.Vector2D;

import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class ExploreState extends BasicGameState {
	private int id;
	private GUI gui;
	private Player player;

	public ExploreState(int id, Player player, GUI gui) {
		this.id = id;
		this.player = player;
		this.gui = gui;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {

	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		player.getMap().renderObjects(gc, sbg, g);
		gui.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		player.getMap().updateObjects(gc, sbg, delta);
		
		// Do graphics shit
		player.getMap().getCamera().centerOn((float) player.getPos().x, (float) player.getPos().y,
				Defines.SIZE, Defines.SIZE);
		gui.update();
	}

	@Override
	public int getID() {
		return id;
	}

}
