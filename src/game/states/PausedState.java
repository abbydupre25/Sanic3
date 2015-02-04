package game.states;

import game.Defines;
import game.gui.PausedPanel;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PausedState extends BasicGameState {
	private int id;
	private PausedPanel panel;
	private Music music;
	
	public PausedState(int id) {
		this.id = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbc)
			throws SlickException {
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);
		TrueTypeFont font = new TrueTypeFont(awtFont, false);
		
		music = new Music("res/audio/greenHill.ogg");
		music.loop();
		music.setVolume(0.01f);
		
		panel = new PausedPanel(20, 20, Defines.WINDOW_WIDTH-40, Defines.WINDOW_HEIGHT-40, font, music);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		panel.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		panel.update(gc, sbg, delta);
	}

	@Override
	public int getID() {
		return id;
	}

}
