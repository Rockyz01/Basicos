package Elementos;

import Juegos.Juego;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * Flecha — proyectil disparado por el arquero (PlayerCharacter.p3).
 * Viaja horizontalmente a alta velocidad hasta impactar un tile o enemigo.
 */
public class Flecha {

    private float x, y;
    private int dir; // -1 izquierda, 1 derecha
    private boolean activa = true;
    private static final float SPEED = Juego.SCALE * 7f;
    private static final int W = (int) (18 * Juego.SCALE);
    private static final int H = (int) (4  * Juego.SCALE);
    private Rectangle2D.Float hitbox;
    private int damage;

    public Flecha(float x, float y, int dir, int damage) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.damage = damage;
        hitbox = new Rectangle2D.Float(x - W / 2f, y - H / 2f, W, H);
    }

    public void update(int[][] lvlData) {
        if (!activa) return;
        x += dir * SPEED;
        hitbox.x = x - W / 2f;

        // Salió del mapa
        if (x < 0 || x > lvlData[0].length * Juego.TILES_SIZE)
            activa = false;

        // Chocó con tile sólido
        if (Utilz.MetodoAyuda.isSolidoPublic(x, y, lvlData))
            activa = false;
    }

    /** Comprueba colisión con un rectángulo de hitbox de enemigo y aplica daño. */
    public boolean checkHit(Rectangle2D.Float enemyHitbox) {
        if (!activa) return false;
        if (hitbox.intersects(enemyHitbox)) {
            activa = false;
            return true;
        }
        return false;
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!activa) return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int ax = (int) (x - xLvlOffset);
        int ay = (int) y;
        int halfW = W / 2;

        // ── Sombra suave ──
        g2.setColor(new Color(0, 0, 0, 50));
        g2.setStroke(new BasicStroke(H * 0.8f));
        g2.drawLine(ax - dir * halfW + dir, ay + 2, ax + dir * halfW + dir, ay + 2);

        // ── Cuerpo del asta (madera) ──
        g2.setColor(new Color(160, 110, 55));
        g2.setStroke(new BasicStroke(H * 0.55f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.drawLine(ax - dir * halfW, ay, ax + dir * halfW, ay);

        // ── Punta metálica ──
        int tipX = ax + dir * halfW;
        int[] puntaX, puntaY;
        int tp = (int)(6 * Juego.SCALE);
        int th = (int)(4 * Juego.SCALE);
        if (dir == 1) {
            puntaX = new int[]{tipX, tipX + tp, tipX};
            puntaY = new int[]{ay - th/2, ay, ay + th/2};
        } else {
            puntaX = new int[]{tipX, tipX - tp, tipX};
            puntaY = new int[]{ay - th/2, ay, ay + th/2};
        }
        g2.setColor(new Color(200, 210, 220));
        g2.fillPolygon(puntaX, puntaY, 3);
        g2.setColor(new Color(140, 150, 160));
        g2.setStroke(new BasicStroke(1f));
        g2.drawPolygon(puntaX, puntaY, 3);

        // ── Plumas (empennage) ──
        int tailX = ax - dir * halfW;
        g2.setColor(new Color(80, 160, 80));
        g2.setStroke(new BasicStroke(H * 0.7f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int fLen = (int)(5 * Juego.SCALE);
        g2.drawLine(tailX, ay, tailX - dir * fLen, ay - (int)(3 * Juego.SCALE));
        g2.setColor(new Color(60, 140, 60));
        g2.drawLine(tailX, ay, tailX - dir * fLen, ay + (int)(3 * Juego.SCALE));

        g2.setStroke(new BasicStroke(1f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    }

    public boolean isActiva()              { return activa; }
    public Rectangle2D.Float getHitbox()   { return hitbox; }
    public int getDamage()                 { return damage; }
}
