package game.gui;

import game.Defines;
import game.Effect;
import game.Inventory;
import game.Inventory.GearSlot;
import game.item.Item;

import java.util.HashMap;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

public class InventoryPanel {
	private TrueTypeFont font;
	private Inventory inv;
	private ItemsPanel itemsPanel;
	private TextPanel textPanel;
	private EquipmentPanel equipPanel;
	private static final int IMAGE_SPACE = 5;
	private StatsPanel statsPanel;
	private InfoPanel infoPanel;

	public InventoryPanel(int x, int y, int width, int height, Inventory inv,
			TrueTypeFont font) throws SlickException {
		this.inv = inv;
		this.font = font;
		itemsPanel = new ItemsPanel(x, y, width - 150, height - 150);
		textPanel = new DescPanel(x, y + height - 150, width - 150, 100);
		equipPanel = new EquipmentPanel(x + width - 150, y, 150, height - 150);
		statsPanel = new StatsPanel(x + width - 150, y + height - 150, 150, 100);
		infoPanel = new InfoPanel(x, y + height - 50, width, 50, new Image(
				"res/rang.png"));
		shiftSelected(0, 0); // Helps initialize description
	}

	/** Renders and handles user input for items in the inventory */
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
			if (active) {
				g.drawRoundRect(selectedX * (Defines.SIZE + IMAGE_SPACE) + x,
						selectedY * (Defines.SIZE + IMAGE_SPACE) + y,
						Defines.SIZE + IMAGE_SPACE, Defines.SIZE + IMAGE_SPACE,
						4);
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

		public Item getSelectedItem() {
			int id = vectorToId(selectedX, selectedY);
			if (id < inv.getItems().size()) {
				return inv.getItems().get(id);
			} else {
				return null;
			}
		}

		public void equip() {
			if (active) {
				inv.equip(vectorToId(selectedX, selectedY));
			}
		}

		public void drop() {
			inv.drop(vectorToId(selectedX, selectedY));
		}

		/**
		 * Detects if there is a tile on the current selected position plus the
		 * parameters
		 */
		public boolean isOnBorder(int x, int y) {
			if (selectedX + x >= tileWidth) {
				return true;
			} else if (vectorToId(selectedX + x, selectedY) >= inv.getItems()
					.size()) {
				return true;
			}
			return false;
		}

		@Override
		public void setActive(boolean active) {
			this.active = active;
			if (active == true) {
				if (inv.getItems().isEmpty()) {
					textPanel.setText("");
				} else if (vectorToId(selectedX, selectedY) >= inv.getItems()
						.size()) {
					textPanel.setText(inv.getItems()
							.get(inv.getItems().size() - 1).getDescription());
				} else {
					Item item = inv.getItems().get(
							vectorToId(selectedX, selectedY));
					textPanel.setText(item.getDescription());
				}
			}
		}
	}

	/** Displays item effects on player stats */
	private class StatsPanel extends TextPanel {
		public StatsPanel(int x, int y, int width, int height) {
			super(x, y, width, height, font);
		}

		@Override
		public void render(Graphics g) {
			g.drawRoundRect(x, y, width, height, 4);
			Item item;
			if (itemsPanel.isActive()) {
				item = itemsPanel.getSelectedItem();
			} else {
				item = equipPanel.getSelectedItem();
			}
			if (item != null) {
				for (int i = 0; i < item.getEffects().size(); i++) {
					Effect e = item.getEffects().get(i);
					String sign = e.getBoost() > 0 ? "+" : "-";
					g.drawString(sign + e.getBoost() + " " + e.getStat(), x, y
							+ i * font.getHeight());
				}
			}
		}
	}

	/** Renders and handles user input for gear in the equipment area */
	private class EquipmentPanel extends SelectablePanel {
		public EquipmentPanel(int x, int y, int width, int height) {
			super(x, y, width, height, IMAGE_SPACE);
			active = false;
		}

