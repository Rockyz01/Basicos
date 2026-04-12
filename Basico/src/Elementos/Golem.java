package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

public class Golem extends Enemy{

	private int attackBoxOffsetX;

    public Golem(float x, float y) {
        super(x, y, GOLEM_WIDTH, GOLEM_HEIGHT, GOLEM);
        initHitBox(40,20);
		initAttackBox();
    }
	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (37 * Juego.SCALE), (int) (65 * Juego.SCALE));
		attackBoxOffsetX = (int) (Juego.SCALE * 50);
	}
	public void update(int [][] lvlData,Jugador jugador){
        updateBehavior(lvlData,jugador);
        updateAnimationTick();
		updateAttackBox();
        
    }
    
	private void updateAttackBox() {
		attackBox.x=hitbox.x;
		attackBox.y=hitbox.y-attackBoxOffsetX;
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
		if (walkDir == LEFT)
			return w;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == LEFT)
			return -1;
		else
			return 1;

	}

}