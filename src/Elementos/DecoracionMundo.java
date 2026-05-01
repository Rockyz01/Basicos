package Elementos;

import Juegos.Juego;
import Utilz.LoadSave;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * DecoracionMundo — sprite estático anclado al mundo (con scroll de cámara),
 * dibujado con su pie tocando el suelo del tile indicado.
 *
 * Pensado para troncos caídos, rocas, lápidas, columnas, etc.
 * NO interactúa físicamente con el jugador (es solo decorado de fondo).
 *
 * Patrón de uso:
 *   DecoracionMundo d = new DecoracionMundo("rocas_grandes.png", col, row, 1.4f);
 *   d.update();   // no hace nada (estático), pero la API es consistente
 *   d.draw(g, xLvlOffset);
 *
 * El sprite se ancla por su esquina inferior-izquierda al pixel (col*TILES_SIZE,
 * (row+1)*TILES_SIZE), de forma que el pie queda apoyado sobre la superficie
 * del tile sólido en (col, row).
 */
public class DecoracionMundo {

    private final String sprite;
    private final float worldX;     // esquina superior-izquierda en mundo
    private final float worldY;
    private final int   drawW;
    private final int   drawH;

    private BufferedImage img;
    private boolean active = false;

    /**
     * @param spriteFile nombre del PNG en /recursos (ej. "tronco_grande.png")
     * @param col        columna del tile del SUELO sobre el que apoya la deco
     * @param row        fila del tile del suelo (la deco se dibuja sobre el borde superior de este tile)
     * @param scale      escala adicional al SCALE global (1.0 = tamaño natural)
     */
    public DecoracionMundo(String spriteFile, int col, int row, float scale) {
        this.sprite = spriteFile;
        this.img    = LoadSave.GetSpriteAtlas(spriteFile);
        if (this.img == null) {
            this.worldX = 0; this.worldY = 0; this.drawW = 0; this.drawH = 0;
            return;
        }
        this.active = true;

        float s = Juego.SCALE * scale;
        this.drawW = (int)(img.getWidth()  * s);
        this.drawH = (int)(img.getHeight() * s);

        // Detectar cuántos pixeles transparentes hay debajo del último pixel
        // opaco del PNG. Sin esto, los recortes con padding inferior parecen
        // "flotar" unos pixeles sobre el piso. Compensamos bajando el sprite
        // exactamente esa cantidad (escalada).
        int bottomPadding = computeBottomPadding(img);
        int bottomPaddingScaled = (int)(bottomPadding * s);

        int ts = Juego.TILES_SIZE;
        // Centrar horizontalmente en la columna y apoyar el último pixel opaco
        // del sprite sobre la cara superior del tile sólido.
        int tileLeftX = col * ts;
        int tileTopY  = row * ts;
        this.worldX = tileLeftX + (ts - drawW) / 2f;
        this.worldY = tileTopY - drawH + bottomPaddingScaled;
    }

    /** Cuenta filas transparentes debajo del último pixel opaco del PNG. */
    private static int computeBottomPadding(BufferedImage img) {
        int w = img.getWidth();
        int h = img.getHeight();
        for (int y = h - 1; y >= 0; y--) {
            for (int x = 0; x < w; x++) {
                if (((img.getRGB(x, y) >> 24) & 0xff) > 0) {
                    return h - 1 - y;
                }
            }
        }
        return 0;
    }

    /** Constructor con scale = 1.0f */
    public DecoracionMundo(String spriteFile, int col, int row) {
        this(spriteFile, col, row, 1f);
    }

    public void update() { /* estático */ }

    public void draw(java.awt.Graphics g, int xLvlOffset) {
        if (!active || img == null) return;
        int sx = (int)(worldX - xLvlOffset);
        if (sx > Juego.GAME_WIDTH || sx + drawW < 0) return;
        g.drawImage(img, sx, (int) worldY, drawW, drawH, null);
    }

    public boolean isActive() { return active; }
    public String  getSprite() { return sprite; }
}
