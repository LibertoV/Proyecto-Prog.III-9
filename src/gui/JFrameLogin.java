package gui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.*;

public class JFrameLogin extends JFramePrincipal {
	private static final long serialVersionUID = 1L;

	public JFrameLogin() {
		super();

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
				"Siguiente");

		getRootPane().getActionMap().put("Siguiente", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				JFrameLobby frame = new JFrameLobby();
				frame.setVisible(true);
			}
		});

		panel.setLayout(new GridBagLayout());

		ImageIcon logo = new ImageIcon("resources/images/logoEmpresa1.png");
		ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(280, 200, Image.SCALE_SMOOTH));
		JLabel imagen = new JLabel(logoAjustado);

		ModernTextField userField = new ModernTextField(15);
		userField.setFont(new Font("Century Gothic",Font.PLAIN,16));
		userField.setForeground(new Color(92, 111, 104));
		ModernPasswordField passwordField = new ModernPasswordField(15);
		
		JButton loginButton = new JButton();
		loginButton.setText("Entrar");
		loginButton.setFont(new Font("Century Gothic",Font.PLAIN,16));
		loginButton.setBackground(new Color(138, 163, 155));
		loginButton.addMouseListener(new java.awt.event.MouseAdapter() {
			// Al entrar el ratón se poner el color más oscuro y se muestra el nombre de la zona
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				loginButton.setBackground(new Color(138, 163, 155).darker());
				
			}
			
			// Al salir el ratón se vuelve al color original y se oculta el nombre de la zona
			public void mouseExited(java.awt.event.MouseEvent evt) {
				loginButton.setBackground(new Color(138, 163, 155));
			}
		});
		JPanel loginPanel = new JPanel(new GridBagLayout());
		loginPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 0, 5, 10);

		gbc.fill = GridBagConstraints.HORIZONTAL;

		// Fila 1: usuario
		gbc.gridy = 0;

		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel panelUsuario = new JLabel();
		panelUsuario.setText("Usuario: ");
		panelUsuario.setFont(new Font("Century Gothic",Font.PLAIN,16));
		panelUsuario.setForeground(new Color(92, 111, 104));
		loginPanel.add(panelUsuario, gbc);
		gbc.gridx = 1;
		loginPanel.add(userField, gbc);

		// Fila 2: contraseña
		gbc.gridy = 1;

		gbc.gridx = 0;
		gbc.anchor = GridBagConstraints.WEST;
		JLabel panelContraseña = new JLabel();
		panelContraseña.setText("Contraseña: ");
		panelContraseña.setFont(new Font("Century Gothic",Font.PLAIN,16));
		panelContraseña.setForeground(new Color(92, 111, 104));
		loginPanel.add(panelContraseña, gbc);
		gbc.gridx = 1;
		loginPanel.add(passwordField, gbc);

		// Fila 3: login
		gbc.gridy = 2;
		gbc.gridx = 0;
		gbc.gridwidth = 2;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.NONE;
		loginPanel.add(loginButton, gbc);

		GridBagConstraints center = new GridBagConstraints();

		center.gridx = 0;
		center.gridy = 0;
		center.anchor = GridBagConstraints.CENTER;
		center.insets = new Insets(-120, 0, 140, 0);

		panel.add(imagen, center);
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

		// El dibujo del componente fue creado con la ayuda de IA generativa
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
