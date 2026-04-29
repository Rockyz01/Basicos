package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Juegos.Juego;
import Utilz.LoadSave;
import ui.MenuButton;

public class Menu extends state implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];
    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;

    // Índice del botón seleccionado con el control
    private int selectedIndex = 0;

    public Menu(Juego juego) {
        super(juego);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth  = (int) (backgroundImg.getWidth()  * Juego.SCALE);
        menuHeight = (int) (backgroundImg.getHeight() * Juego.SCALE);
        menuX = Juego.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int) (45 * Juego.SCALE);
    }

    private void loadButtons() {
        buttons[0] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (150 * Juego.SCALE), 0, Gamestate.PLAYER_SELECTION);
        buttons[1] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (220 * Juego.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (290 * Juego.SCALE), 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        for (MenuButton mb : buttons) mb.update();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImgPink, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);
        for (MenuButton mb : buttons) mb.draw(g);

        // Indicador visual del botón seleccionado con el control
        MenuButton sel = buttons[selectedIndex];
        g.setColor(Color.YELLOW);
        g.drawRect(
            sel.getBounds().x - 4,
            sel.getBounds().y - 4,
            sel.getBounds().width + 8,
            sel.getBounds().height + 8
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        for (MenuButton mb : buttons)
            if (isIn(e, mb)) { mb.setMousePressed(true); break; }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        for (MenuButton mb : buttons) {
            if (isIn(e, mb)) {
                if (mb.isMousePressed()) mb.applyGamestate();
                if (mb.getState() == Gamestate.PLAYING)
                    juego.getAudioPlayer().setLevelSong(
                        juego.getPlaying().getLevelManager().getLevelIndex());
                break;
            }
        }
        resetButtons();
    }

    private void resetButtons() {
        for (MenuButton mb : buttons) mb.resetBools();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        for (MenuButton mb : buttons)
            mb.setMouseOver(isIn(e, mb));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                selectedIndex = (selectedIndex - 1 + buttons.length) % buttons.length;
                break;
            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                selectedIndex = (selectedIndex + 1) % buttons.length;
                break;
            case KeyEvent.VK_ENTER:
            case KeyEvent.VK_SPACE:
                activarBotonSeleccionado();
                break;
        }
    }

    private void activarBotonSeleccionado() {
        MenuButton mb = buttons[selectedIndex];
        mb.applyGamestate();
        if (mb.getState() == Gamestate.PLAYING)
            juego.getAudioPlayer().setLevelSong(
                juego.getPlaying().getLevelManager().getLevelIndex());
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
