package gamestates;

import java.awt.event.MouseEvent;

import Audio.AudioPlayer;
import Juegos.Juego;
import ui.MenuButton;

public class state {
   protected Juego juego;
    public state(Juego juego){
        this.juego=juego;

    }

    public boolean isIn(MouseEvent e,MenuButton mb){
        return mb.getBounds().contains(e.getX(),e.getY());
    }

    public Juego getJuego(){
        return juego;
    }
    	public void setGamestate(Gamestate state) {
		switch (state) {
		case MENU -> juego.getAudioPlayer().playSong(AudioPlayer.MENU_1);
		case PLAYING -> juego.getAudioPlayer().setLevelSong(juego.getPlaying().getLevelManager().getLevelIndex());
		}

		Gamestate.state = state;
	}
}
