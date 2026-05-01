package Elementos;

import Juegos.Juego;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;

/**
 * BolaDeFuego — proyectil mágico disparado por el mago (PlayerCharacter.p4).
 * Viaja horizontalmente con una pequeña oscilación sinusoidal y deja rastro de llamas.
 */
public class BolaDeFuego {

    private float x, y;
    private float baseY;
    private int dir;          // -1 izquierda, 1 derecha
    private boolean activa = true;
    private static final float SPEED = Juego.SCALE * 5.5f;
    private static final int W = (int) (16 * Juego.SCALE);
    private static final int H = (int) (16 * Juego.SCALE);
    private Rectangle2D.Float hitbox;
    private int damage;
    private int tick = 0;

    // Partículas de rastro
    private float[] trailX = new float[12];
    private float[] trailY = new float[12];
    private int trailHead = 0;
    private boolean trailFull = false;

    public BolaDeFuego(float x, float y, int dir, int damage) {
        this.x = x;
        this.y = y;
        this.baseY = y;
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
        // Leve oscilación sinusoidal (efecto mágico)
        y = baseY + (float) Math.sin(tick * 0.25) * 4 * Juego.SCALE;

        hitbox.x = x - W / 2f;
        hitbox.y = y - H / 2f;

        if (x < 0 || x > lvlData[0].length * Juego.TILES_SIZE)
            activa = false;

        if (Utilz.MetodoAyuda.isSolidoPublic(x, y, lvlData))
            activa = false;
    }

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

        int r = W / 2;
        int cx = (int) (x - xLvlOffset);
        int cy = (int) y;

        // ── Rastro de llamas ──
        int trailLen = trailFull ? trailX.length : trailHead;
        for (int i = 0; i < trailLen; i++) {
            int idx = (trailHead - 1 - i + trailX.length) % trailX.length;
            float age = (i + 1f) / trailLen;  // 0=reciente, 1=viejo
            int alpha = (int) ((1f - age) * 160);
            float sz = r * (1f - age * 0.7f);
            int tx = (int) (trailX[idx] - xLvlOffset);
            int ty = (int) trailY[idx];
            // Color degradé naranja→rojo→transparente
            Color tc = new Color(255, (int)(80 * (1 - age)), 0, alpha);
            g2.setColor(tc);
            g2.fillOval((int)(tx - sz), (int)(ty - sz), (int)(sz*2), (int)(sz*2));
        }

        // ── Sombra ──
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval(cx - r, cy - r + 2, r * 2, r * 2);

        // ── Núcleo exterior naranja ──
        g2.setColor(new Color(255, 140, 0, 230));
        g2.fillOval(cx - r, cy - r, r * 2, r * 2);

        // ── Núcleo interior amarillo brillante ──
        int ir = (int)(r * 0.6f);
        g2.setColor(new Color(255, 255, 100, 220));
        g2.fillOval(cx - ir, cy - ir, ir * 2, ir * 2);

        // ── Destello central blanco ──
        int wr = (int)(r * 0.25f);
        g2.setColor(new Color(255, 255, 255, 200));
        g2.fillOval(cx - wr - 1, cy - wr - 1, wr * 2, wr * 2);

        // ── Llamas superiores (picos) ──
        g2.setStroke(new BasicStroke(2f * Juego.SCALE, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int fh = (int)(r * 0.9f);
        g2.setColor(new Color(255, 80, 0, 180));
        g2.drawLine(cx, cy - r, cx, cy - r - fh);
        g2.drawLine(cx - r/2, cy - r + 2, cx - r/2 - 1, cy - r - fh/2);
        g2.drawLine(cx + r/2, cy - r + 2, cx + r/2 + 1, cy - r - fh/2);

        g2.setStroke(new BasicStroke(1f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
    }

    public boolean isActiva()              { return activa; }
    public Rectangle2D.Float getHitbox()   { return hitbox; }
    public int getDamage()                 { return damage; }
}
