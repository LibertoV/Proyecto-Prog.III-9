package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import db.Cliente;
import domain.Compra;
import domain.Producto;
import domain.Farmacia; 
import jdbc.GestorBDInitializerCliente;
import jdbc.GestorBDInitializerCompras;
import jdbc.GestorBDInitializerProducto;
import jdbc.GestorBDInitializerFarmacias; 

public class JFrameFichaCliente extends JFramePrincipal {

	private Cliente cliente;
	private JTextField panelNombre, panelDni, panelTelefono, panelEmail, panelDireccion;
	private GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();
	private DefaultTableModel tableModel; // Lo hacemos propiedad de la clase para refrescarlo
	private JTable tablaPedidos;
	
	public JFrameFichaCliente(Cliente cliente){
		super(); 
		this.cliente = cliente;
		this.setTitle("Ficha del Cliente: " + cliente.getNombre());
		this.setSize(new Dimension(950, 700)); 
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		this.add(crearPanelPrincipal(), BorderLayout.CENTER);
		
		// --- PANEL INFERIOR (BOTONES) ---
		JPanel panelBotonera = new JPanel(new BorderLayout());
		panelBotonera.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// Botón Izquierda: Añadir Compra
		JButton btnAnadirCompra = new JButton("Añadir Compra");
		btnAnadirCompra.addActionListener(e -> {
			// Abrimos el diálogo pasándole 'this' (el frame actual), el cliente y la farmacia actual
			new JDialogAnadirCompra(this, this.cliente, this.idFarActual).setVisible(true);
		});
		JPanel panelIzquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		panelIzquierda.add(btnAnadirCompra);
		
		// Botón Derecha: Cerrar
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener((e)->{
			dispose();
		});
		JPanel panelDerecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
		panelDerecha.add(cerrar);

		panelBotonera.add(panelIzquierda, BorderLayout.WEST);
		panelBotonera.add(panelDerecha, BorderLayout.EAST);
		
		this.add(panelBotonera, BorderLayout.SOUTH);
		
		this.setFocusable(true);
		this.setVisible(true);
		
		// Cargamos los datos de la tabla al iniciar
		actualizarTabla();
	}

	private JPanel crearPanelPrincipal() {
		JPanel total = new JPanel(new BorderLayout(10, 10));
		total.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Parte Superior: Datos Personales
		total.add(crearPanelUsuario(), BorderLayout.NORTH);
		
		// Parte Central: Historial COMPLETO de compras
		total.add(crearPanelTabla(), BorderLayout.CENTER);
		
		return total;
	}

	private JPanel crearPanelTabla() {
		JPanel panelTabla = new JPanel(new BorderLayout());
		panelTabla.setBorder(BorderFactory.createTitledBorder("Historial Global de Compras"));
		
		// Configuramos las columnas
		Vector<String> columnas = new Vector<>();
		columnas.add("Fecha");
		columnas.add("Farmacia"); 
		columnas.add("Producto");
		columnas.add("Cant.");
		
		// Inicializamos el modelo vacío pero con columnas
		tableModel = new DefaultTableModel(null, columnas) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		
		tablaPedidos = new JTable(tableModel);
		tablaPedidos.getTableHeader().setReorderingAllowed(false);
		tablaPedidos.setRowHeight(25);
		
		// Ajuste de anchos
		tablaPedidos.getColumnModel().getColumn(0).setPreferredWidth(120); 
		tablaPedidos.getColumnModel().getColumn(1).setPreferredWidth(150); 
		tablaPedidos.getColumnModel().getColumn(2).setPreferredWidth(250); 
		tablaPedidos.getColumnModel().getColumn(3).setPreferredWidth(50); 
		
		JScrollPane scrollPane = new JScrollPane(tablaPedidos);
		scrollPane.getViewport().setBackground(Color.WHITE);
		
		panelTabla.add(scrollPane, BorderLayout.CENTER);
		
		return panelTabla;
	}
	
