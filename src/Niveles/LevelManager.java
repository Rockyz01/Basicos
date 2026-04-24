package Niveles;

import Juegos.Juego;
import Utilz.LoadSave;
import gamestates.Gamestate;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class LevelManager {

    private Juego game;
    private BufferedImage[][] levelSprites;
    private ArrayList<Level> levels;
    private int lvlIndex = 0;
    private Set<Integer> completedLevels;

    public LevelManager(Juego game) {
        this.game = game;
        importOutsideSprites();
        levels = new ArrayList<>();
        buildAllLevels();
        completedLevels = new HashSet<>();
    }

    private void importOutsideSprites() {
        // Orden: bosque azul, bosque otoñal, nieve, lava, cyberpunk
        BufferedImage img1 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);   // bosque azul
        BufferedImage img2 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS2);  // bosque otoñal
        BufferedImage img3 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS3);  // nieve
        BufferedImage img4 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS4);  // lava
        BufferedImage img5 = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS5);  // cyberpunk

        levelSprites = new BufferedImage[5][];
        levelSprites[0] = loadTilesFromAtlas(img1);
        levelSprites[1] = loadTilesFromAtlas(img2);
        levelSprites[2] = loadTilesFromAtlas(img3);
        levelSprites[3] = loadTilesFromAtlas(img4);
        levelSprites[4] = loadTilesFromAtlas(img5);

        for (int i = 0; i < levelSprites.length; i++)
            System.out.println("Atlas " + (i+1) + ": " + levelSprites[i].length + " tiles");
    }

    private BufferedImage[] loadTilesFromAtlas(BufferedImage atlas) {
        if (atlas == null) return new BufferedImage[0];
        final int TILE_SIZE = 32;
        int cols = atlas.getWidth() / TILE_SIZE;
        int rows = atlas.getHeight() / TILE_SIZE;
        BufferedImage[] tiles = new BufferedImage[cols * rows];
        for (int j = 0; j < rows; j++)
            for (int i = 0; i < cols; i++)
                tiles[j * cols + i] = atlas.getSubimage(i * TILE_SIZE, j * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        return tiles;
    }

    private void buildAllLevels() {
        BufferedImage[] allLevels = LoadSave.GetAllLevels();
        for (BufferedImage img : allLevels)
            levels.add(new Level(img));
    }

    public void loadNextLevel(int indexOverride) {
        if (!(indexOverride >= 0 && indexOverride < levels.size())) {
            completedLevels.add(lvlIndex);
            lvlIndex++;
            if (lvlIndex >= levels.size()) {
                if (completedLevels.size() >= levels.size()) {
                    lvlIndex = 0;
                    System.out.println("¡Juego completo!");
                    Gamestate.state = Gamestate.MENU;
                    return;
                }
                for (int i = 0; i < levels.size(); i++) {
                    if (!completedLevels.contains(i)) {
                        lvlIndex = i;
                        break;
                    }
                }
            }
        } else {
            lvlIndex = indexOverride;
        }

        if (lvlIndex >= levels.size()) return;

        Level newLevel = levels.get(lvlIndex);
        game.getPlaying().getEnemyManager().loadEnemies(newLevel);
        game.getPlaying().getPlayer().loadLvlData(newLevel.getLvlData());
        game.getPlaying().setMaxLvlOffset(newLevel.getLvlOffset());
        game.getPlaying().getObjectManager().loadObjects(newLevel);
    }

    public void draw(Graphics g, int lvlOffset) {
        Level currentLevel = levels.get(lvlIndex);
        int[][] lvlData = currentLevel.getLvlData();

        int atlasIdx = lvlIndex;
        if (atlasIdx >= levelSprites.length) atlasIdx = 0;
        BufferedImage[] tiles = levelSprites[atlasIdx];

        for (int j = 0; j < Juego.TILES_HEIGHT; j++) {
            for (int i = 0; i < lvlData[0].length; i++) {
                int index = currentLevel.getSpriteIndex(i, j);
                if (index <= 0) continue;

                if (index < tiles.length && tiles[index] != null) {
                    g.drawImage(tiles[index],
                            Juego.TILES_SIZE * i - lvlOffset,
                            Juego.TILES_SIZE * j,
                            Juego.TILES_SIZE, Juego.TILES_SIZE, null);
                }
            }
        }
    }

    public void update() {}

    public Level getCurrentLevel() { return levels.get(lvlIndex); }
    public int getAmountOfLevels() { return levels.size(); }
    public int getLevelIndex() { return lvlIndex; }
}