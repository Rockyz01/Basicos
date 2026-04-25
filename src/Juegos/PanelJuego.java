package Juegos;

import javax.swing.JPanel;

import Eventos.EventoMouse;
import Eventos.EventoTeclado;
import static Juegos.Juego.GAME_HEIGHT;
import static Juegos.Juego.GAME_WIDTH;
import java.awt.*;

public class PanelJuego extends JPanel {
    private EventoMouse ev;
    private EventoTeclado et;
    Juego game;

    public PanelJuego(Juego game) {
        ev = new EventoMouse(this);
        et = new EventoTeclado(this);
        this.game = game;
        setPanelSize();
        addKeyListener(et);
        addMouseListener(ev);
        addMouseMotionListener(ev);
    }

    private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
    }

    public void paint(Graphics g) {
        super.paint(g);
        game.render(g);
    }

    public Juego getGame() {
        return game;
    }

    void updateGame() {

    }


}
