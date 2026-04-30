package Eventos;

import Juegos.Juego;
import gamestates.Gamestate;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * ControlXbox — Lee el gamepad en Windows via PowerShell + XInput.
 * Escribe el script a un archivo temporal para evitar problemas con
 * la política de ejecución y los heredocs inline.
 */
public class ControlXbox implements Runnable {

    private final Juego juego;
    private volatile boolean running = false;
    private Thread hilo;
    private File scriptTemporal;

    // Estado anterior para detectar cambios (flanco)
    private boolean estLeft, estRight, estUp, estDown;
    private boolean estA, estB, estX, estY, estStart;

    // Script PowerShell que se escribe a disco
    private static final String PS_SCRIPT =
        "Add-Type -TypeDefinition @\"\n" +
        "using System;\n" +
        "using System.Runtime.InteropServices;\n" +
        "public class XInput {\n" +
        "    [StructLayout(LayoutKind.Sequential)] public struct GAMEPAD {\n" +
        "        public ushort wButtons;\n" +
        "        public byte bLeftTrigger;\n" +
        "        public byte bRightTrigger;\n" +
        "        public short sThumbLX;\n" +
        "        public short sThumbLY;\n" +
        "        public short sThumbRX;\n" +
        "        public short sThumbRY;\n" +
        "    }\n" +
        "    [StructLayout(LayoutKind.Sequential)] public struct STATE {\n" +
        "        public uint dwPacketNumber;\n" +
        "        public GAMEPAD Gamepad;\n" +
        "    }\n" +
        "    [DllImport(\"xinput1_4.dll\")]\n" +
        "    public static extern uint XInputGetState(uint dwUserIndex, ref STATE pState);\n" +
        "    public const ushort DPAD_UP    = 0x0001;\n" +
        "    public const ushort DPAD_DOWN  = 0x0002;\n" +
        "    public const ushort DPAD_LEFT  = 0x0004;\n" +
        "    public const ushort DPAD_RIGHT = 0x0008;\n" +
        "    public const ushort BTN_START  = 0x0010;\n" +
        "    public const ushort BTN_BACK   = 0x0020;\n" +
        "    public const ushort BTN_A      = 0x1000;\n" +
        "    public const ushort BTN_B      = 0x2000;\n" +
        "    public const ushort BTN_X      = 0x4000;\n" +
        "    public const ushort BTN_Y      = 0x8000;\n" +
        "    public const int    DEAD       = 8000;\n" +
        "}\n" +
        "\"@\n" +
        "$prev = ''\n" +
        "while ($true) {\n" +
        "    $s = New-Object XInput+STATE\n" +
        "    $result = [XInput]::XInputGetState(0, [ref]$s)\n" +
        "    if ($result -eq 0) {\n" +
        "        $b  = $s.Gamepad.wButtons\n" +
        "        $lx = $s.Gamepad.sThumbLX\n" +
        "        $ly = $s.Gamepad.sThumbLY\n" +
        "        $left  = (($b -band [XInput]::DPAD_LEFT)  -ne 0) -or ($lx -lt -[XInput]::DEAD)\n" +
        "        $right = (($b -band [XInput]::DPAD_RIGHT) -ne 0) -or ($lx -gt  [XInput]::DEAD)\n" +
        "        $up    = (($b -band [XInput]::DPAD_UP)    -ne 0) -or ($ly -gt  [XInput]::DEAD)\n" +
        "        $down  = (($b -band [XInput]::DPAD_DOWN)  -ne 0) -or ($ly -lt -[XInput]::DEAD)\n" +
        "        $a     = ($b -band [XInput]::BTN_A)     -ne 0\n" +
        "        $bBtn  = ($b -band [XInput]::BTN_B)     -ne 0\n" +
        "        $x     = ($b -band [XInput]::BTN_X)     -ne 0\n" +
        "        $y     = ($b -band [XInput]::BTN_Y)     -ne 0\n" +
        "        $start = ($b -band [XInput]::BTN_START) -ne 0\n" +
        "        $rt    = $s.Gamepad.bRightTrigger -gt 30\n" +
        "        $line = \"$left,$right,$up,$down,$a,$bBtn,$x,$y,$start,$rt\"\n" +
        "        if ($line -ne $prev) {\n" +
        "            [Console]::Out.WriteLine($line)\n" +
        "            [Console]::Out.Flush()\n" +
        "            $prev = $line\n" +
        "        }\n" +
        "    }\n" +
        "    Start-Sleep -Milliseconds 16\n" +
        "}\n";

    public ControlXbox(java.awt.Component ventana) {
        this.juego = null;
    }

    public ControlXbox(Juego juego) {
        this.juego = juego;
    }

