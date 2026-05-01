package Elementos;

import Juegos.Juego;
import Objects.Spike;
import Utilz.LoadSave;
import Utilz.MetodoAyuda;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * PastoDecorativo — dibuja el sprite de pasto como franjas continuas
 * sobre segmentos horizontales de suelo, usando TILEADO (repetición)
 * para preservar el aspecto pixel-art sin deformar el sprite.
 */
public class PastoDecorativo {

    private static final String SPRITE = "pasto.png";
    private static final int FLOOR_ROW_THRESHOLD = 6;

    // Recorte horizontal del sprite: el PNG tiene columnas vacías en los bordes
    // (8px a la izquierda y 7px a la derecha). Si lo tileamos completo, esos huecos
    // se duplican y dejan separaciones visibles entre cada repetición. Usamos solo
    // la franja útil para que las repeticiones queden pegadas.
    private static final int SRC_X_START   = 8;
    private static final int SRC_USEFUL_W  = 113;   // de x=8 a x=120 inclusive
    // Reducción del grosor: el sprite es alto (32px) y se ve robusto al escalarlo.
    // Lo dibujamos al 60% de la altura natural para que se vea más sutil.
    private static final float THICKNESS_SCALE = 0.6f;

    private static BufferedImage pastoImg = null;
    private static boolean loaded = false;

    // Cada segmento: { worldX_inicio, worldX_fin, worldY_superficie }
    private final ArrayList<int[]> segments = new ArrayList<>();

    private int tileW, tileH;
    private boolean active = false;

    public PastoDecorativo() {}

    public void onLevelLoad(int[][] lvlData, ArrayList<Spike> spikes, int levelIndex) {
        segments.clear();
        active = false;

        if (levelIndex != 0 && levelIndex != 1) return;
        if (lvlData == null) return;

        loadSprite();
        if (pastoImg == null) return;

        int ts   = Juego.TILES_SIZE;
        // tileW se basa en el ancho ÚTIL (sin los bordes vacíos del PNG) para
        // que las repeticiones queden pegadas, sin huecos entre cada manojo.
        tileW = (int)(SRC_USEFUL_W * Juego.SCALE);
        tileH = (int)(pastoImg.getHeight() * Juego.SCALE * THICKNESS_SCALE);

        int rows = lvlData.length;
        int cols = lvlData[0].length;

        for (int r = 1; r < rows; r++) {
            int runStart = -1;
            for (int c = 0; c <= cols; c++) {
                boolean valid = false;
                if (c < cols) {
                    valid = MetodoAyuda.IsTileSolid(c, r, lvlData)
                         && !MetodoAyuda.IsTileSolid(c, r - 1, lvlData)
                         && r >= FLOOR_ROW_THRESHOLD
                         && !hasSpikeAt(c, r, spikes, ts);
                }
                if (valid && runStart < 0) {
                    runStart = c;
                } else if (!valid && runStart >= 0) {
                    segments.add(new int[]{runStart * ts, c * ts, r * ts});
                    runStart = -1;
                }
            }
        }

        active = !segments.isEmpty();
    }

    private void loadSprite() {
        if (!loaded) {
            pastoImg = LoadSave.GetSpriteAtlas(SPRITE);
            loaded   = (pastoImg != null);
        }
    }

    public void draw(Graphics g, int xLvlOffset) {
        if (!active || pastoImg == null) return;

        Graphics2D g2d  = (Graphics2D) g;
        Composite  orig = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        int screenW = Juego.GAME_WIDTH;

        for (int[] seg : segments) {
            int wx0   = seg[0];
            int wx1   = seg[1];
            int wy    = seg[2];
            int drawY = wy - tileH;

            int sx0 = wx0 - xLvlOffset;
            int sx1 = wx1 - xLvlOffset;

            if (sx1 < -tileW || sx0 > screenW + tileW) continue;

            // Tilear el sprite a lo largo del segmento
            int x = sx0;
            while (x < sx1) {
                int drawW = Math.min(tileW, sx1 - x);
                int dstX  = x;
                if (dstX < -tileW) { x += tileW; continue; }
                if (dstX > screenW) break;

                // Si drawW < tileW, recortar el sprite proporcionalmente,
                // siempre dentro de la franja útil [SRC_X_START, SRC_X_START + SRC_USEFUL_W).
                int srcW = (int)((float)drawW / tileW * SRC_USEFUL_W);

                g2d.drawImage(pastoImg,
                    dstX, drawY, dstX + drawW, drawY + tileH,
                    SRC_X_START, 0, SRC_X_START + srcW, pastoImg.getHeight(),
                    null);

                x += tileW;
            }
        }

        g2d.setComposite(orig);
    }

    private boolean hasSpikeAt(int col, int row, ArrayList<Spike> spikes, int ts) {
        if (spikes == null) return false;
        float x = col * ts;
        float y = row * ts;
        for (Spike s : spikes) {
            if (Math.abs(s.getHitbox().x - x) < ts
             && Math.abs(s.getHitbox().y - y) < ts * 2) return true;
        }
        return false;
    }
}
