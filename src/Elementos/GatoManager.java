package Elementos;

import Juegos.Juego;
import Objects.Spike;
import Utilz.MetodoAyuda;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

/**
 * GatoManager v5 — gato estático fijo cerca del spawn del jugador.
 *
 * El gato es puramente decorativo. SIEMPRE aparece en cada nivel, sentado cerca
 * del punto de aparición del jugador (un poco a la izquierda) sobre el suelo
 * más cercano, lejos de spikes. Si no encuentra sitio seguro, lo coloca a la
 * derecha. Si tampoco, lo omite.
 */
public class GatoManager {

    // tile value que corresponde a spike en lvlData (Constantes.ObjectConstants.SPIKE = 4)
    private static final int SPIKE_TILE = 4;

    private final ArrayList<GatoDecorativo> gatos = new ArrayList<>();

    private static final float SPIKE_CLEAR_RADIUS = Juego.TILES_SIZE * 2f;

    // Sprite del gato: 32×32 en origen → escalado por Juego.SCALE
    private static final int CAT_SRC_H = 32;

    public GatoManager() {}

    /** Llamar al cargar cada nivel. */
    public void onLevelLoad(int[][] lvlData, ArrayList<Spike> spikes, Point playerSpawn) {
        gatos.clear();
        if (lvlData == null) return;

        int spawnTileCol = (playerSpawn != null) ? playerSpawn.x / Juego.TILES_SIZE : 4;

        // Probar primero a la izquierda del spawn (3 tiles), luego a la derecha (3 tiles).
        int[] offsets = { -3, -2, -4, -1, 2, 3, 4, 1, -5, 5 };
        for (int off : offsets) {
            int col = spawnTileCol + off;
            if (col < 0 || col >= lvlData[0].length) continue;
            int floorRow = findFloorRow(col, lvlData);
            if (floorRow < 0) continue;
            // Que el suelo no sea un spike
            if (lvlData[floorRow][col] == SPIKE_TILE) continue;
            // Que no haya spike cerca
            float catX = col * Juego.TILES_SIZE;
            float catY = (floorRow * Juego.TILES_SIZE) - (int)(CAT_SRC_H * Juego.SCALE);
            if (spikes != null && isNearSpike(catX, catY, spikes)) continue;

            // El gato mira hacia el jugador
            boolean facingRight = (off < 0);
            gatos.add(new GatoDecorativo(catX, catY, facingRight));
            return;
        }
        // Si no encontró sitio cerca del spawn, no spawnea (raro, pero seguro).
    }

    public void update() {
        for (GatoDecorativo g : gatos) g.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        for (GatoDecorativo cat : gatos) cat.draw(g, xLvlOffset);
    }

    // ── Privados ─────────────────────────────────────────────────────────────

    /** Devuelve la fila del suelo más BAJO en la columna (con aire arriba). */
    private int findFloorRow(int col, int[][] lvlData) {
        int best = -1;
        for (int row = 1; row < lvlData.length; row++) {
            if (MetodoAyuda.IsTileSolid(col, row, lvlData)
                && !MetodoAyuda.IsTileSolid(col, row - 1, lvlData)) {
                best = row;
            }
        }
        return best;
    }

    private boolean isNearSpike(float sx, float sy, ArrayList<Spike> spikes) {
        for (Spike spike : spikes) {
            float dx = sx - spike.getHitbox().x;
            float dy = sy - spike.getHitbox().y;
            if (Math.sqrt(dx*dx + dy*dy) < SPIKE_CLEAR_RADIUS)
                return true;
        }
        return false;
    }
}
