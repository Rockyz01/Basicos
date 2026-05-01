package gamestates;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.swing.ImageIcon;
import java.awt.Image;

import Juegos.Juego;
import Utilz.LoadSave;
import static Utilz.Constantes.UI.Buttons.*;
import ui.MenuButton;

public class Menu extends state implements Statemethods {

    private MenuButton[] buttons = new MenuButton[3];

    // Fondo animado GIF
    private ImageIcon gifBackground;
    private Image gifImage;
    private BufferedImage backgroundImgPink;

    // Sprite del título
    private BufferedImage tituloImg;

    // Fade-in
    private long startTime;
    private float titleAlpha = 0f;

    // Botón seleccionado con control
    private int selectedIndex = 0;

    public Menu(Juego juego) {
        super(juego);
        loadGifBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        loadTituloImg();
        loadButtons();
        startTime = System.currentTimeMillis();
    }

    private void loadGifBackground() {
        try {
            URL gifUrl = getClass().getResource("/recursos/intro_bg.gif");
            if (gifUrl != null) {
                gifBackground = new ImageIcon(gifUrl);
                gifImage = gifBackground.getImage();
            }
        } catch (Exception e) {
            System.err.println("Error cargando GIF: " + e.getMessage());
        }
    }

    private void loadTituloImg() {
        tituloImg = LoadSave.GetSpriteAtlas("titulo_sylvaris.png");
    }

    private void loadButtons() {
        // Botones horizontales centrados en la parte inferior de la pantalla
        // Separación entre centros de botones
        int gap = (int)(B_WIDTH * 1.15);
        int totalW = B_WIDTH * 3 + gap * 2 - (B_WIDTH * 2); // simplificado abajo
        // Posición Y: zona inferior, a 78% de la altura de la pantalla
        int btnY = (int)(Juego.GAME_HEIGHT * 0.78);
        int centerX = Juego.GAME_WIDTH / 2;

        // Los 3 botones: izquierda, centro, derecha
        // xPos en MenuButton es el centro del botón
        buttons[0] = new MenuButton(centerX - gap, btnY, 0, Gamestate.PLAYER_SELECTION);
        buttons[1] = new MenuButton(centerX,       btnY, 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(centerX + gap, btnY, 2, Gamestate.QUIT);
    }

    @Override
    public void update() {
        // Sincroniza el resaltado visual con el botón seleccionado por teclado/control.
        // Solo aplicamos selectedIndex si NINGÚN botón tiene mouseOver activo
        // (para no pelear con el ratón cuando el usuario lo está usando).
        boolean mouseHovering = false;
        for (MenuButton mb : buttons)
            if (mb.isMouseOver()) { mouseHovering = true; break; }

        if (!mouseHovering) {
            for (int i = 0; i < buttons.length; i++)
                buttons[i].setMouseOver(i == selectedIndex);
        }

        for (MenuButton mb : buttons) mb.update();
        long elapsed = System.currentTimeMillis() - startTime;
        titleAlpha = Math.min(elapsed / 1500f, 1f);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // 1. Fondo GIF animado (pantalla completa)
        if (gifImage != null) {
            g2d.drawImage(gifImage, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, juego.getPanel());
        } else {
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
        }

        // Fade-in negro al inicio para ocultar primer frame del GIF
        long elapsed = System.currentTimeMillis() - startTime;
        if (elapsed < 800) {
            float blackAlpha = 1f - (elapsed / 800f);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, blackAlpha));
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }

        // 2. Sombra suave detrás de los botones para legibilidad
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
        g2d.setColor(Color.BLACK);
        int btnAreaY = (int)(Juego.GAME_HEIGHT * 0.73);
        int btnAreaH = (int)(Juego.GAME_HEIGHT * 0.20);
        g2d.fillRoundRect(0, btnAreaY, Juego.GAME_WIDTH, btnAreaH + 20, 0, 0);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));

        // 3. Botones horizontales
        for (MenuButton mb : buttons) mb.draw(g);


        // 4. Logo del título — tamaño reducido, centrado en la parte superior
        if (tituloImg != null) {
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, titleAlpha));

            // Título ocupa 40% del ancho (más pequeño que antes)
            int logoW = (int)(Juego.GAME_WIDTH * 0.40);
            int logoH = (int)(logoW * ((double) tituloImg.getHeight() / tituloImg.getWidth()));

            int logoX = Juego.GAME_WIDTH / 2 - logoW / 2;
            // Verticalemente: en el cuarto superior de la pantalla
            int logoY = (int)(Juego.GAME_HEIGHT * 0.04);

            g2d.drawImage(tituloImg, logoX, logoY, logoW, logoH, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
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
        for (int i = 0; i < buttons.length; i++) {
            boolean over = isIn(e, buttons[i]);
            buttons[i].setMouseOver(over);
            if (over) selectedIndex = i; // sincroniza para que el control siga desde aquí
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                selectedIndex = (selectedIndex - 1 + buttons.length) % buttons.length;
                break;
            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                selectedIndex = (selectedIndex + 1) % buttons.length;
                break;
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
