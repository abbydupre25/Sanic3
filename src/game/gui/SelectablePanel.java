package game.gui;

import game.Defines;

import org.newdawn.slick.Graphics;

public abstract class SelectablePanel {
	public int selectedX = 0;
	protected int selectedY = 0;
	protected boolean active = true;
	public int tileWidth;
	protected int tileHeight;
	protected int width;
	protected int height;
	protected int x;
	protected int y;
	protected int space;

	public SelectablePanel(int x, int y, int width, int height, int space) {
		this.width = width;
		this.height = height;
		this.space = space;
		tileWidth = width / (Defines.SIZE + space);
		tileHeight = height / (Defines.SIZE + space);
		this.x = x;
		this.y = y;
	}

	public abstract void render(Graphics g);

	public abstract void shiftSelected(int x, int y);

	protected int vectorToId(int x, int y) {
		return x + y * tileWidth;
	}
	
	public abstract void setActive(boolean active);
	
	public boolean isActive() {
		return active;
	}
}