	/**
	 * Método público para recargar los datos de la tabla desde la BD.
	 * Se llama al iniciar y al cerrar el diálogo de Añadir Compra.
	 */
	public void actualizarTabla() {
		// Limpiamos la tabla actual
		tableModel.setRowCount(0);
		
		// 1. Obtener todas las Compras
		GestorBDInitializerCompras gestorCompras = new GestorBDInitializerCompras();
		List<Compra> todasLasCompras = gestorCompras.obtenerDatos();
		
		// 2. MAPA DE PRODUCTOS (ID -> Nombre)
		GestorBDInitializerProducto gestorProductos = new GestorBDInitializerProducto();
		List<Producto> listaProductos = gestorProductos.obtenerDatos();
		Map<Integer, String> mapaNombresProductos = new HashMap<>();
		for (Producto p : listaProductos) {
			mapaNombresProductos.put(p.getId(), p.getNombre());
		}
		
		// 3. MAPA DE FARMACIAS (ID -> Nombre)
		GestorBDInitializerFarmacias gestorFarmacia = new GestorBDInitializerFarmacias(); 
		List<Farmacia> listaFarmacias = gestorFarmacia.obtenerDatos();
		Map<Integer, String> mapaNombresFarmacias = new HashMap<>();
		for (Farmacia f : listaFarmacias) {
			mapaNombresFarmacias.put(f.getId(), f.getNombre()); 
		}
		
		// 4. Filtrar: Solo compras de ESTE cliente
		List<Compra> comprasFiltradas = todasLasCompras.stream()
				.filter(c -> c.getIdCliente() == this.cliente.getId())
				.collect(Collectors.toList());
		
		if (!comprasFiltradas.isEmpty()) {
			for (Compra c : comprasFiltradas) {
				if (c.getMapaProductos() != null) {
					for (Map.Entry<Integer, Integer> entrada : c.getMapaProductos().entrySet()) {
						int idProd = entrada.getKey();
						int cantidad = entrada.getValue();
						
						String nombreProducto = mapaNombresProductos.getOrDefault(idProd, "Producto ID: " + idProd);
						String nombreFarmacia = mapaNombresFarmacias.getOrDefault(c.getIdFarmacia(), "Farmacia ID: " + c.getIdFarmacia());
						String fechaFormateada = formatearFecha(c.getFecha());
						
						Vector<Object> fila = new Vector<>();
						fila.add(fechaFormateada);
						fila.add(nombreFarmacia);
						fila.add(nombreProducto);
						fila.add(cantidad);
						
						tableModel.addRow(fila);
					}
				}
			}
		}
	}

	private String formatearFecha(Date fechaOriginal) {
		if (fechaOriginal == null) return "";
		SimpleDateFormat salida = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return salida.format(fechaOriginal);
	}

	private JPanel crearPanelUsuario() {
		JPanel panelContenedor = new JPanel(new BorderLayout());
		panelContenedor.setBorder(BorderFactory.createTitledBorder("Datos Personales"));
		
		JPanel panelGrid = new JPanel(new GridLayout(3, 4, 10, 10)); 
		
		panelGrid.add(new JLabel("Nombre:"));
		panelNombre = new JTextField(cliente.getNombre());
		panelNombre.setEnabled(false);
		panelGrid.add(panelNombre);
		
		panelGrid.add(new JLabel("DNI:"));
		panelDni = new JTextField(cliente.getDni());
		panelDni.setEnabled(false);
		panelGrid.add(panelDni);
		
		panelGrid.add(new JLabel("Teléfono:"));
		panelTelefono = new JTextField(cliente.getTlf());
		panelTelefono.setEnabled(false);
		panelGrid.add(panelTelefono);
		
		panelGrid.add(new JLabel("Email:"));
		panelEmail = new JTextField(cliente.getEmail());
		panelEmail.setEnabled(false);
		panelGrid.add(panelEmail);
		
		panelGrid.add(new JLabel("Dirección:"));
		panelDireccion = new JTextField(cliente.getDireccion());
		panelDireccion.setEnabled(false);
		panelGrid.add(panelDireccion);
		
		panelGrid.add(new JLabel("")); 
		panelGrid.add(new JLabel(""));
		
		panelContenedor.add(panelGrid, BorderLayout.CENTER);
		
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton editar = new JButton("Editar");
		JButton guardar = new JButton("Guardar");
		guardar.setEnabled(false);
		
		editar.addActionListener((e)->{
			panelNombre.setEnabled(true);
			panelDni.setEnabled(true);
			panelTelefono.setEnabled(true);
			panelEmail.setEnabled(true);
			panelDireccion.setEnabled(true);
			guardar.setEnabled(true);
			editar.setEnabled(false);
		});
		
		guardar.addActionListener((e)->{
			panelNombre.setEnabled(false);
			panelDni.setEnabled(false);
			panelTelefono.setEnabled(false);
			panelEmail.setEnabled(false);
			panelDireccion.setEnabled(false);
			
			cliente.setNombre(panelNombre.getText());
			cliente.setDni(panelDni.getText());
			cliente.setEmail(panelEmail.getText());
			cliente.setDireccion(panelDireccion.getText());
			cliente.setTlf(panelTelefono.getText());
			
			this.gestorBD.actualizarNombre(cliente, panelNombre.getText());
			this.gestorBD.actualizarDNI(cliente, panelDni.getText());
			this.gestorBD.actualizarEmail(cliente, panelEmail.getText());
			this.gestorBD.actualizarDireccion(cliente, panelDireccion.getText());
			this.gestorBD.actualizarTelefono(cliente, panelTelefono.getText());
			
			guardar.setEnabled(false);
			editar.setEnabled(true);
			JOptionPane.showMessageDialog(this, "Cambios guardados correctamente", "Info", JOptionPane.INFORMATION_MESSAGE);
		});
		
		botones.add(editar);
		botones.add(guardar);
		panelContenedor.add(botones, BorderLayout.SOUTH);
		
		return panelContenedor;
	}
}