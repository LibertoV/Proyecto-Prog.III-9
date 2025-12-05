package gui;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import db.Cliente;
import domain.Laboratorio;
import domain.Receta;
import jdbc.GestorBDInitializerCliente;





public class JFrameListaClientes extends JFramePrincipal{
	private JTextField txtFiltro;
	private Vector<Vector<Object>> datosOriginales; 
	private DefaultTableModel model;
	protected int filaTablaClientes= -1;;
	private static final long serialVersionUID = 1L;
	private List<Receta> recetas;
	private List<Cliente> clientes;
	
	private GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();
	private JTable tablaClientes;
	
	
	public JFrameListaClientes(){
		this.gestorBD.crearBBDD();
		List<Cliente> clientesExistentes = gestorBD.obtenerDatos();
		if(clientesExistentes.isEmpty()) {
			List<Cliente> clientes = initClientes();
			gestorBD.insertarDatos(clientes.toArray(new Cliente[clientes.size()]));
		}
		this.clientes = gestorBD.obtenerDatos();
		this.datosOriginales = convertirClientesAVector(this.clientes);
		this.setTitle("Lista de Clientes");
		this.setSize(new Dimension(1000,750));
		this.setLocationRelativeTo(null);
		
		this.add(crearPanelCabecera(), BorderLayout.NORTH);
		this.add(crearPanelCentral(), BorderLayout.CENTER);
		this.add(crearPanelInferior(),BorderLayout.SOUTH);
		this.requestFocusInWindow(); //IAG
		this.setFocusable(true); //IAG
		this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
	}
	
