package gui;


import java.awt.*;
import javax.swing.*;

public class JFrameLogin extends JFramePrincipal {
    private static final long serialVersionUID = 1L;

    public JFrameLogin() {
        super();
        panel = super.panel;

        // Configuramos el layout del panel principal
        panel.setLayout(new GridBagLayout()); // Importante: GridBagLayout permite centrar fácilmente

        // Creamos los componentes del login
        JTextField userField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Entrar");

        // Panel de login (contenedor)
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 10); // Márgenes entre componentes

        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Fila 0: usuario
        gbc.gridy = 0;
        
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Usuario:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        // Fila 1: contraseña
        gbc.gridy = 1;
        
        gbc.gridx = 0;
        loginPanel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Fila 4: botón de login
        gbc.gridy = 2;
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        loginPanel.add(loginButton, gbc);
        
        
        
        GridBagConstraints center = new GridBagConstraints();
        center.gridx = 0;
        center.gridy = 0;
        center.weightx = 1.0;
        center.weighty = 1.0;
        center.anchor = GridBagConstraints.CENTER;
        panel.add(loginPanel, center);
    }
}

