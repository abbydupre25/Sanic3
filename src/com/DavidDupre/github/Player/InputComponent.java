package com.DavidDupre.github.Player;

import com.DavidDupre.github.Component;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.DavidDupre.github.Defines;

public class InputComponent extends Component {
	private Player player;
	private int[] move;
	private int[] attack;

	private final int LEFT = 0;
	private final int RIGHT = 1;
	private final int UP = 2;
	private final int DOWN = 3;

	private final int SPIN = 0;
	private final int SHOOT = 1;

	public InputComponent(Player player, int[] move, int[] attack) {
		this.player = player;
		this.move = move;
		this.attack = attack;
	}

	@Override
	public void update() {
		if (Keyboard.isKeyDown(move[LEFT]) || Keyboard.isKeyDown(move[RIGHT])
				|| Keyboard.isKeyDown(move[UP])
				|| Keyboard.isKeyDown(move[DOWN])) {
			player.wobble++;
		}
		if (Keyboard.isKeyDown(move[UP])) {
			player.moveForward();
		} else if (Keyboard.isKeyDown(move[DOWN])) {
			player.moveBackward();
		}
		if (Keyboard.isKeyDown(move[LEFT])) {
			player.moveLeft();
		} else if (Keyboard.isKeyDown(move[RIGHT])) {
			player.moveRight();
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKey() == attack[SPIN]) {
				player.azimuth = Math.toDegrees(Math.atan2(Defines.HEIGHT
						- Mouse.getY() - player.position.y, Mouse.getX()
						- player.position.x));
				player.roll();
				player.azimuth = 0;
				break;
			}
			break;
		}
		while (Mouse.next()) {
			if (!Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() == attack[SHOOT]) {
					player.fire();
				}
			}
			break;
		}
	}
}
