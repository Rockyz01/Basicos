package Elementos;

import Juegos.Juego;
import Objects.Spike;
import Utilz.MetodoAyuda;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * AnimalesManager — venado, zorro y árboles decorativos del nivel 1.
 *
 * Árboles: colocados en columnas seguras del suelo principal (fila 10),
 * verificadas manualmente contra el mapa del nivel 1 (100x14 tiles).
 * Columnas usadas: 22, 55, 85 — todas tienen suelo en row=10 y al menos
 * 4 tiles de aire libre por encima, sin paredes adyacentes.
 *
 * Animales: entran desde fuera de pantalla y desaparecen al cruzar.
 */
public class AnimalesManager {

    private final ArrayList<VenadoDecorativo> venados = new ArrayList<>();
    private final ArrayList<ZorroDecorativo>  zorros  = new ArrayList<>();
    private final ArrayList<ArbolDecorativo>  arboles = new ArrayList<>();
    private final ArrayList<DecoracionMundo>  decosEstaticas = new ArrayList<>();
    private final PastoDecorativo pasto = new PastoDecorativo();

    /**
     * Decoraciones estáticas del nivel 1 (troncos caídos, rocas, lápidas, columnas).
     * Cada entrada: { sprite, col, row del SUELO, escala }.
     * Posiciones verificadas manualmente contra el mapa de tiles del nivel 1:
     *  - Todas en fila 10 (suelo principal, NO plataformas flotantes ni filas 5/7).
     *  - Ningún tile en cols extremas (0, 99) → no se cortan en los bordes.
     *  - No coinciden con árboles (TREE_COLS = {18, 85}).
     *
     * Escalas: 0.75 para todo excepto la columna (1.4) que necesita más presencia.
     *
     * Layout de izquierda a derecha por el nivel:
     *   col  4: tronco caído grande     (suelo A, cerca del spawn)
     *   col 22: estatua del monje       (suelo B, después del árbol col 18)
     *   col 55: rocas medianas          (suelo C)
     *   col 65: rocas grandes           (suelo D)
     *   col 70: estatua del caballero   (suelo D, final)
     *   col 83: tocón con huevos        (suelo E inicio, antes del árbol col 85)
     *   col 89: columna de madera       (suelo E, después del árbol — más grande)
     *   col 96: rocas medianas          (suelo E final, lejos del borde 99)
     */
    private static final Object[][] DECOS_NIVEL1 = {
        { "tronco_grande.png",     4,  10, 0.75f },
        { "estatua_monje.png",     22, 10, 0.75f },
        { "rocas_medianas.png",    55, 10, 0.75f },
        { "rocas_grandes.png",     65, 10, 0.75f },
        { "estatua_caballero.png", 70, 10, 0.75f },
        { "tocon_huevos.png",      83, 10, 0.75f },
        { "columna_madera.png",    89, 10, 1.4f  },
        { "rocas_medianas.png",    96, 10, 0.75f },
    };

    /**
     * Decoraciones del Nivel 3 — mundo de NIEVE (suelo principal en fila 11).
     * Sprites recortados de MUNDO_HELADO (árboles cerezo nevados, arbustos
     * de pino con nieve, hongos de nieve, cristales de hielo, arbustos
     * verdes nevados, arbustos rocosos).
     * Columnas validadas dinámicamente: distribuidas a lo largo del nivel
     * (150 tiles), con suelo sólido y aire libre encima.
     *
     * 13 decoraciones en total, mezclando tamaños y tipos para evitar
     * repetición visual y llenar los huecos largos del recorrido.
     */
    private static final Object[][] DECOS_NIVEL3 = {
        { "arbol_cerezo_nieve_L.png", 34,  11, 0.80f },
        { "arbusto_pino_M.png",       55,  11, 0.85f },
        { "arbusto_verde_S.png",      60,  11, 1.10f },  // hueco entre 55 y 71
        { "hongo_nieve_L.png",        71,  11, 0.75f },
        { "cristal_hielo_L.png",      81,  11, 0.85f },
        { "arbol_cerezo_nieve_M.png", 86,  11, 0.85f },  // hueco entre 81 y 91
        { "arbusto_verde_M.png",      91,  11, 0.85f },
        { "arbusto_pino_S.png",       96,  11, 1.10f },  // hueco entre 91 y 113
        { "arbusto_rocas_M.png",      113, 11, 0.85f },
        { "arbusto_pino_S.png",       123, 11, 1.00f },
        { "cristal_hielo_M.png",      130, 11, 0.90f },  // hueco entre 123 y 140
        { "hongo_nieve_M.png",        140, 11, 0.85f },
        { "arbusto_rocas_S.png",      146, 11, 1.10f },  // final del nivel
    };

