package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * BossViking
 * Sprite: viking_boss.png | 6 cols x 9 rows | frame 72x72
 * El sprite mira a la DERECHA por defecto.
 * Row 0: IDLE(6)    Row 1: ATTACK1(4)   Row 2: ATAQUEC(4)
 * Row 3: WALK(6)    Row 4: DEATH(4)     Row 5: HURT(2)
 * Row 6-8: extras
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
    protected void firstUpdateCheck(int[][] lvlData) {
        super.firstUpdateCheck(lvlData);
        // Si está en el aire al spawn, forzar caída rápida para no verse "volando"
        if (inAir) airSpeed = 2.0f * Juego.SCALE;
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
        // viking_boss.png - el sprite mira a la DERECHA por defecto
        // Row 0: IDLE   Row 1: ATTACK1   Row 2: ATAQUEC
        // Row 3: WALK   Row 4: DEATH     Row 5: HURT
        switch (state) {
            case INACTIVO:             return 0;  // IDLE
            case CAMINAR: case CORRER: return 3;  // WALK
            case ATACAR1:              return 1;  // ATTACK1
            case ATAQUEC:              return 2;  // ATAQUEC
            case GOLPE:                return 5;  // HURT
            case MUERTO:               return 4;  // DEATH
            default:                   return 0;
        }
    }

    // Sprite mira a la DERECHA por defecto → flip cuando va a la IZQUIERDA
    public int flipX() { return (walkDir == LEFT) ? w : 0; }
    public int flipW() { return (walkDir == LEFT) ? -1 : 1; }
}
