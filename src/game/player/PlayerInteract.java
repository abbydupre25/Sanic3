package game.player;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;

import game.Defines;
import game.Defines.ComponentType;
import game.GameObject;
import game.component.InteractComponent;
import game.map.Portal;
import game.util.Vector2D;

// TODO add 'interactable' tag to game objects so we can shorten the list and optimize this part

/** Lots of shitty code here */
public class PlayerInteract extends InteractComponent {	
	public PlayerInteract() {

	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		ArrayList<GameObject> gameObjects = gameObject.getMap().getGameObjects();
		switch(gameObject.getAction()){
		case ACTION_INTERACT:
			ArrayList<GameObject> interactable = new ArrayList<GameObject>();
			for(GameObject go : gameObjects){
				if(go != gameObject && go.isOnMap()){
					interactable.add(go);
				}
			}
			
			if(interactable.size()>0){
				float interactDist = Defines.SIZE*2;
				GameObject closestObject = interactable.get(0);
				double minDist = gameObject.getPos().clone().subtract(closestObject.getPos()).magnitude();
				for(int i=0; i<interactable.size(); i++){
					double dist = gameObject.getPos().clone().subtract(interactable.get(i).getPos()).magnitude();
					if(dist < minDist){
						closestObject = interactable.get(i);
						minDist = dist;
					}
				}
				if(minDist<=interactDist){
					// TODO this forces everything to have an interact component
					((InteractComponent) closestObject.getComponent(ComponentType.INTERACT)).interact(gameObject);
				}
			}
			break;
		case ACTION_INVENTORY:
			gameObject.getGame().enterState(Defines.ID_INV);
			gc.getInput().clearKeyPressedRecord();
			break;
		case ACTION_PAUSE:
			gameObject.getGame().enterState(Defines.ID_PAUSED);
			gc.getInput().clearKeyPressedRecord();
		default:
			break;
		}
		
		// TODO make portal its own gameObject and use that interact method
		Portal portal = gameObject.getPortal(gameObject.getPos().x, gameObject.getPos().y);
		if(portal != null){
			if(portal.getTarget() == null){
				System.out.println("NULL MAP");
			}
			((Player) gameObject).setMap(portal.getTarget());
			gameObject.setPos(new Vector2D(portal.getX()*Defines.SIZE, portal.getY()*Defines.SIZE));
		}
	}

	@Override
	protected void initModifierDependents() {
		
	}

	@Override
	public void interact(GameObject gameObject) {
		
	}
}
