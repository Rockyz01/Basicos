package Objects;

import Elementos.Jugador;
import Juegos.Juego;
import Niveles.*;
import static Utilz.Constantes.ObjectConstants.*;
import static Utilz.Constantes.Projectiles.*;
import static Utilz.Constantes.WeaponConstants.*;
import Utilz.LoadSave;
import static Utilz.MetodoAyuda.CanCannonSeePlayer;
import static Utilz.MetodoAyuda.IsProjectileHittingLevel;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ObjectManager {

    private Playing playing;
    private BufferedImage[][] potionImgs, containerImgs;
    private BufferedImage[] cannonImgs;
    private BufferedImage spikeImg, cannonBallImg;
    private BufferedImage[] weaponImgs;
    private BufferedImage[] armorImgs;
    private ArrayList<Potion> potions;
    private ArrayList<GameContainer> containers;
    private ArrayList<Spike> spikes;
    private ArrayList<Cannon> cannons;
    private ArrayList<Weapon> weapons;
    private ArrayList<Armor> armors;
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    public ObjectManager(Playing playing) {
        this.playing = playing;
        loadImgs();
    }

    public void checkSpikesTouched(Jugador p) {
        for (Spike s : spikes)
            if (s.getHitbox().intersects(p.getHitbox()))
                p.kill();
    }

    public void checkObjectTouched(Rectangle2D.Float hitbox) {
        for (Potion p : potions)
            if (p.isActive()) {
                if (hitbox.intersects(p.getHitbox())) {
                    p.setActive(false);
                    applyEffectToPlayer(p);
                    playing.increaseScore(100);
                }
            }
        // Recoger armas
        for (Weapon w : weapons)
            if (w.isActive() && hitbox.intersects(w.getHitbox())) {
                w.setActive(false);
                playing.getPlayer().equipWeapon(w.getWeaponType());
                playing.increaseScore(50);
            }
        // Recoger armaduras
        for (Armor a : armors)
            if (a.isActive() && hitbox.intersects(a.getHitbox())) {
                a.setActive(false);
                playing.getPlayer().addShield(ARMOR_SHIELD[a.getArmorType()]);
                playing.increaseScore(75);
            }
    }

    public void applyEffectToPlayer(Potion p) {
        if (p.getObjType() == RED_POTION)
            playing.getPlayer().changeHealth(RED_POTION_VALUE);
        else
            playing.getPlayer().changePower(BLUE_POTION_VALUE);
    }

public void checkObjectHit(Rectangle2D.Float attackbox) {
    for (GameContainer gc : containers)
        if (gc.isActive() && !gc.doAnimation) {
            if (gc.getHitbox().intersects(attackbox)) {
                gc.setAnimation(true);
                
                int dropX = (int) (gc.getHitbox().x + gc.getHitbox().width / 2);
                int dropY = (int) (gc.getHitbox().y - gc.getHitbox().height / 2);
                
                
                if (gc.getObjType() == BOX) {
                    
                    int randomWeapon = (int) (Math.random() * Utilz.Constantes.WeaponConstants.NUM_WEAPONS);
                    weapons.add(new Weapon(dropX, dropY, randomWeapon));
                } else {
                    
                    if (Math.random() > 0.5) {
                        
                        potions.add(new Potion(dropX, dropY, RED_POTION));
                    } else {
                       
                        int randomArmor = (int) (Math.random() * Utilz.Constantes.WeaponConstants.NUM_ARMORS);
                        armors.add(new Armor(dropX, dropY, randomArmor));
                    }
                }
                
                playing.increaseScore(100);
                return;
            }
        }
}

    public void loadObjects(Level newLevel) {
        potions = new ArrayList<>(newLevel.getPotions());
        containers = new ArrayList<>(newLevel.getContainers());
        spikes = newLevel.getSpikes();
        cannons = newLevel.getCannons();
        weapons = new ArrayList<>(newLevel.getWeapons());
        armors = new ArrayList<>(newLevel.getArmors());
        projectiles.clear();
    }

    private void loadImgs() {
        BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
        potionImgs = new BufferedImage[2][7];
        for (int j = 0; j < potionImgs.length; j++)
            for (int i = 0; i < potionImgs[j].length; i++)
                potionImgs[j][i] = potionSprite.getSubimage(12 * i, 16 * j, 12, 16);

        BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
        containerImgs = new BufferedImage[2][8];
        for (int j = 0; j < containerImgs.length; j++)
            for (int i = 0; i < containerImgs[j].length; i++)
                containerImgs[j][i] = containerSprite.getSubimage(40 * i, 30 * j, 40, 30);

        spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);

        cannonImgs = new BufferedImage[7];
        BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.CANNON_ATLAS);
        for (int i = 0; i < cannonImgs.length; i++)
            cannonImgs[i] = temp.getSubimage(i * 40, 0, 40, 26);

        cannonBallImg = LoadSave.GetSpriteAtlas(LoadSave.CANNON_BALL);

        // Cargar atlas de armas y armaduras
        BufferedImage weaponAtlas = LoadSave.GetSpriteAtlas(LoadSave.WEAPON_ATLAS);
        weaponImgs = new BufferedImage[NUM_WEAPONS];
        for (int i = 0; i < weaponImgs.length; i++) {
            int row = WEAPON_COORDS[i][0];
            int col = WEAPON_COORDS[i][1];
            weaponImgs[i] = weaponAtlas.getSubimage(col * 32, row * 32, 32, 32);
        }
        armorImgs = new BufferedImage[NUM_ARMORS];
        for (int i = 0; i < armorImgs.length; i++) {
            int row = ARMOR_COORDS[i][0];
            int col = ARMOR_COORDS[i][1];
            armorImgs[i] = weaponAtlas.getSubimage(col * 32, row * 32, 32, 32);
        }
    }

    public void update(int[][] lvlData, Jugador player) {
        for (Potion p : potions)
            if (p.isActive())
                p.update();

        for (GameContainer gc : containers)
            if (gc.isActive())
                gc.update();

        updateCannons(lvlData, player);
        updateProjectiles(lvlData, player);
    }

    private void updateProjectiles(int[][] lvlData, Jugador player) {
        for (Projectile p : projectiles)
            if (p.isActive()) {
                p.updatePos();
                if (p.getHitbox().intersects(player.getHitbox())) {
                    player.changeHealth(-25);
                    p.setActive(false);
                } else if (IsProjectileHittingLevel(p, lvlData))
                    p.setActive(false);
            }
    }

    private void updateCannons(int[][] lvlData, Jugador player) {
        for (Cannon c : cannons) {
            if (!c.doAnimation)
                if (c.getTileY() == player.getTileY())
                    if (isPlayerInRange(c, player))
                        if (isPlayerInfrontOfCannon(c, player))
                            if (CanCannonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY()))
                                c.setAnimation(true);

            c.update();
            if (c.getAniIndex() == 4 && c.getAniTick() == 0)
                shootCannon(c);
        }
    }

    private boolean isPlayerInfrontOfCannon(Cannon c, Jugador player) {
        if (c.getObjType() == CANNON_LEFT) {
            if (c.getHitbox().x > player.getHitbox().x)
                return true;
        } else if (c.getHitbox().x < player.getHitbox().x)
            return true;
        return false;
    }

    private boolean isPlayerInRange(Cannon c, Jugador player) {
        int absValue = (int) Math.abs(player.getHitbox().x - c.getHitbox().x);
        return absValue <= Juego.TILES_SIZE * 5;
    }

    private void shootCannon(Cannon c) {
        int dir = 1;
        if (c.getObjType() == CANNON_LEFT)
            dir = -1;
        projectiles.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
    }

    public void draw(Graphics g, int xLvlOffset) {
        drawPotions(g, xLvlOffset);
        drawContainers(g, xLvlOffset);
        drawTraps(g, xLvlOffset);
        drawCannons(g, xLvlOffset);
        drawProjectiles(g, xLvlOffset);
        drawWeapons(g, xLvlOffset);
        drawArmors(g, xLvlOffset);
    }

    private void drawWeapons(Graphics g, int xLvlOffset) {
        for (Weapon w : weapons)
            if (w.isActive())
                g.drawImage(weaponImgs[w.getWeaponType()],
                    (int) (w.getHitbox().x - xLvlOffset),
                    (int) (w.getHitbox().y),
                    WEAPON_SIZE, WEAPON_SIZE, null);
    }

    private void drawArmors(Graphics g, int xLvlOffset) {
        for (Armor a : armors)
            if (a.isActive())
                g.drawImage(armorImgs[a.getArmorType()],
                    (int) (a.getHitbox().x - xLvlOffset),
                    (int) (a.getHitbox().y),
                    WEAPON_SIZE, WEAPON_SIZE, null);
    }

    private void drawProjectiles(Graphics g, int xLvlOffset) {
        for (Projectile p : projectiles)
            if (p.isActive())
                g.drawImage(cannonBallImg, (int) (p.getHitbox().x - xLvlOffset), (int) (p.getHitbox().y), CANNON_BALL_WIDTH, CANNON_BALL_HEIGHT, null);
    }

    private void drawCannons(Graphics g, int xLvlOffset) {
        for (Cannon c : cannons) {
            int x = (int) (c.getHitbox().x - xLvlOffset);
            int width = CANNON_WIDTH;
            if (c.getObjType() == CANNON_RIGHT) {
                x += width;
                width *= -1;
            }
            g.drawImage(cannonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y), width, CANNON_HEIGHT, null);
        }
    }

    private void drawTraps(Graphics g, int xLvlOffset) {
        for (Spike s : spikes)
            g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset), (int) (s.getHitbox().y - s.getyDrawOffset()), SPIKE_WIDTH, SPIKE_HEIGHT, null);
    }

    private void drawContainers(Graphics g, int xLvlOffset) {
        for (GameContainer gc : containers)
            if (gc.isActive()) {
                int type = 0;
                if (gc.getObjType() == BARREL)
                    type = 1;
                g.drawImage(containerImgs[type][gc.getAniIndex()],
                    (int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
                    (int) (gc.getHitbox().y - gc.getyDrawOffset()),
                    CONTAINER_WIDTH, CONTAINER_HEIGHT, null);
            }
    }

    private void drawPotions(Graphics g, int xLvlOffset) {
        for (Potion p : potions)
            if (p.isActive()) {
                int type = 0;
                if (p.getObjType() == RED_POTION)
                    type = 1;
                g.drawImage(potionImgs[type][p.getAniIndex()],
                    (int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
                    (int) (p.getHitbox().y - p.getyDrawOffset()),
                    POTION_WIDTH, POTION_HEIGHT, null);
            }
    }

    public void resetAllObjects() {
        loadObjects(playing.getLevelManager().getCurrentLevel());
        for (Potion p : potions) p.reset();
        for (GameContainer gc : containers) gc.reset();
        for (Cannon c : cannons) c.reset();
        for (Weapon w : weapons) w.setActive(true);
        for (Armor a : armors) a.setActive(true);
    }
}