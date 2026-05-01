package Elementos;

import Juegos.Juego;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Utilz.LoadSave;

/**
 * VenadoDecorativo — venado que cruza el nivel una vez y desaparece.
 * Spritesheet: venado.png  10 frames horizontales  256×148 px/frame.
 */
public class VenadoDecorativo {

    private BufferedImage[] frames;
    private static final int FRAME_COUNT = 10;
    private static final int FRAME_W_SRC = 256;
    private static final int FRAME_H_SRC = 148;

    // Tamaño en pantalla: ~2.5 tiles de alto (~3 tiles de largo)
    private static final float DEER_SCALE = Juego.SCALE * 0.18f;
    private static final int   FRAME_W    = (int)(FRAME_W_SRC * DEER_SCALE);
    private static final int   FRAME_H    = (int)(FRAME_H_SRC * DEER_SCALE);

    // 70 ms/frame en GIF → ~4 ticks a 60fps
    private static final int TICK_PER_FRAME = 4;

    // Velocidad: cruza la pantalla en ~6 segundos
    private static final float SPEED = 1.8f * Juego.SCALE;

    private float worldX;
    private final float worldY;
    private final boolean movingRight;
    private final int levelWidthPx;
    private boolean active = true;
    private boolean visible = true; // false cuando sale de pantalla → no reaparece

    private int animTick = 0;
    private int animInd  = 0;

    private static final float ALPHA = 0.90f;

    public VenadoDecorativo(float startX, float groundY, boolean movingRight, int levelWidth) {
        this.worldX      = startX;
        this.worldY      = groundY - FRAME_H;
        this.movingRight = movingRight;
        this.levelWidthPx = levelWidth;
        loadFrames();
    }

    private void loadFrames() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas("venado.png");
        if (sheet == null) { active = false; return; }
        frames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++)
            frames[i] = sheet.getSubimage(i * FRAME_W_SRC, 0, FRAME_W_SRC, FRAME_H_SRC);
    }

    public void update() {
        if (!active || !visible || frames == null) return;

        worldX += movingRight ? SPEED : -SPEED;

        // Desaparece al salir del nivel, no reaparece
        if (movingRight  && worldX > levelWidthPx + FRAME_W) visible = false;
        if (!movingRight && worldX < -FRAME_W)               visible = false;

        animTick++;
        if (animTick >= TICK_PER_FRAME) {
            animTick = 0;
            animInd  = (animInd + 1) % FRAME_COUNT;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active || !visible || frames == null) return;

        int screenX = (int) worldX - xLvlOffset;
        int screenW = Juego.GAME_WIDTH;
        if (screenX > screenW || screenX < -FRAME_W) return;

        Graphics2D g2d = (Graphics2D) g;
        Composite orig = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));

        if (movingRight)
            g2d.drawImage(frames[animInd], screenX, (int) worldY, FRAME_W, FRAME_H, null);
        else
            g2d.drawImage(frames[animInd], screenX + FRAME_W, (int) worldY, -FRAME_W, FRAME_H, null);

        g2d.setComposite(orig);
    }

    public boolean isActive() { return active && visible; }
}
