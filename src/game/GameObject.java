package game;

import game.Defines.Action;
import game.Defines.ComponentType;
import game.Defines.MoveDir;
import game.component.Component;
import game.component.InteractComponent;
import game.map.Map;
import game.map.Portal;
import game.util.Vector2D;

import java.util.ArrayList;
import java.util.HashMap;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public abstract class GameObject {
	public HashMap<Defines.ComponentType, Component> components;
	private MoveDir currentMove;
	private Action currentAction;
	protected Vector2D pos;
	protected Map map;
	private ArrayList<GameObject> requests;
	private boolean onMap = true;
	private StateBasedGame sbg;

	public GameObject(Vector2D pos, StateBasedGame sbg,
			HashMap<Defines.ComponentType, Component> components) {
		this.pos = pos;
		this.components = components;
		this.sbg = sbg;
		requests = new ArrayList<GameObject>();
	}

	public abstract void update(GameContainer gc, int delta);

	public abstract void render();

	public void setMoveDir(MoveDir move) {
		this.currentMove = move;
	}

	public MoveDir getMoveDir() {
		return currentMove;
	}

	public void setAction(Action action) {
		this.currentAction = action;
	}

	public Action getAction() {
		return currentAction;
	}

	public void setPos(Vector2D pos) {
		this.pos = pos;
	}

	public Vector2D getPos() {
		return pos;
	}

	public Rectangle getBounds() {
		return new Rectangle((float) pos.x, (float) pos.y, Defines.SIZE,
				Defines.SIZE);
	}

	public boolean isBlocked(double x, double d) {
		return map.isInCollision(new Rectangle((float) x, (float) d,
				Defines.SIZE, Defines.SIZE));
	}
	
	public boolean isBeingPortaled(double x, double y) {
		return map.isBeingPortaled(new Rectangle((float) x, (float) y,
				Defines.SIZE, Defines.SIZE));
	}
	
	public Portal getPortal(double x, double d) {
		return map.getPortal(new Rectangle((float) x, (float) d, Defines.SIZE, Defines.SIZE));
	}

	public Component getComponent(ComponentType ct) {
		Component c = components.get(ct);
		// TODO complete this switch statement (if you're into that)
		switch (ct) {
		case INTERACT:
			c = (InteractComponent) c;
			break;
		}
		if (c != null) {
			return c;
		}
		return null;
	}

	public void setRequests(ArrayList<GameObject> requests) {
		this.requests = requests;
	}

	public ArrayList<GameObject> getRequests() {
		return requests;
	}
	
	public Map getMap() {
		return map;
	}
	
	public void setMap(Map map){
		this.map = map;
	}

	public StateBasedGame getGame() {
		return sbg;
	}

	public boolean isOnMap() {
		return onMap;
	}
	
	public void setOnMap(boolean state) {
		onMap = state;
	}
}
