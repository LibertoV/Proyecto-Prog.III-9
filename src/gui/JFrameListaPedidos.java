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

import db.DataHistorial;
import db.DataPedidos;
import domain.Pedido;

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
	
	private ArrayList<Pedido> listaObjetosPedidos = new ArrayList<>();

	public JFrameListaPedidos() {

		// sirve para cargar solo una vez la imagen de la papelera eliminar
		ImageIcon iconoOriginal = new ImageIcon("resources/images/eliminar.png");
		Image imagen = iconoOriginal.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		this.ICONO_ELIMINAR = new ImageIcon(imagen);

		this.setTitle("Lista de Pedidos");
		this.setSize(new Dimension(1200, 850));
		this.setLocationRelativeTo(null);

		// Añadir la cabecera
		this.add(crearPanelCabecera(), BorderLayout.NORTH);

		// Añadir el panel central
		this.add(crearPanelCentral(), BorderLayout.CENTER);

		actualizarTotales();

		this.setVisible(true);
		this.setFocusable(true); // IAG
		this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
	}

	public void agregarNuevoPedido(Pedido pedido) {
	    listaObjetosPedidos.add(pedido);
	    modelo.addRow(pedido.añadirloTabla());
	    actualizarTotales();
	}
	
	private Pedido buscarPedidoPorId(String id) {
	    for (Pedido p : listaObjetosPedidos) {
	        if (p.getId().equals(id)) {
	            return p;
	        }
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
		gbc.insets = new Insets(5, 5, 5, 5); // Para la separacion entre los componentes que se añaden

		JPanel Proveedores = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField txtfiltro = new JTextField(10);
		Proveedores.add(new JLabel("Filtrado: "));
		Proveedores.add(txtfiltro);

		DocumentListener filtro = new DocumentListener() {

			@Override
			public void insertUpdate(DocumentEvent e) {
				filtroPedido(txtfiltro.getText());

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtroPedido(txtfiltro.getText());

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				// TODO Auto-generated method stub

			}

		};
		txtfiltro.getDocument().addDocumentListener(filtro);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 1;
		gbc.weightx = 0.0;
		gbc.weighty = 0.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.WEST;
		panelCentral.add(Proveedores, gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		panelCentral.add(new JLabel(""), gbc);

		Vector<Object> columnas = new Vector<>();
		columnas.add("Nº pedido");
		columnas.add("Fecha de la orden");
		columnas.add("Fecha de llegada");
		columnas.add("Valor");
		columnas.add("Proveedor");
		columnas.add("Eliminar");

		Vector<Vector<Object>> datos = DataPedidos.cargaPedidos("resources/db/pedidos.csv");

		modelo = new DefaultTableModel(datos, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tablaPedidos = new JTable(modelo);
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
					setForeground(table.getSelectionForeground());
				} else if (row == filaHover[0]) {
					setBackground(new Color(220, 240, 255));
					setForeground(table.getForeground());
				} else {
					setBackground(Color.WHITE);
					setForeground(table.getForeground());
				}
				setOpaque(true);

				String nombreColumna = table.getColumnName(column);

				if (nombreColumna.equals("Eliminar")) {

					setIcon(ICONO_ELIMINAR);
					setHorizontalAlignment(SwingConstants.CENTER);
					setToolTipText("Eliminar pedido");
					setText(null);

				} else if (nombreColumna.equals("Proveedor")) {
					setIcon(null);
					setText(null);
					setToolTipText(null);
					if (value instanceof String && !((String) value).isEmpty()) {
						ImageIcon icono = getCachedIcon((String) value);
						if (icono != null)
							setIcon(icono);
						else
							setText("IMG ERROR");
					} else {
						setText("N/A");
					}

				} else {
					setIcon(null);
					setHorizontalAlignment(SwingConstants.CENTER);
					setToolTipText(null);
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
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
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
								"¿Seguro que deseas eliminar este pedido?", "Confirmar eliminación",
								JOptionPane.YES_NO_OPTION);

						if (confirmado == JOptionPane.YES_OPTION) {
							modelo.removeRow(fila);

							actualizarTotales();

							JOptionPane.showMessageDialog(JFrameListaPedidos.this, "Pedido eliminado.");
						}
						return;
					}

					if (e.getClickCount() == 2) {
					    String idSeleccionado = modelo.getValueAt(fila, 0).toString();
					    
					    Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
					    
					    if (pedidoReal != null) {
					        JFrameSelPedido frameSel = new JFrameSelPedido(pedidoReal);
					        frameSel.setVisible(true);
					    } else {
					        JOptionPane.showMessageDialog(JFrameListaPedidos.this, "Todavia no estan cargados de la base de datos");
					    }
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

		tablaPedidos.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int fila = tablaPedidos.getSelectedRow();
				boolean ctrlPresionado = e.isControlDown();
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				    if (fila >= 0) {
				        String idSeleccionado = modelo.getValueAt(fila, 0).toString();
				        Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
				        
				        if (pedidoReal != null) {
				            JFrameSelPedido frameSel = new JFrameSelPedido(pedidoReal);
				            frameSel.setVisible(true);
				        } else {
				             JOptionPane.showMessageDialog(JFrameListaPedidos.this, "Todavia no están cargados de la base de datos.");
				        }
				    }
				    e.consume();
				}

				else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
					if (fila >= 0) {
						int confirmado = JOptionPane.showConfirmDialog(JFrameListaPedidos.this,
								"¿Seguro que deseas eliminar este pedido?", "Confirmar eliminación",
								JOptionPane.YES_NO_OPTION);

						if (confirmado == JOptionPane.YES_OPTION) {
							Object idPedido = modelo.getValueAt(fila, 0);
							modelo.removeRow(fila);
							actualizarTotales();
						}
					}
					e.consume();
				}

				else if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (fila > 0) {
						tablaPedidos.setRowSelectionInterval(fila - 1, fila - 1);
						tablaPedidos.scrollRectToVisible(tablaPedidos.getCellRect(fila - 1, 0, true));
					}
					e.consume();
				}

				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (fila < modelo.getRowCount() - 1) {
						tablaPedidos.setRowSelectionInterval(fila + 1, fila + 1);
						tablaPedidos.scrollRectToVisible(tablaPedidos.getCellRect(fila + 1, 0, true));
					}
					e.consume();

				}

				else if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
					dispose();
					SwingUtilities.invokeLater(() -> new JFrameFarmaciaSel().setVisible(true));
				}

			}

		});

		JPanel OpcionesInferior = crearOpcionesInferior();

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
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
		Vector<Vector<Object>> data = DataPedidos.cargaPedidos("resources/db/pedidos.csv");

		Vector<Vector<Object>> cargaFiltrada = new Vector<Vector<Object>>();
		String filtroLower = filtro.toLowerCase();

		if (filtroLower.isEmpty()) {
			cargaFiltrada.addAll(data);
		} else {
			for (Vector<Object> fila : data) {
				String id = fila.get(0).toString().toLowerCase();
				String proveedor = fila.get(4).toString().toLowerCase();

				if (id.contains(filtroLower) || proveedor.contains(filtroLower)) {
					cargaFiltrada.add(fila);
				}
			}
		}

		modelo.setRowCount(0);
		for (Vector<Object> vector : cargaFiltrada) {
			modelo.addRow(vector);
		}
		modelo.fireTableDataChanged();
		actualizarTotales();

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

		// Centrar botones horizontalmente
		anadir.setAlignmentX(Component.CENTER_ALIGNMENT);

		panelañadir.add(Box.createVerticalGlue());
		panelañadir.add(anadir);
		panelañadir.add(Box.createVerticalGlue());

		return panelañadir;

	}

	private JPanel crearHistorialPedido() {
		JPanel panelhistorial = new JPanel();
		panelhistorial.setLayout(new BoxLayout(panelhistorial, BoxLayout.Y_AXIS));
		panelhistorial.setBorder(BorderFactory.createTitledBorder("Historial de pedidos"));
		panelhistorial.setPreferredSize(new Dimension(400, 200));

		Vector<Object> columnas = new Vector<>();
		columnas.add("Id");
		columnas.add("Fecha llegada");
		columnas.add("Productos recibidos");

		Vector<Vector<Object>> historial = DataHistorial.cargarHistorial();

		// Añadir lo años al combobox
		Vector<String> añosVector = new Vector<>();
		añosVector.add("Todos");
		ArrayList<String> años = new ArrayList<>();

		for (Vector<Object> fila : historial) {
			Object fechaObj = fila.get(1);

			if (fechaObj instanceof String) {
				String fechaStr = (String) fechaObj;

				if (fechaStr != null && fechaStr.length() >= 4) {
					String año = fechaStr.substring(0, 4);

					if (!años.contains(año)) {
						años.add(año);
					}
				}
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

		DefaultTableModel model = new DefaultTableModel(historial, columnas) {
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
				        String idSeleccionado = modelo.getValueAt(fila, 0).toString();
				        Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
				        
				        if (pedidoReal != null) {
				            JFrameSelPedido frameSel = new JFrameSelPedido(pedidoReal);
				            frameSel.setVisible(true);
				        } else {
				             JOptionPane.showMessageDialog(JFrameListaPedidos.this, "todavia no estan cargados de la base de datos.");
				        }
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

				if (e.getClickCount() == 2) {
				    String idSeleccionado = modelo.getValueAt(fila, 0).toString();
				    Pedido pedidoReal = buscarPedidoPorId(idSeleccionado);
				    if (pedidoReal != null) {
				        JFrameSelPedido frameSel = new JFrameSelPedido(pedidoReal);
				        frameSel.setVisible(true);
				    } else {
				        JOptionPane.showMessageDialog(JFrameListaPedidos.this, "todavia no estan cargados de la base de datos.");
				    }
				}
			}
		});
		Vector<Vector<Object>> historialOriginal = new Vector<>();
		for (Vector<Object> fila : historial) {
			historialOriginal.add(new Vector<>(fila));
		}

		comboAños.addActionListener(e -> {
			String añosel = (String) comboAños.getSelectedItem();

			model.setRowCount(0);
			if (añosel.equals("Todos")) {
				for (Vector<Object> fila : historialOriginal) {
					model.addRow(fila);
				}
			} else {
				for (Vector<Object> fila : historialOriginal) {
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