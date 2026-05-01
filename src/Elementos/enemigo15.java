package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

/**
 * Enemigo 15 - Goblin Range (a distancia)
 * Sprite: 6_Goblin_range.png  |  6 cols x 5 rows  |  frame 48x50
 * Fila 0=INACTIVO  1=CORRER  2=ATACAR1  3=GOLPE  4=MUERTO
 * Dispara proyectiles al jugador cuando lo detecta.
 */
public class enemigo15 extends Enemy {

    private static final float SHOOT_RANGE   = Juego.TILES_SIZE * 8;
    private static final float PROJ_SPEED    = Juego.SCALE * 3.0f;
    private static final float PROJ_W        = 8  * Juego.SCALE;
    private static final float PROJ_H        = 6  * Juego.SCALE;
    private static final int   SHOOT_FRAME   = 1;   // frame dentro de ATACAR1 en que dispara
    private static final int   RETREAT_DIST  = (int) (Juego.TILES_SIZE * 3); // distancia mínima al jugador

    // Proyectil interno simple
    private float projX, projY, projSpeedX;
    private boolean projActive = false;
    private Rectangle2D.Float projBox;

    public enemigo15(float x, float y) {
        super(x, y, ENEMIGO15_WIDTH, ENEMIGO15_HEIGHT, ENEMIGO15);
        walkSpeed     = Juego.SCALE * 0.40f;
        attackDistance = SHOOT_RANGE;
        initHitBox(22, 28);
        projBox = new Rectangle2D.Float(0, 0, PROJ_W, PROJ_H);
    }

    @Override
    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateProjectile(jugador);
    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
        if (firstUpdate) firstUpdateCheck(lvlData);
        if (inAir) { updateInAir(lvlData); return; }

        switch (state) {
            case INACTIVO: newState(CORRER); break;
            case CORRER:
                if (canSeePlayer(lvlData, jugador)) {
                    turnTowardsPlayer(jugador);
                    int dist = (int) Math.abs(jugador.hitbox.x - hitbox.x);
                    if (dist <= SHOOT_RANGE) {
                        // Si el jugador está muy cerca, retroceder
                        if (dist < RETREAT_DIST) retreat(lvlData);
                        else newState(ATACAR1);
                    } else {
                        move(lvlData);
                    }
                } else {
                    move(lvlData);
                }
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == SHOOT_FRAME && !attackChecked) {
                    shootProjectile(jugador);
                    attackChecked = true;
                }
                break;
            case GOLPE: break;
        }
    }

    private void retreat(int[][] lvlData) {
        // Alejarse del jugador (dirección contraria)
        float xSpeed = (walkDir == LEFT) ? walkSpeed : -walkSpeed;
        if (Utilz.MetodoAyuda.CanMoveHere(hitbox.x + xSpeed, hitbox.y,
                hitbox.width, hitbox.height, lvlData)
            && Utilz.MetodoAyuda.IsFloor(hitbox, xSpeed, lvlData))
            hitbox.x += xSpeed;
    }

    private void shootProjectile(Jugador jugador) {
        if (projActive) return; // solo un proyectil a la vez
        projX = hitbox.x + hitbox.width / 2f;
        projY = hitbox.y + hitbox.height / 2f;
        projSpeedX = (jugador.hitbox.x > hitbox.x) ? PROJ_SPEED : -PROJ_SPEED;
        projActive = true;
        projBox.x = projX;
        projBox.y = projY;
    }

    private void updateProjectile(Jugador jugador) {
        if (!projActive) return;
        projX += projSpeedX;
        projBox.x = projX;
        projBox.y = projY;

        // Golpear jugador
        if (projBox.intersects(jugador.hitbox)) {
            jugador.changeHealth(-Utilz.Constantes.EnemyConstants.GetEnemyDmg(ENEMIGO15));
            projActive = false;
            return;
        }

        // Destruir si sale de pantalla o va muy lejos
        if (Math.abs(projX - hitbox.x) > SHOOT_RANGE * 1.5f)
            projActive = false;
    }

    /** Dibujar proyectil (llamado desde EnemyManager) */
    public void drawProyectil(Graphics g, int xLvlOffset) {
        if (!projActive) return;
        g.setColor(new Color(255, 160, 0));
        g.fillOval((int)(projBox.x - xLvlOffset), (int)projBox.y,
                   (int)PROJ_W, (int)PROJ_H);
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
