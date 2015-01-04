package game.sharedComponents;

import game.GameObject;
import game.component.InteractComponent;
import game.gui.GUI;

import org.newdawn.slick.GameContainer;

public class ChattyInteract extends InteractComponent {
	private GUI gui;
	private String dialogPath;
	
	public ChattyInteract(GUI gui, String dialogPath) {
		this.gui = gui;
		this.dialogPath = dialogPath;
	}
	
	@Override
	public void interact(GameObject player) {
		gui.startDialog(dialogPath);
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
