package gamestates;

import Elementos.PlayerCharacter;
import Juegos.Juego;
import static Utilz.Constantes.EnemyConstants.INACTIVO;
import Utilz.LoadSave;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.geom.RoundRectangle2D;
import ui.MenuButton;

public class PlayerSelection extends state implements Statemethods {

    private BufferedImage backgroundImg, backgroundImgPink;
    private int menuX, menuY, menuWidth, menuHeight;
    private MenuButton playButton;
    private int playerindex = 0;
    private CharacterAnimation[] characterAnimations;

    // Glow pulsante
    private float glowAlpha = 0f;
    private float glowDir = 0.04f;

    // Partículas decorativas
    private float[] particleX, particleY, particleAlpha, particleSpeed;
    private static final int NUM_PARTICLES = 18;

    // Colores temáticos por personaje
    private static final Color[] CHAR_COLORS = {
        new Color(80, 160, 255),
        new Color(220, 80,  60),
        new Color(80, 220, 130),
        new Color(200, 160, 50)
    };

    // Flechas manuales
    private Rectangle leftArrow, rightArrow;
    private boolean leftHover, rightHover, leftPressed, rightPressed;

    // Animación de cambio de personaje (feedback visual al usar control/teclado)
    private float switchAnim = 0f; // 1 -> 0
    private int switchDir = 0;     // -1 izq, +1 der

    private int tick = 0;

