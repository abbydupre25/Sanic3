package game.mob;

import game.GameObject;
import game.component.InteractComponent;

import org.newdawn.slick.GameContainer;

public class MobInteract extends InteractComponent {

	@Override
	public void interact(GameObject player) {
		System.out.println(player.toString() + 
				" interacted with " + gameObject.toString());
	}

	@Override
	public void update(GameContainer gc, int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

}