	public static List<Cliente> initClientes() {
		List<Cliente> clientes = new ArrayList<>();		
		
		
		try {
			// Abrir el fichero
			File fichero = new File("resources/db/clientes.csv");
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
				String fecha = campos[4];
				int recetas = Integer.parseInt(campos[5]);
				String email = campos[6];
				String direccion = campos[7];
				
				Cliente cliente = new Cliente(id,nombre,dni,tlf,fecha,recetas,email,direccion);
				clientes.add(cliente);
			}
			
			// Cerrar el fichero
			sc.close();
		} catch (Exception e) {
			System.err.println("Error al cargar datos desde clientes.csv");
		}
		return clientes;
	}
	

	private Vector<Vector<Object>> convertirClientesAVector(List<Cliente> clientes) {
	    Vector<Vector<Object>> datosTabla = new Vector<>();
	    for (Cliente cliente : clientes) {
	        Vector<Object> fila = new Vector<>();
	        fila.add(cliente.getId());
	        fila.add(cliente.getNombre());
	        fila.add(cliente.getDni());
	        fila.add(cliente.getTlf());
	        fila.add(cliente.getFechaUltimaCompra());
	        fila.add(cliente.getRecetasPendientes());
	        datosTabla.add(fila);
	    }
	    return datosTabla;
	}

		private JPanel crearPanelCabecera() {
		JPanel panelCabecera = new JPanel(new BorderLayout());
		panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		
		JComboBox<String> filtroCombo = new JComboBox<>(new String[]{"Todos", "Recetas pendientes", "Últimos 7 días"});
		panelFiltro.add(filtroCombo);
		
		JButton añadir = new JButton("+ Añadir cliente");
		añadir.addActionListener((e)->{
			nuevoCliente();
			
			
		});
		panelFiltro.add(añadir, BorderLayout.EAST);
		

		
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
	
	 private void nuevoCliente() {
			JDialog dialog = new JDialog(this, "Nuevo Cliente", true);
			dialog.setSize(300,400);
			dialog.setLayout(new BorderLayout(10,10));
			
			JPanel panelCampos = new JPanel(new GridLayout(7,2,5,10));
			panelCampos.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
			
			panelCampos.add(new JLabel("Nombre:"));
			JTextField textoNombre = new JTextField();
			panelCampos.add(textoNombre);
			
			panelCampos.add(new JLabel("DNI:"));
			JTextField textoDNI = new JTextField();
			panelCampos.add(textoDNI);
			
			panelCampos.add(new JLabel("Telefono:"));
			JTextField textoTelefono = new JTextField();
			panelCampos.add(textoTelefono);
			
			panelCampos.add(new JLabel("Email:"));
			JTextField textoEmail = new JTextField();
			panelCampos.add(textoEmail);
			
			panelCampos.add(new JLabel("Direccion:"));
			JTextField textoDireccion = new JTextField();
			panelCampos.add(textoDireccion);
			
			panelCampos.add(new JLabel("Recetas Pendientes:"));
			SpinnerNumberModel modeloSpinner = new SpinnerNumberModel(0, 0, 500, 1);
			JSpinner spinnerRecetas = new JSpinner(modeloSpinner);
			panelCampos.add(spinnerRecetas);
			JComboBox<Laboratorio> recetasCombo = new JComboBox<Laboratorio>(Laboratorio.values());
			recetasCombo.setRenderer((list, value, index, isSelected, cellHasFocus) -> {
				JLabel result = new JLabel();
				
				Laboratorio laboratorio = (Laboratorio) value;
				
				switch (laboratorio) { 
					case ABBVIE:
						ImageIcon logo1 = new ImageIcon("resources/images/AbbVieLogo_AbbVie dark blue.png");
						 ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado1);
						break;
					case BAYER:
						ImageIcon logo2 = (new ImageIcon("resources/images/Logo_Bayer.svg.png"));
						ImageIcon logoAjustado2 = new ImageIcon(logo2.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado2);
						break;
					case ASTRAZENECA:
						ImageIcon logo3 = (new ImageIcon("resources/images/original.jpg"));
						ImageIcon logoAjustado3 = new ImageIcon(logo3.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado3);
						break;
					case BRISTOL_MYERS:
						ImageIcon logo4 = (new ImageIcon("resources/images/Bristol-Myers_Squibb_logo_(2020).svg"));
						ImageIcon logoAjustado4 = new ImageIcon(logo4.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						result.setIcon(logoAjustado4);
						break;
					case JOHNSON_AND_JOHNSON:
						ImageIcon logo5 = (new ImageIcon("resources/images/JNJ_Logo_SingleLine_Red_RGB.png"));
						ImageIcon logoAjustado5 = new ImageIcon(logo5.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado5);
						break;
					case LILLY:
						ImageIcon logo6 = (new ImageIcon("resources/images/Lilly-Logo.svg.png"));
						ImageIcon logoAjustado6 = new ImageIcon(logo6.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado6);
						break;
					case MERCK:
						ImageIcon logo7 = (new ImageIcon("resources/images/Merck.png"));
						ImageIcon logoAjustado7 = new ImageIcon(logo7.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado7);
						break;
					case NOVARTISNOVO_NORDISK:
						ImageIcon logo8 = (new ImageIcon("resources/images/novo-nordisk-1-logo-svg-vector.svg"));
						ImageIcon logoAjustado8 = new ImageIcon(logo8.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						 result.setIcon(logoAjustado8);
						break;
					case ROCHE:
						ImageIcon logo9 = (new ImageIcon("resources/images/roche-logo-blue.png"));
						ImageIcon logoAjustado9 = new ImageIcon(logo9.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						result.setIcon(logoAjustado9);
						break;
					case SANOFI:
						ImageIcon logo10 = (new ImageIcon("resources/images/Logo_Sanofi_(2022).png"));
						ImageIcon logoAjustado10 = new ImageIcon(logo10.getImage().getScaledInstance(60, 55, Image.SCALE_SMOOTH));
						result.setIcon(logoAjustado10);
						break;
					default:
				}
				
				if (isSelected) {
					result.setBackground(list.getSelectionBackground());
					result.setForeground(list.getSelectionForeground());
				}
				
				return result;
			});
			panelCampos.add(new JLabel(""));
			panelCampos.add(recetasCombo);
			
			dialog.add(panelCampos);
			
			JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			
			JButton guardar = new JButton("Guardar");
			JButton cancelar = new JButton("Cancelar");
			
			guardar.addActionListener(e->{
				//IAG
				if(validarCampos(textoNombre, textoDNI, textoTelefono)) {
		            try {
		                // Obtener nuevo ID (el máximo + 1)
		                int nuevoId = clientes.stream()
		                    .mapToInt(Cliente::getId)
		                    .max()
		                    .orElse(0) + 1;
		                
		                // Crear cliente
		                Cliente nuevoCliente = new Cliente(
		                    nuevoId,
		                    textoNombre.getText().trim(),
		                    textoDNI.getText().trim(),
		                    textoTelefono.getText().trim(),
		                    java.time.LocalDate.now().toString(), // Fecha actual
		                    (Integer) spinnerRecetas.getValue(),
		                    textoEmail.getText().trim(),
		                    textoDireccion.getText().trim()
		                );
		                
		                // Insertar en BD
		                gestorBD.insertarDatos(nuevoCliente);
		                
		                // Actualizar la lista local
		                clientes.add(nuevoCliente);
		                
		                // Actualizar la tabla
		                actualizarTabla();
		                
		                JOptionPane.showMessageDialog(dialog, 
		                    "Cliente añadido correctamente", 
		                    "Éxito", 
		                    JOptionPane.INFORMATION_MESSAGE);
		                
		                dialog.dispose();
		                
		            } catch (NumberFormatException ex) {
		                JOptionPane.showMessageDialog(dialog, 
		                    "El campo 'Recetas Pendientes' debe ser un número", 
		                    "Error", 
		                    JOptionPane.ERROR_MESSAGE);
		            }
		        }
			});
			cancelar.addActionListener(e ->{
				dialog.dispose();
			});
			
			botones.add(cancelar);
			botones.add(guardar);
			dialog.add(botones,BorderLayout.SOUTH);
			dialog.setVisible(true);
		}
	 
	 //IAG
	 private boolean validarCampos(JTextField nombre, JTextField dni, JTextField telefono) {
		    if(nombre.getText().trim().isEmpty()) {
		        JOptionPane.showMessageDialog(this, 
		            "El nombre es obligatorio", 
		            "Error", 
		            JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    
		    if(dni.getText().trim().isEmpty()) {
		        JOptionPane.showMessageDialog(this, 
		            "El DNI es obligatorio", 
		            "Error", 
		            JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    
		    if(telefono.getText().trim().isEmpty()) {
		        JOptionPane.showMessageDialog(this, 
		            "El teléfono es obligatorio", 
		            "Error", 
		            JOptionPane.ERROR_MESSAGE);
		        return false;
		    }
		    
		    return true;
		}

	 private void actualizarTabla() {
		this.clientes = gestorBD.obtenerDatos();
		this.datosOriginales = convertirClientesAVector(this.clientes);
		
		model.setRowCount(0);
    	for (Vector<Object> vector : datosOriginales) {
			model.addRow(vector);
		}
    	model.fireTableDataChanged();
	}

	 private void filtroCliente(String filtro) {
		 
			        
	        
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
	        
	        tablaClientes = new JTable(model);
	        tablaClientes.getTableHeader().setReorderingAllowed(false);
	        
	        tablaClientes.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (e.getClickCount() == 2) {
						int fila = tablaClientes.rowAtPoint(e.getPoint());
						int id = (int)model.getValueAt(fila, 0); // Sirve para mas adelante tener el id del pedido
																// para porteriormente saber su información
						
						Cliente clienteSel = null;
						for(Cliente cliente : clientes) {
							if(cliente.getId()==id) {
								clienteSel = cliente;
								break;
							}
						}
						
						JFrameFichaCliente fichaCliente = new JFrameFichaCliente(clienteSel);
						fichaCliente.setVisible(true);
						dispose();
					}
				}
			});
	        
	        
	        	        
	        JScrollPane scrollPane = new JScrollPane(tablaClientes);
	        scrollPane.setBorder(BorderFactory.createTitledBorder("Listado de Clientes"));
			panelCentral.add(scrollPane);
			tablaClientes.addKeyListener(new KeyListener() {
				
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
			        }else if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_ENTER) {
                        
                        gestionarMenu("Ver ficha");
                     }
					
				}
			});
			return panelCentral;
		}
	    
		private JPanel crearPanelInferior() {
			JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT,10,15));
			
			String ficha = "Ver ficha";
			JButton verFichaCliente = new JButton(ficha);
			verFichaCliente.addActionListener(e ->{
            	gestionarMenu(ficha);
            });
			JButton exportarCSV = new JButton("Exportar CSV");
			panelInferior.add(verFichaCliente);
			panelInferior.add(exportarCSV);
			
			return panelInferior;
		}
		
		private void gestionarMenu(String ficha) {
	    	switch(ficha) {
	    	case "Ver ficha":
	    		int filaSel = tablaClientes.getSelectedRow(); 
	    		if(filaSel != -1) {
	    			int id = (int) model.getValueAt(filaSel, 0);
	    			Cliente clienteSel = null;
	    			for (Cliente cliente : clientes) {
						if(cliente.getId() == id) {
							clienteSel = cliente;
							break;
						}
					}
	    			new JFrameFichaCliente(clienteSel).setVisible(true);;
		    		dispose();
	    		}
	    		break;
	    	}
	    }
		
		//Copiado de lo que realizo la IAG
		private class CustomRowRenderer extends JLabel implements TableCellRenderer {
	        private static final long serialVersionUID = 1L;

	        public CustomRowRenderer() {
	            setOpaque(true); 
	        }

	        @Override
	        public Component getTableCellRendererComponent(JTable table, Object value,
	                                                       boolean isSelected, boolean hasFocus, // <-- Ignoraremos 'isSelected'
	                                                       int row, int column) {
	            
	            setText(value != null ? value.toString() : "");
	            
	            // 💡 Lógica para pintar SOLO si el ratón está encima
	            if (row == filaTablaClientes) {
	                // Fila actual sobre la que está el ratón (pintar de azul claro)
	                setBackground(new Color(173, 216, 230)); 
	                setForeground(table.getSelectionForeground()); // Color de texto claro/blanco
	            } else {
	                // Resto de filas (color por defecto)
	                setBackground(table.getBackground());
	                setForeground(table.getForeground());
	            }
	            
	            // Aunque la tabla no resalte la selección, el foco puede ser útil
	            if (hasFocus) {
	                 // Opcional: puedes poner un borde para indicar el foco
	                 setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); 
	            } else {
	                 // Borde estándar de celda
	                 setBorder(UIManager.getBorder("TableHeader.cellBorder")); 
	            }
	            
	            return this;
	        }
	    }	  
		public static void main(String[] args) {
			// TODO Auto-generated method stub
			
			SwingUtilities.invokeLater(() -> new JFrameListaClientes());
			
		}

		 
	
}
