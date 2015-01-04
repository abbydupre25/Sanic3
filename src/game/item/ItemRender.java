package game.item;

import game.component.RenderComponent;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class ItemRender extends RenderComponent {
	private Image image;
	
	public ItemRender(String imagePath) throws SlickException {
		image = new Image(imagePath);
	}
	
	@Override
	public void update(GameContainer gc, int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub
		
	}

	public Image getImage() {
		return image;
	}
}
