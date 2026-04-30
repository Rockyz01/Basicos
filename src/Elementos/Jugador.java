package Elementos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Audio.AudioPlayer;
import Juegos.Juego;
import Utilz.LoadSave;
import gamestates.Playing;

import static Utilz.Constantes.*;
import static Utilz.Constantes.ConstanteJugador.*;
import static Utilz.Constantes.WeaponConstants.*;
import static Utilz.LoadSave.WeaponConstants.WEAPON_DAMAGE;
import static Utilz.MetodoAyuda.*;

public class Jugador extends Cascaron {
    private BufferedImage[][] idleAni;
    private boolean moving = false;
    private boolean attacking = false;
    private boolean left, right, jump;
    private boolean hit = false;

    private int[][] lvlData;

    private float jumpSpeed;
    private float fallSpeedAfterCollision = 0.5f * Juego.SCALE;

    private BufferedImage statusBarImg;

    private int statusBarWidth = (int) (192 * Juego.SCALE);
    private int statusBarHeight = (int) (58 * Juego.SCALE);
    private int statusBarX = (int) (10 * Juego.SCALE);
    private int statusBarY = (int) (10 * Juego.SCALE);

    private int healthBarWidth = (int) (150 * Juego.SCALE);
    private int healthBarHeight = (int) (4 * Juego.SCALE);
    private int healthBarXStart = (int) (34 * Juego.SCALE);
    private int healthBarYStart = (int) (14 * Juego.SCALE);
    private int healthWidth = healthBarWidth;

    private int powerBarWidth = (int) (104 * Juego.SCALE);
    private int powerBarHeight = (int) (2 * Juego.SCALE);
    private int powerBarXStart = (int) (44 * Juego.SCALE);
    private int powerBarYStart = (int) (34 * Juego.SCALE);
    private int powerWidth = powerBarWidth;
    private int powerMaxValue = 200;
    private int powerValue = powerMaxValue;

    private int flipX = 0;
    private int flipW = 1;

    private boolean attackChecked;
    private Playing playing;

    private int tileY = 0;
    public boolean powerAttackActive;
    private int powerAttackTick;
    private int powerGrowSpeed = 15;
    private int powerGrowTick;

    private final PlayerCharacter playerCharacter;
    private BufferedImage[][] animations;

    // Sistema de armas y escudo
    private int equippedWeapon = -1;
    private int shield = 0;
    private int maxShield = 200;

