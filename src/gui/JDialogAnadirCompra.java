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
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField; // Usado para mostrar precio visualmente
import javax.swing.SpinnerNumberModel;

import db.Cliente;
import domain.Compra;
import domain.Producto;
import jdbc.GestorBDInitializerCliente;
import jdbc.GestorBDInitializerCompras;
import jdbc.GestorBDInitializerProducto;

public class JDialogAnadirCompra extends JDialog {
	private static final long serialVersionUID = 1L;
	
	// Componentes visuales
	private JBuscadorProducto txtNombre;
	private JSpinner spinnerCantidad;
	private JTextField txtPrecio; // Opcional: solo para que el usuario vea el precio al seleccionar
	
	// Datos de contexto necesarios para crear la compra
	private Cliente cliente;
	private int idFarmacia;
	private JFrameFichaCliente parentFrame;

	java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public JDialogAnadirCompra(JFrameFichaCliente parent, Cliente cliente, int idFarmacia) {
		super(parent, "Añadir Compra", true);
		this.parentFrame = parent;
		this.cliente = cliente;
		this.idFarmacia = idFarmacia;
		
		setLayout(new BorderLayout(10,10));
		setSize(new Dimension(450,200)); // Mismas dimensiones que tu ejemplo
		setResizable(false);
		setLocationRelativeTo(parent);
		
		add(crearPanel(), BorderLayout.CENTER);
		add(botonesConfirmacion(), BorderLayout.SOUTH);
		
		this.setFocusable(true);
		
		// Tu KeyListener original para cerrar con Ctrl+E
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
	
	private Component crearPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5); 
		gbc.anchor = GridBagConstraints.WEST; 

		// --- 1. Buscador de Producto ---
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel.add(new JLabel("Producto:"), gbc);

		gbc.gridx = 1;
		gbc.weightx = 1.0; 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		
		GestorBDInitializerProducto prodBD = new GestorBDInitializerProducto();
		txtNombre = new JBuscadorProducto(prodBD.obtenerDatos());
		
		// Listener para actualizar el precio visual cuando se elige producto
		txtNombre.addActionListener(e -> {
			Object seleccion = txtNombre.getSelectedItem();
			if (seleccion instanceof Producto) {
				Producto p = (Producto) seleccion;
				txtPrecio.setText(String.valueOf(p.getPrecioUnitario()) + " €"); 
			} else {
				txtPrecio.setText("");
			}
		});
		panel.add(txtNombre, gbc);

		// --- 2. Cantidad ---
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0.0; 
		panel.add(new JLabel("Cantidad:"), gbc);

		gbc.gridx = 1;
		SpinnerNumberModel modeloCantidad = new SpinnerNumberModel(1, 1, 10000, 1);
		spinnerCantidad = new JSpinner(modeloCantidad);
		panel.add(spinnerCantidad, gbc);

		// --- 3. Precio (Visual, como en tu ejemplo) ---
		gbc.gridx = 0;
		gbc.gridy = 2;
		panel.add(new JLabel("Precio Unitario:"), gbc);

		gbc.gridx = 1;
		txtPrecio = new JTextField(15);
		txtPrecio.setEditable(false);
		panel.add(txtPrecio, gbc);

		return panel;
	}
	
	private Component botonesConfirmacion() {
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton confirmar = new JButton("Añadir");
		JButton cancelar = new JButton("Cancelar");
		
		confirmar.addActionListener(e -> {
			    if (!(txtNombre.getProductoSeleccionado() instanceof Producto)) {
			         JOptionPane.showMessageDialog(this, "Debe seleccionar un producto válido", "Error", JOptionPane.WARNING_MESSAGE);
			         return;
			    } 
			    
			    try {
			        int cantidad = (Integer) spinnerCantidad.getValue();
			        Producto productoSeleccionado = txtNombre.getProductoSeleccionado();
			        
			        Compra nuevaCompra = new Compra();
			        nuevaCompra.setIdCliente(this.cliente.getId());
			        nuevaCompra.setIdFarmacia(this.idFarmacia);
			        nuevaCompra.setFecha(new Date());
			        
			        Map<Integer, Integer> mapaProductos = new HashMap<>();
			        mapaProductos.put(productoSeleccionado.getId(), cantidad);
			        nuevaCompra.setMapaProductos(mapaProductos);
			        
			        // --- 1. GUARDAR LA COMPRA ---
			        new GestorBDInitializerCompras().insertarDatos(nuevaCompra); 
			        
			        // --- 2. ACTUALIZAR TOTALES DEL CLIENTE (NUEVO) ---
			        new GestorBDInitializerCliente().actualizarResumenCompra(this.cliente.getId(), sdf.format(new java.util.Date()));
			        
			        // --- 3. REFRESCAR LA FICHA Y CERRAR ---
			        if (parentFrame != null) {
			            parentFrame.actualizarTabla();
			        }
			        dispose();
			        
			    } catch (Exception ex) {
			        ex.printStackTrace();
			        JOptionPane.showMessageDialog(this, "Error al guardar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			    }
			});
		
		cancelar.addActionListener(e -> {
			dispose();
		});
		
		botones.add(confirmar);
		botones.add(cancelar);
		return botones;
	}
}