    public PlayerSelection(Juego juego) {
        super(juego);
        loadButtons();
        loadBackground();
        backgroundImgPink = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND_IMG);
        loadCharAnimations();
        initParticles();
        initArrows();
    }

    private void initArrows() {
        int arrowW = (int)(40 * Juego.SCALE);
        int arrowH = (int)(40 * Juego.SCALE);
        int centerY = menuY + menuHeight / 2;
        leftArrow  = new Rectangle(menuX - (int)(90 * Juego.SCALE), centerY - arrowH/2, arrowW, arrowH);
        rightArrow = new Rectangle(menuX + menuWidth + (int)(50 * Juego.SCALE), centerY - arrowH/2, arrowW, arrowH);
    }

    private void initParticles() {
        particleX     = new float[NUM_PARTICLES];
        particleY     = new float[NUM_PARTICLES];
        particleAlpha = new float[NUM_PARTICLES];
        particleSpeed = new float[NUM_PARTICLES];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            particleX[i]     = (float)(Math.random() * Juego.GAME_WIDTH);
            particleY[i]     = (float)(Math.random() * Juego.GAME_HEIGHT);
            particleAlpha[i] = (float)(Math.random());
            particleSpeed[i] = 0.3f + (float)(Math.random() * 0.5f);
        }
    }

    private void loadCharAnimations() {
        characterAnimations = new CharacterAnimation[4];
        int i = 0;
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.p1);
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.p2);
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.p3);
        characterAnimations[i++] = new CharacterAnimation(PlayerCharacter.p4);
    }

    private void loadBackground() {
        backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUND);
        menuWidth  = (int)(backgroundImg.getWidth() * Juego.SCALE);
        menuHeight = (int)(backgroundImg.getHeight() * Juego.SCALE);
        menuX = Juego.GAME_WIDTH / 2 - menuWidth / 2;
        menuY = (int)(45 * Juego.SCALE);
    }

    private void loadButtons() {
        playButton = new MenuButton(Juego.GAME_WIDTH / 2, (int)(290 * Juego.SCALE), 0, Gamestate.PLAYING);
    }

    @Override
    public void update() {
        tick++;
        glowAlpha += glowDir;
        if (glowAlpha >= 1f)  { glowAlpha = 1f;  glowDir = -0.04f; }
        else if (glowAlpha <= 0f) { glowAlpha = 0f; glowDir =  0.04f; }

        // decaer animación de cambio
        if (switchAnim > 0f) {
            switchAnim -= 0.08f;
            if (switchAnim < 0f) switchAnim = 0f;
        }

        for (int i = 0; i < NUM_PARTICLES; i++) {
            particleY[i] -= particleSpeed[i];
            particleAlpha[i] += 0.01f;
            if (particleY[i] < -10 || particleAlpha[i] > 1f) {
                particleX[i]     = (float)(Math.random() * Juego.GAME_WIDTH);
                particleY[i]     = Juego.GAME_HEIGHT + 10;
                particleAlpha[i] = 0f;
                particleSpeed[i] = 0.3f + (float)(Math.random() * 0.5f);
            }
        }

        playButton.update();
        for (CharacterAnimation ca : characterAnimations) ca.update();
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Fondo
        g.drawImage(backgroundImgPink, 0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT, null);
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        // Overlay oscuro suave
        g2d.setColor(new Color(0, 0, 0, 110));
        g2d.fillRect(0, 0, Juego.GAME_WIDTH, Juego.GAME_HEIGHT);

        // Partículas
        Color charColor = CHAR_COLORS[playerindex];
        for (int i = 0; i < NUM_PARTICLES; i++) {
            float a = Math.min(particleAlpha[i], 0.55f);
            g2d.setColor(new Color(charColor.getRed(), charColor.getGreen(), charColor.getBlue(), (int)(a*255)));
            int sz = 3 + (i % 4);
            g2d.fillOval((int)particleX[i], (int)particleY[i], sz, sz);
        }

        // (El contorno duro del panel se eliminó: se veía raro contra el background.
        //  En su lugar, dibujamos un halo radial suave detrás del personaje, que se integra mejor.)

        // Vuelve a dibujar el panel del menú encima de las partículas
        g.drawImage(backgroundImg, menuX, menuY, menuWidth, menuHeight, null);

        int centerY = menuY + menuHeight / 2;

        drawTitle(g2d);
        // Personajes laterales un poco más arriba
        int sideY = centerY - (int)(6 * Juego.SCALE);
        drawSideChar(g2d, normalizeIndex(playerindex - 1), menuX - (int)(70 * Juego.SCALE), sideY, 0.55f);
        drawSideChar(g2d, normalizeIndex(playerindex + 1), menuX + menuWidth + (int)(70 * Juego.SCALE), sideY, 0.55f);
        // Personaje seleccionado: subido para no tapar el botón Play
        drawSelectedChar(g2d, playerindex, menuX + menuWidth / 2, centerY - (int)(14 * Juego.SCALE));
        drawStatsPanel(g2d);
        // Flechas estilo madera tallada (mítico, tipo letrero de aldea)
        drawWoodenArrow(g2d, leftArrow,  true,  leftHover,  leftPressed);
        drawWoodenArrow(g2d, rightArrow, false, rightHover, rightPressed);
        drawDots(g2d, charColor);
        playButton.draw(g);
        drawHint(g2d);

        g2d.setStroke(new BasicStroke(1f));
    }

    private void drawTitle(Graphics2D g2d) {
        String title = "SELECCIÓN DE PERSONAJE";
        Font f = new Font("Georgia", Font.BOLD, (int)(11 * Juego.SCALE));
        g2d.setFont(f);
        FontMetrics fm = g2d.getFontMetrics();
        int tx = Juego.GAME_WIDTH / 2 - fm.stringWidth(title) / 2;
        int ty = (int)(18 * Juego.SCALE);
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.drawString(title, tx + 2, ty + 2);
        GradientPaint gp = new GradientPaint(tx, ty - fm.getAscent(), new Color(255, 230, 100), tx, ty, new Color(200, 140, 30));
        g2d.setPaint(gp);
        g2d.drawString(title, tx, ty);
        g2d.setPaint(null);
    }

    private void drawSelectedChar(Graphics2D g2d, int idx, int x, int y) {
        idx = normalizeIndex(idx);

        // Aura verde suave (vibra de bosque mítico) — pulsante con glowAlpha
        int auraSize = (int)(110 * Juego.SCALE);
        int auraAlpha = (int)(45 + glowAlpha * 55);
        Color auraColor = new Color(120, 200, 90); // verde bosque
        RadialGradientPaint auraP = new RadialGradientPaint(
            new java.awt.geom.Point2D.Float(x, y),
            auraSize / 2f,
            new float[]{0f, 0.5f, 1f},
            new Color[]{
                new Color(auraColor.getRed(), auraColor.getGreen(), auraColor.getBlue(), auraAlpha),
                new Color(auraColor.getRed(), auraColor.getGreen(), auraColor.getBlue(), auraAlpha/3),
                new Color(0, 0, 0, 0)
            }
        );
        g2d.setPaint(auraP);
        g2d.fillOval(x - auraSize/2, y - auraSize/2, auraSize, auraSize);
        g2d.setPaint(null);

        // Sombrita en el suelo
        int shadowW = (int)(60 * Juego.SCALE);
        int shadowH = (int)(10 * Juego.SCALE);
        int shadowY = y + (int)(28 * Juego.SCALE);
        RadialGradientPaint shP = new RadialGradientPaint(
            new java.awt.geom.Point2D.Float(x, shadowY + shadowH/2f),
            shadowW / 2f,
            new float[]{0f, 1f},
            new Color[]{new Color(0,0,0,150), new Color(0,0,0,0)}
        );
        g2d.setPaint(shP);
        g2d.fillOval(x - shadowW/2, shadowY, shadowW, shadowH);
        g2d.setPaint(null);

        // Runa dorada flotante encima del personaje (símbolo místico)
        drawMysticRune(g2d, x, y - (int)(48 * Juego.SCALE));

        // Animación de "deslizamiento" cuando cambias personaje
        if (switchAnim > 0f && switchDir != 0) {
            int offset = (int)(switchAnim * 12 * Juego.SCALE) * switchDir;
            java.awt.geom.AffineTransform oldT = g2d.getTransform();
            g2d.translate(-offset, 0);
            characterAnimations[idx].draw(g2d, x, y);
            g2d.setTransform(oldT);
        } else {
            characterAnimations[idx].draw(g2d, x, y);
        }
    }

    /**
     * Runa mística dorada con flotación suave (sin/cos).
     * Disco oscuro de fondo + estrella élfica de 4 puntas alargadas + brillo central.
     */
    private void drawMysticRune(Graphics2D g2d, int cx, int baseY) {
        // flotación suave
        float bob = (float)Math.sin(tick * 0.06f) * 3f * Juego.SCALE;
        int cy = baseY + (int)bob;

        int r = (int)(7 * Juego.SCALE);  // radio del disco

        // Glow exterior dorado (varias capas decrecientes)
        Color glowGold = new Color(240, 208, 96);
        int pulse = (int)(80 + glowAlpha * 80);
        for (int i = 0; i < 3; i++) {
            int rr = r + (3 - i) * (int)(2 * Juego.SCALE);
            int a = pulse / (i + 2);
            g2d.setColor(new Color(glowGold.getRed(), glowGold.getGreen(), glowGold.getBlue(), a));
            g2d.fillOval(cx - rr, cy - rr, rr * 2, rr * 2);
        }

        // Disco oscuro de fondo
        g2d.setColor(new Color(20, 15, 8, 200));
        g2d.fillOval(cx - r, cy - r, r * 2, r * 2);
        // Borde dorado del disco con gradiente
        GradientPaint discBorder = new GradientPaint(cx, cy - r, new Color(255, 244, 184),
                                                     cx, cy + r, new Color(156, 120, 32));
        g2d.setPaint(discBorder);
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(cx - r, cy - r, r * 2, r * 2);
        g2d.setPaint(null);

        // Símbolo: estrella élfica de 4 puntas alargadas
        int s = (int)(5.5f * Juego.SCALE);
        int sm = (int)(1.5f * Juego.SCALE);
        int[] xs = { cx,      cx + sm, cx + s,  cx + sm, cx,      cx - sm, cx - s,  cx - sm };
        int[] ys = { cy - s,  cy - sm, cy,      cy + sm, cy + s,  cy + sm, cy,      cy - sm };
        // sombra
        g2d.setColor(new Color(0, 0, 0, 160));
        Polygon shadow = new Polygon();
        for (int i = 0; i < xs.length; i++) shadow.addPoint(xs[i] + 1, ys[i] + 1);
        g2d.fill(shadow);
        // estrella con gradiente dorado
        GradientPaint starG = new GradientPaint(cx, cy - s, new Color(255, 244, 184),
                                                cx, cy + s, new Color(180, 130, 40));
        g2d.setPaint(starG);
        g2d.fillPolygon(xs, ys, xs.length);
        g2d.setPaint(null);
        g2d.setColor(new Color(122, 88, 16));
        g2d.setStroke(new BasicStroke(0.8f));
        g2d.drawPolygon(xs, ys, xs.length);

        // brillo central
        int brillo = (int)(2 * Juego.SCALE);
        g2d.setColor(new Color(255, 250, 220));
        g2d.fillOval(cx - brillo, cy - brillo, brillo * 2, brillo * 2);
    }

    private void drawSideChar(Graphics2D g2d, int idx, int x, int y, float scale) {
        Composite old = g2d.getComposite();
        java.awt.geom.AffineTransform oldT = g2d.getTransform();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.40f));
        g2d.translate(x, y);
        g2d.scale(scale, scale);
        g2d.translate(-x, -y);
        characterAnimations[idx].draw(g2d, x, y);
        g2d.setTransform(oldT);
        g2d.setComposite(old);
    }

    /**
     * Stats visuales con segmentos (estilo "pips" de juego retro).
     * Cada barra tiene 5 segmentos que se llenan según el valor del personaje,
     * acompañados por iconos vectoriales temáticos (sin texto).
     */
    private void drawStatsPanel(Graphics2D g2d) {
        PlayerCharacter pc = characterAnimations[playerindex].getPc();
        Color cc = CHAR_COLORS[playerindex];

        int panW = (int)(150 * Juego.SCALE);
        int panH = (int)(72 * Juego.SCALE);
        int panX = Juego.GAME_WIDTH / 2 - panW / 2;
        int panY = menuY + menuHeight + (int)(8 * Juego.SCALE);

        // Fondo del panel
        g2d.setColor(new Color(10, 10, 20, 210));
        g2d.fill(new RoundRectangle2D.Float(panX, panY, panW, panH, 16, 16));
        // Borde sutil con color del personaje (suave, no duro)
        g2d.setColor(new Color(cc.getRed(), cc.getGreen(), cc.getBlue(), 90));
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.draw(new RoundRectangle2D.Float(panX, panY, panW, panH, 16, 16));

        int iconSize = (int)(11 * Juego.SCALE);
        int rowH     = (int)(15 * Juego.SCALE);
        int padX     = (int)(10 * Juego.SCALE);
        int startY   = panY + (int)(8 * Juego.SCALE);
        int iconX    = panX + padX;
        int segX     = iconX + iconSize + (int)(8 * Juego.SCALE);
        int segAreaW = panW - (segX - panX) - padX;

        // Normalización a 5 segmentos
        int hpSeg    = clamp((int)Math.round((pc.maxHealth / 150f) * 5f), 1, 5);
        int speedSeg = clamp((int)Math.round(((pc.walkSpeed - 0.6f) / 1.2f) * 5f), 1, 5);
        int jumpSeg  = clamp((int)Math.round(((Math.abs(pc.jumpSpeed) - 2.0f) / 1.5f) * 5f), 1, 5);
        int dmgSeg   = clamp((int)Math.round((pc.damage / 20f) * 5f), 1, 5);

        int segH = (int)(6 * Juego.SCALE);

        // Fila 1: HP (corazón rojo)
        drawHeartIcon(g2d, iconX, startY, iconSize, new Color(220, 80, 80));
        drawSegments (g2d, segX, startY + iconSize/2 - segH/2, segAreaW, segH, hpSeg, new Color(220, 80, 80));

        // Fila 2: VEL (rayo cian)
        drawBoltIcon (g2d, iconX, startY + rowH, iconSize, new Color(120, 220, 240));
        drawSegments (g2d, segX, startY + rowH + iconSize/2 - segH/2, segAreaW, segH, speedSeg, new Color(120, 220, 240));

        // Fila 3: SALTO (ala verde)
        drawWingIcon (g2d, iconX, startY + rowH*2, iconSize, new Color(180, 230, 100));
        drawSegments (g2d, segX, startY + rowH*2 + iconSize/2 - segH/2, segAreaW, segH, jumpSeg, new Color(180, 230, 100));

        // Fila 4: ATAQUE (espada naranja)
        drawSwordIcon(g2d, iconX, startY + rowH*3, iconSize, new Color(240, 160, 70));
        drawSegments (g2d, segX, startY + rowH*3 + iconSize/2 - segH/2, segAreaW, segH, dmgSeg, new Color(240, 160, 70));
    }

    /** Dibuja una fila de 5 segmentos (pips) con los primeros `filled` activos. */
    private void drawSegments(Graphics2D g2d, int x, int y, int totalW, int h, int filled, Color color) {
        int n = 5;
        int gap = (int)(3 * Juego.SCALE);
        int segW = (totalW - gap * (n - 1)) / n;
        for (int i = 0; i < n; i++) {
            int sx = x + i * (segW + gap);
            // base apagada
            g2d.setColor(new Color(50, 50, 65, 200));
            g2d.fillRoundRect(sx, y, segW, h, 4, 4);
            if (i < filled) {
                // segmento activo con gradiente y leve brillo
                GradientPaint gp = new GradientPaint(sx, y, color.brighter(), sx, y + h, color.darker());
                g2d.setPaint(gp);
                g2d.fillRoundRect(sx, y, segW, h, 4, 4);
                g2d.setPaint(null);
                g2d.setColor(new Color(255, 255, 255, 70));
                g2d.fillRoundRect(sx + 1, y + 1, segW - 2, Math.max(1, h/3), 3, 3);
            }
            g2d.setColor(new Color(0, 0, 0, 120));
            g2d.drawRoundRect(sx, y, segW, h, 4, 4);
        }
    }

    // ─── Iconos vectoriales (sin texto, estilo limpio) ───

    private void drawHeartIcon(Graphics2D g2d, int x, int y, int size, Color c) {
        g2d.setColor(new Color(0, 0, 0, 160));
        java.awt.geom.Path2D shadow = heartPath(x + 1, y + 1, size);
        g2d.fill(shadow);
        g2d.setColor(c);
        java.awt.geom.Path2D heart = heartPath(x, y, size);
        g2d.fill(heart);
        g2d.setColor(c.brighter());
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.draw(heart);
        // brillo
        g2d.setColor(new Color(255, 255, 255, 160));
        g2d.fillOval(x + size/4, y + size/4, size/5, size/5);
    }

    private java.awt.geom.Path2D heartPath(int x, int y, int s) {
        java.awt.geom.Path2D p = new java.awt.geom.Path2D.Float();
        float cx = x + s / 2f;
        float cy = y + s * 0.30f;
        p.moveTo(cx, y + s * 0.95f);
        p.curveTo(x - s*0.05f, y + s*0.55f, x + s*0.10f, y + s*0.05f, cx, cy);
        p.curveTo(x + s*0.90f, y + s*0.05f, x + s*1.05f, y + s*0.55f, cx, y + s*0.95f);
        p.closePath();
        return p;
    }

    private void drawBoltIcon(Graphics2D g2d, int x, int y, int size, Color c) {
        int[] xs = {
            x + size*6/10, x + size*2/10, x + size*5/10,
            x + size*3/10, x + size*8/10, x + size*5/10
        };
        int[] ys = {
            y,             y + size*5/10, y + size*5/10,
            y + size,      y + size*4/10, y + size*4/10
        };
        g2d.setColor(new Color(0, 0, 0, 160));
        Polygon shadow = new Polygon();
        for (int i = 0; i < xs.length; i++) shadow.addPoint(xs[i] + 1, ys[i] + 1);
        g2d.fill(shadow);
        g2d.setColor(c);
        g2d.fillPolygon(xs, ys, xs.length);
        g2d.setColor(c.brighter());
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.drawPolygon(xs, ys, xs.length);
    }

    private void drawWingIcon(Graphics2D g2d, int x, int y, int size, Color c) {
        // Forma de ala/pluma
        java.awt.geom.Path2D wing = new java.awt.geom.Path2D.Float();
        wing.moveTo(x + size*0.5f, y);
        wing.curveTo(x + size, y + size*0.3f, x + size, y + size*0.8f, x + size*0.55f, y + size);
        wing.lineTo(x + size*0.45f, y + size);
        wing.curveTo(x, y + size*0.8f, x, y + size*0.3f, x + size*0.5f, y);
        wing.closePath();

        g2d.setColor(new Color(0, 0, 0, 160));
        java.awt.geom.AffineTransform t = java.awt.geom.AffineTransform.getTranslateInstance(1, 1);
        g2d.fill(t.createTransformedShape(wing));
        g2d.setColor(c);
        g2d.fill(wing);
        g2d.setColor(c.brighter());
        g2d.setStroke(new BasicStroke(1.2f));
        g2d.draw(wing);
        // línea central tipo "pluma"
        g2d.setColor(c.darker());
        g2d.drawLine(x + size/2, y + 1, x + size/2, y + size - 2);
    }

    private void drawSwordIcon(Graphics2D g2d, int x, int y, int size, Color c) {
        // Hoja
        int bladeX = x + size*4/10;
        int bladeW = size*2/10;
        // sombra hoja
        g2d.setColor(new Color(0, 0, 0, 160));
        g2d.fillRect(bladeX + 1, y + 1, bladeW, size*7/10);
        // hoja con gradiente plata
        GradientPaint gp = new GradientPaint(bladeX, y, new Color(230,230,240), bladeX + bladeW, y, new Color(150,150,170));
        g2d.setPaint(gp);
        g2d.fillRect(bladeX, y, bladeW, size*7/10);
        g2d.setPaint(null);
        // punta triangular
        int[] pxs = {bladeX, bladeX + bladeW, bladeX + bladeW/2};
        int[] pys = {y, y, y - size/5};
        g2d.setColor(new Color(220, 220, 230));
        g2d.fillPolygon(pxs, pys, 3);
        // Guarda (cruz)
        g2d.setColor(c);
        g2d.fillRect(x + size/10, y + size*7/10, size*8/10, Math.max(2, size/8));
        // Empuñadura
        g2d.setColor(c.darker());
        g2d.fillRect(x + size*4/10, y + size*7/10 + size/8, size*2/10, size*2/10);
        // Pomo
        g2d.setColor(c.brighter());
        g2d.fillOval(x + size*4/10 - 1, y + size - size/6, size*2/10 + 2, size/6);
    }

    private int clamp(int v, int lo, int hi) {
        return Math.max(lo, Math.min(hi, v));
    }

    /**
     * Flecha tallada en madera (estilo letrero rústico/aldea mítica).
     * Forma de pentágono apuntando, con vetas, borde oscuro y reflejo.
     * Hover: brilla con dorado tenue. Pressed: se hunde un poquito.
     */
    private void drawWoodenArrow(Graphics2D g2d, Rectangle r, boolean pointsLeft,
                                 boolean hover, boolean pressed) {
        // Animación: rebote sutil + offset al presionar
        float bob = (float)Math.sin(tick * 0.07f + (pointsLeft ? 0 : Math.PI)) * 1.2f * Juego.SCALE;
        int dx = pointsLeft ? -(int)bob : (int)bob; // se acerca/aleja del centro
        int dy = pressed ? (int)(2 * Juego.SCALE) : 0;

        // Bounding box ajustado
        int x = r.x + dx;
        int y = r.y + dy;
        int w = r.width;
        int h = r.height;

        // Forma de flecha tipo letrero: pentágono apuntando
        int notch = (int)(w * 0.35f); // largo de la "punta"
        int[] xs, ys;
        if (pointsLeft) {
            xs = new int[]{ x,         x + notch, x + w,     x + w,     x + notch };
            ys = new int[]{ y + h / 2, y,         y,         y + h,     y + h     };
        } else {
            xs = new int[]{ x + w,     x + w - notch, x,     x,         x + w - notch };
            ys = new int[]{ y + h / 2, y,             y,     y + h,     y + h         };
        }
        Polygon arrow = new Polygon(xs, ys, xs.length);

        // Sombra (un poco abajo y a la derecha)
        Polygon shadow = new Polygon();
        for (int i = 0; i < xs.length; i++) shadow.addPoint(xs[i] + 3, ys[i] + 3);
        g2d.setColor(new Color(0, 0, 0, 110));
        g2d.fill(shadow);

        // Cuerpo de madera con gradiente (más claro arriba, más oscuro abajo)
        Color woodLight = pressed ? new Color(96, 64, 30) : new Color(140, 95, 50);
        Color woodDark  = pressed ? new Color(50, 30, 14) : new Color(74, 48, 22);
        if (hover) {
            // tonos más cálidos cuando está hover (como recién pulida)
            woodLight = new Color(170, 120, 60);
            woodDark  = new Color(95, 62, 28);
        }
        GradientPaint wood = new GradientPaint(x, y, woodLight, x, y + h, woodDark);
        g2d.setPaint(wood);
        g2d.fill(arrow);
        g2d.setPaint(null);

        // Vetas de madera (líneas curvas tenues)
        g2d.setColor(new Color(60, 35, 15, 90));
        g2d.setStroke(new BasicStroke(1f));
        int veinY1 = y + h / 3;
        int veinY2 = y + (2 * h) / 3;
        g2d.drawLine(x + 4, veinY1, x + w - 4, veinY1 + 1);
        g2d.drawLine(x + 4, veinY2, x + w - 4, veinY2 - 1);

        // Highlight superior (reflejo de luz)
        g2d.setColor(new Color(255, 220, 160, hover ? 90 : 50));
        g2d.setStroke(new BasicStroke(1.2f));
        if (pointsLeft) {
            g2d.drawLine(x + notch + 2, y + 2, x + w - 3, y + 2);
        } else {
            g2d.drawLine(x + 3, y + 2, x + w - notch - 2, y + 2);
        }

        // Borde tallado oscuro
        g2d.setColor(new Color(40, 22, 8));
        g2d.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2d.draw(arrow);

        // Borde dorado tenue cuando hover (vibra mística)
        if (hover) {
            g2d.setColor(new Color(240, 200, 90, 180));
            g2d.setStroke(new BasicStroke(0.8f));
            g2d.draw(arrow);
        }

        // Pequeño "remache" dorado central (clavito decorativo)
        int rivetSize = (int)(3 * Juego.SCALE);
        int rivetX = pointsLeft ? x + notch + (w - notch) / 2 - rivetSize / 2
                                : x + (w - notch) / 2 - rivetSize / 2;
        int rivetY = y + h / 2 - rivetSize / 2;
        g2d.setColor(new Color(180, 140, 50));
        g2d.fillOval(rivetX, rivetY, rivetSize, rivetSize);
        g2d.setColor(new Color(255, 230, 150));
        g2d.fillOval(rivetX + 1, rivetY + 1, Math.max(1, rivetSize / 2), Math.max(1, rivetSize / 2));
    }

    private void drawDots(Graphics2D g2d, Color cc) {
        int n = characterAnimations.length;
        int dotR  = (int)(4 * Juego.SCALE);
        int gap   = (int)(12 * Juego.SCALE);
        int totalW = n * dotR * 2 + (n-1) * gap;
        int startX = Juego.GAME_WIDTH / 2 - totalW / 2;
        int dy = menuY + menuHeight + (int)(100 * Juego.SCALE);
        for (int i = 0; i < n; i++) {
            int dx = startX + i * (dotR * 2 + gap);
            if (i == playerindex) {
                g2d.setColor(cc);
                g2d.fillOval(dx, dy, dotR*2, dotR*2);
                g2d.setColor(cc.brighter());
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(dx-2, dy-2, dotR*2+4, dotR*2+4);
            } else {
                g2d.setColor(new Color(120, 120, 140, 180));
                g2d.fillOval(dx, dy, dotR*2, dotR*2);
            }
        }
    }

    private void drawHint(Graphics2D g2d) {
        String hint = "← →  cambiar  •  ENTER / A  confirmar";
        Font f = new Font("Arial", Font.PLAIN, (int)(4 * Juego.SCALE));
        g2d.setFont(f);
        FontMetrics fm = g2d.getFontMetrics();
        int hx = Juego.GAME_WIDTH / 2 - fm.stringWidth(hint) / 2;
        int hy = Juego.GAME_HEIGHT - (int)(8 * Juego.SCALE);
        g2d.setColor(new Color(0, 0, 0, 130));
        g2d.drawString(hint, hx+1, hy+1);
        g2d.setColor(new Color(180, 170, 140, 180));
        g2d.drawString(hint, hx, hy);
    }

    private int normalizeIndex(int idx) {
        int n = characterAnimations.length;
        return ((idx % n) + n) % n;
    }

    @Override public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (isIn(e, playButton))               playButton.setMousePressed(true);
        if (leftArrow.contains(e.getPoint()))  leftPressed  = true;
        if (rightArrow.contains(e.getPoint())) rightPressed = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isIn(e, playButton) && playButton.isMousePressed()) {
            juego.getPlaying().setPlayerCharacter(characterAnimations[playerindex].getPc());
            juego.getAudioPlayer().setLevelSong(juego.getPlaying().getLevelManager().getLevelIndex());
            playButton.applyGamestate();
        }
        if (leftArrow.contains(e.getPoint())  && leftPressed)  deltaindex(-1);
        if (rightArrow.contains(e.getPoint()) && rightPressed) deltaindex(1);
        leftPressed = false; rightPressed = false;
        resetButtons();
    }

    private void resetButtons() { playButton.resetBools(); }

    @Override
    public void mouseMoved(MouseEvent e) {
        playButton.setMouseOver(false);
        if (isIn(e, playButton)) playButton.setMouseOver(true);
        leftHover  = leftArrow.contains(e.getPoint());
        rightHover = rightArrow.contains(e.getPoint());
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();
        // Soporte teclado + control Xbox (que mapea stick/d-pad a A/D)
        if      (k == KeyEvent.VK_LEFT  || k == KeyEvent.VK_A)  deltaindex(-1);
        else if (k == KeyEvent.VK_RIGHT || k == KeyEvent.VK_D)  deltaindex(1);
        else if (k == KeyEvent.VK_ENTER || k == KeyEvent.VK_SPACE) {
            juego.getPlaying().setPlayerCharacter(characterAnimations[playerindex].getPc());
            juego.getAudioPlayer().setLevelSong(juego.getPlaying().getLevelManager().getLevelIndex());
            playButton.applyGamestate();
        }
        else if (k == KeyEvent.VK_ESCAPE) {
            // Botón Start del control / Escape vuelve al menú principal
            Gamestate.state = Gamestate.MENU;
        }
    }

    private void deltaindex(int i) {
        playerindex = normalizeIndex(playerindex + i);
        switchAnim = 1f;
        switchDir  = i;
    }

    @Override public void keyReleased(KeyEvent e) {}

    public class CharacterAnimation {
        private static final int ANI_SPEED = 20;
        private final PlayerCharacter pc;
        private int animTick, animInd;
        private final BufferedImage[][] animations;
        private final int scale;

        public CharacterAnimation(PlayerCharacter pc) {
            this.pc    = pc;
            this.scale = (int)(Juego.SCALE + 3);
            animations = LoadSave.loadAnimation(pc);
        }

        public void draw(Graphics g, int drawX, int drawY) {
            g.drawImage(
                animations[pc.getRowIndex(INACTIVO)][animInd],
                drawX - pc.spriteW * scale / 2,
                drawY - pc.spriteH * scale / 2,
                pc.spriteH * scale,
                pc.spriteH * scale,
                null
            );
        }

        public void update() {
            animTick++;
            if (animTick >= ANI_SPEED) {
                animTick = 0;
                if (++animInd >= pc.getNoSprite(INACTIVO)) animInd = 0;
            }
        }

        public PlayerCharacter getPc() { return pc; }
    }
}
