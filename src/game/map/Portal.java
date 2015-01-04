package game.map;

import org.newdawn.slick.geom.Rectangle;

public class Portal {
	private Rectangle rect;
	private Map target;
	private float posX, posY;
	
	public Portal(Rectangle rect, Map target, float posX, float posY) {
		this.setRect(rect);
		this.setTarget(target);
		this.setX(posX);
		this.setY(posY);
	}

	public Map getTarget() {
		return target;
	}

	public void setTarget(Map target) {
		this.target = target;
	}

	public Rectangle getRect /*m8*/ () {
		return rect;
	}

	public void setRect(Rectangle rect) {
		this.rect = rect;
	}

	public float getY() {
		return posY;
	}

	public void setY(float posY) {
		this.posY = posY;
	}

	public float getX() {
		return posX;
	}

	public void setX(float posX) {
		this.posX = posX;
	}
}
