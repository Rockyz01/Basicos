package Juegos;

import java.awt.Graphics;

import Audio.AudioPlayer;
import Eventos.ControlXbox;
import gamestates.GameOptions;
import gamestates.Gamestate;
import gamestates.Menu;
import gamestates.PlayerSelection;
import gamestates.Playing;
import ui.AudioOptions;

public class Juego extends Thread {
    private VtaJuego vta;
    private int FPS_SET = 120;
    private int UPS_SET = 200;
    PanelJuego pan;

    private Playing playing;
    private Menu menu;
    private PlayerSelection playerSelection;
    private GameOptions gameOptions;
    private AudioOptions audioOptions;
    private AudioPlayer audioPlayer;
    private ControlXbox controlXbox;

	public final static int TILES_DEFAULT_SIZE = 32 ;
	public final static float SCALE = 2f;
	public final static int TILES_WIDTH = 26;
	public final static int TILES_HEIGHT = 14;
	public final static int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILES_SIZE * TILES_WIDTH;
	public final static int GAME_HEIGHT = TILES_SIZE * TILES_HEIGHT;

    private int levelid,oldlevelid;
    private Boolean LEVEL1=false, LEVEL2=false, LEVEL3=false;

    public Boolean getLEVEL1() {
        return LEVEL1;
    }

    public Boolean getLEVEL2() {
        return LEVEL2;
    }

    public Boolean getLEVEL3() {
        return LEVEL3;
    }



    public void setLEVEL1(Boolean LEVEL1) {
        this.LEVEL1 = LEVEL1;
    }

    public void setLEVEL2(Boolean LEVEL2) {
        this.LEVEL2 = LEVEL2;
    }

    public void setLEVEL3(Boolean LEVEL3) {
        this.LEVEL3 = LEVEL3;
    }

    

    public Juego() {
        inicializar();
        pan = new PanelJuego(this);
        vta = new VtaJuego(pan);
        pan.setFocusable(true);
        pan.requestFocus();
        // Iniciar soporte de control Xbox
        controlXbox = new ControlXbox(this);
        controlXbox.start();
        comenzar();
    }

    private void inicializar() {
        audioOptions = new AudioOptions(this);
        audioPlayer = new AudioPlayer();
        menu= new Menu(this);
        playing= new Playing(this);
        playerSelection=new PlayerSelection(this);
        gameOptions = new GameOptions(this);
        
    }

    private void comenzar() {
        start();
    }

    public void run() {
        double frameportiempo = 1000000000.0 / FPS_SET;
        double updateportiempo = 1000000000.0 / UPS_SET;
        int frame = 0;
        int update = 0;
        long previusTime = System.nanoTime();
        double deltaU = 0;
        double deltaF = 0;
        long lastCheck = System.currentTimeMillis();
        while (true) {
            long currentTime = System.nanoTime();
            deltaU += (currentTime - previusTime) / updateportiempo;
            deltaF += (currentTime - previusTime) / frameportiempo;
            previusTime = currentTime;
            if (deltaU >= 1) {
                updates();
                update++;
                deltaU--;
            }
            if (deltaF >= 1) {
                pan.repaint();
                frame++;
                deltaF--;
            }
            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS:" + frame + "| UPS: " + update);
                frame = 0;
                update=0;
            }

        }
    }
    private void updates() {
        switch (Gamestate.state) {
            case MENU:
                menu.update();
                break;
            case PLAYER_SELECTION:
                playerSelection.update();
                break;
            case PLAYING:
                if (oldlevelid != levelid) {
                    playing.setLvl(levelid);
                    oldlevelid = levelid;
                }
                playing.update();
                break;
            case OPTIONS:
                gameOptions.update();
                break;
            case QUIT:
            default:
                System.exit(0);
                break;
        }
    }

    public void render(Graphics g) {
        switch(Gamestate.state){
            case MENU:
            menu.draw(g);
                break;
            case PLAYER_SELECTION:
            playerSelection.draw(g);
            break; 
            case PLAYING:
            playing.draw(g);
                break;
                case OPTIONS:
                gameOptions.draw(g);
            default:
                break;
        }
    }
    public void windowFocusLost(){
        if(Gamestate.state==Gamestate.PLAYING)
        playing.getPlayer().resetDirBooleans();
        if (controlXbox != null) controlXbox.liberarTodo();
    }

    public Menu getMenu(){
        return menu;
    }
    public Playing getPlaying(){
        return playing;
    }
    public PlayerSelection getPlayerSelection(){
        return playerSelection;
    }
    public GameOptions getGameOptions(){
        return gameOptions;
    }
    public AudioOptions geAudioOptions(){
        return audioOptions;
    }
    public java.awt.Component getPanel() { return pan; }

    public AudioPlayer getAudioPlayer() {
		return audioPlayer;
	}
}
