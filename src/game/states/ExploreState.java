package game.states;

import game.Defines;
import game.gui.GUI;
import game.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
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
		// delta *= 3; // FAST
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
