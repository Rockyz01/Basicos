package Elementos;

// Magic_bear — se queda en su lugar y lanza proyectiles de magia a distancia.
// Sprite: 6cols x 9rows @ 72px
// R0=IDLE(6), R1=CAMINAR(6), R2=CORRER(6), R3=ATACAR_MELEE(5),
// R4=ATAQUE_DISTANCIA(4), R5=MUERTO(2), R6=GOLPE(4)

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class enemigo3 extends Enemy {

    private int attackBoxOffsetX;
    private ArrayList<Proyectil> proyectiles = new ArrayList<>();
    private boolean proyectilLanzado = false;

    private static final float RANGED_DIST = Juego.TILES_SIZE * 6f;
    private static final float MELEE_DIST  = Juego.TILES_SIZE * 1.2f;

    public enemigo3(float x, float y) {
        super(x, y, ENEMIGO3_WIDTH, ENEMIGO3_HEIGHT, ENEMIGO3);
        initHitBox(26, 28);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.4f;
        attackDistance = MELEE_DIST;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y,
            (int)(28 * Juego.SCALE), (int)(28 * Juego.SCALE));
        attackBoxOffsetX = (int)(28 * Juego.SCALE);
    }

    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
        // Actualizar proyectiles
        for (Proyectil p : proyectiles)
            if (p.isActivo()) {
                p.update(lvlData);
                p.checkHitPlayer(jugador);
            }
        proyectiles.removeIf(p -> !p.isActivo());
    }

    private void updateAttackBox() {
        attackBox.x = (walkDir == LEFT)
            ? hitbox.x - attackBoxOffsetX
            : hitbox.x + hitbox.width;
        attackBox.y = hitbox.y;
    }

    private boolean isInRangedRange(Jugador jugador) {
        return Math.abs(jugador.hitbox.x - hitbox.x) <= RANGED_DIST;
    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
        if (firstUpdate) firstUpdateCheck(lvlData);
        if (inAir) { updateInAir(lvlData); return; }

        switch (state) {
            case INACTIVO:
                newState(CAMINAR);
                break;

            case CAMINAR:
            case CORRER:
                turnTowardsPlayer(jugador);
                if (canSeePlayer(lvlData, jugador)) {
                    if (isPlayerCloseForAttack(jugador))
                        newState(ATACAR1);         // melee
                    else if (isInRangedRange(jugador)) {
                        newState(ATAQUEC);          // distancia — se queda quieto
                        proyectilLanzado = false;
                    } else
                        move(lvlData);
                } else {
                    move(lvlData);
                }
                break;

            case ATACAR1: // melee
                if (animInd == 0) attackChecked = false;
                if (animInd == 3 && !attackChecked)
                    checkPlayerHit(attackBox, jugador);
                break;

            case ATAQUEC: // ataque a distancia — SE QUEDA QUIETO
                if (animInd == 0) proyectilLanzado = false;
                // Lanzar proyectil en frame 2 de la animación
                if (animInd == 2 && !proyectilLanzado) {
                    lanzarProyectil(jugador);
                    proyectilLanzado = true;
                }
                break;

            case GOLPE:
            case MUERTO:
                break;
        }
    }

    private void lanzarProyectil(Jugador jugador) {
        int dir = (walkDir == LEFT) ? -1 : 1;
        float px = hitbox.x + (dir == 1 ? hitbox.width : 0);
        float py = hitbox.y + hitbox.height / 2f;
        int dmg = Utilz.Constantes.EnemyConstants.GetEnemyDmg(ENEMIGO3);
        proyectiles.add(new Proyectil(px, py, dir, dmg));
    }

    public void drawProyectiles(Graphics g, int xLvlOffset) {
        g.setColor(new Color(100, 200, 255, 200));
        for (Proyectil p : proyectiles) {
            if (p.isActivo()) {
                Rectangle2D.Float hb = p.getHitbox();
                g.fillOval(
                    (int)(hb.x - xLvlOffset),
                    (int)(hb.y),
                    (int)(hb.width),
                    (int)(hb.height)
                );
            }
        }
    }

    public int getAniRowOffset() {
        switch (state) {
            case INACTIVO: return 0;
            case CAMINAR:  return 1;
            case CORRER:   return 2;
            case ATACAR1:  return 3;
            case ATAQUEC:  return 4;
            case MUERTO:   return 5;
            case GOLPE:    return 6;
            default:       return 0;
        }
    }

    // Sprite mira a la IZQUIERDA → flip invertido
    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
