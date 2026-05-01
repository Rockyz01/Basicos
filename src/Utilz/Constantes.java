package Utilz;

import Juegos.Juego;

public class Constantes {

    public static final float GRAVITY = 0.04f*Juego.SCALE;
    public static final int ANI_SPEED = 25;

    public static class Projectiles{
		public static final int CANNON_BALL_DEFAULT_WIDTH = 15;
		public static final int CANNON_BALL_DEFAULT_HEIGHT = 15;
		
		public static final int CANNON_BALL_WIDTH = (int)(Juego.SCALE * CANNON_BALL_DEFAULT_WIDTH);
		public static final int CANNON_BALL_HEIGHT = (int)(Juego.SCALE * CANNON_BALL_DEFAULT_HEIGHT);
		public static final float SPEED = 0.75f * Juego.SCALE;
	}

    public static class ObjectConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
        public static final int SPIKE = 4;
        public static final int CANNON_LEFT = 5;
        public static final int CANNON_RIGHT = 6;
        public static final int STATUE = 7; 

		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_WIDTH = (int) (Juego.SCALE * CONTAINER_WIDTH_DEFAULT);
		public static final int CONTAINER_HEIGHT = (int) (Juego.SCALE * CONTAINER_HEIGHT_DEFAULT);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_WIDTH = (int) (Juego.SCALE * POTION_WIDTH_DEFAULT);
		public static final int POTION_HEIGHT = (int) (Juego.SCALE * POTION_HEIGHT_DEFAULT);

