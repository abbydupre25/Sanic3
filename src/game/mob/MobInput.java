package game.mob;

import game.Defines.MoveDir;
import game.ai.AI;
import game.ai.WanderAI;
import game.component.InputComponent;

import org.newdawn.slick.GameContainer;

public class MobInput extends InputComponent {
	private AI input;
	private float speed;
	private boolean locked;
	
	public MobInput(float speed) {
		this.speed = speed;
		locked = false;
	}
	
	@Override
	protected void initModifierDependents() {
		input = new WanderAI(gameObject, speed, 1000, 1000);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if(!locked){
			input.calcMove(delta);
		} else {
			gameObject.setMoveDir(MoveDir.MOVE_NULL);;
		}
	}

	public void setLock(boolean state) {
		locked = state;
	}
}
