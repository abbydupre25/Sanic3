package game.states;

import game.Defines;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class PausedState extends BasicGameState {
	private int id;
	private int selected = 1;
	
	public PausedState(int id) {
		this.id = id;
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame sbc)
			throws SlickException {
		
	}

	@Override
	public void render(GameContainer gc, StateBasedGame sbg, Graphics g)
			throws SlickException {
		int x = Defines.WINDOW_WIDTH/2 - 50;
		int y = 60;
		int space = 20;
		g.drawString("PAUSED", x, y);
		g.drawString("Resume game", x, y+space);
		g.drawString("Quit", x, y+space*2);
		g.drawRoundRect(x, y+space*selected, 200, space, 5);
	}

	@Override
	public void update(GameContainer gc, StateBasedGame sbg, int g)
			throws SlickException {
		Input input = gc.getInput();
		if(input.isKeyPressed(Keyboard.KEY_ESCAPE)) {
			sbg.enterState(Defines.ID_EXPLORE);
			gc.getInput().clearKeyPressedRecord();
		} else if (input.isKeyPressed(Keyboard.KEY_S)) {
			selected = 2;
		} else if (input.isKeyPressed(Keyboard.KEY_W)) {
			selected = 1;
		} else if (input.isKeyPressed(Keyboard.KEY_RETURN)) {
			switch(selected){
			case 1:
				sbg.enterState(Defines.ID_EXPLORE);
				gc.getInput().clearKeyPressedRecord();
				break;
			case 2:
				System.exit(0);
			}
		}
	}

	@Override
	public int getID() {
		return id;
	}

}
