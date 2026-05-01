package Elementos;

import Juegos.Juego;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import Utilz.LoadSave;

/**
 * ArbolDecorativo — árbol mágico animado, decorado de fondo del nivel 1.
 *
 * Se coloca a una X fija en el mundo (coordenadas de nivel), justo delante
 * del spawn del jugador. Su Y se fija visualmente en el suelo del fondo,
 * NO en el suelo del tilemap (porque es decorado, no objeto físico).
 *
 * Spritesheet: arbol.png  7 frames horizontales  236×292 px/frame.
 */
public class ArbolDecorativo {

    private BufferedImage[] frames;
    private static final int FRAME_COUNT = 7;
    private static final int FRAME_W_SRC = 236;
    private static final int FRAME_H_SRC = 292;

    // Tamaño moderado: ~4-5 tiles de alto
    private static final float TREE_SCALE = Juego.SCALE * 0.68f;
    private static final int   FRAME_W    = (int)(FRAME_W_SRC * TREE_SCALE);
    private static final int   FRAME_H    = (int)(FRAME_H_SRC * TREE_SCALE);

    // 110 ms/frame → ~7 ticks a 60fps
    private static final int TICK_PER_FRAME = 7;

    // Posición en coordenadas de mundo
    private final float worldX;
    // Y calculada para que el pie del árbol toque el suelo visual del fondo
    private final float worldY;

    // Sin parallax: el árbol se mueve igual que el nivel (está "en" el nivel)
    // Esto evita que "vuele" respecto al suelo
    private static final float PARALLAX_FACTOR = 1.0f;

    private int animTick = 0;
    private int animInd;
    private boolean active = true;

    private static final float ALPHA = 0.88f;

    /**
     * @param worldX   X en coordenadas de mundo (tiles * TILES_SIZE).
     * @param groundY  Y del suelo en píxeles (tile del suelo * TILES_SIZE).
     *                 El árbol se dibuja con el pie aquí.
     * @param frameOff Offset inicial de animación para desincronizar árboles.
     */
    public ArbolDecorativo(float worldX, float groundY, int frameOff) {
        this.worldX  = worldX;
        this.worldY  = groundY - FRAME_H;
        this.animInd = frameOff % FRAME_COUNT;
        loadFrames();
    }

    private void loadFrames() {
        BufferedImage sheet = LoadSave.GetSpriteAtlas("arbol.png");
        if (sheet == null) { active = false; return; }
        frames = new BufferedImage[FRAME_COUNT];
        for (int i = 0; i < FRAME_COUNT; i++)
            frames[i] = sheet.getSubimage(i * FRAME_W_SRC, 0, FRAME_W_SRC, FRAME_H_SRC);
    }

    public void update() {
        if (!active || frames == null) return;
        if (++animTick >= TICK_PER_FRAME) {
            animTick = 0;
            animInd  = (animInd + 1) % FRAME_COUNT;
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active || frames == null) return;

        // PARALLAX_FACTOR = 1.0 → se mueve exactamente igual que el nivel
        int screenX = (int)(worldX - xLvlOffset * PARALLAX_FACTOR);
        int screenW = Juego.GAME_WIDTH;
        if (screenX > screenW + FRAME_W || screenX < -FRAME_W) return;

        Graphics2D g2d = (Graphics2D) g;
        Composite orig = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, ALPHA));
        g2d.drawImage(frames[animInd], screenX, (int) worldY, FRAME_W, FRAME_H, null);
        g2d.setComposite(orig);
    }

    public boolean isActive() { return active; }
}