		public Item getSelectedItem() {
			return inv.getGearSlots()
					.get(Defines.gearTypes[vectorToId(selectedX, selectedY)])
					.getGear();
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
			if (active) {
				g.drawRoundRect(selectedX * (Defines.SIZE + IMAGE_SPACE) + x,
						selectedY * (Defines.SIZE + IMAGE_SPACE) + y,
						Defines.SIZE + IMAGE_SPACE, Defines.SIZE + IMAGE_SPACE,
						4);
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
				if (gear != null) {
					textPanel.setText(gear.getDescription());
				}
			}
		}

		public void unequip() {
			if (active) {
				String gearType = Defines.gearTypes[vectorToId(selectedX,
						selectedY)];
				if (inv.getGearSlots().get(gearType).isOccupied()) {
					inv.unequip(gearType);
				}
			}
		}

		@Override
		public void setActive(boolean active) {
			this.active = active;
			Item gear = inv.getGearSlots()
					.get(Defines.gearTypes[vectorToId(selectedX, selectedY)])
					.getGear();
			if (gear != null && active == true) {
				textPanel.setText(gear.getDescription());
			}
		}
	}

	/** Displays item name and description */
	private class DescPanel extends TextPanel {
		public DescPanel(int x, int y, int width, int height) {
			super(x, y, width, height, font);
		}

		@Override
		public void render(Graphics g) {
			Item item;
			if (itemsPanel.isActive()) {
				item = itemsPanel.getSelectedItem();
			} else {
				item = equipPanel.getSelectedItem();
			}
			if (item != null) {
				g.drawString(item.getName(), x, y);
				for (int i = 0; i < lines.size() && i < maxLines; i++) {
					String s = lines.get(i);
					g.drawString(s, x, y + (i + 1) * font.getHeight());
				}
			}
			g.drawRoundRect(x, y, width, height, 4);
		}

	}

	/** Displays controls and currency */
	private class InfoPanel extends TextPanel {
		private Image rangImage;

		public InfoPanel(int x, int y, int width, int height, Image rangImage) {
			super(x, y, width, height, font);
			this.rangImage = rangImage;
		}

		@Override
		public void render(Graphics g) {
			int currentX = x+10;
			g.drawImage(rangImage, currentX, y + (height - Defines.SIZE) / 2);
			currentX += Defines.SIZE + IMAGE_SPACE;
			String rangString = String.valueOf(inv.getRangs());
			g.drawString(rangString, currentX, y
					+ (height - font.getHeight()) / 2);
			currentX += font.getWidth(rangString) + 20;
			if (itemsPanel.isActive()) {
				Item item = itemsPanel.getSelectedItem();
				if(item != null){
					if(item.isGear()){
						g.drawString("[E] Equip", currentX, y
								+ (height - font.getHeight()) / 2);
						currentX += font.getWidth("[E] Equip") + 10;
					}
					g.drawString("[Q] Drop", currentX, y
							+ (height - font.getHeight()) / 2);
				}
			} else {
				Item item = equipPanel.getSelectedItem();
				if(item != null){
					g.drawString("[E] Unequip", currentX, y
							+ (height - font.getHeight()) / 2);
				}
			}

			g.drawRoundRect(x, y, width, height, 4);
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
		statsPanel.render(g);
		infoPanel.render(g);
	}

	public void update(GameContainer gc, StateBasedGame sbg, int delta) {
		Input input = gc.getInput();
		if (input.isKeyPressed(Keyboard.KEY_TAB)) {
			sbg.enterState(Defines.ID_EXPLORE);
			gc.getInput().clearKeyPressedRecord();
		} else if (input.isKeyPressed(Keyboard.KEY_Q)) {
			if (inv.getItems().size() > 0 && itemsPanel.isActive()) {
				itemsPanel.drop();
			}
		} else if (input.isKeyPressed(Keyboard.KEY_E)) {
			if (itemsPanel.isActive()) {
				itemsPanel.equip();
			} else {
				equipPanel.unequip();
			}
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