    public Jugador(PlayerCharacter playerCharacter, Playing playing) {
        super(0, 0, (int) (playerCharacter.spriteW * Juego.SCALE), (int) (playerCharacter.spriteH * Juego.SCALE));
        this.playerCharacter = playerCharacter;
        this.playing = playing;
        this.state = INACTIVO;
        this.maxHealth = playerCharacter.maxHealth;
        this.currentHealth = playerCharacter.maxHealth;
        this.walkSpeed = Juego.SCALE * playerCharacter.walkSpeed;
        this.jumpSpeed = playerCharacter.jumpSpeed * Juego.SCALE;

        idleAni = LoadSave.loadAnimation(playerCharacter);
        animations = LoadSave.loadAnimation(playerCharacter);
        statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);
        initHitBox(playerCharacter.hitboxW, playerCharacter.hitboxH);
        initAttackBox();
    }

    public void setSpawn(Point spawn) {
        this.x = spawn.x;
        this.y = spawn.y;
        hitbox.x = x;
        hitbox.y = y;
    }

    private void initAttackBox() {
        attackBox = new Rectangle2D.Float(x, y, (int) (45 * Juego.SCALE), (int) (20 * Juego.SCALE));
        resetAttackBox();
    }

    public void update() {
        updatePowerBar();
        updateHealthBar();
        if (currentHealth <= 0) {
            if (state != MUERTO) {
                state = MUERTO;
                animTick = 0;
                animInd = 0;
                playing.setPlayerDying(true);
                playing.getJuego().getAudioPlayer().playEffect(AudioPlayer.DIE);
            } else if (animInd == playerCharacter.getNoSprite(MUERTO) - 1 && animTick >= ANI_SPEED - 1) {
                playing.setGameOver(true);
                playing.getJuego().getAudioPlayer().stopSong();
                playing.getJuego().getAudioPlayer().playEffect(AudioPlayer.GAMEOVER);
            } else
                actualizaAnim();
            return;
        }
        updateAttackBox();
        actualizaAnim();
        if (moving) {
            checkPotionTouched();
            checkSpikesTouched();
            tileY = (int) (hitbox.y / Juego.TILES_SIZE);
            if (powerAttackActive) {
                powerAttackTick++;
                if (powerAttackTick >= 35) {
                    powerAttackTick = 0;
                    powerAttackActive = false;
                }
            }
        }
        if (attacking || powerAttackActive)
            checkAttack();

        colocarAnim();
        actuPosicion();
    }

    private void checkSpikesTouched() {
        playing.checkSpikesTouched(this);
    }

    private void checkPotionTouched() {
        playing.checkPotionTouched(hitbox);
    }

    private void checkAttack() {
        if (attackChecked)
            return;
        attackChecked = true;
        if (powerAttackActive)
            attackChecked = false;
        playing.checkEnemyHit(attackBox);
        playing.checkObjectHit(attackBox);
        playing.getJuego().getAudioPlayer().playAttackSound();
    }

    private void updateAttackBox() {
        if (right && left) {
            if (flipW == 1)
                attackBox.x = hitbox.x + hitbox.width + (int) (Juego.SCALE * 10);
            else
                attackBox.x = hitbox.x - hitbox.width - (int) (Juego.SCALE * 10);
        } else if (right || (powerAttackActive && flipW == 1)) {
            attackBox.x = hitbox.x + hitbox.width + (int) (Juego.SCALE * 10);
        } else if (left || (powerAttackActive && flipW == -1)) {
            attackBox.x = hitbox.x - hitbox.width - (int) (Juego.SCALE * 10);
        }
        attackBox.y = hitbox.y + (Juego.SCALE * 10);
    }

    private void updateHealthBar() {
        healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
    }

    private void updatePowerBar() {
        powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBarWidth);
        powerGrowTick++;
        if (powerGrowTick >= powerGrowSpeed) {
            powerGrowTick = 0;
            changePower(1);
        }
    }

    public void loadLvlData(int[][] lvlData) {
        this.lvlData = lvlData;
        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void render(Graphics g, int lvlOffset) {
        g.drawImage(idleAni[playerCharacter.getRowIndex(state)][animInd],
                (int) (hitbox.x - playerCharacter.xDrawOffset) - lvlOffset + flipX,
                (int) (hitbox.y - playerCharacter.yDrawOffset),
                w * flipW, h, null);
        drawHitBox(g, lvlOffset);
        drawAttackBox(g, lvlOffset);
        drawUI(g);
    }

    private void drawUI(Graphics g) {
        g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);

        g.setColor(Color.RED);
        g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);

        g.setColor(Color.BLUE);
        g.fillRect(powerBarXStart + statusBarX, powerBarYStart + statusBarY, powerWidth, powerBarHeight);

        // Barra de escudo
        if (shield > 0) {
            int shieldBarWidth = (int) ((shield / (float) maxShield) * healthBarWidth);
            g.setColor(new Color(180, 180, 220));
            g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY - 6, shieldBarWidth, 4);
        }
    }

    private void actualizaAnim() {
        animTick++;
        if (animTick >= ANI_SPEED) {
            animTick = 0;
            animInd++;
            if (animInd >= playerCharacter.getNoSprite(state)) {
                animInd = 0;
                attacking = false;
                attackChecked = false;
                hit = false;
            }
        }
    }

    private void actuPosicion() {
        moving = false;
        if (jump)
            jump();
        if (!inAir)
            if (!powerAttackActive)
                if ((!left && !right) || (right && left))
                    return;

        float xSpeed = 0;
        if (left && !right) {
            xSpeed -= walkSpeed;
            flipX = w;
            flipW = -1;
        }
        if (right && !left) {
            xSpeed += walkSpeed;
            flipX = 0;
            flipW = 1;
        }
        if (powerAttackActive) {
            if ((!left && !right) || (left && right)) {
                if (flipW == -1)
                    xSpeed = -walkSpeed;
                else
                    xSpeed = walkSpeed;
            }
            xSpeed *= 3;
        }

        if (!inAir)
            if (!isEntityOnFloor(hitbox, lvlData))
                inAir = true;

        if (inAir && !powerAttackActive) {
            if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
                hitbox.y += airSpeed;
                airSpeed += GRAVITY;
                updateXPos(xSpeed);
            } else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0)
                    resetInAir();
                else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        } else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if (inAir) return;
        playing.getJuego().getAudioPlayer().playEffect(AudioPlayer.JUMP);
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void updateXPos(float xSpeed) {
        if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = GetEntityXPosNexttoWall(hitbox, xSpeed);
            if (powerAttackActive) {
                powerAttackActive = false;
                powerAttackTick = 0;
            }
        }
    }

    public void changeHealth(int valor) {
        if (valor < 0) {
            if (hit)
                return;
            else
                newHit();

            // El escudo absorbe primero
            int dmg = -valor;
            if (shield > 0) {
                if (shield >= dmg) {
                    shield -= dmg;
                    return;
                } else {
                    dmg -= shield;
                    shield = 0;
                    currentHealth -= dmg;
                }
            } else {
                currentHealth += valor;
            }
        } else {
            currentHealth += valor;
        }

        if (currentHealth <= 0) {
            currentHealth = 0;
        } else if (currentHealth >= maxHealth) {
            currentHealth = maxHealth;
        }
    }

    private void newHit() {
        hit = true;
        state = HERIDO;
        animTick = 0;
        animInd = 0;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void colocarAnim() {
        int starAnim = state;

        if (hit) {
            if (animInd <= playerCharacter.getNoSprite(HERIDO) - 1)
                return;
            else {
                hit = false;
                airSpeed = 0f;
                if (!isEntityOnFloor(hitbox, lvlData))
                    inAir = true;
            }
        }

        if (moving)
            state = CORRER;
        else
            state = INACTIVO;
        if (inAir) {
            if (airSpeed < 0)
                state = SALTAR;
            else
                state = CAYENDO;
        }

        if (powerAttackActive) {
            state = ATACAR1;
            animInd = 1;
            animTick = 0;
            return;
        }

        if (attacking) {
            state = ATACAR1;
            if (starAnim != ATACAR1) {
                animInd = 1;
                animTick = 0;
                return;
            }
        }
        if (starAnim != state)
            resetAnimTick();
    }

    public boolean isJump() { return jump; }
    public void setJump(boolean jump) { this.jump = jump; }

    private void resetAnimTick() {
        this.animTick = 0;
        animInd = 0;
    }

    public void setMoving(boolean moving) { this.moving = moving; }

    public void resetDirBooleans() { left = right = false; }

    public boolean isLeft() { return left; }
    public void setLeft(boolean left) { this.left = left; }
    public boolean isRight() { return right; }
    public void setRight(boolean right) { this.right = right; }
    public void setAttacking(boolean attacking) { this.attacking = attacking; }

    public void resetAll() {
        resetDirBooleans();
        inAir = false;
        attacking = false;
        moving = false;
        airSpeed = 0f;
        jump = false;
        hit = false;
        state = INACTIVO;
        currentHealth = maxHealth;
        shield = 0;
        equippedWeapon = -1;

        hitbox.x = x;
        hitbox.y = y;
        resetAttackBox();

        if (!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public void changePower(int valor) {
        powerValue += valor;
        if (powerValue >= powerMaxValue)
            powerValue = powerMaxValue;
        else if (powerValue <= 0)
            powerValue = 0;
    }

    public void kill() {
        currentHealth = 0;
    }

    private void resetAttackBox() {
        if (flipW == 1)
            attackBox.x = hitbox.x + hitbox.width + (int) (Juego.SCALE * 10);
        else
            attackBox.x = hitbox.x - hitbox.width - (int) (Juego.SCALE * 10);
    }

    public int getTileY() { return tileY; }

    public int getDamage() {
        int base = playerCharacter.damage;
        if (equippedWeapon >= 0)
            base += WEAPON_DAMAGE[equippedWeapon];
        return base;
    }

    public void equipWeapon(int weaponType) {
        this.equippedWeapon = weaponType;
    }

    public void addShield(int amount) {
        shield += amount;
        if (shield > maxShield)
            shield = maxShield;
    }

    public int getShield() { return shield; }

    public void powerAttack() {
        if (powerAttackActive) return;
        if (powerValue >= 60) {
            powerAttackActive = true;
            changePower(-60);
        }
    }
}