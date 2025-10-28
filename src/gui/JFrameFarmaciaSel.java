package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class JFrameFarmaciaSel extends  JFrameLobby{
	private static final long serialVersionUID = 1L;
	
	public JFrameFarmaciaSel() {
		super();
		
		
        panel.setBackground(new Color(63,180,190));
		
        ImageIcon logo = new ImageIcon("resources/images/logo.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        
        
        JPanel mainPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JPanel borderLayoutPanel = new JPanel(new BorderLayout());
        JButton options = new JButton("☰");
        options.setFont(new Font("Arial", Font.BOLD,20));
        options.setSize(10,0);
        options.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Seleccionado");
				dispose();
			}
		});
        
        
        JPanel gridLayoutPanel = new JPanel(new GridLayout(2, 2, 10, 10));
		gridLayoutPanel.add(options);
        borderLayoutPanel.add(gridLayoutPanel, BorderLayout.WEST);
        
        mainPanel.add(borderLayoutPanel);
	}
	public static void main(String[] args) {
        // Crear la ventana en el hilo de eventos de Swing
    	SwingUtilities.invokeLater(() -> {
        	// Crear una instancia de la calculadora y hacerla visible
            JFrameFarmaciaSel farmacia = new JFrameFarmaciaSel();
            farmacia.setVisible(true);
        });
    }
}
