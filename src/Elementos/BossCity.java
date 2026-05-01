package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BossCity — jefe final (nivel ciudad).
 * Sprite: city_boss.png | 6 cols x 9 rows | frame 96x96
 * Sigue el mismo patrón que BossToadKing/BossAncient.
 *
 * Fila real del sprite (mira a la DERECHA):
 *   0=SHOOT(6)  1=SHOOT2(4)  2=SPECIAL(6)  3=extra
 *   4=DEATH(6)  5=extra      6=IDLE(6)     7=WALK(6)  8=HURT(4)
 *
 * Comportamiento:
 *  - Cuerpo a cuerpo si el jugador está pegado (ATACAR1).
 *  - Disparo a distancia con BalaCity si está en rango medio (ATAQUEC).
 *  - Al bajar a 50% HP se enrabia (más rápido y dispara doble).
 */
public class BossCity extends Enemy {

    private static final float SHOOT_RANGE  = Juego.TILES_SIZE * 7;
    private static final float MELEE_RANGE  = Juego.TILES_SIZE * 1.4f;
    private static final int   SHOOT_FRAME  = 3;
    private static final int   MELEE_FRAME  = 3;

    private int attackBoxOffsetX;
    private boolean enraged = false;
    private int attackCooldown = 0;
    private static final int COOLDOWN_NORMAL  = 90;
    private static final int COOLDOWN_ENRAGED = 55;

    private final List<BalaCity> balas = new CopyOnWriteArrayList<>();

    public BossCity(float x, float y) {
        super(x, y, BOSS_CITY_WIDTH, BOSS_CITY_HEIGHT, BOSS_CITY);
        // Hitbox modesta como los otros bosses (cabe en cualquier zona del nivel)
        initHitBox(26, 38);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.50f;
        attackDistance = MELEE_RANGE;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
            (int)(26 * Juego.SCALE), (int)(24 * Juego.SCALE));
        attackBoxOffsetX = (int)(20 * Juego.SCALE);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
        updateBalas(lvlData, jugador);
        checkEnrage();
    }

    private void checkEnrage() {
        if (!enraged && currentHealth <= maxHealth / 2) {
            enraged = true;
            walkSpeed = Juego.SCALE * 0.85f;
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
                if (canSeePlayer(lvlData, jugador) && attackCooldown == 0) {
                    float dist = Math.abs(jugador.hitbox.x - hitbox.x);
                    if (dist <= MELEE_RANGE)
                        newState(ATACAR1);
                    else if (dist <= SHOOT_RANGE)
                        newState(ATAQUEC);
                    else
                        move(lvlData);
                } else {
                    move(lvlData);
                }
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == MELEE_FRAME && !attackChecked) {
                    checkPlayerHit(attackBox, jugador);
                    attackCooldown = enraged ? COOLDOWN_ENRAGED : COOLDOWN_NORMAL;
                }
                break;
            case ATAQUEC:
                if (animInd == 0) attackChecked = false;
                if (animInd == SHOOT_FRAME && !attackChecked) {
                    shootBala(0);
                    if (enraged) shootBala((int)(-6 * Juego.SCALE));
                    attackChecked = true;
                    attackCooldown = enraged ? COOLDOWN_ENRAGED : COOLDOWN_NORMAL;
                }
                break;
            case GOLPE:
            case MUERTO:
                break;
        }
    }

    private void shootBala(int yOffset) {
        float bx = (walkDir == RIGHT) ? hitbox.x + hitbox.width : hitbox.x;
        float by = hitbox.y + hitbox.height * 0.30f + yOffset;
        int dir = (walkDir == RIGHT) ? 1 : -1;
        balas.add(new BalaCity(bx, by, dir, GetEnemyDmg(BOSS_CITY)));
    }

    private void updateBalas(int[][] lvlData, Jugador jugador) {
        for (BalaCity b : balas) {
            if (!b.isActiva()) continue;
            b.update(lvlData);
            if (b.isActiva() && b.checkHit(jugador.hitbox))
                jugador.changeHealth(-b.getDamage());
        }
        List<BalaCity> aBorrar = new ArrayList<>();
        for (BalaCity b : balas) if (!b.isActiva()) aBorrar.add(b);
        balas.removeAll(aBorrar);
    }

    public void drawProyectiles(Graphics g, int xLvlOffset) {
        for (BalaCity b : balas) b.draw(g, xLvlOffset);
    }

    @Override
    public int getAniRowOffset() {
        switch (state) {
            case INACTIVO:  return 6;  // Idle
            case CAMINAR:
            case CORRER:    return 7;  // Walk
            case ATACAR1:   return 0;  // Disparo (también melee, queda igual)
            case ATAQUEC:   return 0;  // Disparo
            case ATACAR2:
            case ATACAR3:   return 2;  // Especial (no usado actualmente)
            case GOLPE:     return 8;  // Hurt
            case MUERTO:    return 4;  // Death
            default:        return 6;
        }
    }

    // Las filas IDLE/SHOOT/HURT miran a la DERECHA por defecto → flip cuando va a la IZQUIERDA.
    // PERO la fila WALK del sprite muestra al jefe de ESPALDAS / mirando a la IZQUIERDA →
    // en ese estado el flip es al revés (mismo truco que BossViking).
    public int flipX() {
        boolean walking = (state == CAMINAR || state == CORRER);
        boolean spriteFacesLeft = walking;
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
