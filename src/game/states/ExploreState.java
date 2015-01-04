package game.states;

import game.Defines;
import game.Defines.ComponentType;
import game.History;
import game.Inventory;
import game.component.Component;
import game.gui.GUI;
import game.map.MapManager;
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
	private MapManager mm;
	private GUI gui;
	private Player player;
	private Inventory inv;
	private History history;

	public ExploreState(int id, Inventory inv, History history) {
		this.id = id;
		this.inv = inv;
		this.history = history;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		gui = new GUI(gc, inv, history);
		ItemLoader.setGUI(gui);
		player = ItemLoader.getPlayer(Defines.PLAYER_PATH, inv);
		player = new Player(new Vector2D(100, 100), inv, sbg,
				new HashMap<ComponentType, Component>() {
					{
						put(ComponentType.INPUT, new PlayerInput());
						put(ComponentType.PHYSICS, new PlayerPhysics(.15f));
						put(ComponentType.RENDER, new PlayerRender());
						put(ComponentType.INTERACT, new PlayerInteract());
					}
				});
	
		mm = new MapManager(gc, sbg, gui);
		mm.addPlayer(player);
		player.setMapManager(mm);
		inv.setOwner(player);
		player.setMap(mm.getMap());
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		mm.getMap().renderObjects(gc, sbg, g);
		gui.render(gc, g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		mm.getMap().updateObjects(gc, sbg, delta);
		
		// Do graphics shit
		mm.getMap().getCamera().centerOn((float) player.getPos().x, (float) player.getPos().y,
				Defines.SIZE, Defines.SIZE);
		gui.update();
	}

	@Override
	public int getID() {
		return id;
	}

}
