package Elementos;


import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;


public class enemigo7 extends Enemy {

    private int attackBoxOffsetX;

    public enemigo7(float x, float y) {
        super(x, y, ENEMIGO7_WIDTH, ENEMIGO7_HEIGHT, ENEMIGO7);
        initHitBox((int) (30 * Juego.SCALE), (int) (30 * Juego.SCALE));
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (20 * Juego.SCALE), (int) (20 * Juego.SCALE));
        attackBoxOffsetX = (int) (Juego.SCALE * 30);
    }

    public void update(int [][] lvlData,Jugador jugador){
        updateBehavior(lvlData,jugador);
        updateAnimationTick();
		updateAttackBox();
        
    }
    private void updateAttackBox() {
        if (walkDir == LEFT) {
            attackBox.x = hitbox.x - attackBoxOffsetX - (int) (Juego.SCALE * 5);
        } else if (walkDir == RIGHT) {
            attackBox.x = hitbox.x + attackBoxOffsetX + (int) (Juego.SCALE * 5);
        }
        attackBox.y = hitbox.y;

    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (state) {
			case INACTIVO:
				newState(CORRER);
				break;
			case CORRER:
				if (canSeePlayer(lvlData, jugador)){
					turnTowardsPlayer(jugador);
				if (isPlayerCloseForAttack(jugador))
					newState(ATACAR1);
				}
				move(lvlData);
				break;
			case ATACAR1:
				if (animInd == 0)
					attackChecked = false;

				if (animInd == 3 && !attackChecked)
					checkPlayerHit(attackBox, jugador);

				break;
			case GOLPE:
				break;
			}
		}

	}


    public int flipX() {
        if (walkDir == LEFT) {
            return w;
        } else {
            return 0;
        }
    }

    public int flipW() {
        if (walkDir == LEFT) {
            return -1;
        } else {
            return 1;
        }
    }

}

