package Elementos;

import Juegos.Juego;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import Utilz.LoadSave;

/**
 * GatoDecorativo v4 — estático.
 *
 * El gato está sentado mirando al jugador como un decorado.
 * No camina, no colisiona, no interactúa. Cicla todos los frames
 * del idle de forma continua.
 *
 * Sprite: cat_sit.png  6 frames  32×32  fondo transparente.
 */
public class GatoDecorativo {

    // ── Sprite ───────────────────────────────────────────────────────────────
    private BufferedImage[] frames;
    private static final int FRAME_COUNT = 6;
    private static final int FRAME_W_SRC = 32;
    private static final int FRAME_H_SRC = 32;
    private static final int FRAME_W = (int)(FRAME_W_SRC * Juego.SCALE);
    private static final int FRAME_H = (int)(FRAME_H_SRC * Juego.SCALE);

    // ── Posición ─────────────────────────────────────────────────────────────
    private final float x, y;
    /** Si false, el gato se dibuja mirando al lado contrario (flip horizontal). */
    private final boolean facingRight;

    private boolean active = true;

    // ── Animación ────────────────────────────────────────────────────────────
    private int animTick = 0;
    private int animInd  = 0;
    /**
     * Duración de cada frame en ticks.
     * Todos los frames duran igual para que la animación cicle completa.
     */
    private static final int[] FRAME_DURATION = { 15, 15, 15, 15, 15, 15 };

    // ── Offset de pies ───────────────────────────────────────────────────────
    /** Píxeles extra hacia abajo para pegar el gato al suelo (scaled). */
    private static final int FOOT_OFFSET = (int)(6 * Juego.SCALE);

    // ── Transparencia ────────────────────────────────────────────────────────
    private static final float ALPHA = 0.65f;

    // ── Constructor ──────────────────────────────────────────────────────────
    public GatoDecorativo(float x, float y, boolean facingRight) {
        this.x = x;
        this.y = y;
        this.facingRight = facingRight;
        loadFrames();
    }

    private void loadFrames() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas("cat_sit.png");
        if (sheet == null) { active = false; return; }
        frames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++)
            frames[i] = sheet.getSubimage(i * FRAME_W_SRC, 0, FRAME_W_SRC, FRAME_H_SRC);
    }

    // ── Update ───────────────────────────────────────────────────────────────
    public void update() {
        if (!active || frames == null) return;
        animTick++;
        if (animTick >= FRAME_DURATION[animInd]) {
            animTick = 0;
            animInd = (animInd + 1) % FRAME_COUNT;
        }
    }

    // ── Draw ─────────────────────────────────────────────────────────────────
    public void draw(Graphics g, int xLvlOffset) {
        if (!active || frames == null) return;
        Graphics2D g2d = (Graphics2D) g;
        Composite original = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));

        int drawX = (int) x - xLvlOffset;
        int drawY = (int) y + FOOT_OFFSET;
        BufferedImage frame = frames[animInd];

        if (facingRight)
            g2d.drawImage(frame, drawX, drawY, FRAME_W, FRAME_H, null);
        else
            g2d.drawImage(frame, drawX + FRAME_W, drawY, -FRAME_W, FRAME_H, null);

        g2d.setComposite(original);
    }

    // ── Getters ──────────────────────────────────────────────────────────────
    public boolean isActive() { return active; }
    public float   getX()     { return x; }
    public float   getY()     { return y; }
}
