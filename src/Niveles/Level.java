package Niveles;

import Elementos.*;
import Juegos.Juego;
import Objects.Armor;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike;
import Objects.Weapon;
import Utilz.MetodoAyuda;
import static Utilz.MetodoAyuda.GetGolems;
import static Utilz.MetodoAyuda.GetLevelData;
import static Utilz.MetodoAyuda.GetPlayerSpawn;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private ArrayList<Golem> golems;
    private ArrayList<Cannon> cannons;
    private ArrayList<skeletonW> Skeletonw;
    private ArrayList<skeletonY> skeletony;
    private ArrayList<enemigo1> enemigos1;
    private ArrayList<enemigo2> enemigos2;
    private ArrayList<enemigo3> enemigos3;
    private ArrayList<enemigo4> enemigos4;
    private ArrayList<enemigo5> enemigos5;
    private ArrayList<enemigo6> enemigos6;
    private ArrayList<enemigo7> enemigos7;
    private ArrayList<enemigo8> enemigos8;
    private ArrayList<enemigo9> enemigos9;
    private ArrayList<enemigo10> enemigos10;
    private ArrayList<enemigo11> enemigos11;
    private ArrayList<enemigo12> enemigos12;
    private ArrayList<TrolJefe>  trolJefes;
<<<<<<< HEAD
    private ArrayList<BossAncient>   bossAncients;
    private ArrayList<BossViking>    bossVikings;
    private ArrayList<BossToadKing>  bossToadKings;
    private ArrayList<enemigo13> enemigos13;
    private ArrayList<enemigo14> enemigos14;
    private ArrayList<enemigo15> enemigos15;
=======
>>>>>>> 2c7cdeebd7e2c98430828ddceff33cfb310a32da

    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Weapon> weapons;
    private ArrayList<Armor> armors;

    private BufferedImage img;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        createCannons();
        createWeapons();
        createArmors();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createCannons() { cannons = MetodoAyuda.GetCannons(img); }
    private void createSpikes() { spikes = MetodoAyuda.getSpikes(img); }
    private void createContainers() { containers = MetodoAyuda.GetContainers(img); }
    private void createPotions() { potions = MetodoAyuda.GetPotions(img); }
    private void createWeapons() { weapons = MetodoAyuda.GetWeapons(img); }
    private void createArmors() { armors = MetodoAyuda.GetArmors(img); }
    private void calcPlayerSpawn() { playerSpawn = GetPlayerSpawn(img); }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Juego.TILES_WIDTH;
        maxLvlOffsetX = Juego.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        golems = GetGolems(img);
        Skeletonw = MetodoAyuda.GetSkeletonW(img);
        skeletony = MetodoAyuda.GetSkeletonY(img);
        enemigos1 = MetodoAyuda.GetEnemigo1(img);
        enemigos2 = MetodoAyuda.GetEnemigo2(img);
        enemigos3 = MetodoAyuda.GetEnemigo3(img);
        enemigos4 = MetodoAyuda.GetEnemigo4(img);
        enemigos5 = MetodoAyuda.GetEnemigo5(img);
        enemigos6 = MetodoAyuda.GetEnemigo6(img);
        enemigos7 = MetodoAyuda.GetEnemigo7(img);
        enemigos8 = MetodoAyuda.GetEnemigo8(img);
        enemigos9 = MetodoAyuda.GetEnemigo9(img);
        enemigos10 = MetodoAyuda.GetEnemigo10(img);
        enemigos11 = MetodoAyuda.GetEnemigo11(img);
        enemigos12 = MetodoAyuda.GetEnemigo12(img);
<<<<<<< HEAD
        enemigos13 = MetodoAyuda.GetEnemigo13(img);
        enemigos14 = MetodoAyuda.GetEnemigo14(img);
        enemigos15 = MetodoAyuda.GetEnemigo15(img);
        trolJefes  = MetodoAyuda.GetTrolJefes(img);
        bossAncients  = MetodoAyuda.GetBossAncients(img);
        bossVikings   = MetodoAyuda.GetBossVikings(img);
        bossToadKings = MetodoAyuda.GetBossToadKings(img);
=======
        trolJefes  = MetodoAyuda.GetTrolJefes(img);