    /**
     * Decoraciones del Nivel 4 — mundo de LAVA / FUEGO (suelo en fila 10).
     * Sprites recortados de MUNDO_FUEGO (últimas dos filas: estatuas,
     * estandartes, lanza con bandera, yunque, ruinas, lápida).
     * Mapa relativamente restringido: 6 columnas seguras distribuidas.
     */
    private static final Object[][] DECOS_NIVEL4 = {
        { "estatua_caballero_fuego.png", 2,  10, 0.85f },
        { "lanza_bandera.png",           18, 10, 0.85f },
        { "estandarte_rojo.png",         33, 10, 0.85f },
        { "yunque.png",                  43, 10, 1.20f },
        { "ruinas_piedra.png",           86, 10, 0.60f },
        { "lapida.png",                  96, 10, 1.20f },
    };

    /**
     * Decoraciones del Nivel 5 — mundo CIUDAD / CYBERPUNK (suelo en fila 10).
     * Sprites recortados de MUNDO_CIUDAD (mobiliario urbano).
     * Sprites grandes (≈190x250 px) → escalas pequeñas (0.40–0.50)
     * para que ocupen una proporción razonable del tile.
     */
    private static final Object[][] DECOS_NIVEL5 = {
        { "farola.png",       4,   10, 0.40f },
        { "basurero.png",     33,  10, 0.45f },
        { "caja_carton.png",  68,  10, 0.50f },
        { "cafe_grande.png",  78,  10, 0.45f },
        { "bicicleta.png",    121, 10, 0.45f },
        { "semaforo.png",     132, 10, 0.45f },
        { "edificio.png",     142, 10, 0.45f },
    };

    // Columnas del nivel 1 donde poner árboles (verificadas: suelo row=10, aire libre)
    // col 22 → cerca del spawn (col=2), visible al arrancar
    // col 55 → zona media del nivel
    // col 85 → zona final del nivel
    private static final int[] TREE_COLS = { 18, 85 };

    public AnimalesManager() {}

