package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import Utilz.MetodoAyuda;
import java.awt.geom.Rectangle2D;

/**
 * Enemigo 11 - Vulture (Buitre) — Boss volador
 * Sprite: Vulture.png  |  4 cols x 5 rows  |  frame 48x48
 * Fila 0=INACTIVO  1=CORRER  2=ATACAR1  3=GOLPE  4=MUERTO
 * Vuela: no le afecta la gravedad, patrulla en el aire.
 */
public class enemigo11 extends Enemy {

    private int attackBoxOffsetX;
    private float flyY = -1;
    private static final float FLY_HEIGHT = 80 * Juego.SCALE;

    public enemigo11(float x, float y) {
        super(x, y, ENEMIGO11_WIDTH, ENEMIGO11_HEIGHT, ENEMIGO11);
        walkSpeed = Juego.SCALE * 0.50f;
        attackDistance = Juego.TILES_SIZE * 1.2f;
        initHitBox(30, 26);
        initAttackBox();
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
                (int) (24 * Juego.SCALE), (int) (24 * Juego.SCALE));
        attackBoxOffsetX = (int) (Juego.SCALE * 28);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        if (walkDir == LEFT)
            attackBox.x = hitbox.x - attackBoxOffsetX;
        else
            attackBox.x = hitbox.x + hitbox.width;
        attackBox.y = hitbox.y + hitbox.height / 2f;
    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
        if (firstUpdate) {
            firstUpdate = false;
            inAir = false;
            if (flyY < 0) flyY = hitbox.y - FLY_HEIGHT;
        }

        // Mantener altura de vuelo
        if (hitbox.y > flyY + 2) hitbox.y -= Juego.SCALE * 1.2f;

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
                moveHorizontal(lvlData);
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == 1 && !attackChecked) checkPlayerHit(attackBox, jugador);
                break;
            case GOLPE: break;
        }
    }

    private void moveHorizontal(int[][] lvlData) {
        float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;
        if (MetodoAyuda.CanMoveHere(hitbox.x + xSpeed, hitbox.y,
                hitbox.width, hitbox.height, lvlData))
            hitbox.x += xSpeed;
        else
            changeWalkDir();
    }

    @Override
public int getAniRowOffset() {
        // Mapeo exacto para tus sprites (Attack, Death, Hurt, Idle, Walk)
        switch (state) {
            case ATACAR1:  return 0; // Fila 0: Attack
            case MUERTO:   return 1; // Fila 1: Death
            case GOLPE:    return 2; // Fila 2: Hurt
            case INACTIVO: return 3; // Fila 3: Idle
            case CAMINAR:  return 4; // Fila 4: Walk
            case CORRER:   return 4; // Fila 4: Walk (misma fila)
            default:       return 3; // Por defecto Inactivo
        }
    }

    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
