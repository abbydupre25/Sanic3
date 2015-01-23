package game;

import org.newdawn.slick.Input;

public class Ability {
	private Defines.Action action;
	private Defines.MoveKey key;
	public enum InputType {
		KEY_PRESSED, KEY_DOWN
	}
	private InputType inputType;
	
	public Ability(Defines.Action a, Defines.MoveKey k, InputType t) {
		action = a;
		key = k;
		inputType = t;
	}
	
	public Ability(Defines.Action a, Defines.MoveKey k) {
		this(a, k, InputType.KEY_PRESSED);
	}

	public Defines.Action getAction() {
		return action;
	}

	public void setAction(Defines.Action action) {
		this.action = action;
	}

	public Defines.MoveKey getKey() {
		return key;
	}

	public void setKey(Defines.MoveKey key) {
		this.key = key;
	}
	
	public boolean isBeingInvoked(Input input){
		switch(inputType){
		case KEY_PRESSED:
			return input.isKeyPressed(key.get());
		case KEY_DOWN:
			return input.isKeyDown(key.get());
		default:
			return false;
		}
	}
}
