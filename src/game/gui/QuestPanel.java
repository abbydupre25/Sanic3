package game.gui;

import game.Defines;
import game.Defines.MoveKey;
import game.History;
import game.Objective;
import game.Quest;

import java.util.ArrayList;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

public class QuestPanel {
	private History history;
	private TrueTypeFont font;
	private SelectPanel selectPanel;
	private TextPanel descPanel;
	private ObjectivesPanel oPanel;
	
	public QuestPanel(int x, int y, int width, int height, History history,
			TrueTypeFont font) throws SlickException {
		this.history = history;
		this.font = font;
		selectPanel = new SelectPanel(x, y, 200, height);
		descPanel = new TextPanel(x+200, y, width-200, 100, font);
		oPanel = new ObjectivesPanel(x+200, y+100, width-200, height-100);
	}
	
	private class SelectPanel {
		private int x;
		private int y;
		private int width;
		private int height;
		private int selected;
		
		public SelectPanel(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			selected = 0;
		}
		
		public void render(Graphics g) {
			Object[] quests = history.getQuests().values().toArray();
			for(int i=0; i<quests.length; i++) {
				Quest q = (Quest) quests[i];
				if(q.isComplete()) {
					g.setColor(new Color(1f, 1f, 1f, .5f));
				}
				g.drawString(q.getName(), x, y+i*font.getHeight());
				g.setColor(Color.white);
			}
			if(quests.length > 0) {
				g.drawRoundRect(x, y+selected*font.getHeight(), width, font.getHeight(), 4);
			}
			g.drawRoundRect(x, y, width, height, 4);
		}
		
		public void shiftSelected(int increment) {
			int requestedID = selected+increment;
			if(requestedID >= 0 && requestedID < history.getQuests().size()){
				selected += increment;
			}
		}
		
		public Quest getSelected() {
			Object[] quests = history.getQuests().values().toArray();
			return (Quest) quests[selected];
		}
		
		public boolean isEmpty() {
			return history.getQuests().isEmpty();
		}
	}
	
	private class ObjectivesPanel {
		private int x;
		private int y;
		private int width;
		private int height;
		
		public ObjectivesPanel(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
		
		public void render(Graphics g) {
			if(!selectPanel.isEmpty()) {
				Quest q = selectPanel.getSelected();
				ArrayList<Objective> objectives = q.getObjectives();
				for(int i=0; i<objectives.size(); i++) {
					Objective o = objectives.get(i);
					if(o.isDiscovered()) {
						if(o.isComplete()) {
							g.setColor(new Color(1f, 1f, 1f, .5f));
						}
						g.drawString(o.getDesc(), x, y+i*font.getHeight());
						g.setColor(Color.white);
					}
				}
			}
			g.drawRoundRect(x, y, width, height, 4);
		}
	}
	
	public void shiftSelected(int increment) {
		selectPanel.shiftSelected(increment);
	}
	
	public void render(Graphics g) {
		g.setFont(font);
		selectPanel.render(g);
		descPanel.render(g);
		oPanel.render(g);
		if(!selectPanel.isEmpty()) {
			descPanel.setText(selectPanel.getSelected().getDesc());
		}
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input input = gc.getInput();
		if(input.isKeyPressed(MoveKey.QUEST.get())) {
			sbg.enterState(Defines.ID_EXPLORE);
		} else if (input.isKeyPressed(MoveKey.MOVE_DOWN.get())) {
			selectPanel.shiftSelected(1);
		} else if (input.isKeyPressed(MoveKey.MOVE_UP.get())) {
			selectPanel.shiftSelected(-1);
		}
	}
}
