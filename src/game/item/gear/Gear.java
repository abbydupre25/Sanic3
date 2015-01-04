package game.item.gear;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.state.StateBasedGame;

import game.Defines.ComponentType;
import game.Effect;
import game.component.Component;
import game.item.Item;
import game.util.Vector2D;

public class Gear extends Item {

	private ArrayList<Effect> effects;

	public Gear(Vector2D pos, StateBasedGame sbg, ArrayList<Effect> effects,
			HashMap<ComponentType, Component> components) {
		super(pos, sbg, components);
		this.effects = effects;
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}

}