>>>>>>> 2c7cdeebd7e2c98430828ddceff33cfb310a32da
    }

    private void createLevelData() { lvlData = GetLevelData(img); }

    public int getSpriteIndex(int x, int y) { return lvlData[y][x]; }
    public int[][] getLvlData() { return lvlData; }
    public void setLvlData(int[][] lvlData) { this.lvlData = lvlData; }
    public int getLvlOffset() { return maxLvlOffsetX; }
    public ArrayList<Golem> getGolems() { return golems; }
    public Point getPlayerSpawn() { return playerSpawn; }
    public ArrayList<Potion> getPotions() { return potions; }
    public ArrayList<GameContainer> getContainers() { return containers; }
    public ArrayList<Spike> getSpikes() { return spikes; }
    public ArrayList<Cannon> getCannons() { return cannons; }
    public ArrayList<Weapon> getWeapons() { return weapons; }
    public ArrayList<Armor> getArmors() { return armors; }
    public ArrayList<skeletonW> getSkeletonw() { return Skeletonw; }
    public ArrayList<skeletonY> getSkeletony() { return skeletony; }
    public ArrayList<enemigo1> getEnemigo1() { return enemigos1; }
    public ArrayList<enemigo2> getEnemigo2() { return enemigos2; }
    public ArrayList<enemigo3> getEnemigo3() { return enemigos3; }
    public ArrayList<enemigo4> getEnemigo4() { return enemigos4; }
    public ArrayList<enemigo5> getEnemigo5() { return enemigos5; }
    public ArrayList<enemigo6> getEnemigo6() { return enemigos6; }
    public ArrayList<enemigo7> getEnemigo7() { return enemigos7; }
    public ArrayList<enemigo8> getEnemigo8() { return enemigos8; }
    public ArrayList<enemigo9> getEnemigo9() { return enemigos9; }
    public ArrayList<enemigo10> getEnemigo10() { return enemigos10; }
    public ArrayList<enemigo11> getEnemigo11() { return enemigos11; }
    public ArrayList<enemigo12> getEnemigo12() { return enemigos12; }
