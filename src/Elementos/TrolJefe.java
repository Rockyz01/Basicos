package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

public class TrolJefe extends Enemy {

    private int attackBoxOffsetX;
    private boolean enraged = false;

    // Cooldown entre ataques: el jefe camina un tiempo antes de volver a atacar
    private int attackCooldown = 0;
    private static final int ATTACK_COOLDOWN_MAX = 120; // ~2 segundos a 60ups

    public TrolJefe(float x, float y) {
        super(x, y, TROL_JEFE_WIDTH, TROL_JEFE_HEIGHT, TROL_JEFE);
        initHitBox(30, 30);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.55f;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
                (int) (30 * Juego.SCALE),
                (int) (30 * Juego.SCALE));
        attackBoxOffsetX = (int) (30 * Juego.SCALE);
    }

    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
        checkEnrage();
    }

    private void checkEnrage() {
        if (!enraged && currentHealth <= maxHealth / 2) {
            enraged = true;
            walkSpeed = Juego.SCALE * 0.85f;
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
        if (firstUpdate)
            firstUpdateCheck(lvlData);

        if (inAir) {
            updateInAir(lvlData);
            return;
        }

        // Bajar el cooldown cada tick
        if (attackCooldown > 0)
            attackCooldown--;

        switch (state) {
            case INACTIVO:
                newState(CAMINAR);
                break;

            case CAMINAR:
            case CORRER:
                turnTowardsPlayer(jugador);
                if (canSeePlayer(lvlData, jugador) && isPlayerCloseForAttack(jugador) && attackCooldown == 0) {
                    // Cerca y listo para atacar
                    newState(enraged ? ATAQUEC : ATACAR1);
                } else {
                    // Caminar hacia el jugador
                    move(lvlData);
                }
                break;

            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == 3 && !attackChecked)
                    checkPlayerHit(attackBox, jugador);
                // Cuando termine la animación (en updateAnimationTick) vuelve a INACTIVO
                // y nosotros ponemos el cooldown en el momento que empieza a caminar
                break;

            case ATAQUEC:
                if (animInd == 0) attackChecked = false;
                if (animInd == 3 && !attackChecked)
                    checkPlayerHit(attackBox, jugador);
                break;

            case GOLPE:
            case MUERTO:
                break;
        }
    }

    // Sobrescribimos newState para poner cooldown al terminar un ataque
    @Override
    protected void newState(int enemyState) {
        // Si venimos de un ataque y vamos a caminar → cooldown
        if ((state == ATACAR1 || state == ATAQUEC) &&
            (enemyState == INACTIVO || enemyState == CAMINAR || enemyState == CORRER)) {
            attackCooldown = enraged ? ATTACK_COOLDOWN_MAX / 2 : ATTACK_COOLDOWN_MAX;
        }
        super.newState(enemyState);
    }

    public int getAniRowOffset() {
        switch (state) {
            case INACTIVO: return 0;
            case CAMINAR:  return enraged ? 2 : 1;
            case CORRER:   return enraged ? 8 : 7;
            case ATACAR1:  return 4;
            case ATAQUEC:  return 3;
            case GOLPE:    return 5;
            case MUERTO:   return 6;
            default:       return 0;
        }
    }

    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
