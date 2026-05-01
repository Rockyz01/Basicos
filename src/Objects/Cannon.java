package Objects;

import Juegos.Juego;

public class Cannon extends GameObject {

	private int tileY;

	public Cannon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y / Juego.TILES_SIZE;
		initHitBox(40, 26);
		hitbox.x -= (int) (4 * Juego.SCALE);
		hitbox.y += (int) (6 * Juego.SCALE);
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

	public int getTileY() {
		return tileY;
	}

}

