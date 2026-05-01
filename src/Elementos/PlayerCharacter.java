package Elementos;
import Juegos.Juego;
import static Utilz.Constantes.ConstanteJugador.*;
import Utilz.LoadSave;

public enum PlayerCharacter {
    p1(
        8, 8, 6, 4, 7, 3, 18,
        7, 5, 2, 4, 0, 1, 3,
        LoadSave.PLAYER_ATLAS,
        8, 18, 96, 64,
        20, 27,
        36, 36,
        // Stats: balanceado
        100, 1.0f, -2.25f, 10
    ),
    p2(
        8, 8, 6, 4, 7, 1, 18,
        0, 1, 2, 3, 4, 5, 6,
        LoadSave.PLAYER_ATLAS2,
        8, 18, 96, 64,
        20, 27,
        36, 36,
        // Stats: tanque (mucha vida, lento, fuerte)
        150, 0.8f, -2.25f, 15
    ),
    p3(
        8, 8, 6, 4, 7, 3, 18,
        7, 5, 2, 4, 0, 1, 3,
        LoadSave.PLAYER_ATLAS3,
        8, 18, 96, 64,
        20, 27,
        36, 36,
        // Stats: rápido (poca vida, muy rápido, salto alto)
        // DAÑO DESCOMUNAL para pruebas: mata cualquier enemigo de un golpe (one-shot).
        70, 1.5f, -2.75f, 9999
    ),
    p4(
        8, 8, 6, 4, 7, 1, 18,
        0, 1, 2, 3, 4, 5, 6,
        LoadSave.PLAYER_ATLAS4,
        8, 18, 96, 64,
        20, 27,
        36, 36,
        // Stats: saltarín (salto altísimo)
        100, 1.1f, -3.25f, 10
    );

    public int spriteA_INACTIVO, spriteA_CORRER, spriteA_SALTAR, spriteA_CAYENDO, spriteA_ATACAR1, spriteA_HERIDO, spriteA_MUERTO;
    public int row_INACTIVO, row_CORRER, row_SALTAR, row_CAYENDO, row_ATACAR1, row_HERIDO, row_MUERTO;
    public String playerAtlas;
    public int rowA, colA;
    public int spriteW, spriteH;
    public int hitboxW, hitboxH;
    public int xDrawOffset, yDrawOffset;

    // NUEVOS STATS
    public int maxHealth;
    public float walkSpeed;
    public float jumpSpeed;
    public int damage;

    PlayerCharacter(int spriteA_INACTIVO, int spriteA_CORRER, int spriteA_SALTAR, int spriteA_CAYENDO,
            int spriteA_ATACAR1, int spriteA_HERIDO, int spriteA_MUERTO,
            int row_INACTIVO, int row_CORRER, int row_SALTAR, int row_CAYENDO,
            int row_ATACAR1, int row_HERIDO, int row_MUERTO,
            String playerAtlas, int rowA, int colA, int spriteW, int spriteH,
            int hitboxW, int hitboxH,
            int xDrawOffset, int yDrawOffset,
            int maxHealth, float walkSpeed, float jumpSpeed, int damage) {

        this.spriteA_INACTIVO = spriteA_INACTIVO;
        this.spriteA_CORRER = spriteA_CORRER;
        this.spriteA_SALTAR = spriteA_SALTAR;
        this.spriteA_CAYENDO = spriteA_CAYENDO;
        this.spriteA_ATACAR1 = spriteA_ATACAR1;
        this.spriteA_HERIDO = spriteA_HERIDO;
        this.spriteA_MUERTO = spriteA_MUERTO;

        this.row_ATACAR1 = row_ATACAR1;
        this.row_HERIDO = row_HERIDO;
        this.row_SALTAR = row_SALTAR;
        this.row_MUERTO = row_MUERTO;
        this.row_CAYENDO = row_CAYENDO;
        this.row_CORRER = row_CORRER;
        this.row_INACTIVO = row_INACTIVO;

        this.playerAtlas = playerAtlas;
        this.rowA = rowA;
        this.colA = colA;
        this.spriteW = spriteW;
        this.spriteH = spriteH;

        this.hitboxW = hitboxW;
        this.hitboxH = hitboxH;

        this.xDrawOffset = (int) (xDrawOffset * Juego.SCALE);
        this.yDrawOffset = (int) (yDrawOffset * Juego.SCALE);

        this.maxHealth = maxHealth;
        this.walkSpeed = walkSpeed;
        this.jumpSpeed = jumpSpeed;
        this.damage = damage;
    }

    public int getNoSprite(int player_action) {
        switch (player_action) {
            case INACTIVO: return spriteA_INACTIVO;
            case CORRER: return spriteA_CORRER;
            case SALTAR: return spriteA_SALTAR;
            case CAYENDO: return spriteA_CAYENDO;
            case ATACAR1: return spriteA_ATACAR1;
            case HERIDO: return spriteA_HERIDO;
            case MUERTO: return spriteA_MUERTO;
            default: return 1;
        }
    }

    public int getRowIndex(int player_action) {
        switch (player_action) {
            case INACTIVO: return row_INACTIVO;
            case CORRER: return row_CORRER;
            case SALTAR: return row_SALTAR;
            case CAYENDO: return row_CAYENDO;
            case ATACAR1: return row_ATACAR1;
            case HERIDO: return row_HERIDO;
            case MUERTO: return row_MUERTO;
            default: return 1;
        }
    }

    public String getPlayerAtlas() { return playerAtlas; }
    public int getRowA() { return rowA; }
    public int getColA() { return colA; }
    public int getSpriteW() { return spriteW; }
    public int getSpriteH() { return spriteH; }
}