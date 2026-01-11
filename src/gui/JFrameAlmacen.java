package gui;

import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import domain.Pedido;
import domain.Producto;
import jdbc.GestorBDInitializerPedido;

public class JFrameAlmacen extends JFramePrincipal {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel modelo;
	private JTable tablaAlmacen;
	private List<Pedido> datosFiltrados;
	private JComboBox<String> combo;
	private Vector<String> columnNames;

	private final Color COLOR_FONDO = new Color(245, 247, 250);
	private final Color COLOR_TABLA_CABECERA = new Color(31, 58, 147);

	public JFrameAlmacen() {
		this.setTitle("Gestión de Almacén");
		this.setSize(new Dimension(1100, 750));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setBackground(COLOR_FONDO);
		this.setLayout(new BorderLayout());

		cargarDatos();

		this.add(crearPanelSuperior(), BorderLayout.NORTH);

		JPanel contenedorCentral = new JPanel(new BorderLayout(0, 15));
		contenedorCentral.setOpaque(false);
		contenedorCentral.setBorder(new EmptyBorder(20, 15, 25, 15));

		JPanel panelBusqueda = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
		panelBusqueda.setOpaque(false);

		JTextField txtFiltro = new JTextField(20) {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
				g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
						java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
				g2.setColor(Color.WHITE);
				g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g2.setColor(new Color(0, 100, 255));
				g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
				g2.dispose();
				super.paintComponent(g);
			}
		};

		txtFiltro.setOpaque(false);
		txtFiltro.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 10, 5, 10));
		txtFiltro.setBackground(new Color(0, 0, 0, 0));

		String[] tipoFiltro = { "Nombre", "ID", "Proveedor" };
		combo = new JComboBox<>(tipoFiltro);
		combo.setPreferredSize(new Dimension(120, 35));

		txtFiltro.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				filtrar();
			}

			public void removeUpdate(DocumentEvent e) {
				filtrar();
			}

			public void changedUpdate(DocumentEvent e) {
				filtrar();
			}

			private void filtrar() {
				filtroMedicamento(txtFiltro.getText());
			}
		});

		panelBusqueda.add(new JLabel("Buscar:"));
		panelBusqueda.add(txtFiltro);
		panelBusqueda.add(new JLabel("Criterio:"));
		panelBusqueda.add(combo);

		contenedorCentral.add(panelBusqueda, BorderLayout.NORTH);
		contenedorCentral.add(configurarTabla(), BorderLayout.CENTER);

		this.add(contenedorCentral, BorderLayout.CENTER);
		this.setVisible(true);
	}

	private void cargarDatos() {
		GestorBDInitializerPedido pedidosBD = new GestorBDInitializerPedido();
		List<Pedido> datosCargados = pedidosBD.obtenerDatos();
		datosFiltrados = new ArrayList<>();

		if (datosCargados != null) {
			for (Pedido pedido : datosCargados) {
				if (pedido.getIdFarmacia() == idFarActual) {
					datosFiltrados.add(pedido);
				}
			}
		}
	}

	private JPanel crearPanelSuperior() {
		JPanel panelHeader = new JPanel(new BorderLayout());
		panelHeader.setBackground(Color.WHITE);
		panelHeader.setPreferredSize(new Dimension(0, 60));
		panelHeader.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY));

		ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
		Image imgEscalada = logo1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		JButton btnVolver = new JButton(new ImageIcon(imgEscalada));

		btnVolver.setBorderPainted(false);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setFocusPainted(false);
		btnVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));

		btnVolver.addActionListener(e -> {
			dispose();
			new JFrameFarmaciaSel();
		});

		JLabel lblTitulo = new JLabel("STOCK DE MEDICAMENTOS");
		lblTitulo.setFont(new Font("ARIAL", Font.BOLD, 16));
		lblTitulo.setHorizontalAlignment(JLabel.CENTER);

		panelHeader.add(btnVolver, BorderLayout.WEST);
		panelHeader.add(lblTitulo, BorderLayout.CENTER);

		return panelHeader;
	}

	private JScrollPane configurarTabla() {
		columnNames = new Vector<>(Arrays.asList("ID", "Medicamento", "Cantidad", "Precio", "Proveedor"));
		Vector<Vector<Object>> data = new Vector<>();
		obtenerDatos(data, datosFiltrados);

		modelo = new DefaultTableModel(data, columnNames) {
			@Override
			public boolean isCellEditable(int r, int c) {
				return false;
			}
		};

		tablaAlmacen = new JTable(modelo);
		tablaAlmacen.setRowHeight(30);
		tablaAlmacen.setShowVerticalLines(false);

		JTableHeader header = tablaAlmacen.getTableHeader();
		header.setBackground(COLOR_TABLA_CABECERA);
		header.setForeground(Color.WHITE);
		header.setFont(new Font("ARIAL", Font.BOLD, 13));
		header.setPreferredSize(new Dimension(0, 35));

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		tablaAlmacen.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		tablaAlmacen.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);

		return new JScrollPane(tablaAlmacen);
	}

	private void filtroMedicamento(String filtro) {
		Vector<Vector<Object>> cargaFiltrada = new Vector<>();
		String filtroLower = filtro.toLowerCase().trim();
		String criterio = (String) combo.getSelectedItem();

		for (Pedido pedido : datosFiltrados) {
			for (Producto producto : pedido.getProductos().keySet()) {
				String valor = "";
				if ("ID".equals(criterio))
					valor = String.valueOf(producto.getId());
				else if ("Nombre".equals(criterio))
					valor = producto.getNombre();
				else if ("Proveedor".equals(criterio))
					valor = pedido.getProveedor();

				if (valor.toLowerCase().contains(filtroLower)) {
					cargaFiltrada.add(new Vector<>(
							Arrays.asList(producto.getId(), producto.getNombre(), pedido.getProductos().get(producto),
									producto.getPrecioUnitario() + " €", pedido.getProveedor())));
				}
			}
		}
		modelo.setDataVector(cargaFiltrada, columnNames);
	}

	private void obtenerDatos(Vector<Vector<Object>> linea, List<Pedido> datos) {
		for (Pedido pedido : datos) {
			for (Producto producto : pedido.getProductos().keySet()) {
				linea.add(new Vector<>(
						Arrays.asList(producto.getId(), producto.getNombre(), pedido.getProductos().get(producto),
								producto.getPrecioUnitario() + " €", pedido.getProveedor())));
			}
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
			idFarActual = 1;
			new JFrameAlmacen();
		});
	}
}