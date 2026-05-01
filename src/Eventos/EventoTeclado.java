package Eventos;

import java.awt.event.*;

import Juegos.PanelJuego;
import gamestates.Gamestate;


public class EventoTeclado implements KeyListener {
    private PanelJuego pan;

    public EventoTeclado(PanelJuego pan) {
        this.pan = pan;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
            pan.getGame().getMenu().keyPressed(e);
                break;
                case PLAYER_SELECTION:
            pan.getGame().getPlayerSelection().keyPressed(e);
                break;
                case PLAYING:
                pan.getGame().getPlaying().keyPressed(e);
                break;
                case OPTIONS:
                pan.getGame().getGameOptions().keyPressed(e);
                break;
            default:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (Gamestate.state) {
            case MENU:
                pan.getGame().getMenu().keyReleased(e);
                break;
                case PLAYING:
                pan.getGame().getPlaying().keyReleased(e);
                break;
            default:
                break;
        }
    }
}
