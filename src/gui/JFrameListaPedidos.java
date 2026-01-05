package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import db.DataPedidos;
import domain.Pedido;
import domain.Producto;
import jdbc.GestorBDInitializerPedido;

//LISTADO DE PEDIDOS

public class JFrameListaPedidos extends JFramePrincipal {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel modelo;
	private JTable tablaPedidos;

	private JLabel lblNumPedidos;
	private JLabel lblValorTotal;

	private Cursor cursorDedo = Cursor.getPredefinedCursor(Cursor.HAND_CURSOR);
	private Cursor cursorNormal = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

	private final ImageIcon ICONO_ELIMINAR;

	private ArrayList<Pedido> listaTotalPedidos = new ArrayList<>();
	private ArrayList<Pedido> listaPedidosActivos = new ArrayList<>();
	private ArrayList<Pedido> listaPedidosHistorial = new ArrayList<>();

	public JFrameListaPedidos() {

		// sirve para cargar solo una vez la imagen de la papelera eliminar
		ImageIcon iconoOriginal = new ImageIcon("resources/images/eliminar.png");
		Image imagen = iconoOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		this.ICONO_ELIMINAR = new ImageIcon(imagen);

		this.setTitle("Lista de Pedidos");
		this.setSize(new Dimension(1200, 850));
		this.setLocationRelativeTo(null);

		GestorBDInitializerPedido gestor = new GestorBDInitializerPedido();
		this.listaTotalPedidos = (ArrayList<Pedido>) gestor.obtenerDatos();


		separarPedidos();

		// Añadir la cabecera
		this.add(crearPanelCabecera(), BorderLayout.NORTH);

		// Añadir el panel central
		this.add(crearPanelCentral(), BorderLayout.CENTER);

		actualizarTotales();

		this.setVisible(true);
		this.setFocusable(true); // IAG
		this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
	}

	private void separarPedidos() {
		listaPedidosActivos.clear();
		listaPedidosHistorial.clear();

		long unDiaEnMillis = 24 * 60 * 60 * 1000L;
		Date haceUnDia = new Date(System.currentTimeMillis() - unDiaEnMillis);

		for (Pedido p : listaTotalPedidos) {
			if (p.getFechaLlegada() == null || p.getFechaLlegada().after(haceUnDia)) {
				listaPedidosActivos.add(p);
			} else {
				listaPedidosHistorial.add(p);
			}
		}
	}

	private void cargarTablaDesdeLista(ArrayList<Pedido> lista) {
		modelo.setRowCount(0);
		for (Pedido p : lista) {
			modelo.addRow(p.añadirloTabla());
		}
		actualizarTotales();
	}

	public void agregarNuevoPedido(Pedido pedido) {
		pedido.setIdFarmacia(JFramePrincipal.idFarActual);
		GestorBDInitializerPedido gestor = new GestorBDInitializerPedido();
		gestor.insertarDatos(pedido);

		listaTotalPedidos.add(pedido);
		listaPedidosActivos.add(pedido);
		modelo.addRow(pedido.añadirloTabla());
		actualizarTotales();
	}

	private Pedido buscarPedidoPorId(String id) {
		for (Pedido p : listaTotalPedidos) {
			if (p.getId().equals(id))
				return p;
		}
		return null;
	}

