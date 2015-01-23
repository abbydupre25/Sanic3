package game.player;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import game.Defines;
import game.component.RenderComponent;

public class PlayerRender extends RenderComponent {
	private Animation sprite, up, down, left, right, idleUp, idleDown,
			idleLeft, idleRight, lastSprite,
			dUp, dDown, dLeft, dRight, dIdle;

	public PlayerRender() {
		Image[] movementUp = null, movementDown = null, movementLeft = null, 
				movementRight = null, movementUpIdle = null, movementDownIdle = null,
				movementLeftIdle = null, movementRightIdle = null,
				dashUp = null, dashDown = null, dashLeft = null, dashRight = null, dashIdle = null;

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
			dashUp = new Image[] {new Image("res/sprites/dash/dash1.png"),
					new Image("res/sprites/dash/dash2.png"),
					new Image("res/sprites/dash/dash3.png"),
					new Image("res/sprites/dash/dash4.png")};
			dashDown = new Image[] {new Image("res/sprites/dash/dash1.png").getFlippedCopy(false, true),
					new Image("res/sprites/dash/dash2.png").getFlippedCopy(false, true),
					new Image("res/sprites/dash/dash3.png").getFlippedCopy(false, true),
					new Image("res/sprites/dash/dash4.png").getFlippedCopy(false, true)};
			dashLeft = new Image[] {new Image("res/sprites/dash/dash1.png"),
					new Image("res/sprites/dash/dash2.png"),
					new Image("res/sprites/dash/dash3.png"),
					new Image("res/sprites/dash/dash4.png")};
			for(int i=0; i<dashLeft.length; i++){
				dashLeft[i].rotate(270);
			}
			dashRight = new Image[] {new Image("res/sprites/dash/dash1.png"),
					new Image("res/sprites/dash/dash2.png"),
					new Image("res/sprites/dash/dash3.png"),
					new Image("res/sprites/dash/dash4.png")};
			for(int i=0; i<dashRight.length; i++){
				dashRight[i].rotate(90);
			}
			dashIdle = new Image[] {new Image("res/sprites/dash/dashIdle.png")};
		} catch (SlickException e) {
			e.printStackTrace();
		}
		int[] duration = { 300, 300 };
		int[] dashDuration = { 50, 50, 50, 50 };

		up = new Animation(movementUp, duration, false);
		down = new Animation(movementDown, duration, false);
		left = new Animation(movementLeft, duration, false);
		right = new Animation(movementRight, duration, false);
		idleUp = new Animation(movementUpIdle, duration[0], false);
		idleDown = new Animation(movementDownIdle, duration[0], false);
		idleLeft = new Animation(movementLeftIdle, duration[0], false);
		idleRight = new Animation(movementRightIdle, duration[0], false);
		dUp = new Animation(dashUp, dashDuration, false);
		dDown = new Animation(dashDown, dashDuration, false);
		dLeft = new Animation(dashLeft, dashDuration, false);
		dRight = new Animation(dashRight, dashDuration, false);
		dIdle = new Animation(dashIdle, duration[0], false);
		lastSprite = idleDown;
	}

	@Override
	public void update(GameContainer gc, int delta) {
		sprite = ((Player) gameObject).getSprite();
		if(gameObject.getAction().equals(Defines.Action.ACTION_RUN)){
			switch (gameObject.getMoveDir()) {
			case MOVE_UP:
				lastSprite = dIdle;
				sprite = dUp;
				break;
			case MOVE_DOWN:
				lastSprite = dIdle;
				sprite = dDown;
				break;
			case MOVE_LEFT:
				lastSprite = dIdle;
				sprite = dLeft;
				break;
			case MOVE_RIGHT:
				lastSprite = dIdle;
				sprite = dRight;
				break;
			case MOVE_NULL:
				sprite = lastSprite;
				break;
			default:
				sprite = lastSprite;
				break;
			}
		} else {
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
		}
		((Player) gameObject).setSprite(sprite);
	}

	@Override
	protected void initModifierDependents() {
		// TODO Auto-generated method stub

	}

	public Animation getInitialSprite() {
		return down;
	}
}
