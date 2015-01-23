package game.player;

import org.newdawn.slick.GameContainer;

import game.Defines;
import game.component.PhysicsComponent;
import game.util.Vector2D;

public class PlayerPhysics extends PhysicsComponent {
	private Vector2D pos;
	private float speed;
	private float runSpeed;
	
	public PlayerPhysics(float speed) {
		this.speed = speed;
		runSpeed = speed;
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		pos = gameObject.getPos();
		float speedu = speed;
		if(gameObject.getAction().equals(Defines.Action.ACTION_RUN)){
			speedu = runSpeed;
		}
		switch(gameObject.getMoveDir()){
		case MOVE_UP:
			if(!gameObject.isBlocked(pos.x, pos.y-delta*speedu)){
				pos.y -= delta * speedu;
			}
			break;
		case MOVE_DOWN:
			if(!gameObject.isBlocked(pos.x, pos.y+delta*speedu)){
				pos.y += delta * speedu;
			}
			break;
		case MOVE_LEFT:
			if(!gameObject.isBlocked(pos.x - delta*speedu, pos.y)){
				pos.x -= delta * speedu;
			}
			break;
		case MOVE_RIGHT:
			if(!gameObject.isBlocked(pos.x + delta*speedu, pos.y)){
				pos.x += delta * speedu;
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
	
	public void setRunSpeed(float speed) {
		this.runSpeed = speed;
	}
	
	public float getRunSpeed() {
		return runSpeed;
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}
}
