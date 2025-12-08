package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import db.Cliente;
import db.DataCliente;
import db.DataTrabajador;
import domain.Trabajador;
import jdbc.GestorBDInitializerCliente;
import jdbc.GestorBDInitializerTrabajadores;


public class JFrameListaTrabajadores extends JFramePrincipal {

	
	private static final long serialVersionUID = 1L;
	private Vector<Vector<Object>> datosOriginales; 
	private JTextField txtFiltro;
	private DefaultTableModel model;
	private JTable tablaTrabajadores;
	private List<Trabajador> trabajadores;
	private Vector<String> columnNames = new Vector<>();
	private GestorBDInitializerTrabajadores gestorBD = new GestorBDInitializerTrabajadores();
	
	
	public JFrameListaTrabajadores() {
		this.gestorBD.crearBBDD();
		
		this.trabajadores = gestorBD.obtenerDatos();
		if(this.trabajadores == null || this.trabajadores.isEmpty()) {
	        System.out.println("Cargando desde CSV...");
	        List<Trabajador> trabajadoresCSV = initTrabajador();
	        System.out.println("Trabajadores del CSV: " + trabajadoresCSV.size());
	        gestorBD.insertarDatos(trabajadoresCSV.toArray(new Trabajador[trabajadoresCSV.size()]));
	        this.trabajadores = gestorBD.obtenerDatos();
	        System.out.println("Después de insertar: " + this.trabajadores.size());
	    }
		
		this.datosOriginales = convertirTrabajadoresAVector(this.trabajadores);
		
		this.setTitle("Lista de Trabajadores");
		this.setSize(new Dimension(1000,750));
		this.setLocationRelativeTo(null);
		
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		this.add(crearPanelCentral(), BorderLayout.CENTER);
		this.add(crearPanelInferior(),BorderLayout.SOUTH);
		this.setFocusable(true); //IAG
		this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
	}
	


	private Vector<Vector<Object>> convertirTrabajadoresAVector(List<Trabajador> trabajadores) {
		
		    Vector<Vector<Object>> datosTabla = new Vector<>();
		    for (Trabajador trabajador : trabajadores) {
		        Vector<Object> fila = new Vector<>();
		        fila.add(trabajador.getId());
		        fila.add(trabajador.getNombre());
		        fila.add(trabajador.getDni());
		        fila.add(trabajador.getTelefono());
		        fila.add(trabajador.getPuesto());
		        fila.add(trabajador.getTurno());
		        datosTabla.add(fila);
		    }
		    return datosTabla;
		
	}



	private static List<Trabajador> initTrabajador() {
		List<Trabajador> trabajadores = new ArrayList<>();		
		
		
		try {
			// Abrir el fichero
			File fichero = new File("resources/db/trabajadores.csv");
			Scanner sc = new Scanner(fichero);
			
			// Leer el fichero
			while (sc.hasNextLine()) {
			
				String linea = sc.nextLine();
				
				String[] campos = linea.split(",");
				if (campos[0].equalsIgnoreCase("id")) {
				    continue;
				}
				int id = Integer.parseInt(campos[0]);
				String nombre = campos[1];
				String dni = campos[2];
				String tlf = campos[3];
				String email = campos[4];
				String direccion = campos[5];
				String puesto = campos[6];
				String nss = campos[7];
				String turno = campos[8];
				Float salario = Float.parseFloat(campos[9]);
				
				Trabajador trabajador = new Trabajador(id,nombre,dni,tlf,email,direccion,puesto,nss,turno,salario);
				trabajadores.add(trabajador);
			}
			
			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde trabajadores.csv");
		}
		return trabajadores;
	}



	private JPanel crearPanelInferior() {
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton historial = new JButton("Historial");
		panelInferior.add(historial);
		return panelInferior;
	}

