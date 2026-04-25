package Objects;

import static Utilz.Constantes.ANI_SPEED;
import static Utilz.Constantes.ObjectConstants.*;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import Juegos.Juego;

public class GameObject {

    protected int x,y,objType;
    protected Rectangle2D.Float hitbox;
    protected boolean doAnimation,active=true;
    protected int animTick,animInd;
    protected int xDrawOffset,yDrawOffset;

    public GameObject(int x,int y,int objType) {
        this.x = x;
        this.y = y;
        this.objType = objType;

    }
	protected void updateAnimationTick() {
		animTick++;
		if (animTick >= ANI_SPEED) {
			animTick = 0;
			animInd++;
			if (animInd >= GetSpriteAmount(objType)) {
				animInd = 0;
				if (objType == BARREL || objType == BOX) {
					doAnimation = false;
					active = false;
				}else if (objType == CANNON_LEFT || objType == CANNON_RIGHT)
                doAnimation = false;
			}
		}
	}

    public void reset(){
        animInd=0;
        animTick=0;
        active=true;

        if (objType == BARREL || objType == BOX ||objType ==CANNON_LEFT || objType==CANNON_RIGHT) 
            doAnimation = false;
            else
        doAnimation=true;
    }


       protected void initHitBox(float w, float h) {
        hitbox = new Rectangle2D.Float( x, y,(int)( w*Juego.SCALE),(int)( h*Juego.SCALE));
    }

    public void drawHitBox(Graphics g, int lvlOffset) {
        g.setColor(Color.RED);
        g.drawRect((int) hitbox.x - lvlOffset,
                (int) hitbox.y,
                (int) hitbox.width,
                (int) hitbox.height);
    }
    public int getObjType() {
        return objType;
    }
    public Rectangle2D.Float getHitbox() {
        return hitbox;
    }

    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    } 
    public void setAnimation(boolean doAnimation){
        this.doAnimation = doAnimation;
    }
    public int getxDrawOffset() {
        return xDrawOffset;
    }

    public int getyDrawOffset() {
        return yDrawOffset;
    }

    public int getAniIndex(){
        return animInd;
    }
    public int getAniTick() {
		return animTick;
	}
}
