package game.gui;

import game.Defines;
import game.Inventory;
import game.Inventory.GearSlot;
import game.item.Item;

import java.util.ArrayList;
import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

public class InventoryPanel {
	private TrueTypeFont font;
	private Inventory inv;
	private ItemsPanel itemsPanel;
	private TextPanel textPanel;
	private EquipmentPanel equipPanel;
	private static final int IMAGE_SPACE = 2;

	public InventoryPanel(int x, int y, int width, int height, Inventory inv,
			TrueTypeFont font) {
		this.inv = inv;
		this.font = font;
		itemsPanel = new ItemsPanel(x, y, width - 150, height - 100);
		textPanel = new TextPanel(x, y + height - 100, width, 100);
		equipPanel = new EquipmentPanel(x + width - 150, y, 150, height - 100);
	}

	private class ItemsPanel extends SelectablePanel {
		public ItemsPanel(int x, int y, int width, int height) {
			super(x, y, width, height, IMAGE_SPACE);
		}

		@Override
		public void render(Graphics g) {
			int j = 0;
			int k = 0;
			for (int i = 0; i < inv.getItems().size() && k < tileHeight; i++) {
				Item item = inv.getItems().get(i);
				g.drawImage(item.getImage(), j * (Defines.SIZE + IMAGE_SPACE)
						+ x, k * (Defines.SIZE + IMAGE_SPACE) + y);
				j++;
				if (j >= tileWidth) {
					k++;
					j = 0;
				}
			}
			if(active){
				g.drawRoundRect(selectedX * (Defines.SIZE + IMAGE_SPACE) + x,
						selectedY * (Defines.SIZE + IMAGE_SPACE) + y, Defines.SIZE
								+ IMAGE_SPACE, Defines.SIZE + IMAGE_SPACE, 4);
			}
			g.drawRoundRect(x, y, width, height, 4);
		}

		@Override
		public void shiftSelected(int x, int y) {
			int requestedX = selectedX + x;
			int requestedY = selectedY + y;
			int requestedID = vectorToId(requestedX, requestedY);
			if (requestedID < inv.getItems().size() && requestedID >= 0) {
				if (requestedX >= 0 && requestedX < tileWidth) {
					selectedX = requestedX;
					textPanel.setText(inv.getItems().get(requestedID)
							.getDescription());
				}
				if (requestedY >= 0 && requestedY < tileHeight) {
					selectedY = requestedY;
					textPanel.setText(inv.getItems().get(requestedID)
							.getDescription());
				}
			}
		}

		public void equip() {
			inv.equip(vectorToId(selectedX, selectedY));
		}

		public void drop() {
			inv.drop(vectorToId(selectedX, selectedY));
		}
		
		/** Detects if there is a tile on the current selected position plus the parameters */
		public boolean isOnBorder(int x, int y) {
			if(selectedX + x > tileWidth) {
				return true;
			} else if (vectorToId(selectedX+x, selectedY) >= inv.getItems().size()) {
				return true;
			}
			return false;
		}
		
		@Override
		public void setActive(boolean active) {
			this.active = active;
			if(active==true){
				Item item = inv.getItems().get(vectorToId(selectedX, selectedY));
				if(item != null){
					textPanel.setText(item.getDescription());
				}
			}
		}
	}
	

	private class TextPanel {
		private int x;
		private int y;
		private int width;
		private int height;
		private ArrayList<String> lines;
		private int maxLines;

		public TextPanel(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			lines = new ArrayList<String>();
			maxLines = height / font.getHeight();
		}

		public void render(Graphics g) {
			for (int i = 0; i < lines.size() && i < maxLines; i++) {
				String s = lines.get(i);
				g.drawString(s, x, y + i * font.getHeight());
			}
			g.drawRoundRect(x, y, width, height, 4);
		}

		public void setText(String text) {
			lines = GUI.wrap(text, font, width);
		}
	}

	private class EquipmentPanel extends SelectablePanel {
		public EquipmentPanel(int x, int y, int width, int height) {
			super(x, y, width, height, IMAGE_SPACE);
			active = false;
		}

