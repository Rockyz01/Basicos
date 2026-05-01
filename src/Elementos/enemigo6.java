package Elementos;
// Spiked_slime — lento, cuerpo a cuerpo, difícil de matar
// Sprite: 6cols x 5rows @ 48px
// Fila 0=IDLE(5), 1=CAMINAR(4), 2=MUERTO(2), 3=ATACAR(4), 4=GOLPE(4)
import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

public class enemigo6 extends Enemy {
    private int attackBoxOffsetX;

    public enemigo6(float x, float y) {
        super(x, y, ENEMIGO6_WIDTH, ENEMIGO6_HEIGHT, ENEMIGO6);
        initHitBox(22, 22);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.3f; // muy lento
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(24*Juego.SCALE), (int)(22*Juego.SCALE));
        attackBoxOffsetX = (int)(24*Juego.SCALE);
    }

    public void update(int[][] lvlData, Jugador jugador) {
        updateBehavior(lvlData, jugador);
        updateAnimationTick();
        updateAttackBox();
    }

    private void updateAttackBox() {
        attackBox.x = (walkDir == LEFT) ? hitbox.x - attackBoxOffsetX : hitbox.x + hitbox.width;
        attackBox.y = hitbox.y;
    }

    private void updateBehavior(int[][] lvlData, Jugador jugador) {
        if (firstUpdate) firstUpdateCheck(lvlData);
        if (inAir) { updateInAir(lvlData); return; }
        switch (state) {
            case INACTIVO: newState(CAMINAR); break;
            case CAMINAR:
                turnTowardsPlayer(jugador);
                if (canSeePlayer(lvlData, jugador) && isPlayerCloseForAttack(jugador))
                    newState(ATACAR1);
                else move(lvlData);
                break;
            case ATACAR1:
                if (animInd == 0) attackChecked = false;
                if (animInd == 2 && !attackChecked) checkPlayerHit(attackBox, jugador);
                break;
            case GOLPE: case MUERTO: break;
        }
    }


    public int getAniRowOffset() {
        switch (state) {
            case INACTIVO: return 0;
            case CAMINAR:  return 1;
            case CORRER:   return 4;
            case ATACAR1:  return 3;
            case GOLPE:    return 4;
            case MUERTO:   return 2;
            default:       return 0;
        }
    }
    // Sprite mira a la IZQUIERDA por defecto → flip invertido
    public int flipX() { return (walkDir == RIGHT) ? w : 0; }
    public int flipW() { return (walkDir == RIGHT) ? -1 : 1; }
}
