package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public abstract class JFramePrincipal extends JFrame{
	private static final long serialVersionUID = 1L;
	public JPanel panel;
	public static JFrame panelActual;	
	private Map<String, ImageIcon> cache = new HashMap<>();
	
	public JFramePrincipal() {
		if (!(this instanceof JFrameSelPedido) && !(this instanceof JFrameFichaCliente) && panelActual != null) {
			System.out.println(panelActual);
			panelActual.dispose();
			panelActual = this;	
		}
		
		panel = new JPanel();
		panel.setOpaque(true);
		panel.setBackground(new Color(171, 245, 182));
		
		//Dibujado y diseño de pantalla
		this.setTitle("Ventana principal");		
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(800, 600);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		this.add(panel);
		this.setMinimumSize(new Dimension(800, 600));
		this.setMaximumSize(new Dimension(1280, 720)); 
	}
	
	public ImageIcon getCachedIcon(String path) {
        if (cache.containsKey(path)) return cache.get(path);
        try {
            ImageIcon iconoOriginal = new ImageIcon(path);
            Image img = iconoOriginal.getImage().getScaledInstance(40, 20, Image.SCALE_SMOOTH);
            ImageIcon iconoEscalado = new ImageIcon(img);
            cache.put(path, iconoEscalado);
            return iconoEscalado;
        } catch (Exception ex) {
            return null;
        }
        
    };
	public KeyListener listenerVolver(Class<? extends JFramePrincipal> frameClase) {
		KeyListener listener = new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyReleased(KeyEvent e) {
				boolean ctrlPresionado = e.isControlDown();
				
				if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
		            dispose();
		            try {
						Object instancia = frameClase.getDeclaredConstructor().newInstance();
						((JFrame) instancia).setVisible(true);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
			
		};
		
		return listener;		
	}
}
