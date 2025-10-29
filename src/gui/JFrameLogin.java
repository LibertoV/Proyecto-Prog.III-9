package gui;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


public class JFrameLogin extends JFramePrincipal {
    private static final long serialVersionUID = 1L;

    public JFrameLogin() {
        super();
        
        panel.setLayout(new GridBagLayout());

        
        ImageIcon logo = new ImageIcon("resources/images/logoEmpresa1.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(280, 200, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        
        
        JTextField userField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Entrar");
        
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); 
        
        
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 10);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        // Fila 1: usuario
        gbc.gridy = 0;
        
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        // Fila 2: contraseña
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Fila 3: login
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(loginButton, gbc);
        
        
        
        GridBagConstraints center = new GridBagConstraints();
        
        center.gridx = 0;
        center.gridy = 0;
        center.anchor = GridBagConstraints.CENTER;
        center.insets = new Insets(-120, 0, 140, 0);
        
        panel.add(imagen,center);
        center.gridy = 1;
        panel.add(loginPanel, center);
       
        
        loginButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new JFrameLobby();
			}
        	
        });
    }
}

