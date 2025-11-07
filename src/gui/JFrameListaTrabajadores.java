package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import db.DataCliente;
import db.DataTrabajador;


public class JFrameListaTrabajadores extends JFramePrincipal {

	
	private static final long serialVersionUID = 1L;
	private Vector<Vector<Object>> datosOriginales; 
	private JTextField txtFiltro;
	private DefaultTableModel model;
	private JTable tablaClientes;
	
	
	public JFrameListaTrabajadores() {
		
		
		this.setTitle("Lista de Trabajadores");
		this.setSize(new Dimension(1000,750));
		this.setLocationRelativeTo(null);
		
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		this.add(crearPanelCentral(), BorderLayout.CENTER);
		this.add(crearPanelInferior(),BorderLayout.SOUTH);
	}

	private JPanel crearPanelInferior() {
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton historial = new JButton("Historial");
		panelInferior.add(historial);
		return panelInferior;
	}

	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new BorderLayout());
		Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Nombre");
        columnNames.add("DNI");
        columnNames.add("Telefono");
        columnNames.add("Puesto");
        columnNames.add("Turno");
        
        model = new DefaultTableModel(datosOriginales, columnNames) {
            
            public boolean isCellEditable(int row, int column) {
        		return false;
        	}  
            
        };
        
        tablaClientes = new JTable(model);
        tablaClientes.getTableHeader().setReorderingAllowed(false);


        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Trabajadores"));
		panelCentral.add(scrollPane);
		
		return panelCentral;
	}

	private JPanel crearPanelCabecera() {
		JComponent componentes[] = new JComponent[12];
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton añadir = new JButton("+ Añadir Trabajador");
		panelFiltro.add(añadir, BorderLayout.EAST);
		
		añadir.addActionListener((e)->{
			componentes[0] =  new JLabel("ID ");
			componentes[1] = new JTextField(30);
			componentes[2] =  new JLabel("Nombre ");
			componentes[3] = new JTextField(30);
			componentes[4] =  new JLabel("DNI ");
			componentes[5] = new JTextField(30);
			componentes[6] =  new JLabel("Telefono ");
			componentes[7] = new JTextField(30);
			componentes[8] =  new JLabel("Puesto ");
			componentes[9] = new JTextField(30);
			componentes[10] =  new JLabel("Turno ");
			componentes[11] = new JTextField(30);
			int resultado = JOptionPane.showConfirmDialog(null,componentes, "Añadir un Trabajador",JOptionPane.OK_CANCEL_OPTION);
					if(resultado == JOptionPane.OK_OPTION) {
						System.out.println("Hemos pulsado "+ resultado);
						
					}else if (resultado == JOptionPane.ERROR_MESSAGE){
						System.out.println("Hemos pulsado "+ resultado);
					}
					
			
		});
		JButton editar = new JButton("Editar");
		panelFiltro.add(editar,BorderLayout.EAST);
		JButton eliminar = new JButton("Eliminar");
		
		eliminar.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	int fila = tablaClientes.getSelectedRow();
	        	
	        	if (fila == -1) {
	        		JOptionPane.showMessageDialog(JFrameListaTrabajadores.this, "Por favor seleccione un pedido para eliminar", "Ningun pedido seleccionado" ,JOptionPane.YES_NO_OPTION);
	        	} else {
	        		int seleccionado = JOptionPane.showConfirmDialog(JFrameListaTrabajadores.this, "Estas seguro de que deseas eliminar el pedido seleccionado",
	        				"Confirmar eliminicacion", JOptionPane.YES_NO_OPTION); //Esto devuelve 0 si es SI, 1 si si es no y -1 si se cierra
	        		if (seleccionado == 0) {
	        			Object idPedido = model.getValueAt(fila, 0);
	        			
	        			//Aqui deberia ir el codigo necesario para eliminar el pedido de la base de datos y luego recargar la pagina
	        			
	        		}
	        	}
	        }
	    });

		
		panelFiltro.add(eliminar,BorderLayout.EAST);
		
		
		
		Vector<Vector<Object>> data = DataTrabajador.cargarTrabajadores("src/db/trabajadores.csv");
		
		datosOriginales = new Vector<>();
        for (Vector<Object> fila : data) {
            datosOriginales.add(new Vector<>(fila));
        }
		
		txtFiltro = new JTextField(20);
		
		DocumentListener doclistener = new DocumentListener() {
			
			
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtroTrabajador(txtFiltro.getText());
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtroTrabajador(txtFiltro.getText());
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Cambiando Texto");
				
			}
			
		};
        
		txtFiltro.getDocument().addDocumentListener(doclistener);
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.add(new JLabel("Buscar Trabajador"));
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
		
	}
	
	private void filtroTrabajador(String filtro) {
		 
		 Vector<Vector<Object>> data = DataTrabajador.cargarTrabajadores("src/db/trabajadores.csv");
			
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
	    
	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(() -> new JFrameListaTrabajadores());
	    }
}
