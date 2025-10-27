package gui;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class JFramePrincipal extends JFrame{
	private static final long serialVersionUID = 1L;
	
	public JFramePrincipal() {
	
		
		
	//Dibujado y diseño de pantalla
	this.setTitle("Ventana principal");		
	this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	this.setSize(800, 600);
	this.setLocationRelativeTo(null);
	this.setVisible(true);
	
	}
}