        public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int) (Juego.SCALE * SPIKE_WIDTH_DEFAULT);
		public static final int SPIKE_HEIGHT = (int) (Juego.SCALE * SPIKE_HEIGHT_DEFAULT);

        public static final int CANNON_WIDTH_DEFAULT = 40;
		public static final int CANNON_HEIGHT_DEFAULT = 26;
		public static final int CANNON_WIDTH = (int) (Juego.SCALE * CANNON_WIDTH_DEFAULT);
		public static final int CANNON_HEIGHT = (int) (Juego.SCALE * CANNON_HEIGHT_DEFAULT);

		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
                case CANNON_LEFT,CANNON_RIGHT:
                return 7;
			}
			return 1;
		}
	} 

    public static class EnemyConstants {
        // Nivel 1
        public static final int GOLEM = 0;
        public static final int SKELETONW = 1;
        public static final int SKELETONY = 2;
        public static final int ENEMIGO1 = 3;

        // Nivel 2
        public static final int ENEMIGO2  = 4;
        public static final int ENEMIGO3  = 5;
        public static final int ENEMIGO4  = 6;
        public static final int ENEMIGO5 = 7;

        // Nivel 3
        public static final int ENEMIGO6  = 8;
        public static final int ENEMIGO7  = 9;
        public static final int ENEMIGO8  = 10;
        public static final int ENEMIGO9  = 11;

        // Bosses
        public static final int ENEMIGO10  = 12;
        public static final int ENEMIGO11 = 13;
        public static final int ENEMIGO12  = 14;
        public static final int TROL_JEFE  = 15;
        public static final int ENEMIGO13 = 16;
        public static final int ENEMIGO14 = 17;
        public static final int ENEMIGO15 = 18;
        public static final int BOSS_ANCIENT   = 19;
        public static final int BOSS_VIKING    = 20;
        public static final int BOSS_TOAD_KING = 21;

        //        ACTIONS
        public static final int INACTIVO=0;
        public static final int CAMINAR=1;
        public static final int CORRER=2;
        public static final int ATAQUEC=3;
        public static final int ATACAR1=4;
        public static final int ATACAR2=5;
        public static final int ATACAR3=6;
        public static final int GOLPE=7;
        public static final int MUERTO=8;

        public static final int GOLEM_WIDTH_DEFAULT = 96;
        public static final int GOLEM_HEIGHT_DEFAULT = 64;
        public static final int GOLEM_WIDTH = (int)(GOLEM_WIDTH_DEFAULT * Juego.SCALE);
        public static final int GOLEM_HEIGHT = (int)(GOLEM_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int GOLEM_DRAWOFFSET_X=(int)(37*Juego.SCALE);
        public static final int GOLEM_DRAWOFFSET_Y=(int)(32*Juego.SCALE);

        public static final int SKELETONW_WIDTH_DEFAULT = 96;
        public static final int SKELETONW_HEIGHT_DEFAULT = 42;
        public static final int SKELETONW_WIDTH = (int)(SKELETONW_WIDTH_DEFAULT*Juego.SCALE);
        public static final int SKELETONW_HEIGHT= (int)(SKELETONW_HEIGHT_DEFAULT*Juego.SCALE);
        public static final int SKELETONW_DRAWOFFSET_X=(int)(34*Juego.SCALE);
        public static final int SKELETONW_DRAWOFFSET_Y=(int)(18*Juego.SCALE);

        public static final int SKELETONY_WIDTH_DEFAULT = 96;
        public static final int SKELETONY_HEIGHT_DEFAULT = 42;
        public static final int SKELETONY_WIDTH = (int)(SKELETONY_WIDTH_DEFAULT*Juego.SCALE);
        public static final int SKELETONY_HEIGHT= (int)(SKELETONY_HEIGHT_DEFAULT*Juego.SCALE);
        public static final int SKELETONY_DRAWOFFSET_X=(int)(34*Juego.SCALE);
        public static final int SKELETONY_DRAWOFFSET_Y=(int)(18*Juego.SCALE);

        public static final int ENEMIGO1_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO1_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO1_WIDTH  = (int) (ENEMIGO1_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO1_HEIGHT = (int) (ENEMIGO1_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO1_DRAWOFFSET_X = (int) (17 * Juego.SCALE);
        public static final int ENEMIGO1_DRAWOFFSET_Y = (int) (13 * Juego.SCALE);
        
        public static final int ENEMIGO2_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO2_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO2_WIDTH  = (int) (ENEMIGO2_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO2_HEIGHT = (int) (ENEMIGO2_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO2_DRAWOFFSET_X = (int) (0 * Juego.SCALE);
        public static final int ENEMIGO2_DRAWOFFSET_Y = (int) (18 * Juego.SCALE);
        
        public static final int ENEMIGO3_WIDTH_DEFAULT  = 72;
        public static final int ENEMIGO3_HEIGHT_DEFAULT = 72;
        public static final int ENEMIGO3_WIDTH  = (int) (ENEMIGO3_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO3_HEIGHT = (int) (ENEMIGO3_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO3_DRAWOFFSET_X = (int) (1 * Juego.SCALE);
        public static final int ENEMIGO3_DRAWOFFSET_Y = (int) (31 * Juego.SCALE);

        public static final int ENEMIGO4_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO4_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO4_WIDTH  = (int) (ENEMIGO4_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO4_HEIGHT = (int) (ENEMIGO4_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO4_DRAWOFFSET_X = (int) (20 * Juego.SCALE);
        public static final int ENEMIGO4_DRAWOFFSET_Y = (int) (13 * Juego.SCALE);

        public static final int ENEMIGO5_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO5_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO5_WIDTH  = (int) (ENEMIGO5_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO5_HEIGHT = (int) (ENEMIGO5_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO5_DRAWOFFSET_X = (int) (8 * Juego.SCALE);
        public static final int ENEMIGO5_DRAWOFFSET_Y = (int) (16 * Juego.SCALE);

        public static final int ENEMIGO6_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO6_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO6_WIDTH  = (int) (ENEMIGO6_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO6_HEIGHT = (int) (ENEMIGO6_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO6_DRAWOFFSET_X = (int) (3 * Juego.SCALE);
        public static final int ENEMIGO6_DRAWOFFSET_Y = (int) (20 * Juego.SCALE);

        public static final int ENEMIGO7_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO7_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO7_WIDTH  = (int) (ENEMIGO7_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO7_HEIGHT = (int) (ENEMIGO7_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO7_DRAWOFFSET_X = (int) (0 * Juego.SCALE);
        public static final int ENEMIGO7_DRAWOFFSET_Y = (int) (25 * Juego.SCALE);

        public static final int ENEMIGO8_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO8_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO8_WIDTH  = (int) (ENEMIGO8_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO8_HEIGHT = (int) (ENEMIGO8_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO8_DRAWOFFSET_X = (int) (24 * Juego.SCALE);
        public static final int ENEMIGO8_DRAWOFFSET_Y = (int) (29 * Juego.SCALE);

        public static final int ENEMIGO9_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO9_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO9_WIDTH  = (int) (ENEMIGO9_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO9_HEIGHT = (int) (ENEMIGO9_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO9_DRAWOFFSET_X = (int) (0 * Juego.SCALE);
        public static final int ENEMIGO9_DRAWOFFSET_Y = (int) (32 * Juego.SCALE);

        public static final int ENEMIGO10_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO10_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO10_WIDTH  = (int) (ENEMIGO10_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO10_HEIGHT = (int) (ENEMIGO10_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO10_DRAWOFFSET_X = (int) (8 * Juego.SCALE);
        public static final int ENEMIGO10_DRAWOFFSET_Y = (int) (25 * Juego.SCALE);

        public static final int ENEMIGO11_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO11_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO11_WIDTH  = (int) (ENEMIGO11_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO11_HEIGHT = (int) (ENEMIGO11_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO11_DRAWOFFSET_X = (int) (8 * Juego.SCALE);
        public static final int ENEMIGO11_DRAWOFFSET_Y = (int) (16 * Juego.SCALE);

        public static final int ENEMIGO12_WIDTH_DEFAULT  = 48;
        public static final int ENEMIGO12_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO12_WIDTH  = (int) (ENEMIGO12_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int ENEMIGO12_HEIGHT = (int) (ENEMIGO12_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO12_DRAWOFFSET_X = (int) (0 * Juego.SCALE);
        public static final int ENEMIGO12_DRAWOFFSET_Y = (int) (25 * Juego.SCALE);

        public static final int ENEMIGO13_WIDTH_DEFAULT = 48;
        public static final int ENEMIGO13_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO13_WIDTH = (int) (ENEMIGO13_WIDTH_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO13_HEIGHT = (int) (ENEMIGO13_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO13_DRAWOFFSET_X = (int) (7 * Juego.SCALE);
        public static final int ENEMIGO13_DRAWOFFSET_Y = (int) (27 * Juego.SCALE);

        public static final int ENEMIGO14_WIDTH_DEFAULT = 48;
        public static final int ENEMIGO14_HEIGHT_DEFAULT = 48;
        public static final int ENEMIGO14_WIDTH = (int) (ENEMIGO14_WIDTH_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO14_HEIGHT = (int) (ENEMIGO14_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO14_DRAWOFFSET_X = (int) (8 * Juego.SCALE);
        public static final int ENEMIGO14_DRAWOFFSET_Y = (int) (16 * Juego.SCALE);

        public static final int ENEMIGO15_WIDTH_DEFAULT = 48;
        public static final int ENEMIGO15_HEIGHT_DEFAULT = 50;
        public static final int ENEMIGO15_WIDTH = (int) (ENEMIGO15_WIDTH_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO15_HEIGHT = (int) (ENEMIGO15_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int ENEMIGO15_DRAWOFFSET_X = (int) (13 * Juego.SCALE);
        public static final int ENEMIGO15_DRAWOFFSET_Y = (int) (16 * Juego.SCALE);

        public static final int TROL_JEFE_WIDTH_DEFAULT  = 96;
        public static final int TROL_JEFE_HEIGHT_DEFAULT = 96;
        public static final int TROL_JEFE_WIDTH  = (int) (TROL_JEFE_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int TROL_JEFE_HEIGHT = (int) (TROL_JEFE_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int TROL_JEFE_DRAWOFFSET_X = (int) (33 * Juego.SCALE);
        public static final int TROL_JEFE_DRAWOFFSET_Y = (int) (66 * Juego.SCALE);

        public static final int BOSS_ANCIENT_WIDTH_DEFAULT    = 72;
        public static final int BOSS_ANCIENT_HEIGHT_DEFAULT   = 72;
        public static final int BOSS_ANCIENT_WIDTH    = (int)(BOSS_ANCIENT_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int BOSS_ANCIENT_HEIGHT   = (int)(BOSS_ANCIENT_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int BOSS_ANCIENT_DRAWOFFSET_X = (int)(20 * Juego.SCALE);
        public static final int BOSS_ANCIENT_DRAWOFFSET_Y = (int)(40 * Juego.SCALE);

        public static final int BOSS_VIKING_WIDTH_DEFAULT     = 72;
        public static final int BOSS_VIKING_HEIGHT_DEFAULT    = 72;
        public static final int BOSS_VIKING_WIDTH    = (int)(BOSS_VIKING_WIDTH_DEFAULT    * Juego.SCALE);
        public static final int BOSS_VIKING_HEIGHT   = (int)(BOSS_VIKING_HEIGHT_DEFAULT   * Juego.SCALE);
        public static final int BOSS_VIKING_DRAWOFFSET_X  = (int)(32 * Juego.SCALE);
        public static final int BOSS_VIKING_DRAWOFFSET_Y  = (int)(40 * Juego.SCALE);

        public static final int BOSS_TOAD_KING_WIDTH_DEFAULT  = 72;
        public static final int BOSS_TOAD_KING_HEIGHT_DEFAULT = 72;
        public static final int BOSS_TOAD_KING_WIDTH  = (int)(BOSS_TOAD_KING_WIDTH_DEFAULT  * Juego.SCALE);
        public static final int BOSS_TOAD_KING_HEIGHT = (int)(BOSS_TOAD_KING_HEIGHT_DEFAULT * Juego.SCALE);
        public static final int BOSS_TOAD_KING_DRAWOFFSET_X = (int)(20 * Juego.SCALE);
        public static final int BOSS_TOAD_KING_DRAWOFFSET_Y = (int)(40 * Juego.SCALE);

        public static int GetSpriteAmount(int enemy_type, int enemy_state) {
            switch (enemy_type) {
                case GOLEM:
                    switch (enemy_state) {
                        case INACTIVO: return 8;
                        case CAMINAR:  return 10;
                        case CORRER:   return 8;
                        case ATAQUEC:  return 10;
                        case ATACAR1:  return 9;
                        case ATACAR2:  return 4;
                        case ATACAR3:  return 7;
                        case GOLPE:    return 5;
                        case MUERTO:   return 10;
                        default:       return 1;
                    }
case SKELETONW:
                case SKELETONY:
                    switch (enemy_state) {
                        case INACTIVO: return 5;  // 5 dibujos
                        case CAMINAR:
                        case CORRER:   return 8;  // 8 dibujos
                        case ATACAR1:  return 8;  // 8 dibujos
                        case ATAQUEC:  return 10; // 10 dibujos
                        case MUERTO:   return 10; // 10 dibujos
                        case GOLPE:    return 4;  // 4 dibujos
                        default:       return 1;
                    }
                case ENEMIGO1: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 4;
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 6;
                        default:       return 1;
                    }
                case ENEMIGO2: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 4;
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 2;
                        default:       return 1;
                    }
                case ENEMIGO3: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 5;
                        case CORRER:   return 5;
                        case ATACAR1:  return 4;
                        case ATAQUEC:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO4: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 6;
                        case CORRER:   return 6;
                        case ATACAR1:  return 6;
                        case GOLPE:    return 2;
                        case MUERTO:   return 5;
                        default:       return 1;
                    }
                case ENEMIGO5: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 6;
                        case CORRER:   return 6;
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 6;
                        default:       return 1;
                    }
                case ENEMIGO6: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 4;
                        case ATACAR1:  return 5;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO7: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 6;
                        case ATAQUEC:
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO8: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 4;
                        case ATAQUEC:
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO9: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 4;
                        case ATAQUEC:
                        case ATACAR1:  return 6;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO10: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 4;
                        case ATAQUEC:
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO11: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 4;
                        case ATAQUEC:
                        case ATACAR1:  return 2;
                        case GOLPE:    return 4;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO12: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR:
                        case CORRER:   return 6;
                        case ATAQUEC:
                        case ATACAR1:  return 4;
                        case GOLPE:    return 2;
                        case MUERTO:   return 4;
                        default:       return 1;
                    }
                case ENEMIGO13: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR: 
                        case CORRER: return 4;
                        case ATACAR1: 
                        case ATAQUEC: return 4;
                        case GOLPE: return 2;
                        case MUERTO: return 4;
                        default: return 1;
                    }
                case ENEMIGO14: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR: 
                        case CORRER: return 6;
                        case ATACAR1: 
                        case ATAQUEC: return 4;
                        case GOLPE: return 2;
                        case MUERTO: return 4;
                        default: return 1;
                    }
                case ENEMIGO15: 
                    switch (enemy_state) {
                        case INACTIVO: return 4;
                        case CAMINAR: 
                        case CORRER: return 6;
                        case ATACAR1: return 6;
                        case ATAQUEC: return 6;
                        case GOLPE: return 2;
                        case MUERTO: return 4;
                        default: return 1;
                    }
                case TROL_JEFE:
                    switch (enemy_state) {
case ATACAR1:  return 4;
                        case ATACAR2:  return 4;
                        case ATACAR3:  return 4;
                        case ATAQUEC:  return 5; // ¡Eran 5, no 6!
                        case MUERTO:   return 4; // ¡Eran 4, no 6!
                        case GOLPE:    return 2;
                        case INACTIVO: return 4;
                        case CAMINAR:  return 6;
                        case CORRER:   return 6;
                        default:       return 1;
                    }
                case BOSS_ANCIENT:
                    switch (enemy_state) {
case ATACAR1:  return 4;
                        case ATACAR2:  return 5; // ¡Eran 5, no 6!
                        case ATACAR3:  return 4;
                        case ATAQUEC:  return 5;
                        case MUERTO:   return 4;
                        case GOLPE:    return 2;
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 4;
                        default:       return 1;
                    }
                case BOSS_VIKING:
                    switch (enemy_state) {
case ATACAR1:  return 5; // ¡Eran 5, no 6!
                        case ATACAR2:  return 4;
                        case ATACAR3:  return 4;
                        case ATAQUEC:  return 5; // ¡Eran 5, no 6!
                        case MUERTO:   return 4;
                        case GOLPE:    return 2;
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 6;
                        default:       return 1;
                    }
                case BOSS_TOAD_KING:
                    switch (enemy_state) {
case ATACAR1:  return 4;
                        case ATACAR2:  return 4;
                        case ATACAR3:  return 5; // ¡Eran 5, no 6!
                        case ATAQUEC:  return 4;
                        case MUERTO:   return 4;
                        case GOLPE:    return 2;
                        case INACTIVO: return 4;
                        case CAMINAR:  return 4;
                        case CORRER:   return 5; // ¡Eran 5, no 6!
                        default:       return 1;
                    }
                default:
                    return 1;
            }
        }

        public static int GetMaxHealth(int enemyType) {
            switch (enemyType) {
                case GOLEM:     return 60;
                case SKELETONW: return 50;
                case SKELETONY: return 50;
                case ENEMIGO1:  return 50;
                case ENEMIGO2:  return 50;
                case ENEMIGO3:  return 50;
                case ENEMIGO4:  return 50;
                case ENEMIGO5:  return 50;
                case ENEMIGO6:  return 60;
                case ENEMIGO7:  return 60;
                case ENEMIGO8:  return 60;
                case ENEMIGO9:  return 60;
                case ENEMIGO10: return 100; 
                case ENEMIGO11: return 100; 
                case ENEMIGO12: return 120; 
                case ENEMIGO13: return 50;
                case ENEMIGO14: return 60;
                case ENEMIGO15: return 55;
                case TROL_JEFE: return 300; 
                case BOSS_ANCIENT:   return 250;
                case BOSS_VIKING:    return 280;
                case BOSS_TOAD_KING: return 220;
                default: return 50;
            }
        }

		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
            case GOLEM: return 15;
            case SKELETONW: return 10;
            case SKELETONY: return 10;
            case ENEMIGO1: return 10;
            case ENEMIGO2: return 10;
            case ENEMIGO3: return 10;
            case ENEMIGO4: return 10;
            case ENEMIGO5: return 10;
            case ENEMIGO6: return 10;
            case ENEMIGO7: return 10;
            case ENEMIGO8: return 10;
            case ENEMIGO9: return 10;
            case ENEMIGO10: return 10;
            case ENEMIGO11: return 10;
            case ENEMIGO12: return 10;
            case ENEMIGO13: return 8;
            case ENEMIGO14: return 12;
            case ENEMIGO15: return 10;
            case TROL_JEFE: return 25; 
            case BOSS_ANCIENT:   return 20;
            case BOSS_VIKING:    return 22;
            case BOSS_TOAD_KING: return 18;
            default: return 0;
            }
        }
    }

    public class Ambiente {
        public static final int BIG_CLOUDS_WIDTH_DEFAULT = 448;
        public static final int BIG_CLOUDS_HEIGHT_DEFAULT = 101;
        public static final int BIG_CLOUDS_WIDTH = (int)(BIG_CLOUDS_WIDTH_DEFAULT*Juego.SCALE);
        public static final int BIG_CLOUDS_HEIGHT = (int)(BIG_CLOUDS_HEIGHT_DEFAULT*Juego.SCALE);
        public static final int SMALL_CLOUDS_WIDTH_DEFAULT = 74;
        public static final int SMALL_CLOUDS_HEIGHT_DEFAULT = 24;
        public static final int SMALL_CLOUDS_WIDTH = (int)(SMALL_CLOUDS_WIDTH_DEFAULT*Juego.SCALE);
        public static final int SMALL_CLOUDS_HEIGHT = (int)(SMALL_CLOUDS_HEIGHT_DEFAULT*Juego.SCALE);
     }
     
	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Juego.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Juego.SCALE);
		}
		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Juego.SCALE);
		}
        public static class URMButtons{
            public static final int URM_DEFAULT_SIZE=56;
            public static final int URM_SIZE=(int)(URM_DEFAULT_SIZE*Juego.SCALE);
        }
        public static class VolumeButtons{
            public static final int VOLUME_DEFAULT_WIDTH= 28;
            public static final int VOLUME_DEFAULT_HEIGHT= 44;
            public static final int SLIDER_DEFAULT_WIDTH = 215;

            public static final int VOLUME_WIDTH= (int)(VOLUME_DEFAULT_WIDTH * Juego.SCALE);
            public static final int VOLUME_HEIGHT= (int)(VOLUME_DEFAULT_HEIGHT * Juego.SCALE);
            public static final int SLIDER_WIDTH= (int)(SLIDER_DEFAULT_WIDTH * Juego.SCALE);
        }
    }
    public static class Direccion {
        public static final int LEFT = 0;
        public static final int UP = 1;
        public static final int RIGHT = 2;
        public static final int DOWN = 3;
    }

    public static class ConstanteJugador {
        public static final int INACTIVO = 0;
        public static final int CORRER = 1;
        public static final int SALTAR = 2;
        public static final int CAYENDO = 3;
        public static final int ATACAR1 = 4;
        public static final int HERIDO = 5;
        public static final int MUERTO = 6;

    }
    public static class WeaponConstants {
        public static final int NUM_WEAPONS = 7;
        public static final int NUM_ARMORS  = 4;

        public static final int WEAPON_DEFAULT_SIZE = 32;
        public static final int WEAPON_SIZE = (int) (WEAPON_DEFAULT_SIZE * Juego.SCALE);

        public static final int[][] WEAPON_COORDS = {
            {10, 0}, { 6, 1}, { 9, 1}, {12, 2}, { 7, 6}, { 0, 4}, {11, 9}, 
        };

        public static final int[] WEAPON_DAMAGE = {12, 18, 15, 22, 25, 10, 30};

        public static final int[][] ARMOR_COORDS = {
            {2, 7}, {2, 8}, {2, 9}, {2, 10},
        };

        public static final int[] ARMOR_SHIELD = {25, 50, 75, 100};

        public static final int WEAPON_BLUE_START = 10;  
        public static final int ARMOR_BLUE_START  = 20;  
    }
}