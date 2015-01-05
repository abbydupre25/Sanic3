package game.player;

import org.newdawn.slick.GameContainer;

import game.component.PhysicsComponent;
import game.util.Vector2D;

public class PlayerPhysics extends PhysicsComponent {
	private Vector2D pos;
	private float speed;
	
	public PlayerPhysics(float speed) {
		this.speed = speed;
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		pos = gameObject.getPos();
		switch(gameObject.getMoveDir()){
		case MOVE_UP:
			if(!gameObject.isBlocked(pos.x, pos.y-delta*speed)){
				pos.y -= delta * speed;
			}
			break;
		case MOVE_DOWN:
			if(!gameObject.isBlocked(pos.x, pos.y+delta*speed)){
				pos.y += delta * speed;
			}
			break;
		case MOVE_LEFT:
			if(!gameObject.isBlocked(pos.x - delta*speed, pos.y)){
				pos.x -= delta * speed;
			}
			break;
		case MOVE_RIGHT:
			if(!gameObject.isBlocked(pos.x + delta*speed, pos.y)){
				pos.x += delta * speed;
			}
			break;
		case MOVE_NULL:
			break;
		default:
			break;
		}
	}
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}
}
