package game.player;

import game.Ability;
import game.Defines.Action;
import game.Defines.MoveDir;
import game.Defines.MoveKey;
import game.Inventory;
import game.component.InputComponent;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;

public class PlayerInput extends InputComponent {
	private boolean locked = false;

	@Override
	public void update(GameContainer gc, int delta) {
		if (!locked) {
			MoveDir move = MoveDir.MOVE_NULL;
			Action action = Action.ACTION_NULL;
			Input input = gc.getInput();

			if (input.isKeyPressed(MoveKey.INTERACT.get())) {
				action = Action.ACTION_INTERACT;
			} else if (input.isKeyPressed(MoveKey.INVENTORY.get())) {
				action = Action.ACTION_INVENTORY;
			} else if (input.isKeyPressed(MoveKey.PAUSE.get())) {
				action = Action.ACTION_PAUSE;
			} else if (input.isKeyPressed(MoveKey.QUEST.get())) {
				action = Action.ACTION_QUEST;
			} else {
				for (Inventory.GearSlot gs : ((Player) gameObject).getInv().getGearSlots()
						.values()) {
					if (gs.isOccupied()) {
						for (Ability a : gs.getGear().getAbilities()) {
							if (a.isBeingInvoked(input)) {
								action = a.getAction();
								a.playSound();
							} else if (a.isPlayingSound()) {
								a.stopSound();
							}
						}
					}
				}
			}

			if (input.isKeyDown(MoveKey.MOVE_UP.get())) {
				move = MoveDir.MOVE_UP;
			} else if (input.isKeyDown(MoveKey.MOVE_DOWN.get())) {
				move = MoveDir.MOVE_DOWN;
			} else if (input.isKeyDown(MoveKey.MOVE_LEFT.get())) {
				move = MoveDir.MOVE_LEFT;
			} else if (input.isKeyDown(MoveKey.MOVE_RIGHT.get())) {
				move = MoveDir.MOVE_RIGHT;
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
