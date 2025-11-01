package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import domain.DataCliente;




public class JFrameListaClientes extends JFramePrincipal{
	private JTextField txtFiltro;
	private Vector<Vector<Object>> datosOriginales; 
	private DefaultTableModel model;
	private static final long serialVersionUID = 1L;

	public JFrameListaClientes(){
		
		this.setTitle("Lista de Clientes");
		this.setSize(new Dimension(1000,750));
		this.setLocationRelativeTo(null);
		
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		this.add(crearPanelCentral(), BorderLayout.CENTER);
		this.add(crearPanelInferior(),BorderLayout.SOUTH);
		
	}
	



		private JPanel crearPanelCabecera() {
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JComboBox<String> filtroCombo = new JComboBox<>(new String[]{"Todos", "Recetas pendientes", "Últimos 7 días"});
		panelFiltro.add(filtroCombo);
		
		JButton añadir = new JButton("+ Añadir cliente");
		panelFiltro.add(añadir, BorderLayout.EAST);
		
		
		Vector<Vector<Object>> data = DataCliente.cargaCliente("src/db/clientes.csv");
		
		datosOriginales = new Vector<>();
        for (Vector<Object> fila : data) {
            datosOriginales.add(new Vector<>(fila));
        }
		
		txtFiltro = new JTextField(20);
		
		DocumentListener doclistener = new DocumentListener() {
			
			
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtroCliente(txtFiltro.getText());
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtroCliente(txtFiltro.getText());
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Cambiando Texto");
				
			}
			
		};
        
		txtFiltro.getDocument().addDocumentListener(doclistener);
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.add(new JLabel("Buscar Cliente"));
		panelBusqueda.add(txtFiltro);
		panelCabecera.add(panelBusqueda,BorderLayout.CENTER);
		
		panelCabecera.add(panelFiltro,BorderLayout.EAST);
		
		JButton MenuPrincipal = new JButton("Home");
		panelCabecera.add(MenuPrincipal, BorderLayout.WEST);
		
		MenuPrincipal.addActionListener(e->{
			dispose();
			new JFrameFarmaciaSel();
			
			
		});
		
		
		return panelCabecera;
		
	}
	
	 private void filtroCliente(String filtro) {
		 
		 Vector<Vector<Object>> data = DataCliente.cargaCliente("src/db/clientes.csv");
			
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
	        		String nombreCliente = fila.get(1).toString().toLowerCase();
	        		String dniCliente = fila.get(2).toString().toLowerCase();
	        		
	        		if(nombreCliente.contains(filtroLower)|| dniCliente.contains(filtroLower)) {
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
	    
		private JPanel crearPanelCentral() {
			
			
			JPanel panelCentral = new JPanel(new BorderLayout());
			Vector<String> columnNames = new Vector<>();
	        columnNames.add("ID");
	        columnNames.add("Nombre");
	        columnNames.add("DNI");
	        columnNames.add("Telefono");
	        columnNames.add("Última Compra");
	        columnNames.add("Recetas Pendientes");
	        
	        model = new DefaultTableModel(datosOriginales, columnNames) {
	            
	            public boolean isCellEditable(int row, int column) {
	        		return false;
	        	}  
	            
	        };
	        
	        JTable tablaClientes = new JTable(model);
	        	        
	        JScrollPane scrollPane = new JScrollPane(tablaClientes);
	        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Clientes"));
			panelCentral.add(scrollPane);
			
			return panelCentral;
		}
	    
		private JPanel crearPanelInferior() {
			JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT,10,15));
			JButton verFichaCliente = new JButton("Ver Ficha");
			JButton exportarCSV = new JButton("Exportar CSV");
			panelInferior.add(verFichaCliente);
			panelInferior.add(exportarCSV);
			
			return panelInferior;
		}
		
		 
		 
		 
		 
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JFrameListaClientes());
	}
}
