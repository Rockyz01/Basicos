package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * BossViking
 * Sprite: viking_boss.png | 4 cols x 9 rows | frame 108x72
 * Faces LEFT by default (head on left side of frame)
 * Row 0: ATTACK1(4)  Row 1: ATAQUEC(3)  Row 2: something(3)
 * Row 3: WALK(4)     Row 4: HURT(3)     Row 5: DEATH(2)
 * Row 6-7: extra(3)  Row 8: IDLE(4)
 */
public class BossViking extends Enemy {

    private int attackBoxOffsetX;
    private boolean enraged = false;
    private int attackCooldown = 0;
    private static final int COOLDOWN = 100;

    public BossViking(float x, float y) {
        super(x, y, BOSS_VIKING_WIDTH, BOSS_VIKING_HEIGHT, BOSS_VIKING);
        initHitBox(22, 32);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.55f;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
            (int)(26 * Juego.SCALE), (int)(24 * Juego.SCALE));
        attackBoxOffsetX = (int)(22 * Juego.SCALE);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
        checkEnrage();
    }

    private void checkEnrage() {
        if (!enraged && currentHealth <= maxHealth / 2) {
            enraged = true;
            walkSpeed = Juego.SCALE * 0.9f;
            attackDistance = Juego.TILES_SIZE * 1.5f;
        }
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
        if (attackCooldown > 0) attackCooldown--;

        switch (state) {
            case INACTIVO:
                newState(CAMINAR);
                break;
            case CAMINAR:
            case CORRER:
                turnTowardsPlayer(jugador);
                if (canSeePlayer(lvlData, jugador) && isPlayerCloseForAttack(jugador) && attackCooldown == 0)
                    newState(enraged ? ATAQUEC : ATACAR1);
                else
                    move(lvlData);
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == 3 && !attackChecked) {
                    checkPlayerHit(attackBox, jugador);
                    attackCooldown = COOLDOWN;
                }
                break;
            case ATAQUEC:
                if (animInd == 0) attackChecked = false;
                if (animInd == 2 && !attackChecked) {
                    checkPlayerHit(attackBox, jugador);
                    attackCooldown = COOLDOWN;
                }
                break;
            case GOLPE:
            case MUERTO:
                break;
        }
    }

    @Override
    public int getAniRowOffset() {
        // Sprite viking_boss.png layout (6 cols x 9 rows):
        // Row 0: ATTACK1(4)  Row 1: ATAQUEC(3)  Row 2: extra(3)
        // Row 3: WALK(4)     Row 4: HURT(3)     Row 5: DEATH(2)
        // Row 8: IDLE(4)
        switch (state) {
            case INACTIVO:             return 8;  // IDLE - fila 8
            case CAMINAR: case CORRER: return 3;  // WALK - fila 3
            case ATACAR1:              return 0;  // ATTACK1 - fila 0
            case ATAQUEC:              return 1;  // ATAQUEC - fila 1
            case GOLPE:                return 4;  // HURT - fila 4
            case MUERTO:               return 5;  // DEATH - fila 5
            default:                   return 8;
        }
    }

    // Viking sprite mira a la IZQUIERDA por defecto (la cabeza está en el lado izquierdo del frame)
    // → cuando walkDir es RIGHT hay que hacer flip horizontal para que mire a la derecha
    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
