package game.component;

import org.newdawn.slick.GameContainer;

import game.GameObject;

public abstract class Component {
	protected GameObject gameObject;
	
	abstract public void update(GameContainer gc, int delta);
	abstract protected void initModifierDependents();
	
	public void setModifier(GameObject gameObject) {
		this.gameObject = gameObject;
		initModifierDependents();
	}
}