    public void onLevelLoad(int[][] lvlData, ArrayList<Spike> spikes,
                            int lvlOffsetMax, int levelIndex) {
        venados.clear();
        zorros.clear();
        arboles.clear();
        decosEstaticas.clear();

        if (lvlData == null) return;

        // ── Pasto en suelo de niveles 1 y 2 (levelIndex 0 y 1) ──────────────
        pasto.onLevelLoad(lvlData, spikes, levelIndex);

        // ── Árboles animados: solo en el nivel 1 (bosque) ───────────────────
        if (levelIndex == 0) {
            for (int i = 0; i < TREE_COLS.length; i++) {
                int col = TREE_COLS[i];
                if (col >= lvlData[0].length) continue;

                // Verificar en tiempo de ejecución que esa columna siga siendo segura
                int floorRow = findGroundRowAt(col, lvlData);
                if (floorRow < 0) continue;

                // Comprobar que las 2 filas encima del suelo estén libres (sin sólidos)
                boolean clear = true;
                for (int r = floorRow - 4; r < floorRow; r++) {
                    if (r < 0) continue;
                    if (MetodoAyuda.IsTileSolid(col, r, lvlData)) { clear = false; break; }
                    // También verificar col izquierda y derecha
                    if (col > 0 && MetodoAyuda.IsTileSolid(col - 1, r, lvlData)) { clear = false; break; }
                    if (col < lvlData[0].length - 1 && MetodoAyuda.IsTileSolid(col + 1, r, lvlData)) { clear = false; break; }
                }
                if (!clear) continue;

                // Sin spike debajo
                if (hasSpike(col, spikes)) continue;

                float tx = col * Juego.TILES_SIZE;
                float ty = floorRow * Juego.TILES_SIZE;
                arboles.add(new ArbolDecorativo(tx, ty, i * 2));
            }
        }

        // ── Decoraciones estáticas según el nivel ──────────────────────────
        // Cada nivel tiene su propia tabla temática. Los niveles sin tabla
        // (por ejemplo el nivel 2, bosque otoñal) no añaden decoración estática.
        Object[][] decos = null;
        switch (levelIndex) {
            case 0: decos = DECOS_NIVEL1; break;  // bosque azul
            case 2: decos = DECOS_NIVEL3; break;  // nieve
            case 3: decos = DECOS_NIVEL4; break;  // lava / fuego
            // Nivel 5 (ciudad): sin decoración estática (deshabilitado a petición del usuario)
            default: return;                      // niveles sin decoración estática
        }

        for (Object[] entry : decos) {
            String sprite = (String) entry[0];
            int    col    = (int)    entry[1];
            int    row    = (int)    entry[2];
            float  scale  = (float)  entry[3];

            // Salvaguarda runtime: confirmar que el tile destino sigue siendo
            // suelo válido (no plataforma flotante, no borde) en este nivel.
            if (col < 1 || col >= lvlData[0].length - 1) continue;
            if (row >= lvlData.length) continue;
            if (!MetodoAyuda.IsTileSolid(col, row, lvlData)) continue;
            if (row > 0 && MetodoAyuda.IsTileSolid(col, row - 1, lvlData)) continue;
            if (hasSpike(col, spikes)) continue;

            decosEstaticas.add(new DecoracionMundo(sprite, col, row, scale));
        }
    }

    public void update() {
        for (VenadoDecorativo v : venados) v.update();
        for (ZorroDecorativo  z : zorros)  z.update();
        for (ArbolDecorativo  a : arboles) a.update();
    }

    public void draw(Graphics g, int xLvlOffset) {
        pasto.draw(g, xLvlOffset);
        // Decoraciones estáticas detrás de árboles y animales para que el
        // jugador y los animales pasen por delante.
        for (DecoracionMundo  d : decosEstaticas) d.draw(g, xLvlOffset);
        for (ArbolDecorativo  a : arboles) a.draw(g, xLvlOffset);
        for (VenadoDecorativo v : venados) v.draw(g, xLvlOffset);
        for (ZorroDecorativo  z : zorros)  z.draw(g, xLvlOffset);
    }

    // ── Privados ─────────────────────────────────────────────────────────────

    private int findMainGroundY(int[][] lvlData) {
        int rows = lvlData.length;
        int cols = lvlData[0].length;
        int bestRow = rows - 2, bestCount = 0;
        for (int r = (int)(rows * 0.5); r < rows - 1; r++) {
            int count = 0;
            for (int c = 0; c < cols; c++)
                if (MetodoAyuda.IsTileSolid(c, r, lvlData)
                        && !MetodoAyuda.IsTileSolid(c, r - 1, lvlData))
                    count++;
            if (count > bestCount) { bestCount = count; bestRow = r; }
        }
        return bestRow * Juego.TILES_SIZE;
    }

    private int findGroundRowAt(int col, int[][] lvlData) {
        for (int r = lvlData.length - 1; r >= 1; r--)
            if (MetodoAyuda.IsTileSolid(col, r, lvlData)
                    && !MetodoAyuda.IsTileSolid(col, r - 1, lvlData))
                return r;
        return -1;
    }

    private boolean hasSpike(int col, ArrayList<Spike> spikes) {
        if (spikes == null) return false;
        float x = col * Juego.TILES_SIZE;
        for (Spike s : spikes)
            if (Math.abs(s.getHitbox().x - x) < Juego.TILES_SIZE * 2) return true;
        return false;
    }
}
