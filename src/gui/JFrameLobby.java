package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import domain.FrameManager;

public class JFrameLobby extends JFramePrincipal {
	private static final long serialVersionUID = 1L;

    public JFrameLobby() {
        super();
        panel = super.panel;

        
        panel.setLayout(new BorderLayout()); 
        
        
        ImageIcon logo = new ImageIcon("resources/images/logo.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        
        
        JButton loginButton = new JButton("Cerrar sesión");
        JButton farmaciasButton = new JButton("Farmacias");
        JButton pedidosButton = new JButton("Pedidos");
        JButton almacenButton = new JButton("Almacen");
        JButton ventasButton = new JButton("Ventas");
        JButton medicamentosButton = new JButton("Medicamentos");
        
        
        
        
        JPanel SelectionPanel = new JPanel(new GridLayout(1,7));
        SelectionPanel.setOpaque(false); 
        SelectionPanel.add(imagen);
        SelectionPanel.add(farmaciasButton);
        SelectionPanel.add(pedidosButton);
        SelectionPanel.add(almacenButton);
        SelectionPanel.add(ventasButton);
        SelectionPanel.add(medicamentosButton);
        SelectionPanel.add(loginButton);
        
        SelectionPanel.setPreferredSize(new Dimension(SelectionPanel.getPreferredSize().width, 50));
        
        
        panel.add(SelectionPanel, BorderLayout.NORTH);
        
        
        
        panel.add(new JPanel(), BorderLayout.CENTER);
        
        
       
        
        loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				FrameManager.Relogin();
				System.out.println("deloged");
			}
        	
        });
    }
}