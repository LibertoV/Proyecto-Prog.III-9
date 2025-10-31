package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class JFrameSelPedido extends JFramePrincipal {
	
	private static final long serialVersionUID = 1L;
	public JFrameSelPedido() {
		this.setTitle("Informacion del pedido");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Cabecera
		this.add(crearCabecera(), BorderLayout.NORTH);
		
		//Cuerpo
		//this.add(CrearCuerpo(), BorderLayout.CENTER);
		
		//Parte de abajo
		this.add(crearAbajo(), BorderLayout.SOUTH);
		
		
		
		this.setVisible(true);
		
	}
	
	public JPanel crearCabecera() {
		JPanel main = new JPanel(new BorderLayout());
		
		
		JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel nPed = new JLabel("Nº pedido: ");
		izq.add(nPed);
		
		JLabel numero = new JLabel("000", SwingConstants.RIGHT);
		numero.setPreferredSize(new Dimension(70,20));
		numero.setBackground(Color.white);
		numero.setBorder(BorderFactory.createLineBorder(Color.gray));
		numero.setOpaque(true);
		izq.add(numero);
		main.add(izq, BorderLayout.WEST);
		
		
		JPanel dcha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel estado = new JLabel("Pendiente", SwingConstants.CENTER);
		estado.setPreferredSize(new Dimension(150,20));
		estado.setOpaque(true);
		estado.setBackground(Color.LIGHT_GRAY);
		estado.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	
		dcha.add(estado);
		main.add(dcha, BorderLayout.EAST);
		
		return main;
		
	}
	
	public JPanel crearCuerpo() {
		
		
		return panel;
		
	}
	
	public JPanel crearAbajo() {
		JPanel abajo = new JPanel(new FlowLayout(FlowLayout.LEFT));
		
		JLabel nombreUsuario = new JLabel("Usuario: ");
		abajo.add(nombreUsuario);
		
		JLabel nombre = new JLabel("XXX");
		abajo.add(nombre);
		
		return abajo;
		
		
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JFrameSelPedido());

	}

}
