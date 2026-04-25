package Eventos;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import Juegos.PanelJuego;
import gamestates.Gamestate;

public class EventoMouse implements MouseListener, MouseMotionListener{
    private PanelJuego pan;
    

    public EventoMouse(PanelJuego pan) {
        this.pan = pan;
    }
    public void mouseDragged(MouseEvent e){
      switch (Gamestate.state) {
        case PLAYING:
             pan.getGame().getPlaying().mouseDragged(e);
                    break;
                    case OPTIONS:
                    pan.getGame().getGameOptions().mouseDragged(e);
                    break;
              default:
          break;
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
       switch(Gamestate.state){
        case PLAYING:
       pan.getGame().getPlaying().mouseClicked(e);
          break;
        default:
          break;
       }
      }
      public void mouseMoved(MouseEvent e){
        switch(Gamestate.state){
          case MENU:
          pan.getGame().getMenu().mouseMoved(e);
            break;
          case PLAYER_SELECTION:
          pan.getGame().getPlayerSelection().mouseMoved(e);
          break;
          case PLAYING:
         pan.getGame().getPlaying().mouseMoved(e);
            break;
            case OPTIONS:
                    pan.getGame().getGameOptions().mouseMoved(e);
                    break;
          default:
            break;
         }
      }

    @Override
    public void mousePressed(MouseEvent e) {
      switch(Gamestate.state){
        case MENU:
        pan.getGame().getMenu().mousePressed(e);
          break;
        case PLAYER_SELECTION:
        pan.getGame().getPlayerSelection().mousePressed(e);
        break;
        case PLAYING:
       pan.getGame().getPlaying().mousePressed(e);
          break;
          case OPTIONS:
                    pan.getGame().getGameOptions().mousePressed(e);
                    break;
        default:
          break;
       }
       }

    @Override
    public void mouseReleased(MouseEvent e) {
      switch(Gamestate.state){
        case MENU:
        pan.getGame().getMenu().mouseReleased(e);
          break;
          case PLAYER_SELECTION:
          pan.getGame().getPlayerSelection().mouseReleased(e);
          break;
        case PLAYING:
       pan.getGame().getPlaying().mouseReleased(e);
          break;
          case OPTIONS:
                    pan.getGame().getGameOptions().mouseReleased(e);
                    break;
        default:
          break;
       }
     }

    @Override
    public void mouseEntered(MouseEvent e) {
     }

    @Override
    public void mouseExited(MouseEvent e) {
      }
    
}
