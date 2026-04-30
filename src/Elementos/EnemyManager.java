package Elementos;

import Niveles.Level;
import static Utilz.Constantes.EnemyConstants.*;
import Utilz.LoadSave;
import gamestates.Playing;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class EnemyManager {

	private int score = 0;
	private Playing playing;
	private BufferedImage[][] golemarr, skeletonwarr, skeletonayrr,
	                          enemigo1arr, enemigo2arr, enemigo3arr, enemigo4arr,
	                          enemigo5arr, enemigo6arr, enemigo7arr, enemigo8arr,
	                          enemigo9arr, enemigo10arr, enemigo11arr, enemigo12arr,
	                          enemigo13arr, enemigo14arr, enemigo15arr,
	                          trolJefeArr,
	                          bossAncientArr, bossVikingArr, bossToadKingArr;

	// LEVEL 1
	private ArrayList<Golem> golems = new ArrayList<>();
	private ArrayList<skeletonW> Skeletonw = new ArrayList<>();
	private ArrayList<skeletonY> skeletony = new ArrayList<>();
	private ArrayList<enemigo1> enemigos1 = new ArrayList<>();

	// LEVEL 2
	private ArrayList<enemigo2> enemigos2 = new ArrayList<>();
	private ArrayList<enemigo3> enemigos3 = new ArrayList<>();
	private ArrayList<enemigo4> enemigos4 = new ArrayList<>();
	private ArrayList<enemigo5> enemigos5 = new ArrayList<>();

	// LEVEL 3
	private ArrayList<enemigo6> enemigos6 = new ArrayList<>();
	private ArrayList<enemigo7> enemigos7 = new ArrayList<>();
	private ArrayList<enemigo8> enemigos8 = new ArrayList<>();
	private ArrayList<enemigo9> enemigos9 = new ArrayList<>();

	// BOSS
	private ArrayList<enemigo10> enemigos10 = new ArrayList<>();
	private ArrayList<enemigo11> enemigos11 = new ArrayList<>();
	private ArrayList<enemigo12> enemigos12 = new ArrayList<>();
	private ArrayList<enemigo13> enemigos13 = new ArrayList<>();
	private ArrayList<enemigo14> enemigos14 = new ArrayList<>();
	private ArrayList<enemigo15> enemigos15 = new ArrayList<>();
	private ArrayList<TrolJefe>  trolJefes  = new ArrayList<>();
	private ArrayList<BossAncient>   bossAncients  = new ArrayList<>();
	private ArrayList<BossViking>    bossVikings   = new ArrayList<>();
	private ArrayList<BossToadKing>  bossToadKings = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
	}

	public void loadEnemies(Level level) {
		golems = level.getGolems();
		Skeletonw = level.getSkeletonw();
		skeletony = level.getSkeletony();
		enemigos1 = level.getEnemigo1();
		enemigos2 = level.getEnemigo2();
		enemigos3 = level.getEnemigo3();
		enemigos4 = level.getEnemigo4();
		enemigos5 = level.getEnemigo5();
		enemigos6 = level.getEnemigo6();
		enemigos7 = level.getEnemigo7();
		enemigos8 = level.getEnemigo8();
		enemigos9 = level.getEnemigo9();
		enemigos10 = level.getEnemigo10();
		enemigos11 = level.getEnemigo11();
		enemigos12 = level.getEnemigo12();
		enemigos13 = level.getEnemigo13();
		enemigos14 = level.getEnemigo14();
		enemigos15 = level.getEnemigo15();
		trolJefes  = level.getTrolJefes();
		bossAncients  = level.getBossAncients();
		bossVikings   = level.getBossVikings();
		bossToadKings = level.getBossToadKings();
	}

	public void update(int[][] lvlData, Jugador jugador) {
		boolean isAnyActive = false;

		for (Golem s : golems) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (skeletonW s : Skeletonw) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (skeletonY s : skeletony) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo1 s : enemigos1) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo2 s : enemigos2) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo3 s : enemigos3) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo4 s : enemigos4) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo5 s : enemigos5) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo6 s : enemigos6) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo7 s : enemigos7) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo8 s : enemigos8) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo9 s : enemigos9) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo10 s : enemigos10) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo11 s : enemigos11) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo12 s : enemigos12) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo13 s : enemigos13) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo14 s : enemigos14) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (enemigo15 s : enemigos15) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (TrolJefe  s : trolJefes)  if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (BossAncient  s : bossAncients)  if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (BossViking   s : bossVikings)   if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }
		for (BossToadKing s : bossToadKings) if (s.isActive()) { s.update(lvlData, jugador); isAnyActive = true; }

		if (!isAnyActive)
			playing.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawCrabs(g, xLvlOffset);
		drawSkeletonw(g, xLvlOffset);
		drawSkeletony(g, xLvlOffset);
		drawEnemigo1(g, xLvlOffset);
		drawEnemigo2(g, xLvlOffset);
		drawEnemigo3(g, xLvlOffset);
		drawEnemigo4(g, xLvlOffset);
		drawEnemigo5(g, xLvlOffset);
		drawEnemigo6(g, xLvlOffset);
		drawEnemigo7(g, xLvlOffset);
		drawEnemigo8(g, xLvlOffset);
		drawEnemigo9(g, xLvlOffset);
		drawEnemigo10(g, xLvlOffset);
		drawEnemigo11(g, xLvlOffset);
		drawEnemigo12(g, xLvlOffset);
		drawEnemigo13(g, xLvlOffset);
		drawEnemigo14(g, xLvlOffset);
		drawEnemigo15(g, xLvlOffset);
		drawTrolJefe(g, xLvlOffset);
		drawBossAncient(g, xLvlOffset);
		drawBossViking(g, xLvlOffset);
		drawBossToadKing(g, xLvlOffset);
	}

	private void drawCrabs(Graphics g, int xLvlOffset) {
		for (Golem s : golems)
			if (s.isActive()) {
				g.drawImage(golemarr[s.getEnemyState()][s.getAniIndex()],
					(int) s.getHitbox().x - xLvlOffset - GOLEM_DRAWOFFSET_X + s.flipX(),
					(int) s.getHitbox().y - GOLEM_DRAWOFFSET_Y,
					GOLEM_WIDTH * s.flipW(), GOLEM_HEIGHT, null);
				s.drawAttackBox(g, xLvlOffset);
				s.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawSkeletonw(Graphics g, int xLvlOffset) {
		for (skeletonW d : Skeletonw)
			if (d.isActive()) {
				int row = d.getAniRowOffset();
				int col = d.getAniIndex();
				if (skeletonwarr != null && row < skeletonwarr.length &&
				    skeletonwarr[row] != null && col < skeletonwarr[row].length &&
				    skeletonwarr[row][col] != null)
					g.drawImage(skeletonwarr[row][col],
						(int) d.getHitbox().x - xLvlOffset - SKELETONW_DRAWOFFSET_X + d.flipX(),
						(int) d.getHitbox().y - SKELETONW_DRAWOFFSET_Y,
						SKELETONW_WIDTH * d.flipW(), SKELETONW_HEIGHT, null);
				d.drawAttackBox(g, xLvlOffset);
				d.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawSkeletony(Graphics g, int xLvlOffset) {
		for (skeletonY e : skeletony)
			if (e.isActive()) {
				int row = e.getAniRowOffset();
				int col = e.getAniIndex();
				if (skeletonayrr != null && row < skeletonayrr.length &&
				    skeletonayrr[row] != null && col < skeletonayrr[row].length &&
				    skeletonayrr[row][col] != null)
					g.drawImage(skeletonayrr[row][col],
						(int) e.getHitbox().x - xLvlOffset - SKELETONY_DRAWOFFSET_X + e.flipX(),
						(int) e.getHitbox().y - SKELETONY_DRAWOFFSET_Y,
						SKELETONY_WIDTH * e.flipW(), SKELETONY_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo1(Graphics g, int xLvlOffset) {
		for (enemigo1 e : enemigos1)
			if (e.isActive()) {
				g.drawImage(enemigo1arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO1_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO1_DRAWOFFSET_Y,
					ENEMIGO1_WIDTH * e.flipW(), ENEMIGO1_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo2(Graphics g, int xLvlOffset) {
		for (enemigo2 e : enemigos2)
			if (e.isActive()) {
				g.drawImage(enemigo2arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO2_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO2_DRAWOFFSET_Y,
					ENEMIGO2_WIDTH * e.flipW(), ENEMIGO2_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo3(Graphics g, int xLvlOffset) {
		for (enemigo3 e : enemigos3)
			if (e.isActive()) {
				g.drawImage(enemigo3arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO3_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO3_DRAWOFFSET_Y,
					ENEMIGO3_WIDTH * e.flipW(), ENEMIGO3_HEIGHT, null);
				e.drawProyectiles(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo4(Graphics g, int xLvlOffset) {
		for (enemigo4 e : enemigos4)
			if (e.isActive()) {
				g.drawImage(enemigo4arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO4_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO4_DRAWOFFSET_Y,
					ENEMIGO4_WIDTH * e.flipW(), ENEMIGO4_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo5(Graphics g, int xLvlOffset) {
		for (enemigo5 e : enemigos5)
			if (e.isActive()) {
				g.drawImage(enemigo5arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO5_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO5_DRAWOFFSET_Y,
					ENEMIGO5_WIDTH * e.flipW(), ENEMIGO5_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo6(Graphics g, int xLvlOffset) {
		for (enemigo6 e : enemigos6)
			if (e.isActive()) {
				g.drawImage(enemigo6arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO6_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO6_DRAWOFFSET_Y,
					ENEMIGO6_WIDTH * e.flipW(), ENEMIGO6_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo7(Graphics g, int xLvlOffset) {
		for (enemigo7 e : enemigos7)
			if (e.isActive()) {
				g.drawImage(enemigo7arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO7_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO7_DRAWOFFSET_Y,
					ENEMIGO7_WIDTH * e.flipW(), ENEMIGO7_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo8(Graphics g, int xLvlOffset) {
		for (enemigo8 e : enemigos8)
			if (e.isActive()) {
				g.drawImage(enemigo8arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO8_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO8_DRAWOFFSET_Y,
					ENEMIGO8_WIDTH * e.flipW(), ENEMIGO8_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo9(Graphics g, int xLvlOffset) {
		for (enemigo9 e : enemigos9)
			if (e.isActive()) {
				g.drawImage(enemigo9arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO9_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO9_DRAWOFFSET_Y,
					ENEMIGO9_WIDTH * e.flipW(), ENEMIGO9_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo10(Graphics g, int xLvlOffset) {
		for (enemigo10 e : enemigos10)
			if (e.isActive()) {
				g.drawImage(enemigo10arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO10_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO10_DRAWOFFSET_Y,
					ENEMIGO10_WIDTH * e.flipW(), ENEMIGO10_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo11(Graphics g, int xLvlOffset) {
		for (enemigo11 e : enemigos11)
			if (e.isActive()) {
				g.drawImage(enemigo11arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO11_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO11_DRAWOFFSET_Y,
					ENEMIGO11_WIDTH * e.flipW(), ENEMIGO11_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo12(Graphics g, int xLvlOffset) {
		for (enemigo12 e : enemigos12)
			if (e.isActive()) {
				g.drawImage(enemigo12arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO12_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO12_DRAWOFFSET_Y,
					ENEMIGO12_WIDTH * e.flipW(), ENEMIGO12_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}


	private void drawEnemigo13(Graphics g, int xLvlOffset) {
		for (enemigo13 e : enemigos13)
			if (e.isActive()) {
				g.drawImage(enemigo13arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO13_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO13_DRAWOFFSET_Y,
					ENEMIGO13_WIDTH * e.flipW(), ENEMIGO13_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo14(Graphics g, int xLvlOffset) {
		for (enemigo14 e : enemigos14)
			if (e.isActive()) {
				g.drawImage(enemigo14arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO14_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO14_DRAWOFFSET_Y,
					ENEMIGO14_WIDTH * e.flipW(), ENEMIGO14_HEIGHT, null);
				e.drawAttackBox(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawEnemigo15(Graphics g, int xLvlOffset) {
		for (enemigo15 e : enemigos15)
			if (e.isActive()) {
				g.drawImage(enemigo15arr[e.getAniRowOffset()][e.getAniIndex()],
					(int) e.getHitbox().x - xLvlOffset - ENEMIGO15_DRAWOFFSET_X + e.flipX(),
					(int) e.getHitbox().y - ENEMIGO15_DRAWOFFSET_Y,
					ENEMIGO15_WIDTH * e.flipW(), ENEMIGO15_HEIGHT, null);
				e.drawProyectil(g, xLvlOffset);
				e.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawTrolJefe(Graphics g, int xLvlOffset) {
		for (TrolJefe t : trolJefes)
			if (t.isActive()) {
				// Fila del sprite sheet según estado + frame de animación
				int row = t.getAniRowOffset();
				int col = t.getAniIndex();
				if (trolJefeArr != null && trolJefeArr[row] != null && trolJefeArr[row][col] != null) {
					g.drawImage(trolJefeArr[row][col],
						(int) t.getHitbox().x - xLvlOffset - TROL_JEFE_DRAWOFFSET_X + t.flipX(),
						(int) t.getHitbox().y - TROL_JEFE_DRAWOFFSET_Y,
						TROL_JEFE_WIDTH * t.flipW(), TROL_JEFE_HEIGHT, null);
				}
				t.drawAttackBox(g, xLvlOffset);
				t.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawBossAncient(Graphics g, int xLvlOffset) {
		for (BossAncient t : bossAncients)
			if (t.isActive()) {
				int row = t.getAniRowOffset();
				int col = t.getAniIndex();
				if (bossAncientArr != null && row < bossAncientArr.length &&
				    bossAncientArr[row] != null && col < bossAncientArr[row].length &&
				    bossAncientArr[row][col] != null)
					g.drawImage(bossAncientArr[row][col],
						(int)t.getHitbox().x - xLvlOffset - BOSS_ANCIENT_DRAWOFFSET_X + t.flipX(),
						(int)t.getHitbox().y - BOSS_ANCIENT_DRAWOFFSET_Y,
						BOSS_ANCIENT_WIDTH * t.flipW(), BOSS_ANCIENT_HEIGHT, null);
				t.drawAttackBox(g, xLvlOffset);
				t.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawBossViking(Graphics g, int xLvlOffset) {
		for (BossViking t : bossVikings)
			if (t.isActive()) {
				int row = t.getAniRowOffset();
				int col = t.getAniIndex();
				if (bossVikingArr != null && row < bossVikingArr.length &&
				    bossVikingArr[row] != null && col < bossVikingArr[row].length &&
				    bossVikingArr[row][col] != null)
					g.drawImage(bossVikingArr[row][col],
						(int)t.getHitbox().x - xLvlOffset - BOSS_VIKING_DRAWOFFSET_X + t.flipX(),
						(int)t.getHitbox().y - BOSS_VIKING_DRAWOFFSET_Y,
						BOSS_VIKING_WIDTH * t.flipW(), BOSS_VIKING_HEIGHT, null);
				t.drawAttackBox(g, xLvlOffset);
				t.drawHealthBar(g, xLvlOffset);
			}
	}

	private void drawBossToadKing(Graphics g, int xLvlOffset) {
		for (BossToadKing t : bossToadKings)
			if (t.isActive()) {
				int row = t.getAniRowOffset();
				int col = t.getAniIndex();
				if (bossToadKingArr != null && row < bossToadKingArr.length &&
				    bossToadKingArr[row] != null && col < bossToadKingArr[row].length &&
				    bossToadKingArr[row][col] != null)
					g.drawImage(bossToadKingArr[row][col],
						(int)t.getHitbox().x - xLvlOffset - BOSS_TOAD_KING_DRAWOFFSET_X + t.flipX(),
						(int)t.getHitbox().y - BOSS_TOAD_KING_DRAWOFFSET_Y,
						BOSS_TOAD_KING_WIDTH * t.flipW(), BOSS_TOAD_KING_HEIGHT, null);
				t.drawAttackBox(g, xLvlOffset);
				t.drawHealthBar(g, xLvlOffset);
			}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		int dmg = playing.getPlayer().getDamage();

		for (Golem c : golems)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (skeletonW c : Skeletonw)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (skeletonY c : skeletony)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo1 c : enemigos1)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo2 c : enemigos2)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo3 c : enemigos3)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo4 c : enemigos4)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo5 c : enemigos5)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo6 c : enemigos6)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo7 c : enemigos7)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo8 c : enemigos8)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo9 c : enemigos9)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo10 c : enemigos10)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo11 c : enemigos11)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo12 c : enemigos12)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (TrolJefe c : trolJefes)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(500); // Boss da más puntos
					return;
				}

		for (enemigo13 c : enemigos13)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo14 c : enemigos14)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (enemigo15 c : enemigos15)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(100);
					return;
				}

		for (BossAncient c : bossAncients)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(400);
					return;
				}

		for (BossViking c : bossVikings)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(400);
					return;
				}

		for (BossToadKing c : bossToadKings)
			if (c.isActive() && c.getEnemyState() != MUERTO && c.getEnemyState() != GOLPE)
				if (attackBox.intersects(c.getHitbox())) {
					c.hurt(dmg);
					if (c.getEnemyState() == MUERTO) playing.increaseScore(400);
					return;
				}
	}

	private void loadEnemyImgs() {
		final int cols = 10;
		final int rows = 9;

		golemarr     = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.GOLEM_SPRITE),     cols, rows, GOLEM_WIDTH_DEFAULT, GOLEM_HEIGHT_DEFAULT);
		skeletonwarr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SKELETONW_SPRITE), cols, rows, SKELETONW_WIDTH_DEFAULT, SKELETONW_HEIGHT_DEFAULT);
		skeletonayrr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SKELETONY_SPRITE), cols, rows, SKELETONY_WIDTH_DEFAULT, SKELETONY_HEIGHT_DEFAULT);
		// Nuevos enemigos con sus dimensiones reales
		enemigo1arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO1_SPRITE),  6, 5, ENEMIGO1_WIDTH_DEFAULT, ENEMIGO1_HEIGHT_DEFAULT);
		enemigo2arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO2_SPRITE),  6, 5, ENEMIGO2_WIDTH_DEFAULT, ENEMIGO2_HEIGHT_DEFAULT);
		enemigo3arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO3_SPRITE),  6, 9, ENEMIGO3_WIDTH_DEFAULT, ENEMIGO3_HEIGHT_DEFAULT);
		enemigo4arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO4_SPRITE),  6, 5, ENEMIGO4_WIDTH_DEFAULT, ENEMIGO4_HEIGHT_DEFAULT);
		enemigo5arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO5_SPRITE),  6, 5, ENEMIGO5_WIDTH_DEFAULT, ENEMIGO5_HEIGHT_DEFAULT);
		enemigo6arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO6_SPRITE),  6, 5, ENEMIGO6_WIDTH_DEFAULT, ENEMIGO6_HEIGHT_DEFAULT);
		enemigo7arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO7_SPRITE),  6, 5, ENEMIGO7_WIDTH_DEFAULT,  ENEMIGO7_HEIGHT_DEFAULT);  // 2_Toad   288x240
		enemigo8arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO8_SPRITE),  4, 5, ENEMIGO8_WIDTH_DEFAULT,  ENEMIGO8_HEIGHT_DEFAULT);  // 1_Slime  192x240
		enemigo9arr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO9_SPRITE),  6, 5, ENEMIGO9_WIDTH_DEFAULT,  ENEMIGO9_HEIGHT_DEFAULT);  // Snake    288x240
		enemigo10arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO10_SPRITE), 4, 5, ENEMIGO10_WIDTH_DEFAULT, ENEMIGO10_HEIGHT_DEFAULT); // Scorpio  192x240
		enemigo11arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO11_SPRITE), 4, 5, ENEMIGO11_WIDTH_DEFAULT, ENEMIGO11_HEIGHT_DEFAULT); // Vulture  192x240
		enemigo12arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO12_SPRITE), 6, 5, ENEMIGO12_WIDTH_DEFAULT, ENEMIGO12_HEIGHT_DEFAULT); // 2_Toad   288x240
		enemigo13arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO13_SPRITE), 4, 5, ENEMIGO13_WIDTH_DEFAULT, ENEMIGO13_HEIGHT_DEFAULT); // Bug 192x240
		enemigo14arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO14_SPRITE), 6, 5, ENEMIGO14_WIDTH_DEFAULT, ENEMIGO14_HEIGHT_DEFAULT); // Goblin Melee 288x240
		enemigo15arr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.ENEMIGO15_SPRITE), 6, 5, ENEMIGO15_WIDTH_DEFAULT, ENEMIGO15_HEIGHT_DEFAULT); // Goblin Range 288x250
		// Trol Jefe: 10 columnas × 9 filas, frames 96×96
		trolJefeArr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.TROL_JEFE_SPRITE), 10, 9, TROL_JEFE_WIDTH_DEFAULT, TROL_JEFE_HEIGHT_DEFAULT);
		bossAncientArr  = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.BOSS_ANCIENT_SPRITE),   6, 9, BOSS_ANCIENT_WIDTH_DEFAULT,   BOSS_ANCIENT_HEIGHT_DEFAULT);
		bossVikingArr   = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.BOSS_VIKING_SPRITE),    6, 9, BOSS_VIKING_WIDTH_DEFAULT,    BOSS_VIKING_HEIGHT_DEFAULT);
		bossToadKingArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.BOSS_TOAD_KING_SPRITE), 6, 9, BOSS_TOAD_KING_WIDTH_DEFAULT, BOSS_TOAD_KING_HEIGHT_DEFAULT);
	}

	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		int atlasW = atlas.getWidth();
		int atlasH = atlas.getHeight();

		for (int j = 0; j < ySize; j++) {
			for (int i = 0; i < xSize; i++) {
				int x = i * spriteW;
				int y = j * spriteH;
				if (x + spriteW <= atlasW && y + spriteH <= atlasH) {
					tempArr[j][i] = atlas.getSubimage(x, y, spriteW, spriteH);
				}
			}
		}
		return tempArr;
	}


	public void killAllEnemies() {
		for (Golem e : golems)         if (e.isActive()) e.hurt(99999);
		for (skeletonW e : Skeletonw)  if (e.isActive()) e.hurt(99999);
		for (skeletonY e : skeletony)  if (e.isActive()) e.hurt(99999);
		for (enemigo1 e : enemigos1)   if (e.isActive()) e.hurt(99999);
		for (enemigo2 e : enemigos2)   if (e.isActive()) e.hurt(99999);
		for (enemigo3 e : enemigos3)   if (e.isActive()) e.hurt(99999);
		for (enemigo4 e : enemigos4)   if (e.isActive()) e.hurt(99999);
		for (enemigo5 e : enemigos5)   if (e.isActive()) e.hurt(99999);
		for (enemigo6 e : enemigos6)   if (e.isActive()) e.hurt(99999);
		for (enemigo7 e : enemigos7)   if (e.isActive()) e.hurt(99999);
		for (enemigo8 e : enemigos8)   if (e.isActive()) e.hurt(99999);
		for (enemigo9 e : enemigos9)   if (e.isActive()) e.hurt(99999);
		for (enemigo10 e : enemigos10) if (e.isActive()) e.hurt(99999);
		for (enemigo11 e : enemigos11) if (e.isActive()) e.hurt(99999);
		for (enemigo12 e : enemigos12) if (e.isActive()) e.hurt(99999);
		for (enemigo13 e : enemigos13) if (e.isActive()) e.hurt(99999);
		for (enemigo14 e : enemigos14) if (e.isActive()) e.hurt(99999);
		for (enemigo15 e : enemigos15) if (e.isActive()) e.hurt(99999);
		for (TrolJefe e : trolJefes)   if (e.isActive()) e.hurt(99999);
		for (BossAncient  e : bossAncients)  if (e.isActive()) e.hurt(99999);
		for (BossViking   e : bossVikings)   if (e.isActive()) e.hurt(99999);
		for (BossToadKing e : bossToadKings) if (e.isActive()) e.hurt(99999);
	}
	public void resetAllEnemies() {
		for (Golem s : golems) s.resetEnemy();
		for (skeletonW s : Skeletonw) s.resetEnemy();
		for (skeletonY s : skeletony) s.resetEnemy();
		for (enemigo1 s : enemigos1) s.resetEnemy();
		for (enemigo2 s : enemigos2) s.resetEnemy();
		for (enemigo3 s : enemigos3) s.resetEnemy();
		for (enemigo4 s : enemigos4) s.resetEnemy();
		for (enemigo5 s : enemigos5) s.resetEnemy();
		for (enemigo6 s : enemigos6) s.resetEnemy();
		for (enemigo7 s : enemigos7) s.resetEnemy();
		for (enemigo8 s : enemigos8) s.resetEnemy();
		for (enemigo9 s : enemigos9) s.resetEnemy();
		for (enemigo10 s : enemigos10) s.resetEnemy();
		for (enemigo11 s : enemigos11) s.resetEnemy();
		for (enemigo12 s : enemigos12) s.resetEnemy();
		for (enemigo13 s : enemigos13) s.resetEnemy();
		for (enemigo14 s : enemigos14) s.resetEnemy();
		for (enemigo15 s : enemigos15) s.resetEnemy();
		for (TrolJefe  s : trolJefes)  s.resetEnemy();
		for (BossAncient  s : bossAncients)  s.resetEnemy();
		for (BossViking   s : bossVikings)   s.resetEnemy();
		for (BossToadKing s : bossToadKings) s.resetEnemy();
	}
}