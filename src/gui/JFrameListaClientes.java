package gui;

import java.awt.BorderLayout;


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
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
	
	private List<Cliente> clientes;
	private Vector<String> columnNames = new Vector<>();
	private GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();
	private JTable tablaClientes;
	private JComboBox combo;
	
	
	public JFrameListaClientes(){
		this.gestorBD.crearBBDD();
		
		this.clientes = gestorBD.obtenerDatos();
		if(this.clientes == null || this.clientes.isEmpty()) {
	        System.out.println("Cargando desde CSV...");
	        List<Cliente> clientesCSV = initClientes();
	        System.out.println("Clientes del CSV: " + clientesCSV.size());
	        gestorBD.insertarDatos(clientesCSV.toArray(new Cliente[clientesCSV.size()]));
	        this.clientes = gestorBD.obtenerDatos();
	        System.out.println("Después de insertar: " + this.clientes.size());
	    }
		
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
		
		
		
		JButton añadir = new JButton();
		añadir.setBackground(Color.white);
		añadir.setText("+ Añadir cliente");
		ImageIcon logoAñadir = new ImageIcon("resources/images/añadirCliente.png");
		ImageIcon logoAjustado2 = new ImageIcon(logoAñadir.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
		añadir.setIcon(logoAjustado2);
		
		añadir.addActionListener((e)->{
			nuevoCliente();
			
			
		});
			
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
		String[]tipoFiltro ={"Nombre", "ID", "DNI"};
		combo = new JComboBox<>(tipoFiltro);
		combo.addActionListener(e -> {
            txtFiltro.setText("");           // Limpia el campo de texto
            filtroCliente("");
        });
		panelFiltro.add(combo);
		panelFiltro.add(añadir, BorderLayout.EAST);
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.add(new JLabel("Buscar Cliente"));
		panelBusqueda.add(txtFiltro);
		panelCabecera.add(panelBusqueda,BorderLayout.CENTER);
		
		panelCabecera.add(panelFiltro,BorderLayout.EAST);
		
		ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JButton MenuPrincipal = new JButton(logoAjustado1);
		MenuPrincipal.setBorder(null);
		MenuPrincipal.setBackground(Color.white);
		panelCabecera.add(MenuPrincipal, BorderLayout.WEST);
		
		MenuPrincipal.addActionListener(e->{
			dispose();
			new JFrameFarmaciaSel();
			
			
		});
		
		
		return panelCabecera;
		
	}
	
	 

	 private void nuevoCliente() {
			JDialog dialog = new JDialog(this, "Nuevo Cliente", true);
			dialog.setSize(380,400);
			dialog.setLayout(new BorderLayout());
			
			
			JPanel panelCabecera = new JPanel();
			panelCabecera.setBackground(new Color(194, 192, 148));
			JLabel newCl = new JLabel("AÑADIR NUEVO CLIENTE");
			newCl.setFont(new Font("Arial", Font.PLAIN,16));
			newCl.setForeground(Color.black);
			panelCabecera.add(newCl);
			
			JPanel panelCampos = new JPanel(new GridLayout(7,2,5,12));
			panelCampos.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
			panelCampos.setBackground(new Color(194, 192, 148));
			
			panelCampos.add(new JLabel("NOMBRE:"));
			JTextField textoNombre = new JTextField();
			panelCampos.add(textoNombre);
			
			panelCampos.add(new JLabel("DNI:"));
			JTextField textoDNI = new JTextField();
			panelCampos.add(textoDNI);
			
			panelCampos.add(new JLabel("TELEFONO:"));
			JTextField textoTelefono = new JTextField();
			panelCampos.add(textoTelefono);
			
			panelCampos.add(new JLabel("EMAIL:"));
			JTextField textoEmail = new JTextField();
			panelCampos.add(textoEmail);
			
			panelCampos.add(new JLabel("DIRECCIÓN:"));
			JTextField textoDireccion = new JTextField();
			panelCampos.add(textoDireccion);
			
			dialog.add(panelCabecera, BorderLayout.NORTH);
			dialog.add(panelCampos);
			
			JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
			botones.setBackground(new Color(194, 192, 148));
			JButton guardar = new JButton("GUARDAR");
			JButton cancelar = new JButton("CANCELAR");
			
			for (Component comp : panelCampos.getComponents()) {
				if(comp instanceof JLabel) {
					comp.setFont(new Font("Arial", Font.BOLD, 14));
					
				}else if(comp instanceof JTextField){
					comp.setFont(new Font("Arial", Font.PLAIN,14)); //IAG
				}
			}
			
			guardar.setBackground(new Color(42, 114, 33));
			guardar.setForeground(Color.white);
			guardar.setFont(new Font("Arial",Font.BOLD,14));
			guardar.setPreferredSize(new Dimension(120,40));
			
			cancelar.setForeground(Color.white);
			cancelar.setBackground(new Color(167, 38, 8));
			cancelar.setFont(new Font("Arial",Font.BOLD,14));
			cancelar.setPreferredSize(new Dimension(120,40));
			
			
			
			guardar.addActionListener(e->{
				//IAG
				if(validarCampos(textoNombre, textoDNI, textoTelefono)) {
		            try {
		            	int nuevoId = gestorBD.obtenerPrimerIdDisponible();
		            	
		            	System.out.println("Asignando ID: " + nuevoId);
		                // Crear cliente
		                Cliente nuevoCliente = new Cliente(
		                    nuevoId,
		                    textoNombre.getText().trim(),
		                    textoDNI.getText().trim(),
		                    textoTelefono.getText().trim(),
		                    java.time.LocalDate.now().toString(), // Fecha actual
		                    0,
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
					
			guardar.setBorderPainted(false);
			cancelar.setBorderPainted(false);
			
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
	    	int columnaFiltrada = columnNames.indexOf(combo.getSelectedItem());
	    	if (columnaFiltrada < 0) columnaFiltrada = 0;
	    	if(filtro.isEmpty()) {
	    		cargaFiltrada.addAll(convertirClientesAVector(this.clientes));
	    	}else {
	    		for(Vector<Object> fila : convertirClientesAVector(this.clientes)) {
	        		String idCliente = fila.get(columnaFiltrada).toString().toLowerCase();
	        		if(idCliente.contains(filtroLower)) {
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
	        
	        agregarMenuContextualTabla();
	        
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
	    //IAG
		private void agregarMenuContextualTabla() {
			JPopupMenu menuContextual = new JPopupMenu();
		    
		    JMenuItem itemVerFicha = new JMenuItem("Ver Ficha");
		    JMenuItem itemEliminar = new JMenuItem("Eliminar Cliente");
		    
		    // Ver ficha
		    itemVerFicha.addActionListener(e -> {
		        int fila = tablaClientes.getSelectedRow();
		        if (fila != -1) {
		            gestionarMenu("Ver ficha");
		        }
		    });
		    
		    // Eliminar cliente
		    itemEliminar.addActionListener(e -> {
		        int fila = tablaClientes.getSelectedRow();
		        if (fila != -1) {
		            int id = (int) model.getValueAt(fila, 0);
		            String nombre = (String) model.getValueAt(fila, 1);
		            String dni = (String) model.getValueAt(fila, 2);
		            
		            int confirmacion = JOptionPane.showConfirmDialog(
		                this,
		                "¿Está seguro que desea eliminar al cliente:\n" + 
		                nombre + " (" + dni + ")?",
		                "Confirmar Eliminación",
		                JOptionPane.YES_NO_OPTION,
		                JOptionPane.WARNING_MESSAGE
		            );
		            
		            if (confirmacion == JOptionPane.YES_OPTION) {
		                eliminarClienteDeTabla(id, fila);
		            }
		        }
		    });
		   
		  
		    
		    menuContextual.add(itemVerFicha);
		    menuContextual.addSeparator();
		    menuContextual.add(itemEliminar);
		    
		    // Añadir listener a la tabla
		    tablaClientes.addMouseListener(new MouseAdapter() {
		        @Override
		        public void mousePressed(MouseEvent e) {
		            mostrarMenu(e);
		        }
		        
		        @Override
		        public void mouseReleased(MouseEvent e) {
		            mostrarMenu(e);
		        }
		        
		        private void mostrarMenu(MouseEvent e) {
		            if (e.isPopupTrigger()) {
		                // Seleccionar la fila donde se hizo clic
		                int fila = tablaClientes.rowAtPoint(e.getPoint());
		                if (fila >= 0 && fila < tablaClientes.getRowCount()) {
		                    tablaClientes.setRowSelectionInterval(fila, fila);
		                    menuContextual.show(e.getComponent(), e.getX(), e.getY());
		                }
		            }
		        }
		    });
			
		}
		//IAG
		private void eliminarClienteDeTabla(int id, int fila) {
		    try {
		        System.out.println("Intentando eliminar cliente con ID: " + id);
		        
		        // PRIMERO eliminar de la base de datos
		        gestorBD.borrarCliente(id);
		        
		        // ACTUALIZAR DESDE LA BD (esto es lo importante)
		        actualizarTabla();
		        
		        System.out.println("Cliente eliminado. Total ahora: " + clientes.size());
		        
		        JOptionPane.showMessageDialog(
		            this,
		            "Cliente eliminado correctamente",
		            "Éxito",
		            JOptionPane.INFORMATION_MESSAGE
		        );
		        
		    } catch (Exception ex) {
		        System.err.println("Error completo: " + ex.getMessage());
		        ex.printStackTrace();
		        JOptionPane.showMessageDialog(
		            this,
		            "Error al eliminar el cliente: " + ex.getMessage(),
		            "Error",
		            JOptionPane.ERROR_MESSAGE
		        );
		    }
		}

		private JPanel crearPanelInferior() {
			JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT,10,15));
			
			String ficha = "Ver ficha";
			JButton verFichaCliente = new JButton(ficha);
			verFichaCliente.addActionListener(e ->{
            	gestionarMenu(ficha);
            });
			JButton eliminar = new JButton("Eliminar");
			eliminar.addActionListener(e->{
				int fila = tablaClientes.getSelectedRow();
		        if (fila != -1) {
		            int id = (int) model.getValueAt(fila, 0);
		            String nombre = (String) model.getValueAt(fila, 1);
		            String dni = (String) model.getValueAt(fila, 2);
		            
		            int confirmacion = JOptionPane.showConfirmDialog(
		                this,
		                "¿Está seguro que desea eliminar al cliente:\n" + 
		                nombre + " (" + dni + ")?",
		                "Confirmar Eliminación",
		                JOptionPane.YES_NO_OPTION,
		                JOptionPane.WARNING_MESSAGE
		            );
		            
		            if (confirmacion == JOptionPane.YES_OPTION) {
		                eliminarClienteDeTabla(id, fila);
		            }
		        }
			});
			panelInferior.add(verFichaCliente);
			panelInferior.add(eliminar);
			
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
