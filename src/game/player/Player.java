package game.player;

import game.Defines.Action;
import game.Defines.ComponentType;
import game.Defines.MoveDir;
import game.Effect;
import game.GameObject;
import game.History;
import game.Inventory;
import game.component.Component;
import game.item.Item;
import game.util.Vector2D;

import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

public class Player extends GameObject {
	private Animation sprite;
	private Inventory inv;
	private History history;
	private HashMap<String, Float> stats;
	private HashMap<ComponentType, Component> components;

	public Player(Vector2D pos, StateBasedGame sbg,
			HashMap<ComponentType, Component> components) {
		super(pos, sbg, components);
		for (Component c : components.values()) {
			c.setModifier(this);
		}
		this.components = components;
		sprite = ((PlayerRender) components.get(ComponentType.RENDER))
				.getInitialSprite();
		setMoveDir(MoveDir.MOVE_NULL);
		setAction(Action.ACTION_NULL);
		this.inv = new Inventory(this);
		this.history = new History();
		stats = new HashMap<String, Float>();
	}

	@Override
	public void update(GameContainer gc, int delta) {
		for (Component c : components.values()) {
			c.update(gc, delta);
		}
		sprite.update(delta);
	}

	@Override
	public void render() {
		sprite.draw((int) pos.x, (int) pos.y);
	}

	public void setSprite(Animation sprite) {
		this.sprite = sprite;
	}

	public Animation getSprite() {
		return sprite;
	}

	public Inventory getInv() {
		return inv;
	}

	public void setInv(Inventory inv) {
		this.inv = inv;
	}
	
	public History getHistory() {
		return history;
	}

	public void setHistory(History history) {
		this.history = history;
	}

	public void equip(Item item) {
		for (Effect e : item.getEffects()) {
			if (stats.containsKey(e.getStat())) {
				float originalValue = stats.get(e.getStat());
				stats.remove(e.getStat());
				stats.put(e.getStat(), originalValue + e.getBoost());
			} else {
				stats.put(e.getStat(), e.getBoost());
			}
			if (e.getStat().equals("speed")) { // TODO magic
				PlayerPhysics physics = ((PlayerPhysics) components
						.get(ComponentType.PHYSICS));
				physics.setSpeed(physics.getSpeed() + e.getBoost());
			}
		}
	}

	public void unequip(Item item) {
		for (Effect e : item.getEffects()) {
			float originalValue = stats.get(e.getStat());
			stats.remove(e.getStat());
			stats.put(e.getStat(), originalValue - e.getBoost());
			if (e.getStat().equals("speed")) { // TODO magic
				PlayerPhysics physics = ((PlayerPhysics) components
						.get(ComponentType.PHYSICS));
				physics.setSpeed(physics.getSpeed() - e.getBoost());
			}
		}
	}

	@Override
	public void setMovementLock(boolean state) {
		((PlayerInput) components.get(ComponentType.INPUT)).setLock(state);
	}
}