<<<<<<< HEAD
    public ArrayList<enemigo13> getEnemigo13() { return enemigos13; }
    public ArrayList<enemigo14> getEnemigo14() { return enemigos14; }
    public ArrayList<enemigo15> getEnemigo15() { return enemigos15; }
    public ArrayList<TrolJefe>  getTrolJefes()  { return trolJefes; }
    public ArrayList<BossAncient>   getBossAncients()  { return bossAncients; }
    public ArrayList<BossViking>    getBossVikings()   { return bossVikings; }
    public ArrayList<BossToadKing>  getBossToadKings() { return bossToadKings; }

    // ─────────────────────────────────────────────────────────────────────────
    // Helpers de testing: vaciar enemigos/bosses o dejar solo 1 de cada tipo.
    // Se usan desde LevelManager para tener un primer nivel "de prueba".
    // ─────────────────────────────────────────────────────────────────────────

    /** Borra TODOS los enemigos y bosses de este nivel. */
    public void clearAllEnemiesAndBosses() {
        if (golems     != null) golems.clear();
        if (Skeletonw  != null) Skeletonw.clear();
        if (skeletony  != null) skeletony.clear();
        if (enemigos1  != null) enemigos1.clear();
        if (enemigos2  != null) enemigos2.clear();
        if (enemigos3  != null) enemigos3.clear();
        if (enemigos4  != null) enemigos4.clear();
        if (enemigos5  != null) enemigos5.clear();
        if (enemigos6  != null) enemigos6.clear();
        if (enemigos7  != null) enemigos7.clear();
        if (enemigos8  != null) enemigos8.clear();
        if (enemigos9  != null) enemigos9.clear();
        if (enemigos10 != null) enemigos10.clear();
        if (enemigos11 != null) enemigos11.clear();
        if (enemigos12 != null) enemigos12.clear();
        if (enemigos13 != null) enemigos13.clear();
        if (enemigos14 != null) enemigos14.clear();
        if (enemigos15 != null) enemigos15.clear();
        if (trolJefes     != null) trolJefes.clear();
        if (bossAncients  != null) bossAncients.clear();
        if (bossVikings   != null) bossVikings.clear();
        if (bossToadKings != null) bossToadKings.clear();
    }

    /**
     * Modo prueba: vacía TODO y luego inyecta una instancia de cada uno de los 15 enemigos
     * y de cada uno de los bosses, distribuidos a lo largo del nivel sobre suelo sólido.
     * Útil para ver cómo se ven todos los sprites en un solo nivel.
     */
    public void keepOneOfEachEnemyAndBoss() {
        clearAllEnemiesAndBosses();
        if (lvlData == null) return;

        // Lista de "factories" — un creador por cada tipo. El orden importa: así
        // los enemigos van apareciendo de izquierda a derecha en este orden.
        java.util.function.BiConsumer<Float, Float>[] spawners = new java.util.function.BiConsumer[] {
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> golems.add(new Golem(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> Skeletonw.add(new skeletonW(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> skeletony.add(new skeletonY(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos1.add(new enemigo1(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos2.add(new enemigo2(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos3.add(new enemigo3(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos4.add(new enemigo4(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos5.add(new enemigo5(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos6.add(new enemigo6(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos7.add(new enemigo7(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos8.add(new enemigo8(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos9.add(new enemigo9(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos10.add(new enemigo10(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos11.add(new enemigo11(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos12.add(new enemigo12(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos13.add(new enemigo13(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos14.add(new enemigo14(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> enemigos15.add(new enemigo15(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> trolJefes.add(new TrolJefe(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> bossAncients.add(new BossAncient(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> bossVikings.add(new BossViking(x, y)),
            (java.util.function.BiConsumer<Float, Float>) (x, y) -> bossToadKings.add(new BossToadKing(x, y)),
        };

        // Encontrar posiciones de suelo distribuidas a lo largo del nivel
        int[] cols = findSpreadFloorColumns(spawners.length);
        for (int i = 0; i < spawners.length; i++) {
            int col = cols[i];
            int floorRow = findFloorRow(col);
            if (floorRow < 0) continue;
            // Coloca el enemigo dos tiles arriba del suelo para que caiga limpio
            float spawnX = col * Juegos.Juego.TILES_SIZE;
            float spawnY = (floorRow - 2) * Juegos.Juego.TILES_SIZE;
            spawners[i].accept(spawnX, spawnY);
        }
    }

    /** Devuelve N columnas distribuidas a lo ancho del mapa que tengan suelo. */
    private int[] findSpreadFloorColumns(int n) {
        int width = lvlData[0].length;
        // Saltar las primeras 4 columnas (donde suele estar el spawn del jugador)
        int startCol = 4;
        int usable = width - startCol - 2;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            // Distribución uniforme de columnas candidatas
            int target = startCol + (int) ((i + 0.5) * usable / n);
            // Buscar la columna más cercana que tenga suelo
            int col = findNearestColumnWithFloor(target);
            result[i] = col;
        }
        return result;
    }

    /** Busca alrededor de targetCol una columna cuyo suelo esté en la mitad inferior del mapa.
     *  Si no la encuentra, acepta cualquier columna con suelo. */
    private int findNearestColumnWithFloor(int targetCol) {
        int width = lvlData[0].length;
        int height = lvlData.length;
        int lowThreshold = (int)(height * 0.65); // queremos floors en row >= 65% del alto
        int fallback = -1;
        for (int delta = 0; delta < width; delta++) {
            for (int sign : new int[]{1, -1}) {
                int col = targetCol + delta * sign;
                if (col < 0 || col >= width) continue;
                int floor = findFloorRow(col);
                if (floor < 0) continue;
                if (floor >= lowThreshold) return col;
                if (fallback < 0) fallback = col;
            }
        }
        return (fallback >= 0) ? fallback : Math.max(0, Math.min(width - 1, targetCol));
    }

    /**
     * Devuelve la fila del suelo más BAJO en la columna que tenga al menos 3 tiles
     * de aire arriba (para evitar plataformas pequeñas/techos donde el enemigo
     * quedaría apretado). Si no hay ninguno con esa holgura, relaja el criterio.
     */
    private int findFloorRow(int col) {
        if (lvlData == null) return -1;
        if (col < 0 || col >= lvlData[0].length) return -1;

        int bestSpacious = -1; // con 3 tiles de aire arriba
        int bestAny      = -1; // cualquiera con aire encima
        for (int row = 1; row < lvlData.length; row++) {
            if (!Utilz.MetodoAyuda.IsTileSolid(col, row, lvlData)) continue;
            if (Utilz.MetodoAyuda.IsTileSolid(col, row - 1, lvlData)) continue;
            // tile sólido con aire inmediatamente arriba — candidato
            bestAny = row;
            // ¿tiene 3 tiles de aire arriba?
            boolean spacious = true;
            for (int up = 1; up <= 3 && spacious; up++) {
                if (row - up < 0) { spacious = false; break; }
                if (Utilz.MetodoAyuda.IsTileSolid(col, row - up, lvlData)) spacious = false;
            }
            if (spacious) bestSpacious = row;
        }
        return (bestSpacious >= 0) ? bestSpacious : bestAny;
    }
=======
    public ArrayList<TrolJefe>  getTrolJefes()  { return trolJefes; }
>>>>>>> 2c7cdeebd7e2c98430828ddceff33cfb310a32da
}