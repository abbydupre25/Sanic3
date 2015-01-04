package game.mob;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.component.RenderComponent;

public class MobRender extends RenderComponent {
	private Animation sprite, up, down, left, right, idleUp, idleDown,
			idleLeft, idleRight, lastSprite;

	public MobRender() {
		Image[] movementUp = null, movementDown = null, movementLeft = null, 
				movementRight = null, movementUpIdle = null, movementDownIdle = null,
				movementLeftIdle = null, movementRightIdle = null;

		try {
			movementUp = new Image[] { new Image("res/bk1.png"),
					new Image("res/bk2.png") };
			movementDown = new Image[] { new Image("res/frt1.png"),
					new Image("res/frt2.png") };
			movementLeft = new Image[] { new Image("res/lft1.png"),
					new Image("res/lft2.png") };
			movementRight = new Image[] { new Image("res/lft1.png").getFlippedCopy(true, false),
					new Image("res/lft2.png").getFlippedCopy(true, false) };
			movementUpIdle = new Image[] { new Image("res/bk1.png") };
			movementDownIdle = new Image[] { new Image("res/frt1.png") };
			movementLeftIdle = new Image[] { new Image("res/lft1.png") };
			movementRightIdle = new Image[] { new Image("res/lft1.png").getFlippedCopy(true, false) };
		} catch (SlickException e) {
			e.printStackTrace();
		}
		int[] duration = { 300, 300 };

		up = new Animation(movementUp, duration, false);
		down = new Animation(movementDown, duration, false);
		left = new Animation(movementLeft, duration, false);
		right = new Animation(movementRight, duration, false);
		idleUp = new Animation(movementUpIdle, duration[0], false);
		idleDown = new Animation(movementDownIdle, duration[0], false);
		idleLeft = new Animation(movementLeftIdle, duration[0], false);
		idleRight = new Animation(movementRightIdle, duration[0], false);
		lastSprite = idleDown;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		sprite = ((Mob) gameObject).getSprite();
		switch (gameObject.getMoveDir()) {
		case MOVE_UP:
			lastSprite = idleUp;
			sprite = up;
			break;
		case MOVE_DOWN:
			lastSprite = idleDown;
			sprite = down;
			break;
		case MOVE_LEFT:
			lastSprite = idleLeft;
			sprite = left;
			break;
		case MOVE_RIGHT:
			lastSprite = idleRight;
			sprite = right;
			break;
		case MOVE_NULL:
			sprite = lastSprite;
			break;
		default:
			sprite = lastSprite;
			break;
		}
		((Mob) gameObject).setSprite(sprite);
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub

	}

	public Animation getInitialSprite() {
		return down;
	}
}
