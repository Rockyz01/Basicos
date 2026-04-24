package ui;

import static Utilz.Constantes.UI.URMButtons.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import Juegos.Juego;
import Utilz.LoadSave;
import gamestates.Gamestate;
import gamestates.Playing;

public class LevelCompletedOverlay {
    private Playing playing;
    private  UrmButton menu,next;
    private BufferedImage img;
    private int bgX,bgY,bgW,bgH;

public LevelCompletedOverlay(Playing playing){
    this.playing = playing;
    initImg();
    initButtons();
    
}

private void initButtons() {
    int menuX=(int)(340*Juego.SCALE);
    int nextX=(int)(435*Juego.SCALE);
    int y=(int)(195*Juego.SCALE);
    next=new UrmButton(nextX,y,URM_SIZE,URM_SIZE,0);
    menu=new UrmButton(menuX,y,URM_SIZE,URM_SIZE,2);
}

private void initImg() {
    img=LoadSave.GetSpriteAtlas(LoadSave.COMPLETED_IMG);
    bgW=(int)(img.getWidth()*Juego.SCALE);
    bgH=(int)(img.getHeight()*Juego.SCALE);
    bgX=Juego.GAME_WIDTH/2 - bgW / 2;
    bgY=(int)(75*Juego.SCALE);
}
public void draw(Graphics g) {
g.drawImage(img,bgX,bgY,bgW,bgH,null);
next.draw(g);
menu.draw(g);
}

public void update(){

}
private boolean isIn(UrmButton b,MouseEvent e){
    return b.getBounds().contains(e.getX(),e.getY());
}
public void mouseMoved(MouseEvent e){
    next.setMouseOver(false);
    menu.setMouseOver(false);

    if(isIn(menu, e))
    menu.setMouseOver(true);
    else if(isIn(next, e))
    next.setMouseOver(true);
}
public void mouseReleased(MouseEvent e){
		if (isIn(menu, e)) {
			if (menu.isMousePressed()) {
				playing.resetAll(); 
				playing.setGamestate(Gamestate.MENU);
			}
		} else if (isIn(next, e))
			if (next.isMousePressed()){
				playing.loadNextLevel(-1);
                playing.getJuego().getAudioPlayer().setLevelSong(playing.getLevelManager().getLevelIndex());
            }
		menu.resetBools();
		next.resetBools();

}
public void mousePressed(MouseEvent e){
    if(isIn(menu, e))
    menu.setMousePressed(true);
    else if(isIn(next, e))
    next.setMousePressed(true);
}
}
