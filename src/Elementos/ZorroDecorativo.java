package Elementos;

import Juegos.Juego;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Utilz.LoadSave;

/**
 * ZorroDecorativo — zorro que cruza el nivel una vez y desaparece.
 * Spritesheet: zorro.png  31 frames horizontales  296×130 px/frame.
 */
public class ZorroDecorativo {

    private BufferedImage[] frames;
    private static final int FRAME_COUNT = 31;
    private static final int FRAME_W_SRC = 296;
    private static final int FRAME_H_SRC = 130;

    // Un poco más pequeño que el venado (animal de primer plano pero compacto)
    private static final float FOX_SCALE = Juego.SCALE * 0.16f;
    private static final int   FRAME_W   = (int)(FRAME_W_SRC * FOX_SCALE);
    private static final int   FRAME_H   = (int)(FRAME_H_SRC * FOX_SCALE);

    // 60 ms/frame → ~4 ticks a 60fps
    private static final int TICK_PER_FRAME = 4;

    // El zorro corre más rápido que el venado
    private static final float SPEED = 2.6f * Juego.SCALE;

    private float worldX;
    private final float worldY;
    private final boolean movingRight;
    private final int levelWidthPx;
    private boolean active  = true;
    private boolean visible = true;

    private int animTick   = 0;
    private int animInd    = 0;
    private int spawnDelay;
    private boolean waiting;

    private static final float ALPHA = 0.95f;

    public ZorroDecorativo(float startX, float groundY, boolean movingRight,
                           int levelWidth, int delayTicks) {
        this.worldX      = startX;
        this.worldY      = groundY - FRAME_H;
        this.movingRight = movingRight;
        this.levelWidthPx = levelWidth;
        this.spawnDelay  = delayTicks;
        this.waiting     = (delayTicks > 0);
        loadFrames();
    }

    private void loadFrames() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas("zorro.png");
        if (sheet == null) { active = false; return; }
        frames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++)
            frames[i] = sheet.getSubimage(i * FRAME_W_SRC, 0, FRAME_W_SRC, FRAME_H_SRC);
    }

    public void update() {
        if (!active || !visible || frames == null) return;

        if (waiting) {
            if (--spawnDelay <= 0) waiting = false;
            return;
        }

        worldX += movingRight ? SPEED : -SPEED;

        // Desaparece al salir, no reaparece
        if (movingRight  && worldX > levelWidthPx + FRAME_W) visible = false;
        if (!movingRight && worldX < -FRAME_W)               visible = false;

        animTick++;
        if (animTick >= TICK_PER_FRAME) {
            animTick = 0;
            animInd  = (animInd + 1) % FRAME_COUNT;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active || !visible || waiting || frames == null) return;

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
