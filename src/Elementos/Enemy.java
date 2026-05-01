package Elementos;

import Juegos.Juego;
import static Utilz.Constantes.*;
import static Utilz.Constantes.Direccion.*;
import static Utilz.Constantes.EnemyConstants.*;
import static Utilz.MetodoAyuda.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

public abstract class Enemy extends Cascaron{

    protected int enemyType;
    protected boolean firstUpdate=true;
    protected int walkDir=LEFT;
    protected int tileY;
    protected float attackDistance=Juego.TILES_SIZE;
    protected boolean active=true;
    protected boolean attackChecked;

    public Enemy(float x, float y, int w, int h,int enemyType) {
        super(x, y, w, h);
        this.enemyType=enemyType;
        maxHealth=GetMaxHealth(enemyType);
        currentHealth=maxHealth;
        walkSpeed=Juego.SCALE*0.40f;
    }

    protected void firstUpdateCheck(int [][] lvlData){
                    if(!isEntityOnFloor(hitbox,lvlData))
                inAir=true;
                firstUpdate=false;
    }
    protected void updateInAir(int[][]lvlData){
                    if(CanMoveHere(hitbox.x,hitbox.y+airSpeed,hitbox.width,hitbox.height,lvlData)){
                        hitbox.y+=airSpeed;
                        airSpeed+=GRAVITY;

                    }else{
                        inAir=false;
                        hitbox.y=GetEntityYPosUnderRoofOrAboveFloor(hitbox,airSpeed);
                    tileY=(int)(hitbox.y/Juego.TILES_SIZE);
                    }
                }
    
                protected void move(int[][] lvlData){
                                                float xSpeed=0;
                            if(walkDir==LEFT)
                            xSpeed = -walkSpeed;
                            else
                            xSpeed = walkSpeed;
                            if(CanMoveHere(hitbox.x+xSpeed,hitbox.y,hitbox.width,hitbox.height, lvlData))
                            if(IsFloor(hitbox,xSpeed,lvlData)){
                                hitbox.x +=xSpeed;
                                return;
                            }
                            changeWalkDir();
                }
                protected void turnTowardsPlayer(Jugador jugador) {
                    if (jugador.hitbox.x > hitbox.x)
                        walkDir = RIGHT;
                    else
                        walkDir = LEFT;
                }
            
protected boolean canSeePlayer(int[][] lvlData, Jugador jugador) {
    int playerTileY = (int) (jugador.getHitbox().y / Juego.TILES_SIZE);
    if (playerTileY == tileY)
        if (isPlayerInRange(jugador)) {
            if (IsSightClear(lvlData, hitbox, jugador.hitbox, tileY))
                return true;
        }

    return false;
}

protected boolean isPlayerInRange(Jugador jugador) {
    int absValue = (int) Math.abs(jugador.hitbox.x - hitbox.x);
    return absValue <= attackDistance * 7;
}

protected boolean isPlayerCloseForAttack(Jugador jugador) {
    int absValue = (int) Math.abs(jugador.hitbox.x - hitbox.x);
    return absValue <= attackDistance;
}

protected void newState(int enemyState){
    this.state=enemyState;
    animTick=0;
    animInd=0;
}
public void hurt(int amount) {
    currentHealth -= amount;
    if (currentHealth <= 0)
        newState(MUERTO);
    else
        newState(GOLPE);
}
public void checkBossTouched(Rectangle2D.Float hitbox, Jugador player) {
    if (hitbox.intersects(player.getHitbox())) {
        player.changeHealth(-GetEnemyDmg(enemyType));
    }
}
protected void checkPlayerHit(Rectangle2D.Float attackBox, Jugador jugador) {
    if (attackBox.intersects(jugador.hitbox))
    jugador.changeHealth(-GetEnemyDmg(enemyType));
    attackChecked = true;

}

    protected void updateAnimationTick(){
        animTick ++;
        if(animTick>= ANI_SPEED){
            animTick=0;
            animInd ++;
            if(animInd>=GetSpriteAmount(enemyType, state)){
                animInd=0;

                switch (state) {
                    case ATACAR1, ATAQUEC, GOLPE -> state = INACTIVO;
                    case MUERTO -> active = false;
                    }
            }
        }
    }
    protected void changeWalkDir() {
        if(walkDir==LEFT)
        walkDir=RIGHT;
        else
        walkDir=LEFT;
    }

    /** Mapea estado → fila del sprite sheet. Sobreescribir en subclases con sprites nuevos. */
    public int getAniRowOffset() {
        return state;
    }
    public void resetEnemy(){
        hitbox.x=x;
        hitbox.y=y;
        firstUpdate=true;
        currentHealth=maxHealth;
        newState(INACTIVO);
        active=true;
        airSpeed=0;
    }
    public boolean isActive(){
        return active;
    }
    public void drawHealthBar(Graphics g, int lvlOffset) {
    // Solo mostrar si el enemigo ha recibido daño
    if (currentHealth >= maxHealth)
        return;

    int barWidth = (int) (40 * Juego.SCALE);
    int barHeight = (int) (4 * Juego.SCALE);
    int barX = (int) (hitbox.x + hitbox.width / 2 - barWidth / 2) - lvlOffset;
    int barY = (int) (hitbox.y - 10 * Juego.SCALE);

    // Fondo negro
    g.setColor(Color.BLACK);
    g.fillRect(barX - 1, barY - 1, barWidth + 2, barHeight + 2);

    // Fondo rojo oscuro
    g.setColor(new Color(80, 0, 0));
    g.fillRect(barX, barY, barWidth, barHeight);

    // Vida actual en verde
    int healthWidth = (int) ((currentHealth / (float) maxHealth) * barWidth);
    g.setColor(Color.GREEN);
    g.fillRect(barX, barY, healthWidth, barHeight);
}

    public void update(int[][] lvlData, Jugador jugador) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }
}
