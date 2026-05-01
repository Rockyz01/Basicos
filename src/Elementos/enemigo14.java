package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * Enemigo 14 - Goblin Melee (cuerpo a cuerpo)
 * Sprite: 5_Goblin_melee.png  |  6 cols x 5 rows  |  frame 48x48
 * Fila 0=INACTIVO  1=CORRER  2=ATACAR1  3=GOLPE  4=MUERTO
 */
public class enemigo14 extends Enemy {

    private int attackBoxOffsetX;

    public enemigo14(float x, float y) {
        super(x, y, ENEMIGO14_WIDTH, ENEMIGO14_HEIGHT, ENEMIGO14);
        walkSpeed = Juego.SCALE * 0.50f;
        initHitBox(24, 28);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
                (int) (22 * Juego.SCALE), (int) (26 * Juego.SCALE));
        attackBoxOffsetX = (int) (Juego.SCALE * 24);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        updateAttackBox();
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
    }

    private void updateAttackBox() {
        if (walkDir == LEFT)
            attackBox.x = hitbox.x - attackBoxOffsetX;
        else
            attackBox.x = hitbox.x + hitbox.width;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
        if (firstUpdate) firstUpdateCheck(lvlData);
        if (inAir) { updateInAir(lvlData); return; }
        switch (state) {
            case INACTIVO: newState(CORRER); break;
            case CORRER:
                if (canSeePlayer(lvlData, jugador)) {
                    turnTowardsPlayer(jugador);
                    if (isPlayerCloseForAttack(jugador)) {
                        newState(ATACAR1);
                        break;
                    }
                }
                move(lvlData);
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == 1 && !attackChecked) checkPlayerHit(attackBox, jugador);
                break;
            case GOLPE: break;
        }
    }

    @Override
    public int getAniRowOffset() {
        switch (state) {
            case INACTIVO:              return 0;
            case CAMINAR: case CORRER:  return 1;
            case ATACAR1: case ATAQUEC: return 2;
            case GOLPE:                 return 3;
            case MUERTO:                return 4;
            default:                    return 0;
        }
    }

    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
