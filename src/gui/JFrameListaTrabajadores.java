package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
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
import javax.swing.JDialog;
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
import jdbc.GestorBDInitializerFarmacias;
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
		
		this.trabajadores = trabajadores.stream()
	            .filter(t -> t.getIdFarmacia() == JFramePrincipal.idFarActual)
	            .toList();
		
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
	    int numTotalFarmacias = 30; 
	    int contador = 0;
	    try {
	        File fichero = new File("resources/db/trabajadores.csv");
	        Scanner sc = new Scanner(fichero);
	        while (sc.hasNextLine()) {
	            String linea = sc.nextLine();
	            String[] campos = linea.split(",");
	            if (campos[0].equalsIgnoreCase("id")) continue;

	            int id = Integer.parseInt(campos[0]);
	            String nombre = campos[1];
	            String dni = campos[2];
	            String tlf = campos[3];
	            String email = campos[4];
	            String direccion = campos[5];
	            String puesto = campos[6];
	            String nss = campos[7];
	            String turno = campos[8];
	            String salario = campos[9];
	            int idFarmaciaAsignada = (contador % numTotalFarmacias) + 1;
	            
	            Trabajador trabajador = new Trabajador(id, nombre, dni, tlf, email, direccion, puesto, nss, turno, salario, idFarmaciaAsignada);
	            trabajadores.add(trabajador);
	            contador++;
	        }
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
					int id = (int)model.getValueAt(fila, 0); // Sirve para mas adelante tener el id del pedido
															// para porteriormente saber su información
					
					Trabajador trabajadorSel = null;
					for (Trabajador trabajador : trabajadores) {
						if(trabajador.getId()== id) {
							trabajadorSel = trabajador;
						}
					}
					
					JFrameFichaTrabajador frameSel = new JFrameFichaTrabajador(trabajadorSel);			
					frameSel.setVisible(true);
				}
			}
		});


        JScrollPane scrollPane = new JScrollPane(tablaTrabajadores);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Listado de Trabajadores");
        titledBorder.setTitleFont(new Font("Century Gothic",Font.BOLD,14));
        scrollPane.setBorder(titledBorder);
        
		panelCentral.add(scrollPane);
		
		
		return panelCentral;
	}
	//Modificada con IAG
	private void actualizarTabla() {
	    
	    this.trabajadores = gestorBD.obtenerDatos().stream()
	            .filter(t -> t.getIdFarmacia() == JFramePrincipal.idFarActual)
	            .toList();
	            
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
			nuevoTrabajador();
			
					
			
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
		
		eliminar.addActionListener(e-> {
			int fila = tablaTrabajadores.getSelectedRow();
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
	                eliminarTrabajadorDeTabla(id, fila);
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
	
	private void nuevoTrabajador() {
		JDialog dialog = new JDialog(this, "Nuevo Trabajador", true);
		dialog.setSize(580,600);
		dialog.setLayout(new BorderLayout());
		
		
		JPanel panelCabecera = new JPanel();
		panelCabecera.setBackground(new Color(237, 246, 249));
		JLabel newCl = new JLabel();
		newCl.setText("AÑADIR NUEVO TRABAJADOR");
		ImageIcon logoAñadir = new ImageIcon("resources/images/añadirCliente.png");
		dialog.setIconImage(logoAñadir.getImage());
	
		newCl.setFont(new Font("Century Gothic", Font.BOLD, 16));
		newCl.setForeground(new Color(10, 16, 13));
		panelCabecera.add(newCl);
		
		JPanel panelCampos = new JPanel(new GridLayout(9,2,5,12));
		panelCampos.setBorder(BorderFactory.createEmptyBorder(15,20,15,20));
		panelCampos.setBackground(new Color(237, 246, 249));
		
		panelCampos.add(new JLabel("NOMBRE:"));
		ModernTextField textoNombre = new ModernTextField(15);
		panelCampos.add(textoNombre);
		
		panelCampos.add(new JLabel("DNI:"));
		ModernTextField textoDNI = new ModernTextField(15);
		panelCampos.add(textoDNI);
		
		panelCampos.add(new JLabel("TELEFONO:"));
		ModernTextField textoTelefono = new ModernTextField(15);
		panelCampos.add(textoTelefono);
		
		panelCampos.add(new JLabel("EMAIL:"));
		ModernTextField textoEmail = new ModernTextField(15);
		panelCampos.add(textoEmail);
		
		panelCampos.add(new JLabel("DIRECCIÓN:"));
		ModernTextField textoDireccion = new ModernTextField(15);
		panelCampos.add(textoDireccion);
		
		panelCampos.add(new JLabel("PUESTO:"));
		ModernTextField textoPuesto = new ModernTextField(15);
		panelCampos.add(textoPuesto);
		
		panelCampos.add(new JLabel("NSS:"));
		ModernTextField textoNss = new ModernTextField(15);
		panelCampos.add(textoNss);
		
		panelCampos.add(new JLabel("TURNO:"));
		ModernTextField textoTurno = new ModernTextField(15);
		panelCampos.add(textoTurno);
		
		panelCampos.add(new JLabel("SALARIO:"));
		ModernTextField textoSalario = new ModernTextField(15);
		panelCampos.add(textoSalario);
		
		dialog.add(panelCabecera, BorderLayout.NORTH);
		dialog.add(panelCampos);
		
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER));
		botones.setBackground(new Color(237, 246, 249));
		JButton guardar = new JButton("GUARDAR");
		JButton cancelar = new JButton("CANCELAR");
		
		for (Component comp : panelCampos.getComponents()) {
			if(comp instanceof JLabel) {
				comp.setFont(new Font("Century Gothic", Font.BOLD, 14));
				comp.setForeground(new Color(10, 16, 13));
			}else if(comp instanceof JTextField){
				comp.setFont(new Font("Arial", Font.PLAIN,14)); //IAG
				comp.setBackground(new Color(237, 246, 249));;
			}
		}
		
		guardar.setBackground(new Color(18, 102, 79));
		guardar.setForeground(new Color(237, 246, 249));
		guardar.setFont(new Font("Century Gothic",Font.BOLD,14));
		guardar.setPreferredSize(new Dimension(120,40));
		
		cancelar.setForeground(new Color(237, 246, 249));
		cancelar.setBackground(new Color(182, 23, 75));
		cancelar.setFont(new Font("Century Gothic",Font.BOLD,14));
		cancelar.setPreferredSize(new Dimension(120,40));
		
		
		
		guardar.addActionListener(e->{
			//IAG
			if(validarCampos(textoNombre, textoDNI, textoTelefono, textoNss, textoPuesto, textoTurno, textoSalario)) {
	            try {
	            	int nuevoId = gestorBD.obtenerPrimerIdDisponible();
	            	
	            	System.out.println("Asignando ID: " + nuevoId);
	                // Crear cliente
	                Trabajador nuevoTrabajador = new Trabajador(
	                    nuevoId,
	                    textoNombre.getText().trim(),
	                    textoDNI.getText().trim(),
	                    textoTelefono.getText().trim(),
	                    textoEmail.getText().trim(),
	                    textoDireccion.getText().trim(),
	                    textoPuesto.getText().trim(),
	                    textoNss.getText().trim(),
	                    textoTurno.getText().trim(),
	                    textoSalario.getText().trim(),
	                    JFramePrincipal.idFarActual
	                );
	                
	                // Insertar en BD
	                gestorBD.insertarDatos(nuevoTrabajador);
	                
	                // Actualizar la lista local
	                trabajadores.add(nuevoTrabajador);
	                
	                // Actualizar la tabla
	                actualizarTabla();
	                
	                JOptionPane.showMessageDialog(dialog, 
	                    "Trabajador añadido correctamente", 
	                    "Éxito", 
	                    JOptionPane.INFORMATION_MESSAGE);
	                
	                dialog.dispose();
	                
	            } catch (NumberFormatException ex) {
	                JOptionPane.showMessageDialog(dialog, 
	                    "El campo 'Salario' debe ser un número", 
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
 private boolean validarCampos(JTextField nombre, JTextField dni, JTextField telefono, JTextField nss, JTextField puesto, JTextField turno, JTextField salario) {
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
	    
	    if(nss.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	            "El Nss es obligatorio", 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    if(puesto.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	            "El Puesto es obligatorio", 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    if(turno.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	            "El Turno es obligatorio", 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    if(salario.getText().trim().isEmpty()) {
	        JOptionPane.showMessageDialog(this, 
	            "El Turno es obligatorio", 
	            "Error", 
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	    
	    return true;
	}


 private void eliminarTrabajadorDeTabla(int id, int fila) {
	    try {
	        System.out.println("Intentando eliminar trabajador con ID: " + id);
	        
	        // PRIMERO eliminar de la base de datos
	        gestorBD.borrarTrabajador(id);
	        
	        // ACTUALIZAR DESDE LA BD (esto es lo importante)
	        actualizarTabla();
	        
	        System.out.println("Trabajador eliminado. Total ahora: " + trabajadores.size());
	        
	        JOptionPane.showMessageDialog(
	            this,
	            "Trabajador eliminado correctamente",
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

 
	
	private void filtroTrabajador(String filtro) {
		 
		 Vector<Vector<Object>> data = convertirTrabajadoresAVector(this.trabajadores);
			
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