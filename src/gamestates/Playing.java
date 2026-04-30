package gamestates;

import Elementos.EnemyManager;
import Elementos.GatoManager;
import Elementos.Jugador;
import Elementos.PlayerCharacter;
import Juegos.Juego;
import Niveles.LevelManager;
import Objects.ObjectManager;
import static Utilz.Constantes.Ambiente.*;
import Utilz.LoadSave;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;
import ui.GameOverOverlay;
import ui.LevelCompletedOverlay;
import ui.PauseOverlay;

public class Playing extends state implements Statemethods {
    private Jugador player;
    private LevelManager levelMan;
    private EnemyManager enemyManager;
    private GatoManager gatoManager;
    private ObjectManager objectManager;
    private PauseOverlay pauseOverlay;
    private GameOverOverlay gameOverOverlay;
    private LevelCompletedOverlay levelCompletedOverlay;
    private int score;
    private boolean paused = false;

    private int xLvlOffset;
    private int leftBorder = (int) (0.2 * Juego.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Juego.GAME_WIDTH);
    private int maxLvlOffsetX;

    private BufferedImage[] backgroundImgs;
    private BufferedImage bigCloud, smallcloud;
    public int[] smallcloudPos;
    private Random rnd = new Random();

    private boolean gameOver;
    private boolean lvlCompleted;
    private boolean playerDying;

    private int oldLvl = 0, Lvl = 0;