	private JPanel crearPanelCabecera() {
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		lblNumPedidos = new JLabel("0", SwingConstants.RIGHT);
		lblNumPedidos.setPreferredSize(new Dimension(100, 20));
		lblNumPedidos.setBackground(Color.WHITE);
		lblNumPedidos.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblNumPedidos.setOpaque(true);
		panelFiltro.add(new JLabel("Numero de pedidos: "));
		panelFiltro.add(lblNumPedidos);

		lblValorTotal = new JLabel("0.00", SwingConstants.RIGHT);
		lblValorTotal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lblValorTotal.setOpaque(true);
		lblValorTotal.setBackground(Color.WHITE);
		lblValorTotal.setPreferredSize(new Dimension(100, 20));

		panelFiltro.add(new JLabel("Valor total: "));
		panelFiltro.add(lblValorTotal);
		panelCabecera.add(panelFiltro, BorderLayout.EAST);

		ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
		ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JButton Salir = new JButton(logoAjustado1);
		Salir.setBorder(null);
		panelCabecera.add(Salir, BorderLayout.WEST);

		Salir.addActionListener(e -> {
			dispose();
			new JFrameFarmaciaSel();

		});

		JLabel lblReloj = new JLabel("Hora...");
		lblReloj.setFont(new Font("Arial", Font.BOLD, 14));
		lblReloj.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

		panelCabecera.add(lblReloj, BorderLayout.CENTER);
		Thread hiloReloj = new Thread(() -> {
			java.text.SimpleDateFormat formato = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			while (true) {
				try {
					String horaActual = formato.format(new Date());

					SwingUtilities.invokeLater(() -> lblReloj.setText(horaActual));

					Thread.sleep(1000);
				} catch (InterruptedException e) {
					break;
				}
			}
		});

		hiloReloj.setDaemon(true);
		hiloReloj.start();

		return panelCabecera;

	}

	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
		panelBusqueda.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 150, 200))); 
																										

		JLabel lblBuscar = new JLabel("Buscar Pedido:");
		lblBuscar.setForeground(Color.WHITE);
		lblBuscar.setForeground(Color.BLACK);
		lblBuscar.setFont(new Font("Arial", Font.BOLD, 13));

		JTextField txtfiltro = new JTextField(20) {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
				g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
						java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
				g2.setColor(new Color(0, 100, 255));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g2.dispose();
				super.paintComponent(g);
			}
		};
		txtfiltro.setOpaque(false);
		txtfiltro.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));

		String[] criterios = { "Nº Pedido", "Proveedor" };
		JComboBox<String> comboCriterio = new JComboBox<>(criterios);
		comboCriterio.setPreferredSize(new Dimension(120, 30));

		panelBusqueda.add(lblBuscar);
		panelBusqueda.add(txtfiltro);
		panelBusqueda.add(comboCriterio);

		txtfiltro.getDocument().addDocumentListener(new DocumentListener() {
			private void ejecutarFiltro() {
				String texto = txtfiltro.getText().toLowerCase();
				String criterio = (String) comboCriterio.getSelectedItem();
				ArrayList<Pedido> filtrados = new ArrayList<>();

				for (Pedido p : listaPedidosActivos) {
					boolean coincide = false;
					if ("Nº Pedido".equals(criterio)) {
						coincide = p.getId().toLowerCase().contains(texto);
					} else {
						coincide = p.getProveedor().toLowerCase().contains(texto);
					}
					if (coincide)
						filtrados.add(p);
				}
				cargarTablaDesdeLista(filtrados);
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				ejecutarFiltro();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				ejecutarFiltro();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				ejecutarFiltro();
			}
		});

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelCentral.add(panelBusqueda, gbc);

		Vector<Object> columnas = new Vector<>();
		columnas.add("Nº pedido");
		columnas.add("Fecha de la orden");
		columnas.add("Fecha de llegada");
		columnas.add("Valor");
		columnas.add("Proveedor");
		columnas.add("Eliminar");

		modelo = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaPedidos = new JTable(modelo);
		cargarTablaDesdeLista(this.listaPedidosActivos);
		tablaPedidos.getTableHeader().setReorderingAllowed(false);
		tablaPedidos.setRowHeight(20); 

		final int[] filaHover = { -1 };

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (isSelected) {
					setBackground(new Color(173, 216, 230));
				} else if (row == filaHover[0]) {
					setBackground(new Color(220, 240, 255));
				} else {
					setBackground(Color.WHITE);
				}

				setOpaque(true);
				String nombreColumna = table.getColumnName(column);

				if (nombreColumna.equals("Eliminar")) {
					setIcon(ICONO_ELIMINAR);
					setHorizontalAlignment(SwingConstants.CENTER);
					setText(null);
				} else if (nombreColumna.equals("Proveedor")) {
					setHorizontalAlignment(SwingConstants.CENTER);
					if (value instanceof String && !((String) value).isEmpty()) {
						setIcon(getCachedIcon((String) value));
						setText(null);
					}
				} else {
					setIcon(null);
					setHorizontalAlignment(SwingConstants.CENTER);
				}
				return this;
			}
		};

		for (int i = 0; i < tablaPedidos.getColumnCount(); i++) {
			tablaPedidos.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		tablaPedidos.getColumn("Eliminar").setMaxWidth(60);
		tablaPedidos.getColumn("Eliminar").setMinWidth(60);

		JScrollPane scroll = new JScrollPane(tablaPedidos);
		gbc.gridy = 1;
		gbc.weighty = 1.0;
		gbc.fill = GridBagConstraints.BOTH;
		panelCentral.add(scroll, gbc);

		tablaPedidos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				filaHover[0] = -1;
				setCursor(cursorNormal);
				tablaPedidos.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaPedidos.rowAtPoint(e.getPoint());
				int columna = tablaPedidos.columnAtPoint(e.getPoint());
				if (fila >= 0 && columna >= 0) {
					if (tablaPedidos.getColumnName(columna).equals("Eliminar")) {
						int confirmado = JOptionPane.showConfirmDialog(JFrameListaPedidos.this,
								"¿Seguro que deseas eliminar?", "Confirmar", JOptionPane.YES_NO_OPTION);
						if (confirmado == JOptionPane.YES_OPTION) {
							String idParaBorrar = modelo.getValueAt(fila, 0).toString();
							new GestorBDInitializerPedido().borrarPedido(idParaBorrar);
							listaPedidosActivos.removeIf(p -> p.getId().equals(idParaBorrar));
							listaTotalPedidos.removeIf(p -> p.getId().equals(idParaBorrar));
							modelo.removeRow(fila);
							actualizarTotales();
						}
					} else if (e.getClickCount() == 2) {
						String id = modelo.getValueAt(fila, 0).toString();
						Pedido p = buscarPedidoPorId(id);
						if (p != null)
							new JFrameSelPedido(p).setVisible(true);
					}
				}
			}
		});

		tablaPedidos.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int fila = tablaPedidos.rowAtPoint(e.getPoint());
				setCursor(cursorDedo);
				if (fila != filaHover[0]) {
					filaHover[0] = fila;
					tablaPedidos.repaint();
				}
			}
		});

		JPanel OpcionesInferior = crearOpcionesInferior();
		gbc.gridy = 2;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		panelCentral.add(OpcionesInferior, gbc);

		return panelCentral;
	}

	private void actualizarTotales() {
		if (modelo == null || lblNumPedidos == null || lblValorTotal == null)
			return;

		double total = calcularRecursivo(0); // aqui va el metodo
		int filas = modelo.getRowCount();

		lblNumPedidos.setText(String.valueOf(filas));
		lblValorTotal.setText(String.format("%.2f €", total));

	}

	public double calcularRecursivo(int fila) {
		if (fila == modelo.getRowCount()) {
			return 0.0;
		}

		double valorPedido = 0.0;
		Object val = modelo.getValueAt(fila, 3);

		try {
			String valStr = val.toString().replace("€", "").replace(",", ".").trim();
			valorPedido = Double.parseDouble(valStr);
		} catch (NumberFormatException e) {
		}

		return valorPedido + calcularRecursivo(fila + 1);
	}

	private void filtroPedido(String filtro) {
		String filtroLower = filtro.toLowerCase();
		ArrayList<Pedido> filtrados = new ArrayList<>();

		for (Pedido p : listaPedidosActivos) {
			if (p.getId().toLowerCase().contains(filtroLower) || p.getProveedor().toLowerCase().contains(filtroLower)) {
				filtrados.add(p);
			}
		}
		cargarTablaDesdeLista(filtrados);
	}

	private JPanel crearOpcionesInferior() {
		JPanel panelOpciones = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.weighty = 1.0;

		JPanel panelAnadir = crearAñadir();
		gbc.gridx = 0;
		gbc.weightx = 0.3;
		gbc.fill = GridBagConstraints.BOTH;
		panelOpciones.add(panelAnadir, gbc);

		JPanel panelHistorial = crearHistorialPedido();
		gbc.gridx = 1;
		gbc.weightx = 0.7;

		gbc.fill = GridBagConstraints.BOTH;
		panelOpciones.add(panelHistorial, gbc);

		return panelOpciones;
	}

	private JPanel crearAñadir() {
		JPanel panelañadir = new JPanel();
		panelañadir.setLayout(new BoxLayout(panelañadir, BoxLayout.Y_AXIS));
		panelañadir.setBorder(BorderFactory.createTitledBorder("Añadir y elimina pedidos"));

		panelañadir.setPreferredSize(new Dimension(200, 150));

		JButton anadir = new JButton("Añadir Pedido");
		anadir.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialogAñadirPedido dialog = new JDialogAñadirPedido(JFrameListaPedidos.this);
				dialog.setVisible(true);
			}
		});

		anadir.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelañadir.add(Box.createVerticalGlue());
		panelañadir.add(anadir);
		panelañadir.add(Box.createVerticalGlue());

		return panelañadir;

	}

	private JPanel crearHistorialPedido() {
		JPanel panelhistorial = new JPanel();
		panelhistorial.setLayout(new BoxLayout(panelhistorial, BoxLayout.Y_AXIS));
		panelhistorial.setBorder(BorderFactory.createTitledBorder("Historial (+24h antigüedad)"));
		panelhistorial.setPreferredSize(new Dimension(400, 200));

		Vector<Object> columnas = new Vector<>();
		columnas.add("Id");
		columnas.add("Fecha llegada");
		columnas.add("Productos recibidos");

		Vector<Vector<Object>> datosHistorial = new Vector<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		for (Pedido p : listaPedidosHistorial) {
			Vector<Object> fila = new Vector<>();
			fila.add(p.getId());

			if (p.getFechaLlegada() != null) {
				fila.add(sdf.format(p.getFechaLlegada()));
			} else {
				fila.add("---");
			}

			int cantidadTotal = 0;
			for (Producto prod : p.getProductos().keySet()) {
				cantidadTotal += p.getProductos().get(prod);
			}

			fila.add(cantidadTotal);

			datosHistorial.add(fila);
		}
		Vector<String> añosVector = new Vector<>();
		añosVector.add("Todos");
		ArrayList<String> años = new ArrayList<>();

		for (Pedido p : listaPedidosHistorial) {
			if (p.getFechaLlegada() != null) {
				String año = new SimpleDateFormat("yyyy").format(p.getFechaLlegada());
				if (!años.contains(año))
					años.add(año);
			}
		}
		Collections.sort(años, Collections.reverseOrder());
		añosVector.addAll(años);

		JComboBox<String> comboAños = new JComboBox<>(añosVector);

		JPanel panelFiltro = new JPanel();
		panelFiltro.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelFiltro.add(new JLabel("Filtrar por año:"));
		panelFiltro.add(comboAños);
		panelFiltro.setMaximumSize(new Dimension(Integer.MAX_VALUE, panelFiltro.getPreferredSize().height));

		DefaultTableModel model = new DefaultTableModel(datosHistorial, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		JTable tablaHistorial = new JTable(model);
		tablaHistorial.getTableHeader().setReorderingAllowed(false);

		final int[] filaHover = { -1 };

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (isSelected) {
					c.setBackground(new Color(173, 216, 230));
				} else if (row == filaHover[0]) {
					c.setBackground(new Color(220, 240, 255));
				} else {
					c.setBackground(Color.WHITE);
				}

				return c;
			}
		};

		for (int i = 0; i < tablaHistorial.getColumnCount(); i++) {
			tablaHistorial.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		JScrollPane scroll = new JScrollPane(tablaHistorial);

		tablaHistorial.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int fila = tablaHistorial.getSelectedRow();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (fila >= 0) {
						String idSeleccionado = model.getValueAt(fila, 0).toString();
						Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
						if (pedidoReal != null)
							new JFrameSelPedido(pedidoReal);
					}
					e.consume();
				}

				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (fila > 0) {
						tablaHistorial.setRowSelectionInterval(fila - 1, fila - 1);
						tablaHistorial.scrollRectToVisible(tablaHistorial.getCellRect(fila - 1, 0, true));
					}
					e.consume();
				}

				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (fila < modelo.getRowCount() - 1) {
						tablaHistorial.setRowSelectionInterval(fila + 1, fila + 1);
						tablaHistorial.scrollRectToVisible(tablaHistorial.getCellRect(fila + 1, 0, true));
					}
					e.consume();

				}

			}
		});

		tablaHistorial.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int fila = tablaHistorial.rowAtPoint(e.getPoint());
				setCursor(cursorDedo);
				if (fila != filaHover[0]) {
					filaHover[0] = fila;
					tablaHistorial.repaint();
				}
			}
		});

		tablaHistorial.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				setCursor(cursorNormal);
				filaHover[0] = -1;
				tablaHistorial.repaint();
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaHistorial.rowAtPoint(e.getPoint());
				if (e.getClickCount() == 2 && fila >= 0) {
					String idSeleccionado = model.getValueAt(fila, 0).toString();
					Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
					if (pedidoReal != null)
						new JFrameSelPedido(pedidoReal);
				}
			}
		});

		Vector<Vector<Object>> datosHistorialOriginal = new Vector<>(datosHistorial);

		comboAños.addActionListener(e -> {
			String añosel = (String) comboAños.getSelectedItem();

			model.setRowCount(0);
			if (añosel.equals("Todos")) {
				for (Vector<Object> fila : datosHistorialOriginal) {
					model.addRow(fila);
				}
			} else {
				for (Vector<Object> fila : datosHistorialOriginal) {
					Object fechaObj = fila.get(1);
					if (fechaObj instanceof String) {
						String fechaStr = (String) fechaObj;

						if (fechaStr != null && fechaStr.length() >= 4) {
							String año = fechaStr.substring(0, 4);

							if (año.equals(añosel)) {
								model.addRow(fila);
							}
						}
					}
				}

			}

		});
		panelhistorial.add(Box.createVerticalGlue());
		panelhistorial.add(panelFiltro);
		panelhistorial.add(Box.createRigidArea(new Dimension(0, 5)));
		panelhistorial.add(scroll);
		panelhistorial.add(Box.createVerticalGlue());

		return panelhistorial;
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JFrameListaPedidos());
	}
}