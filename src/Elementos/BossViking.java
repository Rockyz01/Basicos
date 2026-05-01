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
        // Pegar al suelo, pero dejando 1 pixel libre para que las esquinas
        // inferiores de la hitbox no se interpreten como "dentro" de un sólido
        // (mismo truco que usa la gravedad en GetEntityYPosUnderRoofOrAboveFloor).
        Utilz.MetodoAyuda.SnapEntityToFloor(hitbox, lvlData);
        hitbox.y -= 1;
        inAir = false;
        airSpeed = 0;
        tileY = (int) (hitbox.y / Juego.TILES_SIZE);
        firstUpdate = false;
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

    /**
     * Movimiento propio del jefe: respeta paredes y aplica gravedad si camina al vacío.
     * No se queda atascado en bordes (no usa IsFloor para frenar el avance).
     */
    @Override
    protected void move(int[][] lvlData) {
        float xSpeed = (walkDir == LEFT) ? -walkSpeed : walkSpeed;
        if (Utilz.MetodoAyuda.CanMoveHere(hitbox.x + xSpeed, hitbox.y,
                                          hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            // Si quedó pisando aire, dejar que caiga por gravedad
            if (!Utilz.MetodoAyuda.isEntityOnFloor(hitbox, lvlData)) {
                inAir = true;
            }
        } else {
            // Topó con una pared, da la vuelta
            changeWalkDir();
        }
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
        // Sprite real (viking_boss.png 6x9, 72x72):
        // Row 0 = IDLE (6f)   Row 1 = ATTACK1 (4f)  Row 2 = ATAQUEC (4f)
        // Row 3 = WALK (6f)   Row 4 = DEATH (4f)    Row 5 = HURT (2f)
        switch (state) {
            case INACTIVO: return 0;
            case ATACAR1:
            case ATACAR2:
            case ATACAR3:  return 1;
            case ATAQUEC:  return 2;
            case CAMINAR:
            case CORRER:   return 3;
            case MUERTO:   return 4;
            case GOLPE:    return 5;
            default:       return 0;
        }
    }

    // Las filas IDLE/ATACAR miran a la DERECHA por defecto → flip cuando va a la IZQUIERDA.
    // PERO la fila CAMINAR del sprite mira a la IZQUIERDA → en ese estado el flip es al revés.
    public int flipX() {
        boolean walking = (state == CAMINAR || state == CORRER);
        boolean spriteFacesLeft = walking;
        // Si el sprite mira hacia donde caminamos, no flip; si no, flip.
        boolean needsFlip = spriteFacesLeft ? (walkDir == RIGHT) : (walkDir == LEFT);
        return needsFlip ? w : 0;
    }
    public int flipW() {
        boolean walking = (state == CAMINAR || state == CORRER);
        boolean spriteFacesLeft = walking;
        boolean needsFlip = spriteFacesLeft ? (walkDir == RIGHT) : (walkDir == LEFT);
        return needsFlip ? -1 : 1;
    }
}
