package game.player;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

import game.Defines;
import game.Defines.Action;
import game.Defines.MoveDir;
import game.component.InputComponent;

public class PlayerInput extends InputComponent {
	
	private boolean locked = false;
	
	private enum MoveKey {
		MOVE_NULL(0), MOVE_UP(Keyboard.KEY_W), MOVE_DOWN(Keyboard.KEY_S), MOVE_LEFT(
				Keyboard.KEY_A), MOVE_RIGHT(Keyboard.KEY_D), INTERACT(
				Keyboard.KEY_E), INVENTORY(Keyboard.KEY_TAB), PAUSE(Keyboard.KEY_ESCAPE);

		private int i;

		MoveKey(int i) {
			this.i = i;
		}

		public int get() {
			return i;
		}
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		if(!locked) {
			MoveDir move;
			Defines.Action action;
			Input input = gc.getInput();
	
			if (input.isKeyPressed(MoveKey.INTERACT.get())) {
				action = Defines.Action.ACTION_INTERACT;
			} else if (input.isKeyPressed(MoveKey.INVENTORY.get())){
				action = Defines.Action.ACTION_INVENTORY;
			} else if(input.isKeyPressed(MoveKey.PAUSE.get())) {
				action = Defines.Action.ACTION_PAUSE;
			} else {
				action = Defines.Action.ACTION_NULL;
			}
	
			if (input.isKeyDown(MoveKey.MOVE_UP.get())) {
				move = MoveDir.MOVE_UP;
			} else if (input.isKeyDown(MoveKey.MOVE_DOWN.get())) {
				move = MoveDir.MOVE_DOWN;
			} else if (input.isKeyDown(MoveKey.MOVE_LEFT.get())) {
				move = MoveDir.MOVE_LEFT;
			} else if (input.isKeyDown(MoveKey.MOVE_RIGHT.get())) {
				move = MoveDir.MOVE_RIGHT;
			} else {
				move = MoveDir.MOVE_NULL;
			}
	
			gameObject.setMoveDir(move);
			gameObject.setAction(action);
		} else {
			gameObject.setMoveDir(MoveDir.MOVE_NULL);
			gameObject.setAction(Action.ACTION_NULL);
		}
	}

	@Override
	protected void initModifierDependents() {
		
	}

	public void setLock(boolean state) {
		locked = state;
	}

}
