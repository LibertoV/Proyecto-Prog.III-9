package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class JFrameLogin extends JFramePrincipal {
    private static final long serialVersionUID = 1L;
    
    // 1. Declaramos el mapa de usuarios
    private Map<String, String> baseDeDatosUsuarios;
    
    // Declaramos los campos como variables de instancia para acceder a ellos desde los métodos
    private ModernTextField userField;
    private ModernPasswordField passwordField;
    private JPanel loginPanel;
    private JPanel panelPrincipal; // Referencia al panel principal (super.panel)
    private GridBagConstraints centerConstraints;

    public JFrameLogin() {
        super();
        this.panelPrincipal = panel; // Asignamos el panel del padre

        // 2. Inicializamos el mapa con perfiles de prueba
        baseDeDatosUsuarios = new HashMap<>();
        baseDeDatosUsuarios.put("admin", "admin");
        baseDeDatosUsuarios.put("empleado", "empresa2024");
        baseDeDatosUsuarios.put("invitado", "0000");

        // Configuración del layout
        panel.setLayout(new GridBagLayout());

        ImageIcon logo = new ImageIcon("resources/images/logoEmpresa1.png");
        // Nota: Asegúrate de manejar el caso si la imagen es null para evitar errores
        Image img = logo.getImage();
        ImageIcon logoAjustado = null;
        if(img != null) {
             logoAjustado = new ImageIcon(img.getScaledInstance(280, 200, Image.SCALE_SMOOTH));
        }
        JLabel imagen = new JLabel(logoAjustado);

        // Inicialización de campos
        userField = new ModernTextField(15);
        userField.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        userField.setForeground(new Color(92, 111, 104));
        
        passwordField = new ModernPasswordField(15);

        JButton loginButton = new JButton();
        loginButton.setText("Entrar");
        loginButton.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        loginButton.setBackground(new Color(138, 163, 155));
        
        // Efecto Hover del botón
        loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(138, 163, 155).darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                loginButton.setBackground(new Color(138, 163, 155));
            }
        });

        loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // --- Construcción del Formulario ---
        // Fila 0: Usuario
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel panelUsuario = new JLabel("Usuario: ");
        panelUsuario.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        panelUsuario.setForeground(new Color(92, 111, 104));
        loginPanel.add(panelUsuario, gbc);
        gbc.gridx = 1;
        loginPanel.add(userField, gbc);

        // Fila 1: Contraseña
        gbc.gridy = 1;
        gbc.gridx = 0;
        JLabel panelContrasena = new JLabel("Contraseña: ");
        panelContrasena.setFont(new Font("Century Gothic", Font.PLAIN, 16));
        panelContrasena.setForeground(new Color(92, 111, 104));
        loginPanel.add(panelContrasena, gbc);
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);

        // Fila 2: Botón
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        loginPanel.add(loginButton, gbc);

        // Añadir todo al panel principal
        centerConstraints = new GridBagConstraints();
        centerConstraints.gridx = 0;
        centerConstraints.gridy = 0;
        centerConstraints.anchor = GridBagConstraints.CENTER;
        centerConstraints.insets = new Insets(-120, 0, 140, 0);

        panel.add(imagen, centerConstraints);
        centerConstraints.gridy = 1;
        panel.add(loginPanel, centerConstraints);

        // 3. Lógica del Botón: Llamamos a la función de validación
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });

        // 4. Lógica de la tecla ENTER: También llama a la función de validación
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
            .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "Siguiente");

        getRootPane().getActionMap().put("Siguiente", new AbstractAction() {
            private static final long serialVersionUID = 1L;
            public void actionPerformed(ActionEvent e) {
                intentarLogin();
            }
        });
    }

    /**
     * Método centralizado para validar credenciales e iniciar la animación
     */
    private void intentarLogin() {
        String usuarioInput = userField.getText();
        String passwordInput = new String(passwordField.getPassword()); // Convertimos char[] a String

        // Verificamos si el usuario existe y la contraseña coincide
        if (baseDeDatosUsuarios.containsKey(usuarioInput) && 
            baseDeDatosUsuarios.get(usuarioInput).equals(passwordInput)) {
            
            // LOGIN CORRECTO: Iniciamos la animación
            iniciarAnimacionCarga();
            
        } else {
            // LOGIN INCORRECTO
            JOptionPane.showMessageDialog(this, 
                "Usuario o contraseña incorrectos", 
                "Error de acceso", 
                JOptionPane.ERROR_MESSAGE);
            // Limpiamos la contraseña para que reintente
            passwordField.setText(""); 
        }
    }

    private void iniciarAnimacionCarga() {
        loading spinner = new loading(3);

        panelPrincipal.add(spinner, centerConstraints);
        loginPanel.setVisible(false);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        spinner.setOnFinish(() -> {
            SwingUtilities.invokeLater(() -> {
                JFrameLobby lobby = new JFrameLobby();
                lobby.setVisible(true);
                dispose(); // Cerramos la ventana de login
            });
        });

        spinner.iniciar();
    }

    // --- Clase Loading (Tu código original) ---
    public class loading extends JPanel implements Runnable {
        private static final long serialVersionUID = 1L;
        private int angulo = 0;
        private boolean ejecutando = false;
        private int duracion; 
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
            if (onFinish != null) {
                SwingUtilities.invokeLater(onFinish);
            }
        }

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
            g2.setColor(new Color(0, 0, 0, 50));
            g2.drawArc(x + 2, y + 2, radio * 2, radio * 2, angulo, 270);
            g2.setColor(new Color(66, 135, 245));
            g2.drawArc(x, y, radio * 2, radio * 2, angulo, 300);
        }
    }
}