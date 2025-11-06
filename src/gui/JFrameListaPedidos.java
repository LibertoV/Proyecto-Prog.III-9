package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component; 
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image; 
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.ImageIcon; 
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import db.DataHistorial;
import db.DataPedidos;


//LISTADO DE PEDIDOS

public class JFrameListaPedidos extends JFramePrincipal {
	private static final long serialVersionUID = 1L;
	private DefaultTableModel modelo;
	
	
	public JFrameListaPedidos() {
		this.setTitle("Lista de Pedidos");
		this.setSize(new Dimension(1200,850));
		this.setLocationRelativeTo(null);
		
		
		//Añadir la cabecera
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		
		//Añadir el panel central
		this.add(crearPanelCentral(), BorderLayout.CENTER);
		
		this.setVisible(true);
	}
	
	private JPanel crearPanelCabecera() {
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel txtfiltro = new JLabel("10", SwingConstants.RIGHT);
		txtfiltro.setPreferredSize(new Dimension(100,20));
		txtfiltro.setBackground(Color.WHITE);
		txtfiltro.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		txtfiltro.setOpaque(true);
		panelFiltro.add(new JLabel("Numero de pedidos: "));
		panelFiltro.add(txtfiltro);
		
		
		JLabel precioTotal = new JLabel("0.00", SwingConstants.RIGHT);
		precioTotal.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		precioTotal.setOpaque(true); 
		precioTotal.setBackground(Color.WHITE);
		precioTotal.setPreferredSize(new Dimension(100, 20));
		
				
		panelFiltro.add(new JLabel("Valor total: "));
		panelFiltro.add(precioTotal);
		panelCabecera.add(panelFiltro, BorderLayout.EAST);
		
		
		ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JButton Salir = new JButton(logoAjustado1);
		Salir.setBorder(null);
		panelCabecera.add(Salir, BorderLayout.WEST);
		
		Salir.addActionListener(e->{
			dispose();
			new JFrameFarmaciaSel();
			
			
		});
		
		
		return panelCabecera;
		
	}
	
	
	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5,5,5,5); //Para la separacion entre los componentes que se añaden
		
		JPanel Proveedores = new JPanel(new FlowLayout(FlowLayout.LEFT));
		JTextField txtfiltro = new JTextField(10);
		Proveedores.add(new JLabel("Filtrado por proveedor: "));
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

        Vector<Vector<Object>> datos= DataPedidos.cargarPedidos();
        
        
        
        modelo = new DefaultTableModel(datos, columnas) {
        	@Override
        	public boolean isCellEditable(int row, int column) {
        		return false;
        	}
        };
        
        JTable tablaPedidos = new JTable(modelo);
        tablaPedidos.getTableHeader().setReorderingAllowed(false);
        
        
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
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					JFrameSelPedido frameSel = new JFrameSelPedido();
		            frameSel.setVisible(true);
					
				}
				
			}
		});
		
		
		
		JPanel OpcionesInferior = crearOpcionesInferior(); 
		
		gbc.gridx=0;
		gbc.gridy= 2;
		gbc.gridwidth = 2;
		gbc.weightx = 1.0;
		gbc.weighty = 0.0; 
		
		gbc.fill= GridBagConstraints.HORIZONTAL; 
		
		panelCentral.add(OpcionesInferior,gbc);
		
		return panelCentral;
		
	}
	
	private void filtroPedido(String filtro) {
		Vector<Vector<Object>> data = DataPedidos.cargarPedidos();
		
		Vector<Vector<Object>> cargaFiltrada = new Vector<Vector<Object>>();
    	String filtroLower = filtro.toLowerCase();
    
    	if (filtroLower.isEmpty()) {
    		cargaFiltrada.addAll(data);
    	}else {
    		for(Vector<Object> fila : data) {
    			String id = fila.get(0).toString().toLowerCase();
    			String proveedor = fila.get(4).toString().toLowerCase();
    			
    			if(id.contains(filtroLower) || proveedor.contains(filtroLower)) {
    				cargaFiltrada.add(fila);
    			}
    		}
    	}
    	
    	modelo.setRowCount(0);
    	for(Vector<Object> vector : cargaFiltrada) {
    		modelo.addRow(vector);
    	}
    	modelo.fireTableDataChanged();
		
	}
	
	private JPanel crearOpcionesInferior() {
	    JPanel panelOpciones = new JPanel(new GridBagLayout());
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5,5,5,5);
	    
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
	    JButton eliminar = new JButton("Eliminar Pedido");
	    
	    // Centrar botones horizontalmente
	    anadir.setAlignmentX(Component.CENTER_ALIGNMENT);
	    eliminar.setAlignmentX(Component.CENTER_ALIGNMENT);
	    
	    panelañadir.add(Box.createVerticalGlue()); 
	    panelañadir.add(anadir);
	    panelañadir.add(Box.createVerticalStrut(10));
	    panelañadir.add(eliminar);
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

	    
	    Vector<String> añosVector = new Vector<>();
	    añosVector.add("Todos");

	    for (Vector<Object> fila : historial) {
	        Object fechaObj = fila.get(1); 

	        if (fechaObj instanceof String) {
	            String fechaStr = (String) fechaObj;
	            
	            if (fechaStr != null && fechaStr.length() >= 4) {
	                String año = fechaStr.substring(0, 4); 
	                
	                if (!añosVector.contains(año)) {
	                    añosVector.add(año);
	                }
	            }
	        }
	    }
	    
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

	    JScrollPane scroll = new JScrollPane(tablaHistorial);

	    tablaHistorial.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            if (e.getClickCount() == 2) {
	                JFrameSelPedido frameSel = new JFrameSelPedido();
	                frameSel.setVisible(true);
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