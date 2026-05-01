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
        walkSpeed = Juego.SCALE * 0.55f;
        attackDistance = Juego.TILES_SIZE * 1.5f;
        initHitBox(28, 20);
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
        if (inAir) { updateInAir(lvlData); return; }
        
        switch (state) {
            case INACTIVO: 
                newState(CAMINAR); 
                break;
            case CAMINAR:
                // El enemigo patrulla de lado a lado. 
                // SOLO voltea a ver al jugador si lo tiene en la mira sin paredes de por medio.
                if (canSeePlayer(lvlData, jugador)) {
                    turnTowardsPlayer(jugador);
                    if (isPlayerCloseForAttack(jugador)) {
                        newState(ATACAR1);
                    }
                }
                
                // Si no está atacando, que siga caminando
                if (state != ATACAR1) {
                    move(lvlData);
                }
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                // El golpe se registra en el frame 2 de la animación (cámbialo si tu sprite pega en otro frame)
                if (animInd == 2 && !attackChecked) checkPlayerHit(attackBox, jugador);
                break;
            case GOLPE: 
            case MUERTO: 
                break;
        }
    }

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
