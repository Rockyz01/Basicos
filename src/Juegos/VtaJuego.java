package Juegos;

import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;

import javax.swing.JFrame;

public class VtaJuego extends JFrame {
    private JFrame jframe;
    public VtaJuego(PanelJuego n) {
        
		jframe = new JFrame();

		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(n);
		
		jframe.setResizable(false);
		jframe.pack();
		jframe.setLocationRelativeTo(null);
		jframe.setVisible(true);
		jframe.addWindowFocusListener(new WindowFocusListener() {

			@Override
			public void windowLostFocus(WindowEvent e) {
				n.getGame().windowFocusLost();
			}

			@Override
			public void windowGainedFocus(WindowEvent e) {

			}
		});

	}

}