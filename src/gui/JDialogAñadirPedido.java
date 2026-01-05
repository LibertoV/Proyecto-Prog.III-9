package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;

import domain.Pedido;
import domain.Producto;

public class JDialogAñadirPedido extends JDialog {

	private JTable tablaProductos;
	private DefaultTableModel modelo;
	private JDateChooser dateChooserPedido;
	private JDateChooser dateChooserEstimada;
	private JTextField txtProveedor;
	private JFrameListaPedidos padre;
	private HashMap<Producto, Integer> listaProductos;
	private ArrayList<Producto> productosEnTabla;

	public JDialogAñadirPedido(JFrameListaPedidos parent) {
		super(parent, "Añadir nuevo pedido", true);
		this.padre = parent;
		this.listaProductos = new HashMap<Producto, Integer>();
	    this.productosEnTabla = new ArrayList<Producto>();
		setLayout(new BorderLayout(10, 10));
		setSize(new Dimension(500, 600));
		setLocationRelativeTo(parent);
		setResizable(false);

		add(crearPanelDetalles(), BorderLayout.NORTH);
		add(crearPanelTabla(), BorderLayout.CENTER);
		add(crearParteAbajo(), BorderLayout.SOUTH);

		this.setFocusable(true);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				boolean ctrlPresionado = e.isControlDown();
				if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
					dispose();
				}
			}
		});
	}

	public void recibirProducto(Producto p, int cantidad) {
		listaProductos.put(p, cantidad);
		productosEnTabla.add(p);
		modelo.addRow(p.vectorPed());
	}

	private Component crearPanelDetalles() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);
		gbc.anchor = GridBagConstraints.WEST;

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(new JLabel("Proveedor:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		txtProveedor = new JTextField(30);
		panel.add(txtProveedor, gbc);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(new JLabel("Fecha Pedido:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dateChooserPedido = new JDateChooser();
		dateChooserPedido.setDateFormatString("yyyy-MM-dd");
		panel.add(dateChooserPedido, gbc);

		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.weightx = 0.0;
		gbc.fill = GridBagConstraints.NONE;
		panel.add(new JLabel("Fecha Estimada:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		dateChooserEstimada = new JDateChooser();
		dateChooserEstimada.setDateFormatString("yyyy-MM-dd");
		panel.add(dateChooserEstimada, gbc);

		return panel;
	}

	private Component crearPanelTabla() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(BorderFactory.createTitledBorder("Productos del Pedido"));

		String[] columnas = { "Producto", "Cantidad", "Precio Unitario (€)" };
		modelo = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tablaProductos = new JTable(modelo);

		JScrollPane scroll = new JScrollPane(tablaProductos);
		panel.add(scroll, BorderLayout.CENTER);

		JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnAnadirFila = new JButton("Añadir Producto");
		JButton btnEliminarFila = new JButton("Eliminar Producto");

		panelBotonesTabla.add(btnAnadirFila);
		panelBotonesTabla.add(btnEliminarFila);
		
		btnAnadirFila.addActionListener(e -> {
			JDialogAñadirProducto dialog = new JDialogAñadirProducto(this);
			dialog.setVisible(true);
		});

		btnEliminarFila.addActionListener(e -> {
			int filaSel = tablaProductos.getSelectedRow();
			if (filaSel != -1) {
				Producto pParaBorrar = productosEnTabla.get(filaSel);
				listaProductos.remove(pParaBorrar);
				productosEnTabla.remove(filaSel);
				modelo.removeRow(filaSel);
			} else {
				JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para eliminar.", "Error", JOptionPane.WARNING_MESSAGE);
			}
		});

		panel.add(panelBotonesTabla, BorderLayout.SOUTH);
		return panel;
	}

	private Component crearParteAbajo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton btnGuardar = new JButton("Guardar");
		JButton btnCancelar = new JButton("Cancelar");

		getRootPane().setDefaultButton(btnGuardar);
		panel.add(btnGuardar);
		panel.add(btnCancelar);

		btnGuardar.addActionListener(e -> {

			if (txtProveedor.getText().trim().isEmpty()) {
				JOptionPane.showMessageDialog(this, "El campo Proveedor no puede estar vacío.", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (dateChooserPedido.getDate() == null || dateChooserEstimada.getDate() == null) {
				JOptionPane.showMessageDialog(this, "Debe seleccionar ambas fechas.", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			if (dateChooserEstimada.getDate().before(dateChooserPedido.getDate())) {
				JOptionPane.showMessageDialog(this, "La fecha estimada no puede ser anterior a la fecha del pedido.", "Error de Fechas", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (listaProductos.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Debe añadir al menos un producto al pedido.", "Error", JOptionPane.WARNING_MESSAGE);
				return;
			}

			String ID = String.valueOf(new Random().nextInt(1000) + 1000);
			java.sql.Date fechaSqlPedido = new java.sql.Date(dateChooserPedido.getDate().getTime());
			java.sql.Date fechaSqlLlegada = new java.sql.Date(dateChooserEstimada.getDate().getTime());

			Pedido nuevoPedido = new Pedido(ID, txtProveedor.getText(), fechaSqlPedido, fechaSqlLlegada, JFramePrincipal.idFarActual);

			for (Producto p : listaProductos.keySet()) {
				nuevoPedido.agregarProducto(p,listaProductos.get(p));
			}

			padre.agregarNuevoPedido(nuevoPedido);

			JOptionPane.showMessageDialog(this, "Pedido guardado con exito");
			dispose();
		});

		btnCancelar.addActionListener(e -> {
			dispose();
		});
		return panel;
	}
}