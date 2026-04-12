package Objects;

import static Utilz.Constantes.ObjectConstants.*;
import Juegos.Juego;

public class GameContainer extends GameObject {
	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == BOX) {
			initHitBox(25, 18);

			xDrawOffset = (int) (7 * Juego.SCALE);
			yDrawOffset = (int) (12 * Juego.SCALE);

		} else {
			initHitBox(23, 25);
			xDrawOffset = (int) (8 * Juego.SCALE);
			yDrawOffset = (int) (5 * Juego.SCALE);
		}

		hitbox.y += yDrawOffset + (int) (Juego.SCALE * 2);
		hitbox.x += xDrawOffset / 2;
	}

	public void update() {
		if (doAnimation)
			updateAnimationTick();
	}

}

