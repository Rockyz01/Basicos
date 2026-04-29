package Niveles;

import Elementos.*;
import Juegos.Juego;
import Objects.Armor;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Spike;
import Objects.Weapon;
import Utilz.MetodoAyuda;
import static Utilz.MetodoAyuda.GetGolems;
import static Utilz.MetodoAyuda.GetLevelData;
import static Utilz.MetodoAyuda.GetPlayerSpawn;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Level {

    private ArrayList<Golem> golems;
    private ArrayList<Cannon> cannons;
    private ArrayList<skeletonW> Skeletonw;
    private ArrayList<skeletonY> skeletony;
    private ArrayList<enemigo1> enemigos1;
    private ArrayList<enemigo2> enemigos2;
    private ArrayList<enemigo3> enemigos3;
    private ArrayList<enemigo4> enemigos4;
    private ArrayList<enemigo5> enemigos5;
    private ArrayList<enemigo6> enemigos6;
    private ArrayList<enemigo7> enemigos7;
    private ArrayList<enemigo8> enemigos8;
    private ArrayList<enemigo9> enemigos9;
    private ArrayList<enemigo10> enemigos10;
    private ArrayList<enemigo11> enemigos11;
    private ArrayList<enemigo12> enemigos12;
    private ArrayList<TrolJefe>  trolJefes;

    private ArrayList<Potion> potions;
    private ArrayList<Spike> spikes;
    private ArrayList<GameContainer> containers;
    private ArrayList<Weapon> weapons;
    private ArrayList<Armor> armors;

    private BufferedImage img;
    private int[][] lvlData;
    private int lvlTilesWide;
    private int maxTilesOffset;
    private int maxLvlOffsetX;

    private Point playerSpawn;

    public Level(BufferedImage img) {
        this.img = img;
        createLevelData();
        createEnemies();
        createPotions();
        createContainers();
        createSpikes();
        createCannons();
        createWeapons();
        createArmors();
        calcLvlOffsets();
        calcPlayerSpawn();
    }

    private void createCannons() { cannons = MetodoAyuda.GetCannons(img); }
    private void createSpikes() { spikes = MetodoAyuda.getSpikes(img); }
    private void createContainers() { containers = MetodoAyuda.GetContainers(img); }
    private void createPotions() { potions = MetodoAyuda.GetPotions(img); }
    private void createWeapons() { weapons = MetodoAyuda.GetWeapons(img); }
    private void createArmors() { armors = MetodoAyuda.GetArmors(img); }
    private void calcPlayerSpawn() { playerSpawn = GetPlayerSpawn(img); }

    private void calcLvlOffsets() {
        lvlTilesWide = img.getWidth();
        maxTilesOffset = lvlTilesWide - Juego.TILES_WIDTH;
        maxLvlOffsetX = Juego.TILES_SIZE * maxTilesOffset;
    }

    private void createEnemies() {
        golems = GetGolems(img);
        Skeletonw = MetodoAyuda.GetSkeletonW(img);
        skeletony = MetodoAyuda.GetSkeletonY(img);
        enemigos1 = MetodoAyuda.GetEnemigo1(img);
        enemigos2 = MetodoAyuda.GetEnemigo2(img);
        enemigos3 = MetodoAyuda.GetEnemigo3(img);
        enemigos4 = MetodoAyuda.GetEnemigo4(img);
        enemigos5 = MetodoAyuda.GetEnemigo5(img);
        enemigos6 = MetodoAyuda.GetEnemigo6(img);
        enemigos7 = MetodoAyuda.GetEnemigo7(img);
        enemigos8 = MetodoAyuda.GetEnemigo8(img);
        enemigos9 = MetodoAyuda.GetEnemigo9(img);
        enemigos10 = MetodoAyuda.GetEnemigo10(img);
        enemigos11 = MetodoAyuda.GetEnemigo11(img);
        enemigos12 = MetodoAyuda.GetEnemigo12(img);
        trolJefes  = MetodoAyuda.GetTrolJefes(img);
    }

    private void createLevelData() { lvlData = GetLevelData(img); }

    public int getSpriteIndex(int x, int y) { return lvlData[y][x]; }
    public int[][] getLvlData() { return lvlData; }
    public void setLvlData(int[][] lvlData) { this.lvlData = lvlData; }
    public int getLvlOffset() { return maxLvlOffsetX; }
    public ArrayList<Golem> getGolems() { return golems; }
    public Point getPlayerSpawn() { return playerSpawn; }
    public ArrayList<Potion> getPotions() { return potions; }
    public ArrayList<GameContainer> getContainers() { return containers; }
    public ArrayList<Spike> getSpikes() { return spikes; }
    public ArrayList<Cannon> getCannons() { return cannons; }
    public ArrayList<Weapon> getWeapons() { return weapons; }
    public ArrayList<Armor> getArmors() { return armors; }
    public ArrayList<skeletonW> getSkeletonw() { return Skeletonw; }
    public ArrayList<skeletonY> getSkeletony() { return skeletony; }
    public ArrayList<enemigo1> getEnemigo1() { return enemigos1; }
    public ArrayList<enemigo2> getEnemigo2() { return enemigos2; }
    public ArrayList<enemigo3> getEnemigo3() { return enemigos3; }
    public ArrayList<enemigo4> getEnemigo4() { return enemigos4; }
    public ArrayList<enemigo5> getEnemigo5() { return enemigos5; }
    public ArrayList<enemigo6> getEnemigo6() { return enemigos6; }
    public ArrayList<enemigo7> getEnemigo7() { return enemigos7; }
    public ArrayList<enemigo8> getEnemigo8() { return enemigos8; }
    public ArrayList<enemigo9> getEnemigo9() { return enemigos9; }
    public ArrayList<enemigo10> getEnemigo10() { return enemigos10; }
    public ArrayList<enemigo11> getEnemigo11() { return enemigos11; }
    public ArrayList<enemigo12> getEnemigo12() { return enemigos12; }
    public ArrayList<TrolJefe>  getTrolJefes()  { return trolJefes; }
}