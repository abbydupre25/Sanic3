package game.mob;

import game.Defines.ComponentType;
import game.Defines.MoveDir;
import game.GameObject;
import game.component.Component;
import game.util.Vector2D;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Mob extends GameObject {
	private Animation sprite;
	
	public Mob(Vector2D pos, StateBasedGame sbg, HashMap<ComponentType, Component> components){
		super(pos, sbg, components);
		for (Component c: components.values()) {
			c.setModifier(this);
		}
		sprite = ((MobRender) components.get(ComponentType.RENDER)).getInitialSprite();
		setMoveDir(MoveDir.MOVE_NULL);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		for (Component c: components.values()) {
			c.update(gc, delta);
		}
		sprite.update(delta);
	}

	@Override
	public void render() {
		sprite.draw((int)pos.x, (int)pos.y);
	}
	
	@Override
	public void setMovementLock(boolean state) {
		((MobInput) components.get(ComponentType.INPUT)).setLock(state);
	}
	
	public void setSprite(Animation sprite){
		this.sprite = sprite;
	}
	
	public Animation getSprite(){
		return sprite;
	}
}
