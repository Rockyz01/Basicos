package Elementos;

import Juegos.Juego;
import java.awt.geom.Rectangle2D;

/**
 * Proyectil — bola de magia lanzada por el Magic_bear.
 * Viaja horizontalmente hasta que choca con un tile o sale del mapa.
 */
public class Proyectil {

    private float x, y;
    private int dir; // -1 = izquierda, 1 = derecha
    private boolean activo = true;
    private static final float SPEED = Juego.SCALE * 3.5f;
    private static final int W = (int)(12 * Juego.SCALE);
    private static final int H = (int)(12 * Juego.SCALE);
    private Rectangle2D.Float hitbox;
    private int damage;

    public Proyectil(float x, float y, int dir, int damage) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.damage = damage;
        hitbox = new Rectangle2D.Float(x - W/2f, y - H/2f, W, H);
    }

    public void update(int[][] lvlData) {
        if (!activo) return;
        x += dir * SPEED;
        hitbox.x = x - W/2f;

        // Desactivar si sale del mapa
        if (x < 0 || x > lvlData[0].length * Juego.TILES_SIZE)
            activo = false;

        // Desactivar si choca con tile sólido
        if (Utilz.MetodoAyuda.isSolidoPublic(x, y, lvlData))
            activo = false;
    }

    public boolean checkHitPlayer(Jugador jugador) {
        if (!activo) return false;
        if (hitbox.intersects(jugador.getHitbox())) {
            jugador.changeHealth(-damage);
            activo = false;
            return true;
        }
        return false;
    }

    public Rectangle2D.Float getHitbox() { return hitbox; }
    public boolean isActivo() { return activo; }
    public float getX() { return x; }
    public float getY() { return y; }
}
