package Objects;

import static Utilz.LoadSave.WeaponConstants.WEAPON_SIZE;
import java.awt.geom.Rectangle2D;

public class Armor {
    private int x, y;
    private int armorType;  // índice 0..3
    private Rectangle2D.Float hitbox;
    private boolean active = true;

    public Armor(int x, int y, int armorType) {
        this.x = x;
        this.y = y;
        this.armorType = armorType;
        hitbox = new Rectangle2D.Float(x, y, WEAPON_SIZE, WEAPON_SIZE);
    }

    public Rectangle2D.Float getHitbox() { return hitbox; }
    public int getArmorType() { return armorType; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public int getX() { return x; }
    public int getY() { return y; }
}