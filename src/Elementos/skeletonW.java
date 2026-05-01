package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;


public class skeletonW extends Enemy {

    private int attackBoxOffsetX;

    public skeletonW(float x, float y) {
        super(x, y, SKELETONW_WIDTH, SKELETONW_HEIGHT, SKELETONW);
        initHitBox(16, 20);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (15 * Juego.SCALE), (int) (20 * Juego.SCALE));
        attackBoxOffsetX = (int) (Juego.SCALE * 15);
    }

    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        if (walkDir == LEFT) {
            attackBox.x = hitbox.x - attackBoxOffsetX;
        } else {
            attackBox.x = hitbox.x + hitbox.width;
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
                if (animInd == 0)
                    attackChecked = false;
                if (animInd == 3 && !attackChecked)
                    checkPlayerHit(attackBox, jugador);
                // Al terminar el ataque volver a correr directamente
                if (animInd >= GetSpriteAmount(enemyType, state) - 1)
                    newState(CORRER);
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

    @Override
    public int getAniRowOffset() {
        // enemy_sprite.png (10 cols x 9 rows, frame 96x42)
        // Row 0: WALK(8)    Row 1: ATTACK1(8)   Row 2: ATTACK2(10)
        // Row 3: DEATH(10)  Row 4: IDLE(5)      Row 5-8: extras
        switch (state) {
            case INACTIVO:             return 4;  // IDLE - fila 4
            case CORRER: case CAMINAR: return 0;  // WALK - fila 0
            case ATACAR1:              return 1;  // ATTACK1 - fila 1
            case ATAQUEC:              return 2;  // ATTACK2 - fila 2
            case GOLPE:                return 5;  // recuperacion
            case MUERTO:               return 3;  // DEATH - fila 3
            default:                   return 4;
        }
    }
}
