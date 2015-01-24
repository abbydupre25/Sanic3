package game;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

public class Ability {
	private Defines.Action action;
	private Defines.MoveKey key;

	public enum InputType {
		KEY_PRESSED, KEY_DOWN
	}

	private InputType inputType;
	private Sound sound;

	public Ability(Defines.Action a, Defines.MoveKey k, InputType t,
			String soundPath) throws SlickException {
		action = a;
		key = k;
		inputType = t;
		sound = new Sound(soundPath);
	}

	public Ability(Defines.Action a, Defines.MoveKey k) throws SlickException {
		this(a, k, InputType.KEY_PRESSED, null);
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
	
	public void playSound() {
		if(sound != null) {
			sound.play();
		}
	}
	
	public boolean isPlayingSound() {
		if(sound != null) {
			return sound.playing();
		}
		return false;
	}
	
	public void stopSound() {
		if(sound != null) {
			sound.stop();
		}
	}

	public boolean isBeingInvoked(Input input) {
		switch (inputType) {
		case KEY_PRESSED:
			return input.isKeyPressed(key.get());
		case KEY_DOWN:
			return input.isKeyDown(key.get());
		default:
			return false;
		}
	}
}
