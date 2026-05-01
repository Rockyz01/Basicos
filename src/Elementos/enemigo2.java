package Elementos;
// Frost_golem — lento pero fuerte, cuerpo a cuerpo
// Sprite: 6cols x 5rows @ 48px
// Fila 0=IDLE(4), 1=CAMINAR(6), 2=MUERTO(2), 3=ATACAR(4), 4=GOLPE(4)
import Juegos.Juego;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import java.awt.geom.Rectangle2D;

public class enemigo2 extends Enemy {
    private int attackBoxOffsetX;

    public enemigo2(float x, float y) {
        super(x, y, ENEMIGO2_WIDTH, ENEMIGO2_HEIGHT, ENEMIGO2);
        initHitBox(22, 28);
        initAttackBox();
        walkSpeed = Juego.SCALE * 0.35f; // más lento
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int)(26*Juego.SCALE), (int)(26*Juego.SCALE));
        attackBoxOffsetX = (int)(26*Juego.SCALE);
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
