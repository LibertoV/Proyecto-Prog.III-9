package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;


public class JFrameFarmaciaSel extends JFrameLobby {
	private static final long serialVersionUID = 1L;

	private JPanel menu; 
	private boolean menuAbierto = false; 
	private JPanel central;

	public JFrameFarmaciaSel() {
		super();
		this.setTitle("Menu Principal Farmacia");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setFocusable(true); 
		this.addKeyListener(listenerVolver(JFrameLobby.class));
		this.setLocationRelativeTo(null);

		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.setBackground(new Color(240, 245, 249)); 

		mainPanel.add(CrearCabeza(), BorderLayout.NORTH);

		central = new JPanel(new BorderLayout());
		central.add(gridCajas(), BorderLayout.CENTER);

		menu = crearMenuLateral(); 
		central.add(menu, BorderLayout.WEST);

		mainPanel.add(central, BorderLayout.CENTER);

		add(mainPanel);
		setVisible(true);
	}

	private JPanel crearMenuLateral() {
		JPanel panelMenu = new JPanel(new GridLayout(9, 1, 0, 10));
		panelMenu.setBackground(new Color(50, 50, 50)); 
		panelMenu.setPreferredSize(new Dimension(200, 0));

		 String[] opciones = {"Pedidos", "Almacen", "Ventas", "Clientes","Trabajadores","Salir"};

		for (String op : opciones) {
			JButton btn = new JButton(op);
			btn.setForeground(Color.WHITE); 
			btn.setBackground(new Color(50, 50, 50)); 
			btn.setFont(new Font("Arial", Font.PLAIN, 14));

			btn.setOpaque(true);
			btn.setContentAreaFilled(false);

			btn.setHorizontalAlignment(SwingConstants.LEFT);
			btn.setBorder(new EmptyBorder(0, 20, 0, 0));
			
			btn.addActionListener(e ->{
            	gestionarMenu(op);
            });


			btn.addMouseListener(new MouseAdapter() {
				public void mouseEntered(MouseEvent e) {
					btn.setOpaque(true);
					btn.setContentAreaFilled(true);
					btn.setBackground(new Color(63, 180, 190)); 
					btn.setForeground(Color.BLACK);

				}

				public void mouseExited(MouseEvent e) {
					btn.setOpaque(true);
					btn.setContentAreaFilled(false);
					btn.setBackground(new Color(50, 50, 50)); 
					btn.setForeground(Color.WHITE);
				}
			});
			
			panelMenu.add(btn);
		}

		panelMenu.setVisible(false); 
		return panelMenu;
	}
	
	private void gestionarMenu(String opcion) {
    	switch(opcion) {
    	case "Pedidos":
    		new JFrameListaPedidos();
    		dispose();
    		break;
    	case "Clientes":
    		new JFrameListaClientes();
    		dispose();
    		break;
    	case "Ventas":
    		new JFrameVentas();
    		dispose();
    		break;
    	case "Trabajadores":
    		new JFrameListaTrabajadores();
    		dispose();
    		break;
    	case "Salir":
    		new JFrameLobby();
    		dispose();
    		break;
    	case "Almacen":
    		new JFrameAlmacen();
    		dispose();
    		break;
    	}
    	
    }

	private Component gridCajas() {
		JPanel grid = new JPanel(new GridLayout(2, 2, 30, 30));
		grid.setBorder(new EmptyBorder(40, 40, 40, 40));
		grid.setBackground(new Color(240, 245, 249)); 

		grid.add(crearTarjeta("VENTAS"));
		grid.add(crearTarjeta("PEDIDOS"));
		grid.add(crearTarjeta("CLIENTES"));
		grid.add(crearTarjeta("ALMACEN"));

		return grid; 
	}

	private Component crearTarjeta(String string) {
		JPanel tarjeta = new JPanel(new GridBagLayout());
		tarjeta.setBackground(Color.WHITE); 

		tarjeta.setBorder(new LineBorder(new Color(220, 220, 220), 1));
		tarjeta.setCursor(new Cursor(Cursor.HAND_CURSOR));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.insets = new Insets(5, 0, 10, 0);

		ImageIcon logo1 = new ImageIcon("resources/images/" + string + ".png");
		ImageIcon logoAjustado1 = null;
		logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		
		
		JLabel icono = new JLabel();

		icono.setIcon(logoAjustado1);

		tarjeta.add(icono, gbc); 

		gbc.gridy = 1;
		gbc.insets = new Insets(0, 0, 0, 0);

		JLabel lblTexto = new JLabel(string);
		lblTexto.setFont(new Font("Arial", Font.BOLD, 18));
		lblTexto.setForeground(new Color(60, 60, 60)); 
		tarjeta.add(lblTexto, gbc);

		tarjeta.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				tarjeta.setBackground(new Color(63, 180, 190)); 
				lblTexto.setForeground(Color.WHITE); 
				tarjeta.setBorder(new LineBorder(new Color(63, 180, 190).darker(), 2)); 
			}

			@Override
			public void mouseExited(MouseEvent e) {
				tarjeta.setBackground(Color.WHITE); 
				lblTexto.setForeground(new Color(60, 60, 60)); 
				tarjeta.setBorder(new LineBorder(new Color(220, 220, 220), 1)); 
			}

				
			@Override
			public void mouseClicked(MouseEvent e) {
				String formatoBueno = string.substring(0,1).toUpperCase() + string.substring(1).toLowerCase();
				gestionarMenu(formatoBueno);
			}

		});
		return tarjeta;
	}

	private Component CrearCabeza() {
		JPanel cabeza = new JPanel(new BorderLayout());
		cabeza.setBackground(Color.WHITE); 
		cabeza.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

		ImageIcon logo1 = new ImageIcon("resources/images/imagenMenuTresRayas.png");
		ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));

		JButton botonMenu = new JButton(logoAjustado1);
		botonMenu.setBorder(null);
		botonMenu.setPreferredSize(new Dimension(60, 60));
		botonMenu.setContentAreaFilled(false); 
		botonMenu.setCursor(new Cursor(Cursor.HAND_CURSOR));

		botonMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				toggleMenu();
			}
		});

		JLabel tituloLabel = new JLabel("MENU PRINCIPAL", SwingConstants.CENTER);
		tituloLabel.setFont(new Font("Arial", Font.BOLD, 20));
		tituloLabel.setForeground(new Color(60, 60, 60)); 

		cabeza.add(tituloLabel, BorderLayout.CENTER);
		cabeza.add(botonMenu, BorderLayout.WEST);

		return cabeza;
	}

	private void toggleMenu() {
		menuAbierto = !menuAbierto;
		if (menu != null) {
			menu.setVisible(menuAbierto);
			central.revalidate();
			central.repaint();
		}
	}
	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { 
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); 
            } catch (Exception e) {}
            new JFrameFarmaciaSel();
        });
    }
}