    public Playing(Juego juego) {
        super(juego);
        inicializar();

        // Backgrounds en orden: bosque azul, bosque otoñal, nieve, lava, cyberpunk
        backgroundImgs = new BufferedImage[5];
        backgroundImgs[0] = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG);   // bosque azul
        backgroundImgs[1] = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG2);  // bosque otoñal
        backgroundImgs[2] = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG3);  // nieve
        backgroundImgs[3] = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG4);  // lava
        backgroundImgs[4] = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BG_IMG5);  // cyberpunk

        bigCloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallcloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallcloudPos = new int[8];
        for (int i = 0; i < smallcloudPos.length; i++)
            smallcloudPos[i] = (int) (70 * Juego.SCALE) + rnd.nextInt((int) (150 * Juego.SCALE));

        calcLvlOffset();
        loadStartLevel();
    }

    public void loadNextLevel(int i) {
        resetAll();
        levelMan.loadNextLevel(i);
        player.setSpawn(levelMan.getCurrentLevel().getPlayerSpawn());
        gatoManager.onLevelLoad(
            levelMan.getCurrentLevel().getLvlData(),
            levelMan.getCurrentLevel().getSpikes(),
            levelMan.getCurrentLevel().getPlayerSpawn());
    }

    private void loadStartLevel() {
        enemyManager.loadEnemies(levelMan.getCurrentLevel());
        gatoManager.onLevelLoad(
            levelMan.getCurrentLevel().getLvlData(),
            levelMan.getCurrentLevel().getSpikes(),
            levelMan.getCurrentLevel().getPlayerSpawn());
        objectManager.loadObjects(levelMan.getCurrentLevel());
    }

    private void calcLvlOffset() {
        maxLvlOffsetX = levelMan.getCurrentLevel().getLvlOffset();
    }

    private void inicializar() {
        levelMan = new LevelManager(juego);
        enemyManager = new EnemyManager(this);
        gatoManager = new GatoManager();
        objectManager = new ObjectManager(this);

        player = new Jugador(PlayerCharacter.p1, this);
        player.loadLvlData(levelMan.getCurrentLevel().getLvlData());
        player.setSpawn(levelMan.getCurrentLevel().getPlayerSpawn());

        pauseOverlay = new PauseOverlay(this);
        gameOverOverlay = new GameOverOverlay(this);
        levelCompletedOverlay = new LevelCompletedOverlay(this);
    }

    public void setPlayerCharacter(PlayerCharacter pc) {
        player = new Jugador(pc, this);
        player.loadLvlData(levelMan.getCurrentLevel().getLvlData());
        player.setSpawn(levelMan.getCurrentLevel().getPlayerSpawn());
    }

    public Jugador getPlayer() { return player; }

    public void windowFocusLost() { player.resetDirBooleans(); }

    public void increaseScore(int amount) { score += amount; }

    @Override
    public void update() {
        if (paused) {
            pauseOverlay.update();
        } else if (oldLvl != Lvl) {
            loadNextLevel(Lvl);
            oldLvl = Lvl;
        } else if (lvlCompleted) {
            levelCompletedOverlay.update();
            switch (Lvl) {
                case 0: getJuego().setLEVEL1(true); break;
                case 1: getJuego().setLEVEL2(true); break;
                case 2: getJuego().setLEVEL3(true); break;
                default: break;
            }
        } else if (!gameOver) {
            levelMan.update();
            objectManager.update(levelMan.getCurrentLevel().getLvlData(), player);
            player.update();
            enemyManager.update(levelMan.getCurrentLevel().getLvlData(), player);
            gatoManager.update();
            checkCloseToBorder();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLvlOffset;
        if (diff > rightBorder)
            xLvlOffset += diff - rightBorder;
        else if (diff < leftBorder)
            xLvlOffset += diff - leftBorder;
        if (xLvlOffset > maxLvlOffsetX)
            xLvlOffset = maxLvlOffsetX;
        else if (xLvlOffset < 0)
            xLvlOffset = 0;
    }

    @Override
    public void draw(Graphics g) {
        // Background según el nivel actual
        int currentLvl = levelMan.getLevelIndex();
        if (currentLvl < 0 || currentLvl >= backgroundImgs.length) currentLvl = 0;
        if (backgroundImgs[currentLvl] != null)
            g.drawImage(backgroundImgs[currentLvl], 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);

        drawClouds(g);

        levelMan.draw(g, xLvlOffset);
        player.render(g, xLvlOffset);
        enemyManager.draw(g, xLvlOffset);
        objectManager.draw(g, xLvlOffset);
        gatoManager.draw(g, xLvlOffset);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("Score: " + score, 20, 40);

        if (paused) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
            pauseOverlay.draw(g);
        } else if (gameOver)
            gameOverOverlay.draw(g);
        else if (lvlCompleted)
            levelCompletedOverlay.draw(g);
    }

    private void drawClouds(Graphics g) {
        for (int i = 0; i < 3; i++)
            g.drawImage(bigCloud, 0 + i * BIG_CLOUDS_WIDTH - (int) (xLvlOffset * 0.3), (int) (204 * Juego.SCALE), BIG_CLOUDS_WIDTH, BIG_CLOUDS_HEIGHT, null);
        for (int i = 0; i < smallcloudPos.length; i++)
            g.drawImage(smallcloud, SMALL_CLOUDS_WIDTH * 4 * i - (int) (xLvlOffset * 0.7), smallcloudPos[i], SMALL_CLOUDS_WIDTH, SMALL_CLOUDS_HEIGHT, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (!gameOver) {
            if (e.getButton() == MouseEvent.BUTTON1)
                player.setAttacking(true);
            else if (e.getButton() == MouseEvent.BUTTON3)
                player.powerAttack();
        }
    }

    public void mouseDragged(MouseEvent e) {
        if (!gameOver)
            if (paused)
                pauseOverlay.mouseDragged(e);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseOverlay.mousePressed(e);
            else if (lvlCompleted) levelCompletedOverlay.mousePressed(e);
        } else
            gameOverOverlay.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseOverlay.mouseReleased(e);
            else if (lvlCompleted) levelCompletedOverlay.mouseReleased(e);
        } else
            gameOverOverlay.mouseReleased(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!gameOver) {
            if (paused) pauseOverlay.mouseMoved(e);
            else if (lvlCompleted) levelCompletedOverlay.mouseMoved(e);
        } else
            gameOverOverlay.mouseMoved(e);
    }

    public void setMaxLvlOffset(int lvlOffset) { this.maxLvlOffsetX = lvlOffset; }
    public void unpauseGame() { paused = false; }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver)
            gameOverOverlay.keyPressed(e);
        else
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: player.setLeft(true); break;
                case KeyEvent.VK_D: player.setRight(true); break;
                case KeyEvent.VK_SPACE: player.setJump(true); break;
                case KeyEvent.VK_ESCAPE: paused = !paused; break;
                // Ataques con O y P
                case KeyEvent.VK_O: player.setAttacking(true); break;
                case KeyEvent.VK_P: player.powerAttack(); break;
                case KeyEvent.VK_K: enemyManager.killAllEnemies(); break; // DEBUG: matar todos
            }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameOver)
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A: player.setLeft(false); break;
                case KeyEvent.VK_D: player.setRight(false); break;
                case KeyEvent.VK_SPACE: player.setJump(false); break;
            }
    }

    public void resetAll() {
        gameOver = false;
        paused = false;
        lvlCompleted = false;
        playerDying = false;
        player.resetAll();
        enemyManager.resetAllEnemies();
        objectManager.resetAllObjects();
        score = 0;
    }

    public void checkEnemyHit(Rectangle2D.Float attackBox) { enemyManager.checkEnemyHit(attackBox); }
    public void checkPotionTouched(Rectangle2D.Float hitbox) { objectManager.checkObjectTouched(hitbox); }
    public void setGameOver(boolean gameOver) { this.gameOver = gameOver; }
    public EnemyManager getEnemyManager() { return enemyManager; }

    public void setLevelCompleted(boolean levelCompleted) {
        this.lvlCompleted = levelCompleted;
        if (levelCompleted) juego.getAudioPlayer().lvlCompleted();
    }

    public ObjectManager getObjectManager() { return objectManager; }
    public void checkObjectHit(Rectangle2D.Float attackBox) { objectManager.checkObjectHit(attackBox); }
    public LevelManager getLevelManager() { return levelMan; }
    public void checkSpikesTouched(Jugador p) { objectManager.checkSpikesTouched(p); }
    public void setPlayerDying(boolean playerDying) { this.playerDying = playerDying; }
    public void setLvl(int Lvl) { this.Lvl = Lvl; }
}