	private JPanel crearPanelCentral() {
		JPanel panelCentral = new JPanel(new BorderLayout());
		
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
        
        tablaTrabajadores = new JTable(model);
        tablaTrabajadores.getTableHeader().setReorderingAllowed(false);
        tablaTrabajadores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					int fila = tablaTrabajadores.rowAtPoint(e.getPoint());
					Object id = model.getValueAt(fila, 0); // Sirve para mas adelante tener el id del pedido
															// para porteriormente saber su información
					JFrameFichaTrabajador frameSel = new JFrameFichaTrabajador();
					frameSel.setVisible(true);
				}
			}
		});


        JScrollPane scrollPane = new JScrollPane(tablaTrabajadores);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Listado de Trabajadores");
        titledBorder.setTitleFont(new Font("Century Gothic",Font.BOLD,14));
        scrollPane.setBorder(titledBorder);
        
		panelCentral.add(scrollPane);
		
		tablaTrabajadores.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean ctrlPresionado = e.isControlDown();
				if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
		            dispose();
		            SwingUtilities.invokeLater(() -> new JFrameFarmaciaSel().setVisible(true)); 
		        }else if(ctrlPresionado && e.getKeyCode() == KeyEvent.VK_ENTER) {
					new JFrameFichaTrabajador();
		    		
                 }
				
			}
		});
		return panelCentral;
	}
	
	 private void actualizarTabla() {
			this.trabajadores = gestorBD.obtenerDatos();
			this.datosOriginales = convertirTrabajadoresAVector(this.trabajadores);
			
			model.setRowCount(0);
	    	for (Vector<Object> vector : datosOriginales) {
				model.addRow(vector);
			}
	    	model.fireTableDataChanged();
		}
	private JPanel crearPanelCabecera() {
		JComponent componentes[] = new JComponent[12];
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JButton añadir = new JButton("+ Añadir Trabajador");
		ImageIcon logoAñadir = new ImageIcon("resources/images/añadirCliente.png");
		ImageIcon logoAjustado2 = new ImageIcon(logoAñadir.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		añadir.setIcon(logoAjustado2);
		añadir.setBackground(Color.white);
		añadir.setFont(new Font("Century Gothic",Font.BOLD,14));
		panelFiltro.add(añadir, BorderLayout.EAST);
		
		añadir.addActionListener((e)->{
			componentes[0] =  new JLabel("ID ");
			componentes[1] = new ModernTextField(30);
			componentes[2] =  new JLabel("Nombre ");
			componentes[3] = new ModernTextField(30);
			componentes[4] =  new JLabel("DNI ");
			componentes[5] = new ModernTextField(30);
			componentes[6] =  new JLabel("Telefono ");
			componentes[7] = new ModernTextField(30);
			componentes[8] =  new JLabel("Puesto ");
			componentes[9] = new ModernTextField(30);
			componentes[10] =  new JLabel("Turno ");
			componentes[11] = new ModernTextField(30);
			int resultado = JOptionPane.showConfirmDialog(null,componentes, "Añadir un Trabajador",JOptionPane.OK_CANCEL_OPTION);
					if(resultado == JOptionPane.OK_OPTION) {
						System.out.println("Hemos pulsado "+ resultado);
						
					}else if (resultado == JOptionPane.ERROR_MESSAGE){
						System.out.println("Hemos pulsado "+ resultado);
					}
					
			
		});
		JButton editar = new JButton("Editar");
		editar.setFont(new Font("Century Gothic",Font.BOLD,14));
		panelFiltro.add(editar,BorderLayout.EAST);
		JButton eliminar = new JButton("Eliminar");
		eliminar.setBackground(new Color(182, 23, 75));
		eliminar.setForeground(Color.black);
		eliminar.setFont(new Font("Century Gothic",Font.BOLD,14));
		ImageIcon logoeliminar = new ImageIcon("resources/images/eliminar.png");
		ImageIcon logoAjustado = new ImageIcon(logoeliminar.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH));
		eliminar.setIcon(logoAjustado);
		
		eliminar.addActionListener(new ActionListener() {
	        @Override
	        public void actionPerformed(ActionEvent e) {
	        	int fila = tablaTrabajadores.getSelectedRow();
	        	
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
		
		
		
		Vector<Vector<Object>> data = convertirTrabajadoresAVector(gestorBD.obtenerDatos());

		
		datosOriginales = new Vector<>();
        for (Vector<Object> fila : data) {
            datosOriginales.add(new Vector<>(fila));
        }
		
		txtFiltro = new ModernTextField(20);
		
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
		JLabel buscar = new JLabel("Buscar Trabajador");
		buscar.setFont(new Font("Century Gothic",Font.BOLD,14));
		panelBusqueda.add(buscar);
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
