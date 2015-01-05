package game.player;

import game.Defines.Action;
import game.Defines.ComponentType;
import game.Defines.MoveDir;
import game.Effect;
import game.GameObject;
import game.Inventory;
import game.component.Component;
import game.item.Item;
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
	private ArrayList<Item> gears;
	private HashMap<ComponentType, Component> components;

	public Player(Vector2D pos, Inventory inv,
			StateBasedGame sbg,
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
		this.inv = inv;
		gears = new ArrayList<Item>();
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
	
	public void equip(Item item) {
		if(!gears.contains(item)) {
			gears.add(item);
			// Have to do it this dumb way because Floats aren't mutable
			// Maybe I should stop using hashmaps for stuff like this
			for(Effect e : item.getEffects()) {
				if(stats.containsKey(e.getStat())){
					float originalValue = stats.get(e.getStat());
					stats.remove(e.getStat());
					stats.put(e.getStat(), originalValue + e.getBoost());
				} else {
					stats.put(e.getStat(), e.getBoost());
				}
				if(e.getStat().equals("speed")) { // TODO magic
					PlayerPhysics physics = ((PlayerPhysics) components.get(ComponentType.PHYSICS));
					physics.setSpeed(physics.getSpeed()+e.getBoost());
				}
			}
			System.out.println("equipped " + item.getName());
		}
	}
	
	public void unequip(Item item) {
		gears.remove(item);
		for(Effect e : item.getEffects()) {
			float originalValue = stats.get(e.getStat());
			stats.remove(e.getStat());
			stats.put(e.getStat(), originalValue - e.getBoost());
		}
		System.out.println("unequipped " + item.getName());
	}
}
