package game.mob;

import game.component.PhysicsComponent;
import game.util.Vector2D;

import org.newdawn.slick.GameContainer;

public class MobPhysics extends PhysicsComponent{
	private Vector2D pos;
	private float speed;	
	
	public MobPhysics(float speed){
		this.speed = speed;
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		// Blockage checking is redundant here because of the AI. Remove it for optimization
		pos = gameObject.getPos();
		Mob mob = (Mob) gameObject;
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

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

}
