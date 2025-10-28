package gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public abstract class JFramePrincipal extends JFrame{
	private static final long serialVersionUID = 1L;
	public JPanel panel;
	
	public JFramePrincipal() {
	
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
}
