package game.item;

import game.GameObject;
import game.component.InteractComponent;
import game.player.Player;

import org.newdawn.slick.GameContainer;

public class ItemInteract extends InteractComponent {

	@Override
	public void interact(GameObject player) {
		System.out.println(player.toString() + 
				" interacted with " + gameObject.toString());
		((Player) player).getInv().add((Item) gameObject);
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}

	@Override
	protected void initModifierDependents() {
		
	}
	
	public void actionOnDrop() {
		// Useful to override
	}

}
