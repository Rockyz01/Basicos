package Juegos;

import javax.swing.JPanel;
import Eventos.EventoMouse;
import Eventos.EventoTeclado;
import static Juegos.Juego.GAME_HEIGHT;
import static Juegos.Juego.GAME_WIDTH;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class PanelJuego extends JPanel {
    private EventoMouse ev;
    private EventoTeclado et;
    Juego game;

    public PanelJuego(Juego game) {
        ev = new EventoMouse(this);
        et = new EventoTeclado(this);
        this.game = game;
        setPanelSize();

        // CRÍTICO: el panel DEBE ser focusable para recibir KeyEvents
        setFocusable(true);
        setFocusTraversalKeysEnabled(false); // evita que TAB robe el foco

        addKeyListener(et);
        addMouseListener(ev);
        addMouseMotionListener(ev);

        // Recuperar foco automáticamente si se pierde (ej. al conectar control BT)
        addFocusListener(new FocusListener() {
            @Override public void focusGained(FocusEvent e) {}
            @Override public void focusLost(FocusEvent e) {
                // Recuperar foco en el siguiente tick del EDT
                javax.swing.SwingUtilities.invokeLater(() -> requestFocusInWindow());
            }
        });
    }

    private void setPanelSize() {
        Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
        setPreferredSize(size);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        game.render(g);
    }

    public Juego getGame() {
        return game;
    }

    void updateGame() {}
}
