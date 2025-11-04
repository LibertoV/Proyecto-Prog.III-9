package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import domain.FrameManager;

public class JFrameFichaCliente extends JFramePrincipal{

	
	private static final long serialVersionUID = 1L;
	
	public JFrameFichaCliente(){
		this.setTitle("Ficha Clientes");
		this.setSize(new Dimension(500,375));
		this.setLocationRelativeTo(null);
		
		
		this.add(crearPanelUsuario(), FlowLayout.LEFT);
	}

	private Component crearPanelUsuario() {
		JPanel panelPrincipal = new JPanel(new FlowLayout());
		
		JPanel panelNombre = new JPanel();
		panelNombre.add(new JLabel("Nombre Usuario"));
		panelNombre.add(new JTextField(20));
		panelPrincipal.add(panelNombre,FlowLayout.LEFT);
		return panelPrincipal;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(() -> new JFrameFichaCliente());
		
	}

}
