package Objects;

import static Utilz.LoadSave.WeaponConstants.WEAPON_SIZE;
import java.awt.geom.Rectangle2D;


public class Weapon {
    private int x, y;
    private int weaponType;  // índice 0..6
    private Rectangle2D.Float hitbox;
    private boolean active = true;

    public Weapon(int x, int y, int weaponType) {
        this.x = x;
        this.y = y;
        this.weaponType = weaponType;
        hitbox = new Rectangle2D.Float(x, y, WEAPON_SIZE, WEAPON_SIZE);
    }

    public Rectangle2D.Float getHitbox() { return hitbox; }
    public int getWeaponType() { return weaponType; }
    public boolean isActive() { return active; }
    public void setActive(boolean a) { this.active = a; }
    public int getX() { return x; }
    public int getY() { return y; }
}