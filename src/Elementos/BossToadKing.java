package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * BossToadKing
 * Sprite: toad_king_boss.png | 4 cols x 9 rows | frame 108x72
 * Faces LEFT by default
 * Row 0: IDLE(3)    Row 1: WALK(3)   Row 2: ATTACK1(4)
 * Row 3: ATAQUEC(3) Row 4: HURT(3)   Row 5: DEATH(2)
 */
public class BossToadKing extends Enemy {

    private int attackBoxOffsetX;
    private boolean enraged = false;
    private int attackCooldown = 0;
    private static final int COOLDOWN = 115;

    public BossToadKing(float x, float y) {
        super(x, y, BOSS_TOAD_KING_WIDTH, BOSS_TOAD_KING_HEIGHT, BOSS_TOAD_KING);
        initHitBox(20, 26);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.45f;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
            (int)(22 * Juego.SCALE), (int)(20 * Juego.SCALE));
        attackBoxOffsetX = (int)(18 * Juego.SCALE);
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
            walkSpeed = Juego.SCALE * 0.72f;
            attackDistance = Juego.TILES_SIZE * 1.3f;
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
        switch (state) {
            case INACTIVO:             return 1;  // idle
            case CAMINAR: case CORRER: return 8;  // caminar
            case ATACAR1:              return 0;  // ataque con lengua
            case ATAQUEC:              return 2;  // ataque con lengua larga
            case GOLPE:                return 4;  // daño
            case MUERTO:               return 4;  // muerte
            default:                   return 1;
        }
    }

    // ToadKing sprite mira a la DERECHA por defecto (igual que los demás)
    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
