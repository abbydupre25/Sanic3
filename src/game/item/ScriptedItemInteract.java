package game.item;

import game.GameObject;
import game.gui.GUI;
import game.player.Player;

import org.newdawn.slick.GameContainer;

public class ScriptedItemInteract extends ItemInteract {
	private GUI gui;
	private String scriptPath;
	
	public ScriptedItemInteract(GUI gui, String scriptPath) {
		this.gui = gui;
		this.scriptPath = scriptPath;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		
	}

	@Override
	protected void initModifierDependents() {
		
	}

	@Override
	public void interact(GameObject player) {
		gui.startScript(scriptPath);
		((Player) player).getInv().add((Item) gameObject);
	}
	
	@Override
	public void actionOnDrop() {
		
	}

}