    public void start() {
        if (running) return;
        running = true;
        hilo = new Thread(this, "ControlXbox-Thread");
        hilo.setDaemon(true);
        hilo.start();
        System.out.println("[ControlXbox] Iniciado.");
    }

    public void stop() {
        running = false;
        if (hilo != null) hilo.interrupt();
        borrarScriptTemporal();
    }

    @Override
    public void run() {
        String os = System.getProperty("os.name", "").toLowerCase();
        if (os.contains("linux"))
            runLinux();
        else if (os.contains("win"))
            runWindows();
        else
            System.out.println("[ControlXbox] SO no soportado: " + os);
    }

    // ── WINDOWS via PowerShell escrito a archivo temporal ────────────────────
    private void runWindows() {
        System.out.println("[ControlXbox] Iniciando gamepad via PowerShell/XInput...");
        try {
            // Escribir el script a un archivo .ps1 temporal
            scriptTemporal = File.createTempFile("xbox_ctrl_", ".ps1");
            scriptTemporal.deleteOnExit();
            try (FileWriter fw = new FileWriter(scriptTemporal)) {
                fw.write(PS_SCRIPT);
            }
            System.out.println("[ControlXbox] Script temporal: " + scriptTemporal.getAbsolutePath());

            // Ejecutar con -ExecutionPolicy Bypass para evitar bloqueos
            ProcessBuilder pb = new ProcessBuilder(
                "powershell",
                "-NoProfile",
                "-NonInteractive",
                "-ExecutionPolicy", "Bypass",
                "-File", scriptTemporal.getAbsolutePath()
            );
            pb.redirectErrorStream(false); // separar stderr para no mezclar
            Process proc = pb.start();

            // Leer errores en hilo aparte para diagnóstico
            Thread errThread = new Thread(() -> {
                try (BufferedReader err = new BufferedReader(
                        new InputStreamReader(proc.getErrorStream()))) {
                    String line;
                    while ((line = err.readLine()) != null)
                        System.err.println("[ControlXbox-PS-ERR] " + line);
                } catch (Exception ignored) {}
            }, "ControlXbox-StdErr");
            errThread.setDaemon(true);
            errThread.start();

            BufferedReader reader = new BufferedReader(
                new InputStreamReader(proc.getInputStream())
            );

            String linea;
            while (running && (linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                // Formato: left,right,up,down,a,b,x,y,start
                String[] p = linea.split(",");
                if (p.length < 10) continue;

                boolean left  = Boolean.parseBoolean(p[0]);
                boolean right = Boolean.parseBoolean(p[1]);
                boolean up    = Boolean.parseBoolean(p[2]);
                boolean down  = Boolean.parseBoolean(p[3]);
                boolean a     = Boolean.parseBoolean(p[4]);
                boolean b     = Boolean.parseBoolean(p[5]);
                boolean x     = Boolean.parseBoolean(p[6]);
                boolean y     = Boolean.parseBoolean(p[7]);
                boolean start = Boolean.parseBoolean(p[8]);
                boolean rt    = Boolean.parseBoolean(p[9]);

                // Enviar solo cambios (flanco)
                if (left  != estLeft)  { teclaDirecta(KeyEvent.VK_A,      left);  estLeft  = left;  }
                if (right != estRight) { teclaDirecta(KeyEvent.VK_D,      right); estRight = right; }
                if (up    != estUp)    { teclaDirecta(KeyEvent.VK_W,      up);    estUp    = up;    }
                if (down  != estDown)  { teclaDirecta(KeyEvent.VK_S,      down);  estDown  = down;  }
                if (a     != estA)     {
                    teclaDirecta(KeyEvent.VK_SPACE, a);
                    teclaDirecta(KeyEvent.VK_ENTER, a);
                    estA = a;
                }
                if (x != estX)         { teclaDirecta(KeyEvent.VK_Z,      x);     estX     = x;     }
                if (y != estY)         { teclaDirecta(KeyEvent.VK_X,      y);     estY     = y;     }
                // B = dash, Start = pausa/escape, RT = ataque
                if (b     != estB)     { teclaDirecta(KeyEvent.VK_F2,     b);     estB     = b;     }
                if (start != estStart) { teclaDirecta(KeyEvent.VK_ESCAPE, start); estStart = start; }
                if (rt    != estY)     { teclaDirecta(KeyEvent.VK_F,      rt);    estY     = rt;    }
            }
            proc.destroy();
        } catch (Exception e) {
            System.err.println("[ControlXbox] Error: " + e.getMessage());
            e.printStackTrace();
        }
        borrarScriptTemporal();
        System.out.println("[ControlXbox] Proceso terminado.");
    }

    private void borrarScriptTemporal() {
        if (scriptTemporal != null && scriptTemporal.exists())
            scriptTemporal.delete();
    }

    // ── LINUX via /dev/input/jsX ──────────────────────────────────────────────
    private void runLinux() {
        java.io.File dev = null;
        for (String path : new String[]{"/dev/input/js0", "/dev/input/js1", "/dev/input/js2"}) {
            java.io.File f = new java.io.File(path);
            if (f.exists() && f.canRead()) { dev = f; break; }
        }
        if (dev == null) {
            System.out.println("[ControlXbox] No se encontró gamepad Linux.");
            return;
        }
        System.out.println("[ControlXbox] Linux gamepad: " + dev.getAbsolutePath());

        try (java.io.FileInputStream fis = new java.io.FileInputStream(dev);
             java.nio.channels.FileChannel ch = fis.getChannel()) {

            java.nio.ByteBuffer buf = java.nio.ByteBuffer.allocate(8)
                .order(java.nio.ByteOrder.LITTLE_ENDIAN);

            while (running) {
                buf.clear();
                int leidos;
                try { leidos = ch.read(buf); } catch (java.io.IOException e) { break; }
                if (leidos < 8) continue;
                buf.flip();
                buf.getInt();
                short valor  = buf.getShort();
                byte  tipo   = buf.get();
                byte  numero = buf.get();
                int tipoReal = tipo & 0x7F;
                if (tipoReal == 1) procesarBotonLinux(numero & 0xFF, valor != 0);
                else if (tipoReal == 2) procesarEjeLinux(numero & 0xFF, valor);
            }
        } catch (Exception e) {
            System.err.println("[ControlXbox] Error Linux: " + e.getMessage());
        }
    }

    private final short[] ejesLinux = new short[16];
    private static final int UMBRAL = 8000;

    private void procesarBotonLinux(int num, boolean pres) {
        switch (num) {
            case 0: teclaDirecta(KeyEvent.VK_SPACE, pres); teclaDirecta(KeyEvent.VK_ENTER, pres); break;
            case 1: teclaDirecta(KeyEvent.VK_ESCAPE, pres); break;
            case 2: teclaDirecta(KeyEvent.VK_Z, pres); break;
            case 3: teclaDirecta(KeyEvent.VK_X, pres); break;
            case 7: teclaDirecta(KeyEvent.VK_ESCAPE, pres); break;
        }
    }

    private void procesarEjeLinux(int num, short valor) {
        if (num >= ejesLinux.length) return;
        short ant = ejesLinux[num];
        ejesLinux[num] = valor;
        if (num == 0 || num == 6) {
            if ((valor < -UMBRAL) != (ant < -UMBRAL)) teclaDirecta(KeyEvent.VK_A, valor < -UMBRAL);
            if ((valor >  UMBRAL) != (ant >  UMBRAL)) teclaDirecta(KeyEvent.VK_D, valor > UMBRAL);
        }
        if (num == 1 || num == 7) {
            if ((valor < -UMBRAL) != (ant < -UMBRAL)) teclaDirecta(KeyEvent.VK_W, valor < -UMBRAL);
            if ((valor >  UMBRAL) != (ant >  UMBRAL)) teclaDirecta(KeyEvent.VK_S, valor > UMBRAL);
        }
    }

    // ── Envía eventos de teclado directamente al gamestate activo ─────────────
    private void teclaDirecta(int vkCode, boolean presionar) {
        if (juego == null) return;
        java.awt.Component panel = juego.getPanel();
        KeyEvent ke = new KeyEvent(
            panel != null ? panel : new java.awt.Label(),
            presionar ? KeyEvent.KEY_PRESSED : KeyEvent.KEY_RELEASED,
            System.currentTimeMillis(), 0, vkCode, KeyEvent.CHAR_UNDEFINED
        );
        switch (Gamestate.state) {
            case MENU:
                if (presionar) juego.getMenu().keyPressed(ke);
                else           juego.getMenu().keyReleased(ke);
                break;
            case PLAYING:
                if (presionar) juego.getPlaying().keyPressed(ke);
                else           juego.getPlaying().keyReleased(ke);
                break;
            case OPTIONS:
                if (presionar) juego.getGameOptions().keyPressed(ke);
                else           juego.getGameOptions().keyReleased(ke);
                break;
            case PLAYER_SELECTION:
                if (presionar) juego.getPlayerSelection().keyPressed(ke);
                else           juego.getPlayerSelection().keyReleased(ke);
                break;
        }
    }

    public void liberarTodo() {
        for (int vk : new int[]{
            KeyEvent.VK_A, KeyEvent.VK_D, KeyEvent.VK_W, KeyEvent.VK_S,
            KeyEvent.VK_SPACE, KeyEvent.VK_ENTER, KeyEvent.VK_Z,
            KeyEvent.VK_X, KeyEvent.VK_ESCAPE
        }) teclaDirecta(vk, false);
    }

    public boolean isRunning() { return running; }
}
