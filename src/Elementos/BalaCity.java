package Elementos;

import Juegos.Juego;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * BalaCity — proyectil mecánico/láser disparado por BossCity.
 * Viaja recto, con destello brillante al frente y un rastro corto.
 */
public class BalaCity {

    private float x, y;
    private int dir;          // -1 izquierda, +1 derecha
    private boolean activa = true;
    private static final float SPEED = Juego.SCALE * 6.0f;
    private static final int W = (int) (18 * Juego.SCALE);
    private static final int H = (int) (8  * Juego.SCALE);
    private Rectangle2D.Float hitbox;
    private final int damage;
    private int tick = 0;

    // Rastro
    private float[] trailX = new float[6];
    private float[] trailY = new float[6];
    private int trailHead = 0;
    private boolean trailFull = false;

    public BalaCity(float x, float y, int dir, int damage) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.damage = damage;
        hitbox = new Rectangle2D.Float(x - W / 2f, y - H / 2f, W, H);
        for (int i = 0; i < trailX.length; i++) { trailX[i] = x; trailY[i] = y; }
    }

    public void update(int[][] lvlData) {
        if (!activa) return;
        tick++;

        // Guardar rastro
        trailX[trailHead] = x;
        trailY[trailHead] = y;
        trailHead = (trailHead + 1) % trailX.length;
        if (trailHead == 0) trailFull = true;

        x += dir * SPEED;
        hitbox.x = x - W / 2f;
        hitbox.y = y - H / 2f;

        if (x < 0 || x > lvlData[0].length * Juego.TILES_SIZE) {
            activa = false;
            return;
        }
        if (Utilz.MetodoAyuda.isSolidoPublic(x, y, lvlData))
            activa = false;
    }

    public boolean checkHit(Rectangle2D.Float playerHitbox) {
        if (!activa) return false;
        if (hitbox.intersects(playerHitbox)) {
            activa = false;
            return true;
        }
        return false;
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!activa) return;
        Graphics2D g2 = (Graphics2D) g;
        Object oldHint = g2.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int cx = (int) (x - xLvlOffset);
        int cy = (int) y;

        // Rastro
        int trailLen = trailFull ? trailX.length : trailHead;
        for (int i = 0; i < trailLen; i++) {
            int idx = (trailHead - 1 - i + trailX.length) % trailX.length;
            float age = (i + 1f) / trailLen;
            int alpha = (int) ((1f - age) * 140);
            int sw = (int) (W * (1f - age * 0.6f));
            int sh = (int) (H * (1f - age * 0.6f));
            int tx = (int) (trailX[idx] - xLvlOffset);
            int ty = (int) trailY[idx];
            g2.setColor(new Color(255, 200, 100, alpha));
            g2.fillOval(tx - sw / 2, ty - sh / 2, sw, sh);
        }

        // Cuerpo de la bala (cápsula naranja-amarilla)
        g2.setColor(new Color(255, 160, 40, 230));
        g2.fillOval(cx - W / 2, cy - H / 2, W, H);
        // Núcleo brillante
        int ir = (int) (Math.min(W, H) * 0.45f);
        g2.setColor(new Color(255, 240, 160, 230));
        g2.fillOval(cx - ir, cy - ir / 2, ir * 2, ir);
        // Punta blanca al frente
        int tipR = (int) (3 * Juego.SCALE);
        int tipX = cx + (dir > 0 ? W / 2 - tipR : -W / 2);
        g2.setColor(new Color(255, 255, 255, 230));
        g2.fillOval(tipX, cy - tipR, tipR * 2, tipR * 2);

        // Borde sutil
        g2.setStroke(new BasicStroke(1f));
        g2.setColor(new Color(120, 60, 0, 200));
        g2.drawOval(cx - W / 2, cy - H / 2, W, H);

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            oldHint != null ? oldHint : RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    }

    public boolean isActiva()             { return activa; }
    public Rectangle2D.Float getHitbox()  { return hitbox; }
    public int getDamage()                { return damage; }
}
