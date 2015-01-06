package game.sharedComponents;

import game.GameObject;
import game.component.InteractComponent;
import game.gui.GUI;
import game.player.Player;

import org.newdawn.slick.GameContainer;

public class ChattyInteract extends InteractComponent {
	private GUI gui;
	private String dialogPath;
	private boolean isChatting;
	private GameObject player;
	
	public ChattyInteract(GUI gui, String dialogPath) {
		this.gui = gui;
		this.dialogPath = dialogPath;
		isChatting = false;
	}
	
	@Override
	public void interact(GameObject player) {
		if(!isChatting) {
			this.player = player;
			gui.startDialog(dialogPath);
			isChatting = true;
			gameObject.setMovementLock(true);
			player.setMovementLock(true);
		}
	}

	@Override
	public void update(GameContainer gc, int delta) {
		if(isChatting && !gui.isVisible()){
			isChatting = false;
			gameObject.setMovementLock(false);
			player.setMovementLock(false);
		}
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

}