		@Override
		public void render(Graphics g) {
			HashMap<String, GearSlot> gearSlots = inv.getGearSlots();
			int j = 0;
			int k = 0;
			// Draw each gear image
			for (GearSlot gs : gearSlots.values()) {
				if (gs.isOccupied()) {
					for (int i = 0; i < Defines.gearTypes.length; i++) {
						if (gs.getGear().getGearType()
								.equals(Defines.gearTypes[i])) {
							g.drawImage(gs.getGear().getImage(), j
									* (Defines.SIZE + IMAGE_SPACE) + x, k
									* (Defines.SIZE + IMAGE_SPACE) + y);
						}
						j++;
						if (j >= tileWidth) {
							k++;
							j = 0;
						}
					}
					j = 0;
					k = 0;
				}
			}
			if(active){
				g.drawRoundRect(selectedX * (Defines.SIZE + IMAGE_SPACE) + x,
						selectedY * (Defines.SIZE + IMAGE_SPACE) + y, Defines.SIZE
								+ IMAGE_SPACE, Defines.SIZE + IMAGE_SPACE, 4);
			}
			g.drawRoundRect(x, y, width, height, 4);
		}

		@Override
		public void shiftSelected(int x, int y) {
			int requestedX = selectedX + x;
			int requestedY = selectedY + y;
			int requestedID = vectorToId(requestedX, requestedY);
			if (requestedID < inv.getGearSlots().size() && requestedID >= 0) {
				if (requestedX >= 0 && requestedX < tileWidth) {
					selectedX = requestedX;
				}
				if (requestedY >= 0 && requestedY < tileHeight) {
					selectedY = requestedY;
				}
				Item gear = inv.getGearSlots()
						.get(Defines.gearTypes[requestedID]).getGear();
				if(gear != null){
					textPanel.setText(gear.getDescription());
				}
			}
		}

		public void unequip() {
			if(active) {
				String gearType = Defines.gearTypes[vectorToId(selectedX, selectedY)];
				if(inv.getGearSlots().get(gearType).isOccupied()){
					inv.unequip(gearType);
				}
			}
		}

		@Override
		public void setActive(boolean active) {
			this.active = active;
			Item gear = inv.getGearSlots()
					.get(Defines.gearTypes[vectorToId(selectedX, selectedY)]).getGear();
			if(gear != null && active==true){
				textPanel.setText(gear.getDescription());
			}
		}
	}

	private void shiftSelected(int x, int y) {
		if (itemsPanel.active) {
			if (itemsPanel.isOnBorder(x, y)) {
				equipPanel.setActive(true);
				itemsPanel.setActive(false);
			} else {
				itemsPanel.shiftSelected(x, y);
			}
		} else {
			if (equipPanel.selectedX + x < 0) {
				equipPanel.setActive(false);
				itemsPanel.setActive(true);
			} else {
				equipPanel.shiftSelected(x, y);
			}
		}
	}

	public void render(Graphics g) {
		g.setFont(font);
		itemsPanel.render(g);
		textPanel.render(g);
		equipPanel.render(g);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input input = gc.getInput();
		if (input.isKeyPressed(Keyboard.KEY_TAB)) {
			sbg.enterState(Defines.ID_EXPLORE);
			gc.getInput().clearKeyPressedRecord();
		} else if (input.isKeyPressed(Keyboard.KEY_Q)) {
			if (inv.getItems().size() > 0) {
				itemsPanel.drop();
			}
		} else if (input.isKeyPressed(Keyboard.KEY_E)) {
			itemsPanel.equip();
		} else if (input.isKeyPressed(Keyboard.KEY_U)) {
			equipPanel.unequip();
		} else if (input.isKeyPressed(Keyboard.KEY_W)) {
			shiftSelected(0, -1);
		} else if (input.isKeyPressed(Keyboard.KEY_S)) {
			shiftSelected(0, 1);
		} else if (input.isKeyPressed(Keyboard.KEY_A)) {
			shiftSelected(-1, 0);
		} else if (input.isKeyPressed(Keyboard.KEY_D)) {
			shiftSelected(1, 0);
		}
	}
}
