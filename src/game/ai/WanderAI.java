package game.ai;

import java.util.Random;

import game.GameObject;
import game.Defines.MoveDir;
import game.util.Vector2D;

public class WanderAI extends AI {	
	private GameObject gameObject;
	private double speed;
	private double waitTime, moveTime, targetWaitTime, targetMoveTime;
	private int delta;

	private MoveDir currentMove;
	private double lastMoveChange;

	Random random = new Random();
	
	public WanderAI(GameObject gameObject, double speed, double waitTime, double moveTime) {
		this.gameObject = gameObject;
		this.speed = speed;
		this.waitTime = waitTime;
		this.moveTime = moveTime;
		targetWaitTime = waitTime;
		targetMoveTime = moveTime;
		lastMoveChange = System.currentTimeMillis();
		randomMove();
	}
	
	private void randomMove() {
		switch (random.nextInt(4)) {
		case 0:
			currentMove = MoveDir.MOVE_LEFT;
			break;
		case 1:
			currentMove = MoveDir.MOVE_RIGHT;
			break;
		case 2:
			currentMove = MoveDir.MOVE_UP;
			break;
		case 3:
			currentMove = MoveDir.MOVE_DOWN;
			break;
		}
		gameObject.setMoveDir(currentMove);
	}
	
	private void setWait() {
		currentMove = MoveDir.MOVE_NULL;
		gameObject.setMoveDir(currentMove);
		
		// Randomize times a bit
		waitTime = targetWaitTime + random.nextFloat()*targetWaitTime - targetWaitTime/2;
		moveTime = targetMoveTime + random.nextFloat()*targetMoveTime - targetMoveTime/2;
	}
	
	private void edgeCheck() {
		Vector2D pos = gameObject.getPos();
		switch(gameObject.getMoveDir()){
		case MOVE_UP:
			blockedCheck(pos.x, pos.y-delta*speed);
			break;
		case MOVE_DOWN:
			blockedCheck(pos.x, pos.y+delta*speed);
			break;
		case MOVE_LEFT:
			blockedCheck(pos.x - delta*speed, pos.y);
			break;
		case MOVE_RIGHT:
			blockedCheck(pos.x + delta*speed, pos.y);
			break;
		case MOVE_NULL:
			break;
		default:
			break;
		}
	}
	
	/** Seems redundant */
	private void blockedCheck(double x, double y) {
		// AI shouldn't run through portals
		// TODO this failed once
		if(gameObject.isBlocked(x, y) || gameObject.isBeingPortaled(x, y)){
			blocked();
		} 
	}
	
	private void blocked() {
		randomMove();
	}
	
	@Override
	public void calcMove(int delta) {
		double currentTime = System.currentTimeMillis();
		this.delta = delta;
		if (currentMove == MoveDir.MOVE_NULL) {
			if (currentTime - lastMoveChange >= waitTime) {
				lastMoveChange = currentTime;
				randomMove();
			}
		} else {
			edgeCheck();
			if (currentTime - lastMoveChange >= moveTime) {
				lastMoveChange = currentTime;
				setWait();
			}
		}
	}
}
