package Juegos;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Rectangulo {
    int x,y,w,h;
    int dirX=1,dirY=1;
    Color color;
    private Random random=new Random();
    public Rectangulo(int x, int y) {
        this.x = x;
        this.y = y;
        h=w=random.nextInt(50);
        color=newColor();
    }
    
    private Color newColor() {
            int cv=random.nextInt(255);
            int ca=random.nextInt(255);
            int cr=random.nextInt(255);
            color=new Color(cr, cv, ca);
            return color;
     }
     public void actualizarRect(int ancho, int alto)
     {
        x+=dirX;
        if(x>ancho-25||x<0)
            dirX*=-1;
        y+=dirY;
        if(y>alto-25||y<0)
            dirY*=-1;
     }
     public void draw(Graphics g){
        g.setColor(color);
        g.fillRect(x, y, w, h);
     }
}
