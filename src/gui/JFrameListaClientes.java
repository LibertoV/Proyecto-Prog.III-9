package gui;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.stream.Collectors;

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
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import db.Cliente;
import domain.Compra; // ASEGÚRATE DE TENER ESTE IMPORT
import jdbc.GestorBDInitializerCliente;
import jdbc.GestorBDInitializerCompras; // ASEGÚRATE DE TENER ESTE IMPORT

public class JFrameListaClientes extends JFramePrincipal {
    
    private JTextField txtFiltro;
    private Vector<Vector<Object>> datosOriginales; 
    private DefaultTableModel model;
    protected int filaTablaClientes = -1;
    private static final long serialVersionUID = 1L;
    
    private List<Cliente> clientes; // Esta lista ahora contendrá solo los filtrados
    private List<Cliente> todosLosClientes; // Guardamos copia de todos por si acaso
    private Vector<String> columnNames = new Vector<>();
    private GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();
    private JTable tablaClientes;
    private JComboBox<String> combo;
    private final Color COLOR_CABECERA_TABLA = new Color(31, 58, 147);
    private final Color COLOR_FONDO = new Color(245, 247, 250);
    
    public JFrameListaClientes(){
    	super(); 
        this.gestorBD.crearBBDD(); // Asegura que la tabla existe
        
        // INTENTO 1: Leer de la Base de Datos (SQLite) IAG
        this.todosLosClientes = gestorBD.obtenerDatos();
        
        // INTENTO 2: Solo si la BD está vacía (0 registros), cargamos el CSV IAG
        if (this.todosLosClientes == null || this.todosLosClientes.isEmpty()) {
            System.out.println(">>> BASE DE DATOS VACÍA: Iniciando importación única desde CSV...");
            
            List<Cliente> clientesCSV = gestorBD.obtenerDatosCSV(); // El método con el Regex
            
            if (!clientesCSV.isEmpty()) {
                // Guardamos los datos del CSV en el archivo .db para siempre
                gestorBD.insertarDatos(clientesCSV.toArray(new Cliente[0]));
                
                // Ahora que están guardados, los recuperamos de la BD 
                // para que el objeto 'todosLosClientes' tenga los IDs y cálculos correctos
                this.todosLosClientes = gestorBD.obtenerDatos();
                System.out.println(">>> Importación finalizada. Ya no se usará el CSV.");
            }
        } else {
            System.out.println(">>> BASE DE DATOS OK: Saltando carga de CSV.");
        }
       
        
        // 3. Filtrar por Farmacia Actual IAG
        GestorBDInitializerCompras gestorCompras = new GestorBDInitializerCompras();
        List<Compra> listaCompras = gestorCompras.obtenerDatos();
        
        Set<Integer> idsConCompras = new HashSet<>();
        for (Compra c : listaCompras) {
            if (c.getIdFarmacia() == this.idFarActual) {
                idsConCompras.add(c.getIdCliente());
            }
        }
        
        // Aplicamos el filtro IAG
        this.clientes = this.todosLosClientes.stream()
                .filter(c -> idsConCompras.contains(c.getId()))
                .collect(Collectors.toList());

        // LOG DE SEGURIDAD: Si no ves nada, mira estos números en la consola IAG
        System.out.println("Clientes totales en sistema: " + this.todosLosClientes.size());
        System.out.println("Clientes que han comprado en farmacia " + this.idFarActual + ": " + this.clientes.size());
        
        this.datosOriginales = convertirClientesAVector(this.clientes);
        this.setTitle("Lista de Clientes - Farmacia " + this.idFarActual);
        this.setSize(new Dimension(1000,750));
        this.setLocationRelativeTo(null);
        
        this.add(crearPanelCabecera(), BorderLayout.NORTH);
        this.add(crearPanelCentral(), BorderLayout.CENTER);
        this.add(crearPanelInferior(),BorderLayout.SOUTH);
        this.requestFocusInWindow(); 
        this.setFocusable(true); 
        this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
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
            fila.add(cliente.getCompras());
            datosTabla.add(fila);
        }
        return datosTabla;
    }

    private JPanel crearPanelCabecera() {
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        panelCabecera.setBackground(COLOR_FONDO);
        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelFiltro.setBackground(COLOR_FONDO);
        
        JButton añadir = new JButton();
        añadir.setBackground(Color.white);
        añadir.setText("+ Añadir cliente");
        ImageIcon logoAñadir = new ImageIcon("resources/images/añadirCliente.png");
        ImageIcon logoAjustado2 = new ImageIcon(logoAñadir.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH));
        añadir.setIcon(logoAjustado2);
        añadir.setFont(new Font("Century Gothic", Font.BOLD, 16));
        añadir.addActionListener((e)->{
            nuevoCliente();
        });
            
        txtFiltro = new ModernTextField(20);
        
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
        combo.setBackground(Color.white);
        combo.setForeground(Color.black);
        combo.setPreferredSize(new Dimension(100, 35));
        combo.setFont(new Font("Century Gothic", Font.BOLD, 14));
        combo.addActionListener(e -> {
            txtFiltro.setText("");
            filtroCliente("");
        });
        panelFiltro.add(combo);
        panelFiltro.add(añadir, BorderLayout.EAST);
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setBackground(COLOR_FONDO);
        
        JLabel bCliente = new JLabel("Buscar Cliente");
        bCliente.setFont(new Font("Century Gothic", Font.BOLD, 16));
        bCliente.setBackground(COLOR_FONDO);
        bCliente.setForeground(Color.black);
        panelBusqueda.add(bCliente);
        
        panelBusqueda.add(txtFiltro);
        panelCabecera.add(panelBusqueda,BorderLayout.CENTER);
        
        panelCabecera.add(panelFiltro,BorderLayout.EAST);
        
        ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton MenuPrincipal = new JButton(logoAjustado1);
        MenuPrincipal.setBorder(null);
        MenuPrincipal.setBackground(new Color(0, 28, 85));
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
        panelCabecera.setBackground(new Color(237, 246, 249));
        JLabel newCl = new JLabel();
        newCl.setText("AÑADIR NUEVO CLIENTE");
        ImageIcon logoAñadir = new ImageIcon("resources/images/añadirCliente.png");
        dialog.setIconImage(logoAñadir.getImage());
    
        newCl.setFont(new Font("Century Gothic", Font.BOLD, 16));
        newCl.setForeground(new Color(10, 16, 13));
        panelCabecera.add(newCl);
        
        JPanel panelCampos = new JPanel(new GridLayout(7,2,5,12));
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
                comp.setFont(new Font("Arial", Font.PLAIN,14)); 
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
                        java.time.LocalDate.now().toString(), 
                        0,
                        textoEmail.getText().trim(),
                        textoDireccion.getText().trim()
                    );
                    
                    // Insertar en BD
                    gestorBD.insertarDatos(nuevoCliente);
                    
                    // Actualizar la lista local
                    clientes.add(nuevoCliente);
                    todosLosClientes.add(nuevoCliente); // También al backup
                    
                    // Actualizar la tabla
                    actualizarTabla();
                    
                    JOptionPane.showMessageDialog(dialog, 
                        "Cliente añadido correctamente", 
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, 
                        "Error en formato numérico", 
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
 
    private boolean validarCampos(JTextField nombre, JTextField dni, JTextField telefono) {
        if(nombre.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if(dni.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El DNI es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        if(telefono.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "El teléfono es obligatorio", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        return true;
    }

    private void actualizarTabla() {
        // Recargar datos si fuera necesario, pero mantenemos el filtro de memoria actual
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
        } else {
            for(Vector<Object> fila : convertirClientesAVector(this.clientes)) {
                String datoCelda = fila.get(columnaFiltrada).toString().toLowerCase();
                if(datoCelda.contains(filtroLower)) {
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
        columnNames.add("Compras");
        
        model = new DefaultTableModel(datosOriginales, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }  
        };
        
        tablaClientes = new JTable(model);
        tablaClientes.getTableHeader().setReorderingAllowed(false);
        tablaClientes.setFont(new Font("Century Gothic", Font.BOLD, 14));
        CustomHeaderRenderer headerRenderer = new CustomHeaderRenderer(tablaClientes);
        tablaClientes.getTableHeader().setDefaultRenderer(headerRenderer);
        
        CustomDataCellRenderer dataRenderer = new CustomDataCellRenderer();
        for (int i = 0; i < tablaClientes.getColumnCount(); i++) {
            tablaClientes.getColumnModel().getColumn(i).setCellRenderer(dataRenderer);
        }
        
        agregarMenuContextualTabla();
        
        tablaClientes.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    gestionarAperturaFicha();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaClientes);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Listado de Clientes");
        titledBorder.setTitleFont(new Font("Century Gothic",Font.BOLD,14));
        scrollPane.setForeground(Color.white);
        scrollPane.setBorder(titledBorder);
        scrollPane.setBackground(COLOR_FONDO);
        panelCentral.add(scrollPane);
        
        tablaClientes.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {}
            
            @Override
            public void keyReleased(KeyEvent e) {}
            
            @Override
            public void keyPressed(KeyEvent e) {
                boolean ctrlPresionado = e.isControlDown();
                if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
                    dispose();
                    SwingUtilities.invokeLater(() -> new JFrameFarmaciaSel().setVisible(true)); 
                }else if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_ENTER) {
                    gestionarAperturaFicha();
                }
            }
        });
        return panelCentral;
    }
    
    //IAG sin cambios con gemini
    private void gestionarAperturaFicha() {
        int fila = tablaClientes.getSelectedRow();
        if (fila != -1) {
            int idClienteSeleccionado = (int)model.getValueAt(fila, 0); 
            
            Cliente clienteSel = null;
            for(Cliente cliente : clientes) {
                if(cliente.getId() == idClienteSeleccionado) {
                    clienteSel = cliente;
                    break;
                }
            }
            
            if(clienteSel != null) {
                JFrameFichaCliente ficha = new JFrameFichaCliente(clienteSel);
                
                // --- ESTO ES LO QUE TIENES QUE AÑADIR ---
                ficha.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosed(java.awt.event.WindowEvent e) {
                        // Cuando se cierra la ficha, la lista principal se recarga sola
                        cargarDatosDesdeBD();
                    }
                });
                
                
                ficha.setVisible(true);
            }
        }
    }

    private void agregarMenuContextualTabla() {
        JPopupMenu menuContextual = new JPopupMenu();
        
        JMenuItem itemVerFicha = new JMenuItem("Ver Ficha");
        JMenuItem itemEliminar = new JMenuItem("Eliminar Cliente");
        
        // Ver ficha
        itemVerFicha.addActionListener(e -> gestionarAperturaFicha());
        
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
            public void mousePressed(MouseEvent e) { mostrarMenu(e); }
            @Override
            public void mouseReleased(MouseEvent e) { mostrarMenu(e); }
            
            private void mostrarMenu(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int fila = tablaClientes.rowAtPoint(e.getPoint());
                    if (fila >= 0 && fila < tablaClientes.getRowCount()) {
                        tablaClientes.setRowSelectionInterval(fila, fila);
                        menuContextual.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void eliminarClienteDeTabla(int id, int fila) {
        try {
            System.out.println("Intentando eliminar cliente con ID: " + id);
            gestorBD.borrarCliente(id);
            
            // Eliminar de la lista en memoria
            clientes.removeIf(c -> c.getId() == id);
            
            actualizarTabla();
            
            JOptionPane.showMessageDialog(this, "Cliente eliminado correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            System.err.println("Error completo: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar el cliente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT,10,15));
        panelInferior.setBackground(COLOR_FONDO);
        
        JButton verFichaCliente = new JButton("Ver ficha");
        verFichaCliente.setFont(new Font("Century Gothic", Font.BOLD, 14));
        verFichaCliente.setBackground(Color.white);
        verFichaCliente.setForeground(Color.black);
        verFichaCliente.addActionListener(e -> gestionarAperturaFicha());
        
        JButton eliminar = new JButton("Eliminar");
        eliminar.setFont(new Font("Century Gothic", Font.BOLD, 14));
        eliminar.setBackground(Color.white);
        eliminar.setForeground(Color.black);
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
    
    // Método que gestiona el menú (mantenido por compatibilidad con listeners antiguos)
    private void gestionarMenu(String ficha) {
        if(ficha.equals("Ver ficha")) {
            gestionarAperturaFicha();
        }
    }
    
    private class CustomRowRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public CustomRowRenderer() {
            setOpaque(true); 
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, 
                                                       int row, int column) {
            setText(value != null ? value.toString() : "");
            if (row == filaTablaClientes) {
                setBackground(new Color(173, 216, 230)); 
                setForeground(table.getSelectionForeground()); 
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            if (hasFocus) {
                 setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); 
            } else {
                 setBorder(UIManager.getBorder("TableHeader.cellBorder")); 
            }
            return this;
        }
    }       
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameListaClientes());
    }
    
    private class CustomHeaderRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private final Color HEADER_BG_COLOR = new Color(124, 122, 122);
        private final Color HEADER_FG_COLOR = Color.WHITE; 
        
        public CustomHeaderRenderer(JTable table) {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setFont(table.getTableHeader().getFont().deriveFont(Font.BOLD));
            setBorder(BorderFactory.createEtchedBorder());
            setBackground(COLOR_CABECERA_TABLA);
            setForeground(Color.white);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setText(value != null ? value.toString() : "");
            return this;
        }
    }
    
    private class CustomDataCellRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        private final Color DATA_BG_COLOR = new Color(166, 225, 250);
        private final Color ALT_DATA_BG_COLOR = Color.WHITE; 
        private final Color SELECTION_BG_COLOR = new Color(70, 130, 180); 
        private final Font letraGothic = new Font("Century Gothic", Font.PLAIN, 14);
        public CustomDataCellRenderer() {
            setOpaque(true);
            setFont(letraGothic);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            setFont(letraGothic);
            setText(value != null ? value.toString() : "");
            
            if (isSelected) {
                setBackground(SELECTION_BG_COLOR);
                setForeground(Color.WHITE);
            } else {
                setBackground(COLOR_FONDO);
                setForeground(Color.BLACK); 
            }
            setHorizontalAlignment(CENTER);
            if (hasFocus) {
                setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            } else {
                setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); 
            }
            return this;
        }
    }
    public void cargarDatosDesdeBD() {
        
        this.todosLosClientes = gestorBD.obtenerDatos();
        
        GestorBDInitializerCompras gestorCompras = new GestorBDInitializerCompras();
        List<Compra> listaCompras = gestorCompras.obtenerDatos();
        
        Set<Integer> idsConCompras = new HashSet<>();
        for (Compra c : listaCompras) {
            if (c.getIdFarmacia() == this.idFarActual) {
                idsConCompras.add(c.getIdCliente());
            }
        }
        
        this.clientes = this.todosLosClientes.stream()
                .filter(c -> idsConCompras.contains(c.getId()))
                .collect(Collectors.toList());

        
        actualizarTabla();
    }
}
