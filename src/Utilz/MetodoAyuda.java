package Utilz;

import Elementos.Golem;
import Elementos.enemigo1;
import Elementos.enemigo10;
import Elementos.enemigo11;
import Elementos.enemigo12;
import Elementos.enemigo2;
import Elementos.enemigo3;
import Elementos.enemigo4;
import Elementos.enemigo5;
import Elementos.enemigo6;
import Elementos.enemigo7;
import Elementos.enemigo8;
import Elementos.enemigo9;
import Elementos.skeletonW;
import Elementos.skeletonY;
import Juegos.Juego;
import Objects.Armor;
import Objects.Cannon;
import Objects.GameContainer;
import Objects.Potion;
import Objects.Projectile;
import Objects.Spike;
import Objects.Weapon;
import static Utilz.Constantes.EnemyConstants.*;
import static Utilz.Constantes.ObjectConstants.*;
import static Utilz.Constantes.WeaponConstants.*;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MetodoAyuda {
    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!isSolido(x, y, lvlData))
            if (!isSolido(x + width, y + height, lvlData))
                if (!isSolido(x + width, y, lvlData))
                    if (!isSolido(x, y + height, lvlData))
                        return true;
        return false;
    }

    private static boolean isSolido(float x, float y, int[][] lvlData) {
        int maxWidth = lvlData[0].length * Juego.TILES_SIZE;
        if (x < 0 || x >= maxWidth) return true;
        if (y < 0 || y >= Juego.GAME_HEIGHT) return true;
        float xIndex = x / Juego.TILES_SIZE;
        float yIndex = y / Juego.TILES_SIZE;
        return IsTileSolid((int) xIndex, (int) yIndex, lvlData);
    }

    public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
        return isSolido(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
    }

    public static boolean IsTileSolid(int xTile, int yTile, int[][] lvlData) {
        int value = lvlData[yTile][xTile];
        if (value <= 0 || value == 11)
            return false;
        return true;
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
        if (!isSolido(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
            if (!isSolido(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
                return false;
        return true;
    }

    public static float GetEntityXPosNexttoWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Juego.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * Juego.TILES_SIZE;
            int xOffset = (int) (Juego.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else
            return currentTile * Juego.TILES_SIZE;
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Juego.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currentTile * Juego.TILES_SIZE;
            int yOffset = (int) (Juego.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else
            return currentTile * Juego.TILES_SIZE;
    }

    public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
        if (xSpeed > 0)
            return isSolido(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
        else
            return isSolido(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
    }

    public static boolean IsAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
        if (IsAllTilesClear(xStart, xEnd, y, lvlData))
            for (int i = 0; i < xEnd - xStart; i++) {
                if (!IsTileSolid(xStart + i, y + 1, lvlData))
                    return false;
            }
        return true;
    }

    public static boolean IsSightClear(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Juego.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Juego.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTileWalkable(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTileWalkable(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean CanCannonSeePlayer(int[][] lvlData, Rectangle2D.Float firstHitbox, Rectangle2D.Float secondHitbox, int yTile) {
        int firstXTile = (int) (firstHitbox.x / Juego.TILES_SIZE);
        int secondXTile = (int) (secondHitbox.x / Juego.TILES_SIZE);
        if (firstXTile > secondXTile)
            return IsAllTilesClear(secondXTile, firstXTile, yTile, lvlData);
        else
            return IsAllTilesClear(firstXTile, secondXTile, yTile, lvlData);
    }

    public static boolean IsAllTilesClear(int xStart, int xEnd, int y, int[][] lvlData) {
        for (int i = 0; i < xEnd - xStart; i++)
            if (IsTileSolid(xStart + i, y, lvlData))
                return false;
        return true;
    }

    public static int[][] GetLevelData(BufferedImage img) {
        int[][] lvlData = new int[img.getHeight()][img.getWidth()];
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int valor = color.getRed();
                if (valor >= 105)
                    valor = 0;
                lvlData[j][i] = valor;
            }
        return lvlData;
    }

    public static ArrayList<Golem> GetGolems(BufferedImage img) {
        ArrayList<Golem> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == GOLEM)
                    list.add(new Golem(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static Point GetPlayerSpawn(BufferedImage img) {
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == 100)
                    return new Point(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE);
            }
        return new Point(1 * Juego.TILES_SIZE, 1 * Juego.TILES_SIZE);
    }

    public static ArrayList<Potion> GetPotions(BufferedImage img) {
        ArrayList<Potion> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int valor = color.getBlue();
                if (valor == RED_POTION || valor == BLUE_POTION)
                    list.add(new Potion(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, valor));
            }
        return list;
    }

    public static ArrayList<GameContainer> GetContainers(BufferedImage img) {
        ArrayList<GameContainer> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int valor = color.getBlue();
                if (valor == BOX || valor == BARREL)
                    list.add(new GameContainer(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, valor));
            }
        return list;
    }

    public static ArrayList<Spike> getSpikes(BufferedImage img) {
        ArrayList<Spike> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getBlue() == SPIKE)
                    list.add(new Spike(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, SPIKE));
            }
        return list;
    }

    public static ArrayList<Cannon> GetCannons(BufferedImage img) {
        ArrayList<Cannon> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value == CANNON_LEFT || value == CANNON_RIGHT)
                    list.add(new Cannon(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, value));
            }
        return list;
    }

    public static ArrayList<Weapon> GetWeapons(BufferedImage img) {
        ArrayList<Weapon> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value >= WEAPON_BLUE_START && value < WEAPON_BLUE_START + NUM_WEAPONS) {
                    int weaponType = value - WEAPON_BLUE_START;
                    list.add(new Weapon(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, weaponType));
                }
            }
        return list;
    }

    public static ArrayList<Armor> GetArmors(BufferedImage img) {
        ArrayList<Armor> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                int value = color.getBlue();
                if (value >= ARMOR_BLUE_START && value < ARMOR_BLUE_START + NUM_ARMORS) {
                    int armorType = value - ARMOR_BLUE_START;
                    list.add(new Armor(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE, armorType));
                }
            }
        return list;
    }

    public static ArrayList<skeletonW> GetSkeletonW(BufferedImage img) {
        ArrayList<skeletonW> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == SKELETONW)
                    list.add(new skeletonW(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<skeletonY> GetSkeletonY(BufferedImage img) {
        ArrayList<skeletonY> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == SKELETONY)
                    list.add(new skeletonY(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo1> GetEnemigo1(BufferedImage img) {
        ArrayList<enemigo1> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO1)
                    list.add(new enemigo1(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo2> GetEnemigo2(BufferedImage img) {
        ArrayList<enemigo2> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO2)
                    list.add(new enemigo2(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo3> GetEnemigo3(BufferedImage img) {
        ArrayList<enemigo3> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO3)
                    list.add(new enemigo3(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo4> GetEnemigo4(BufferedImage img) {
        ArrayList<enemigo4> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getBlue() == ENEMIGO4)
                    list.add(new enemigo4(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo5> GetEnemigo5(BufferedImage img) {
        ArrayList<enemigo5> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getAlpha() == ENEMIGO5)
                    list.add(new enemigo5(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo6> GetEnemigo6(BufferedImage img) {
        ArrayList<enemigo6> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getAlpha() == ENEMIGO6)
                    list.add(new enemigo6(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo7> GetEnemigo7(BufferedImage img) {
        ArrayList<enemigo7> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getAlpha() == ENEMIGO7)
                    list.add(new enemigo7(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo8> GetEnemigo8(BufferedImage img) {
        ArrayList<enemigo8> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO8)
                    list.add(new enemigo8(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo9> GetEnemigo9(BufferedImage img) {
        ArrayList<enemigo9> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO9)
                    list.add(new enemigo9(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo10> GetEnemigo10(BufferedImage img) {
        ArrayList<enemigo10> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getAlpha() == ENEMIGO10)
                    list.add(new enemigo10(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo11> GetEnemigo11(BufferedImage img) {
        ArrayList<enemigo11> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getAlpha() == ENEMIGO11)
                    list.add(new enemigo11(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }

    public static ArrayList<enemigo12> GetEnemigo12(BufferedImage img) {
        ArrayList<enemigo12> list = new ArrayList<>();
        for (int j = 0; j < img.getHeight(); j++)
            for (int i = 0; i < img.getWidth(); i++) {
                Color color = new Color(img.getRGB(i, j));
                if (color.getGreen() == ENEMIGO12)
                    list.add(new enemigo12(i * Juego.TILES_SIZE, j * Juego.TILES_SIZE));
            }
        return list;
    }
}