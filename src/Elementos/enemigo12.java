package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

/**
 * Enemigo 12 - Dark Toad (Rana oscura) — Boss final
 * Sprite: 2_Toad.png  |  6 cols x 5 rows  |  frame 48x48
 * Fila 0=INACTIVO  1=CORRER  2=ATACAR1  3=GOLPE  4=MUERTO
 * Al 50% HP entra en modo furioso: más velocidad y golpea más rápido.
 */
public class enemigo12 extends Enemy {

    private int attackBoxOffsetX;
    private boolean enraged = false;

    public enemigo12(float x, float y) {
        super(x, y, ENEMIGO12_WIDTH, ENEMIGO12_HEIGHT, ENEMIGO12);
<<<<<<< HEAD
        walkSpeed = Juego.SCALE * 0.55f;
        attackDistance = Juego.TILES_SIZE * 1.5f;
        initHitBox(28, 20);
=======
        initHitBox(14, 25);
>>>>>>> 2c7cdeebd7e2c98430828ddceff33cfb310a32da
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
                (int) (28 * Juego.SCALE), (int) (26 * Juego.SCALE));
        attackBoxOffsetX = (int) (Juego.SCALE * 32);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        checkEnrage();
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
    }

    private void checkEnrage() {
        if (!enraged && currentHealth <= maxHealth / 2) {
            enraged = true;
            walkSpeed = Juego.SCALE * 0.80f;
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
        if (inAir) {
            updateInAir(lvlData);
        } else {
            switch (state) {
                case INACTIVO:
                    if (canSeePlayer(lvlData, jugador)) newState(CORRER);
                    break;
                case CORRER:
                    turnTowardsPlayer(jugador);
                    if (isPlayerCloseForAttack(jugador)) {
                        newState(ATACAR1);
                        break;
                    }
                    move(lvlData);
                    break;
                case ATACAR1:
                    if (animInd == 0) attackChecked = false;
                    int hitFrame = enraged ? 2 : 3;
                    if (animInd == hitFrame && !attackChecked) checkPlayerHit(attackBox, jugador);
                    break;
                case GOLPE: break;
            }
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
