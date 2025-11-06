package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import db.DataVentas;

public class JFrameVentas extends JFramePrincipal{

	private static final long serialVersionUID = 1L;
	private Vector<Vector<Object>> datosOriginales; 
	private DefaultTableModel model;
	private JTextField txtFiltro;
	
	
	public JFrameVentas() {
		this.setTitle("Lista de Clientes");
		this.setSize(new Dimension(1000,750));
		this.setLocationRelativeTo(null);
		
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		this.add(crearPanelCentral(), BorderLayout.CENTER);
	
	}


	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new BorderLayout());
		Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Fecha");
        columnNames.add("Cliente");
        columnNames.add("Farmaceutico");
        columnNames.add("Producto");
        columnNames.add("Cantidad");
        columnNames.add("Precio");
        
        
        model = new DefaultTableModel(datosOriginales, columnNames) {
            
            public boolean isCellEditable(int row, int column) {
        		return false;
        	}  
            
        };
        
        JTable tablaVentas = new JTable(model);
        
//        CustomRowRenderer rowRenderer = new CustomRowRenderer();
//        for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
//            tablaClientes.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
//        }
        
//        MouseMotionListener motionListener = new MouseMotionListener() {
//
//
//    		
//
//			@Override
//    		public void mouseDragged(MouseEvent e) {
//    			// TODO Auto-generated method stub
//    			
//    		}
//
//    		@Override
//    		public void mouseMoved(MouseEvent e) {
//    			
//    			Point puntosRaton = new Point(e.getX(),e.getY());
//    			filaTablaClientes = tablaClientes.rowAtPoint(puntosRaton);
//    			tablaClientes.repaint();
//    			
//    		}
//    		
//    	};
//        
//    	MouseListener miMouseListener = new MouseListener() {
//
//    		@Override
//    		public void mouseClicked(MouseEvent e) {
//    			// TODO Auto-generated method stub
//    			
//    		}
//
//    		@Override
//    		public void mousePressed(MouseEvent e) {
//    			// TODO Auto-generated method stub
//    			
//    		}
//
//    		@Override
//    		public void mouseReleased(MouseEvent e) {
//    			// TODO Auto-generated method stub
//    			
//    		}
//
//    		@Override
//    		public void mouseEntered(MouseEvent e) {
//    			// TODO Auto-generated method stub
//    			
//    		}
//
//    		@Override
//    		public void mouseExited(MouseEvent e) {
//    			filaTablaClientes=-1;
//    			tablaClientes.repaint();
//    			
//    		}
//    		
//    	};
    	
//    	tablaClientes.addMouseMotionListener(motionListener);
//    	tablaClientes.addMouseListener(miMouseListener);
        	        
        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Ventas"));
		panelCentral.add(scrollPane);
		
		return panelCentral;
	}


	private JPanel crearPanelCabecera() {
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(4,4,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JComboBox<String> filtroCombo = new JComboBox<>(new String[]{"Todos", "Últimos 7 días"});
		panelFiltro.add(filtroCombo);
		
		
		
		
		Vector<Vector<Object>> data = DataVentas.cargarVentas("src/db/ventas.csv");
		
		datosOriginales = new Vector<>();
        for (Vector<Object> fila : data) {
            datosOriginales.add(new Vector<>(fila));
        }
		
		txtFiltro = new JTextField(20);
		
		DocumentListener doclistener = new DocumentListener() {
			
			
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtroVenta(txtFiltro.getText());
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtroVenta(txtFiltro.getText());
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Cambiando Texto");
				
			}
			
		};
        
		txtFiltro.getDocument().addDocumentListener(doclistener);
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.add(new JLabel("Buscar Venta"));
		panelBusqueda.add(txtFiltro);
		panelCabecera.add(panelBusqueda,BorderLayout.CENTER);
		
		panelCabecera.add(panelFiltro,BorderLayout.EAST);
		
		ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JButton MenuPrincipal = new JButton(logoAjustado1);
		MenuPrincipal.setBorder(null);
		panelCabecera.add(MenuPrincipal, BorderLayout.WEST);
		
		MenuPrincipal.addActionListener(e->{
			dispose();
			new JFrameFarmaciaSel();
			
			
		});
		
		
		return panelCabecera;
		
		
		
	};
	private void filtroVenta(String filtro) {
		 
		 Vector<Vector<Object>> data = DataVentas.cargarVentas("src/db/ventas.csv");
			
			datosOriginales = new Vector<>();
	        for (Vector<Object> fila : data) {
	            datosOriginales.add(new Vector<>(fila));
	        }
	        
	        
	    	Vector<Vector<Object>> cargaFiltrada = new Vector<Vector<Object>>();
	    	String filtroLower = filtro.toLowerCase();
	    	
	    	if(filtro.isEmpty()) {
	    		cargaFiltrada.addAll(datosOriginales);
	    	}else {
	    		for(Vector<Object> fila : datosOriginales) {
	        		String fecha = fila.get(1).toString().toLowerCase();
	        		String cliente = fila.get(2).toString().toLowerCase();
	        		String farmaceutico = fila.get(3).toString().toLowerCase();
	        		if(fecha.contains(filtroLower)|| cliente.contains(filtroLower)|| farmaceutico.contains(filtroLower)) {
	        			cargaFiltrada.add(fila);
	        			
	        		}
	    		}
	    	
	    	}
	    	
	    	model.setRowCount(0);
	    	for (Vector<Object> vector : cargaFiltrada) {
				model.addRow(vector);
			}
	    	model.fireTableDataChanged();
	    	
	    	
	    	
	    };
	    
	
	
}
