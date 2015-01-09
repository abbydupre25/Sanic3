package game.item;

import game.Defines.ComponentType;
import game.Effect;
import game.GameObject;
import game.component.Component;
import game.util.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.StateBasedGame;

public class Item extends GameObject {
	private Image image;
	private String name;
	private ArrayList<Effect> effects;
	private String desc; // description
	private String gearType;

	public Item(Vector2D pos, StateBasedGame sbg,
			HashMap<ComponentType, Component> components) {
		this(pos, sbg, new ArrayList<Effect>(), components);
	}
	
	public Item(Vector2D pos, StateBasedGame sbg, ArrayList<Effect> effects,
			HashMap<ComponentType, Component> components) {
		super(pos, sbg, components);
		for (Component c: components.values()) {
			c.setModifier(this);
		}
		this.setImage(((ItemRender) components.get(ComponentType.RENDER)).getImage());
		setName("unnamed");
		this.effects = effects;
		this.desc = "";
		this.setGearType("");
	}

	@Override
	public void update(GameContainer gc, int delta) {
		for (Component c: components.values()) {
			c.update(gc, delta);
		}
	}

	@Override
	public void render() {
		image.draw((int)pos.x, (int)pos.y);
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<Effect> getEffects() {
		return effects;
	}
	
	public String getDescription() {
		return desc;
	}
	
	public void setDescription(String desc) {
		this.desc = desc;
	}

	public String getGearType() {
		return gearType;
	}

	public void setGearType(String gearType) {
		this.gearType = gearType;
	}
	
	public boolean isGear() {
		return !gearType.equals("");
	}
}
