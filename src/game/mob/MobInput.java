package game.mob;

import game.ai.AI;
import game.ai.WanderAI;
import game.component.InputComponent;

import org.newdawn.slick.GameContainer;

public class MobInput extends InputComponent {
	private AI input;
	private float speed;
	
	public MobInput(float speed) {
		this.speed = speed;
	}
	
	@Override
	protected void initModifierDependents() {
		input = new WanderAI(gameObject, speed, 1000, 1000);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		input.calcMove(delta);
	}
}
