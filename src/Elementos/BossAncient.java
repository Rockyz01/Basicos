package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * BossAncient
 * Sprite: ancient_boss.png | 4 cols x 9 rows | frame 108x72
 * Faces RIGHT by default (like all other enemies)
 * Row 0: IDLE(3)  Row 1: WALK(4)  Row 2: ATTACK1(3)
 * Row 3: ATAQUEC(3)  Row 4: HURT(3)  Row 5: DEATH(2)
 */
public class BossAncient extends Enemy {

    private int attackBoxOffsetX;
    private boolean enraged = false;
    private int attackCooldown = 0;
    private static final int COOLDOWN = 120;

    public BossAncient(float x, float y) {
        super(x, y, BOSS_ANCIENT_WIDTH, BOSS_ANCIENT_HEIGHT, BOSS_ANCIENT);
        initHitBox(22, 30);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.5f;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
            (int)(24 * Juego.SCALE), (int)(20 * Juego.SCALE));
        attackBoxOffsetX = (int)(20 * Juego.SCALE);
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
            walkSpeed = Juego.SCALE * 0.8f;
            attackDistance = Juego.TILES_SIZE * 1.4f;
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
                if (animInd == 2 && !attackChecked) {
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
            case INACTIVO:             return 1;  // parado
            case CAMINAR: case CORRER: return 6;  // caminar
            case ATACAR1:              return 7;  // ataque cuerpo a cuerpo
            case ATAQUEC:              return 0;  // ataque desde arriba
            case GOLPE:                return 3;  // daño
            case MUERTO:               return 4;  // muerte
            default:                   return 1;
        }
    }

    // Sprites face RIGHT by default — same as all other enemies
    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
