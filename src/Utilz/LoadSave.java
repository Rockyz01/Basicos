package Utilz;

import Elementos.PlayerCharacter;
import Juegos.Juego;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;

public class LoadSave {
    
    public static final String LEVEL_ATLAS =  "Jungle_Tileset.png";
    public static final String LEVEL_ATLAS2 = "outside11_sprites.png";
    public static final String LEVEL_ATLAS3 = "Tileset.png";
    public static final String LEVEL_ATLAS4 = "outside_sprites2.png";
    public static final String LEVEL_ATLAS5 = "outside8_sprites.png";
    
    public static final String TILESET_LAVA = "tileset_lava.png";


    public static final String PLAYER_ATLAS = "player_sprites2.png";
    public static final String PLAYER_ATLAS2 = "player_sprites3.png";
    public static final String PLAYER_ATLAS3 = "player_sprites4.png";
    public static final String PLAYER_ATLAS4 = "player_sprites5.png";
    

    public static final String MENU_BUTTONS= "button_atlas2.png";
    public static final String MENU_BACKGROUND= "menu_background.png";
    public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";

    public static final String PLAYING_BG_IMG = "background_layer_1.png";
    public static final String PLAYING_BG_IMG2 = "playing_bg_img4.png";  
    public static final String PLAYING_BG_IMG3 = "playing_bg_snow.png";
    public static final String PLAYING_BG_IMG4 = "playing_bg_img3.png";
    public static final String PLAYING_BG_IMG5 = "playing_bg_city.png"; 

    public static final String SMALL_CLOUDS = "";
    public static final String BIG_CLOUDS = "";

    public static final String WEAPON_ATLAS = "weapons_atlas.png";


    public static final String GOLEM_SPRITE="enemy_sprite.png";
    public static final String SKELETONW_SPRITE="enemy_sprite.png";
    public static final String SKELETONY_SPRITE="enemy_sprite.png";
    public static final String ENEMIGO1_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO2_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO3_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO4_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO5_SPRITE = "enemy_sprite2.png";
    public static final String ENEMIGO6_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO7_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO8_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO9_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO10_SPRITE = "enemy_sprite.png";
    public static final String ENEMIGO11_SPRITE = "enemy_sprite3.png";
    public static final String ENEMIGO12_SPRITE = "enemy_sprite.png";


    
    public static final String MENU_BACKGROUND_IMG= "menu_background2.png";
    public static final String STATUS_BAR= "health_power_bar.png";
    public static final String COMPLETED_IMG= "completed_sprite.png";
    public static final String OPTIONS_MENU= "options_background.png";

    public static final String TRAP_ATLAS= "trap_atlas.png";
    public static final String POTION_ATLAS= "potions_sprites.png";
    public static final String CONTAINER_ATLAS= "objects_sprites.png";
    public static final String CANNON_ATLAS= "cannon_atlas.png";
    public static final String CANNON_BALL= "ball.png";
    public static final String DEATH_SCREEN= "death_screen.png";

    public static BufferedImage[][] loadAnimation(PlayerCharacter pc) {
        BufferedImage img = LoadSave.GetSpriteAtlas(pc.playerAtlas);
        BufferedImage[][] animations = new BufferedImage[pc.rowA][pc.colA];
        for (int i = 0; i < animations.length; i++)
            for (int j = 0; j < animations[i].length; j++)
                animations[i][j] = img.getSubimage(j * pc.spriteW, i * pc.spriteH, pc.spriteW, pc.spriteH);
        return animations;

    }

    

public static BufferedImage GetSpriteAtlas(String name) {
    BufferedImage img = null;
    InputStream is = LoadSave.class.getResourceAsStream("/recursos/" + name);

    if(is == null){
        System.err.println("Error: No se encontró el recurso: /recursos/" + name);
        return null;  // O lanzar excepción según convenga
    }

    try {
        img = ImageIO.read(is);
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return img;
}

    public static BufferedImage[] GetAllLevels() {
        URL url = LoadSave.class.getResource("/recursos/lvls");
        File file = null;

        try {
            file = new File(url.toURI());
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }

        File[] files = file.listFiles();
        File[] filesSorted = new File[files.length];
        for (int i = 0; i < filesSorted.length; i++) {
            for (int j = 0; j < files.length; j++) {
                if (files[j].getName().equals((i + 1) + ".png")) {
                    filesSorted[i] = files[j];
                }
            }
        }

        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
        for (int i = 0; i < imgs.length; i++) {
            try {
                imgs[i] = ImageIO.read(filesSorted[i]);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return imgs;
    }
public static class WeaponConstants {
    // Cantidad de armas y armaduras
    public static final int NUM_WEAPONS = 7;
    public static final int NUM_ARMORS  = 4;

    // Tamaño del sprite en el atlas (32x32 originales)
    public static final int WEAPON_DEFAULT_SIZE = 32;
    public static final int WEAPON_SIZE = (int) (WEAPON_DEFAULT_SIZE * Juego.SCALE);

    // Coordenadas (fila, columna) de cada arma en el sprite sheet
    public static final int[][] WEAPON_COORDS = {
        {10, 0},  // arma 0
        { 6, 1},  // arma 1
        { 9, 1},  // arma 2
        {12, 2},  // arma 3
        { 7, 6},  // arma 4
        { 0, 4},  // arma 5
        {11, 9},  // arma 6
    };

    // Daño que da cada arma (mismo orden que WEAPON_COORDS)
    public static final int[] WEAPON_DAMAGE = {12, 18, 15, 22, 25, 10, 30};

    // Coordenadas de cada armadura
    public static final int[][] ARMOR_COORDS = {
        {2, 7},
        {2, 8},
        {2, 9},
        {2, 10},
    };

    // Escudo (HP extra) que da cada armadura
    public static final int[] ARMOR_SHIELD = {25, 50, 75, 100};

    
    public static final int WEAPON_BLUE_START = 10;  // 10..16
    public static final int ARMOR_BLUE_START  = 20;  // 20..23
}


}
