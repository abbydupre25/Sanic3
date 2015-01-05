package game.gui;

import game.Defines;
import game.Inventory;
import game.item.Item;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

public class InventoryPanel {
	private int width;
	private int height;
	private int x;
	private int y;
	private TrueTypeFont font;
	private int maxLines;
	private Inventory inv;
	private int selected = 0;
	
	public InventoryPanel(int x, int y, int width, int height, Inventory inv, TrueTypeFont font) {
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		this.inv = inv;
		this.font = font;
		this.maxLines = height / font.getHeight();
	}
	
	public void render(Graphics g) {
		g.setFont(font);
		g.drawString("inventory", 50, 50);
		ArrayList<String> lines = new ArrayList<String>();
		for (Item i : inv.getItems()) {
			lines.add(i.getName());
		}
		for(int i=0; i<lines.size(); i++){
			g.drawString(lines.get(i), 50, y+(i+1)*font.getHeight());
		}
		g.drawRoundRect(x, y+(selected+1)*font.getHeight(), width, font.getHeight(), 5);
	}
	
	private void shiftSelected(int increment) {
		int requestedID = selected + increment;
		if(requestedID>=0 && requestedID<inv.getItems().size()){
			selected = requestedID;
		}
	}
	
	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input input = gc.getInput();
		if(input.isKeyPressed(Keyboard.KEY_TAB)){
			sbg.enterState(Defines.ID_EXPLORE);
			gc.getInput().clearKeyPressedRecord();
		} else if (input.isKeyPressed(Keyboard.KEY_Q)){
			if(inv.getItems().size()>0){
				inv.drop(0);
				shiftSelected(-1);
			}
		} else if (input.isKeyPressed(Keyboard.KEY_E)){
			inv.equip(selected);
		} else if (input.isKeyPressed(Keyboard.KEY_U)){
			inv.unequip(selected);
		} else if (input.isKeyPressed(Keyboard.KEY_W)){
			shiftSelected(-1);
		} else if (input.isKeyPressed(Keyboard.KEY_S)){
			shiftSelected(1);
		}
	}
}
