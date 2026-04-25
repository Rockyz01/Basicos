package Elementos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import Juegos.Juego;

public abstract class Cascaron {
    protected float x, y;
    protected int h, w;
    protected Rectangle2D.Float hitbox;
    protected int animInd, animTick = 0;
    protected int state;
    protected float airSpeed;
    protected boolean inAir = false;
    protected int maxHealth;
    protected int currentHealth;
    protected Rectangle2D.Float attackBox;
    protected float walkSpeed = 2.0f*Juego.SCALE;

    public Cascaron(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.w = w;
    }
    protected void drawAttackBox(Graphics g, int lvlOffsetX) {
        g.setColor(Color.red);
        g.drawRect((int)attackBox.x -lvlOffsetX,(int)attackBox.y,(int)attackBox.width,(int)attackBox.height);
    }

    protected void drawHitBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
     /*    g.drawRect((int) hitbox.x - lvlOffset,
                (int) hitbox.y,
                (int) hitbox.width,
                (int) hitbox.height);*/
    }

    protected void initHitBox(float w, float h) {
        hitbox = new Rectangle2D.Float( x, y,(int)( w*Juego.SCALE),(int)( h*Juego.SCALE));
    }

    protected Rectangle2D getHitBox() {
        return hitbox;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getH() {
        return h;
    }

    public int getW() {
        return w;
    }

    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }
    public int getEnemyState(){
        return state;
    }
    public int getAniIndex(){
        return animInd;
    }
    public int getCurrentHealth(){
        return currentHealth;
    }
    protected void newState(int state) {
		this.state = state;
		animTick = 0;
		animInd = 0;
	}
}
