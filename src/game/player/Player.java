package game.player;

import game.Defines.Action;
import game.Defines.ComponentType;
import game.Defines.MoveDir;
import game.Effect;
import game.GameObject;
import game.Inventory;
import game.component.Component;
import game.item.gear.Gear;
import game.map.Map;
import game.map.MapManager;
import game.util.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.state.StateBasedGame;

// TODO can't wear two hats / pants / armors at once
public class Player extends GameObject {
	private Animation sprite;
	private Inventory inv;
	private MapManager mm;
	private HashMap<String, Float> stats;
	private ArrayList<Gear> gears;

	public Player(Vector2D pos, Inventory inv,
			StateBasedGame sbg,
			HashMap<ComponentType, Component> components) {
		super(pos, sbg, components);
		for (Component c : components.values()) {
			c.setModifier(this);
		}
		sprite = ((PlayerRender) components.get(ComponentType.RENDER))
				.getInitialSprite();
		setMoveDir(MoveDir.MOVE_NULL);
		setAction(Action.ACTION_NULL);
		this.inv = inv;
		gears = new ArrayList<Gear>();
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
	
	public void changeMap(Map map) {
		mm.setMap(map);
		setMap(map);
	}
	
	public void setMapManager(MapManager mm) {
		this.mm = mm;
	}
	
	public void addGear(Gear gear) {
		gears.add(gear);
		// Have to do it this dumb way because Floats aren't mutable
		// Maybe I should stop using hashmaps for stuff like this
		for(Effect e : gear.getEffects()) {
			float originalValue = stats.get(e.getStat());
			stats.remove(e.getStat());
			stats.put(e.getStat(), originalValue + e.getBoost());
		}
	}
	
	public void removeGear(Gear gear) {
		gears.remove(gear);
		for(Effect e : gear.getEffects()) {
			float originalValue = stats.get(e.getStat());
			stats.remove(e.getStat());
			stats.put(e.getStat(), originalValue - e.getBoost());
		}
	}
}
