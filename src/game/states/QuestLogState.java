package game.states;

import game.Defines;
import game.History;
import game.gui.QuestPanel;
import game.player.Player;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class QuestLogState extends BasicGameState {
	private int id;
	private History history;
	private QuestPanel qp;
	
	public QuestLogState(int id, Player player) {
		this.id = id;
		this.history = player.getHistory();
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbg)
			throws SlickException {
		java.awt.Font awtFont = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);
		TrueTypeFont font = new TrueTypeFont(awtFont, false);
		qp = new QuestPanel(20, 20, Defines.WINDOW_WIDTH-40, Defines.WINDOW_HEIGHT-40, history, font);
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		qp.render(g);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int delta)
			throws SlickException {
		qp.update(gc,  sbg,  delta);
	}

	@Override
	public int getID() {
		return id;
	}
}
