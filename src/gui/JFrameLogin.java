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
                loading spinner = new loading(3);
                
                panel.add(spinner, center);
                loginPanel.setVisible(false);
                panel.revalidate();
                panel.repaint();   

                spinner.setOnFinish(() -> {
                    SwingUtilities.invokeLater(() -> {
                    	new JFrameLobby();
                    });
                });

                spinner.iniciar();
            }
        });

    }

    public class loading extends JPanel implements Runnable {
		private static final long serialVersionUID = 1L;
		private int angulo = 0;
        private boolean ejecutando = false;
        private int duracion; // milisegundos
        private Runnable onFinish; 

        public loading(int duracion) {
            this.duracion = duracion * 1000;
            setPreferredSize(new Dimension(60, 60));
            setOpaque(false);
        }

        public void iniciar() {
            if (!ejecutando) {
                ejecutando = true;
                Thread hilo = new Thread(this, "loading");
                hilo.setDaemon(true);
                hilo.start();
            }
        }

        public void setOnFinish(Runnable onFinish) {
            this.onFinish = onFinish;
        }

        @Override
        public void run() {
            long inicio = System.currentTimeMillis();

            while (System.currentTimeMillis() - inicio < duracion) {
                angulo = (angulo + 10) % 360;
                repaint();
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            ejecutando = false;

            SwingUtilities.invokeLater(onFinish);
            
        }
        
        
        //El dibujo del componente fue creado con la ayuda de IA generativa
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int w = getWidth();
            int h = getHeight();
            int radio = Math.min(w, h) / 3;
            int x = w / 2 - radio;
            int y = h / 2 - radio;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(6, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            // Sombra / fondo
            g2.setColor(new Color(0, 0, 0, 50));
            g2.drawArc(x + 2, y + 2, radio * 2, radio * 2, angulo, 270);

            // Arco principal
            g2.setColor(new Color(66, 135, 245));
            g2.drawArc(x, y, radio * 2, radio * 2, angulo, 300);
        }
    }

}

