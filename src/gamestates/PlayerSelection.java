package gamestates;

import Elementos.PlayerCharacter;
import Juegos.Juego;
import static Utilz.Constantes.EnemyConstants.INACTIVO;
import Utilz.LoadSave;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import ui.MenuButton;

public class PlayerSelection extends state implements Statemethods  {
    
    private BufferedImage backgroundImg,backgroundImgPink;
    private int menuX,menuY,menuWidth,menuHeight;
    private MenuButton playButton;
    private int playerindex=0;

    private CharacterAnimation[] characterAnimations;

    public PlayerSelection(Juego juego) {
        super(juego);
    loadButtons();
    loadBackground();
    backgroundImgPink=LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);

    loadCharAnimations();
    }

    private void loadCharAnimations() {
        characterAnimations=new CharacterAnimation[4];
        int i=0;
        characterAnimations[i++]=new CharacterAnimation(PlayerCharacter.p1);
        characterAnimations[i++]=new CharacterAnimation(PlayerCharacter.p2);
        characterAnimations[i++]=new CharacterAnimation(PlayerCharacter.p3);
        characterAnimations[i++]=new CharacterAnimation(PlayerCharacter.p4);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth=(int)(backgroundImg.getWidth()*Juego.SCALE);
        menuHeight=(int)(backgroundImg.getHeight()*Juego.SCALE);
        menuX=Juego.GAME_WIDTH / 2 - menuWidth/2;
        menuY=(int)(45*Juego.SCALE);
    }

    private void loadButtons() {
        playButton=new MenuButton(Juego.GAME_WIDTH / 2, (int) (290 * Juego.SCALE), 0, Gamestate.PLAYING); 
        /* buttons[0] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (150* Juego.SCALE), 0, Gamestate.PLAYING);
        buttons[1] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (220 * Juego.SCALE), 1, Gamestate.OPTIONS);
        buttons[2] = new MenuButton(Juego.GAME_WIDTH / 2, (int) (290 * Juego.SCALE), 2, Gamestate.QUIT); */
    }

    @Override
    public void update() {
        playButton.update();
        for(CharacterAnimation ca: characterAnimations )
        ca.update();
  
    }

@Override
public void draw(Graphics g) {
    g.drawImage(backgroundImgPink,0,0,Juego.GAME_WIDTH,Juego.GAME_HEIGHT,null);
    g.drawImage(backgroundImg, menuX, menuY,menuWidth,menuHeight,null);

    playButton.draw(g);

    int centerY = menuY + menuHeight/2;

    //center (seleccionado)
    drawChar(g, playerindex, menuX + menuWidth/2, centerY);

    //Left
    drawChar(g, playerindex - 1, menuX - menuWidth/4, centerY);

    //Right
    drawChar(g, playerindex + 1, menuX + menuWidth + menuWidth/4, centerY);

    //4º jugador (más a la izquierda)
    drawChar(g, playerindex + 2, menuX + menuWidth + menuWidth*3/4, centerY);

}
    private void drawChar(Graphics g, int playerindex,int x,int y){
        if(playerindex<0)
        playerindex =characterAnimations.length-1;
        else if (playerindex>=characterAnimations.length)
        playerindex=0;

        characterAnimations[playerindex].draw(g,x,y); 
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }
    
    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, playButton)) {
            playButton.setMousePressed(true);
           
        }
    }
    
    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, playButton)) {
            if (playButton.isMousePressed()) {
                
                juego.getPlaying().setPlayerCharacter(characterAnimations[playerindex].getPc());
                juego.getAudioPlayer().setLevelSong(
                    juego.getPlaying().getLevelManager().getLevelIndex());
                    playButton.applyGamestate();
            }
        }
        resetButtons(); 
    }
    
    private void resetButtons() {
        playButton.resetBools();
    }
    
    @Override
    public void mouseMoved(MouseEvent e) {
        playButton.setMouseOver(false);
        if (isIn(e, playButton)) {
            playButton.setMouseOver(true);
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        deltaindex(1);}
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT )
        deltaindex(-1);
    }
    
    private void deltaindex(int i) {
        playerindex += i;
        if(playerindex<0)
        playerindex =characterAnimations.length-1;
        else if (playerindex>=characterAnimations.length)
        playerindex=0;

    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public class CharacterAnimation {
    private static final int ANI_SPEED = 20;
    private final PlayerCharacter pc;
    private int animTick,animInd;
    private final BufferedImage [][] animations;
    private int scale;


    public CharacterAnimation(PlayerCharacter pc){
        this.pc= pc;
        this.scale=(int)(Juego.SCALE+3);
        animations=LoadSave.loadAnimation(pc);

    }
       public void draw(Graphics g,int drawX,int drawY ){
       g.drawImage(animations[pc.getRowIndex(INACTIVO)][animInd],
        drawX - pc.spriteW*scale/2 ,
        drawY - pc.spriteH*scale/2,
        pc.spriteH*scale,
        pc.spriteH*scale,
        null);
       }
       public void update(){
        animTick++;
        if (animTick >= ANI_SPEED) {
            animTick = 0;
            animInd++;
            if (animInd >= pc.getNoSprite(INACTIVO)) {
                animInd = 0;
            }
        }

       }
       public PlayerCharacter getPc(){
        return pc;
        
       }

    }
}    