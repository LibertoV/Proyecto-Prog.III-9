package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import domain.Venta;
import jdbc.GestorBDInitializerVentas;

public class JFrameVentas extends JFramePrincipal {

    private static final long serialVersionUID = 1L;
    private Vector<Vector<Object>> datosOriginales;
    private DefaultTableModel model;
    private JTextField txtFiltro;
    private JComboBox<String> filtroCombo;
    private List<Venta> ventas;
    private GestorBDInitializerVentas gestorVentas = new GestorBDInitializerVentas();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public JFrameVentas() {
        this.setTitle("Lista de Ventas - Farmacia " + JFramePrincipal.idFarActual);
        this.setSize(new Dimension(1000, 750));
        this.setLocationRelativeTo(null);

        cargarVentas();

        this.add(crearPanelCabecera(), BorderLayout.NORTH);
        this.add(crearPanelCentral(), BorderLayout.CENTER);
        this.add(crearPanelInferior(), BorderLayout.SOUTH);
        this.setFocusable(true);
        this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));
    }

    private void cargarVentas() {
        this.ventas = gestorVentas.obtenerVentasPorFarmacia(JFramePrincipal.idFarActual);
        this.datosOriginales = convertirVentasAVector(this.ventas);
    }

    private Vector<Vector<Object>> convertirVentasAVector(List<Venta> ventas) {
        Vector<Vector<Object>> datosTabla = new Vector<>();
        for (Venta venta : ventas) {
            Vector<Object> fila = new Vector<>();
            fila.add(venta.getId());
            fila.add(sdf.format(venta.getFecha()));
            fila.add(venta.getNombreCliente());
            fila.add(venta.getNombreProducto());
            fila.add(venta.getCantidad());
            fila.add(String.format("%.2f €", venta.getPrecioUnitario()));
            fila.add(String.format("%.2f €", venta.calcularTotal()));
            datosTabla.add(fila);
        }
        return datosTabla;
    }

    private JPanel crearPanelCentral() {
        JPanel panelCentral = new JPanel(new BorderLayout());

        Vector<String> columnNames = new Vector<>();
        columnNames.add("ID");
        columnNames.add("Fecha");
        columnNames.add("Cliente");
        columnNames.add("Producto");
        columnNames.add("Cantidad");
        columnNames.add("Precio Unit.");
        columnNames.add("Total");

        model = new DefaultTableModel(datosOriginales, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaVentas = new JTable(model);
        tablaVentas.getTableHeader().setReorderingAllowed(false);
        tablaVentas.setRowHeight(25);

        JScrollPane scrollPane = new JScrollPane(tablaVentas);
        TitledBorder titledBorder = BorderFactory.createTitledBorder("Listado de Ventas");
        titledBorder.setTitleFont(new Font("Century Gothic", Font.BOLD, 14));
        scrollPane.setBorder(titledBorder);

        panelCentral.add(scrollPane);

        return panelCentral;
    }

    private JPanel crearPanelCabecera() {
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel panelFiltro = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        String[] opciones = {"Todas", "Últimos 7 días", "Último mes", "Último año"};
        filtroCombo = new JComboBox<>(opciones);
        filtroCombo.setFont(new Font("Century Gothic", Font.BOLD, 14));
        filtroCombo.addActionListener(e -> aplicarFiltroPorFecha());
        panelFiltro.add(filtroCombo);

        txtFiltro = new ModernTextField(20);

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
        JLabel buscar = new JLabel("Buscar Venta");
        buscar.setFont(new Font("Century Gothic", Font.BOLD, 14));
        panelBusqueda.add(buscar);
        panelBusqueda.add(txtFiltro);
        panelCabecera.add(panelBusqueda, BorderLayout.CENTER);

        panelCabecera.add(panelFiltro, BorderLayout.EAST);

        ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton MenuPrincipal = new JButton(logoAjustado1);
        MenuPrincipal.setBorder(null);
        panelCabecera.add(MenuPrincipal, BorderLayout.WEST);

        MenuPrincipal.addActionListener(e -> {
            dispose();
            new JFrameFarmaciaSel();
        });

        return panelCabecera;
    }
    //Realizado con ayuda de IAG
    private JPanel crearPanelInferior() {
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JLabel lblTotal = new JLabel("Total ventas mostradas: " + ventas.size());
        lblTotal.setFont(new Font("Century Gothic", Font.BOLD, 14));
        
        double totalIngresos = ventas.stream()
                .mapToDouble(Venta::calcularTotal)
                .sum();
        JLabel lblIngresos = new JLabel(String.format("Ingresos totales: %.2f €", totalIngresos));
        lblIngresos.setFont(new Font("Century Gothic", Font.BOLD, 14));
        
        panelInferior.add(lblTotal);
        panelInferior.add(new JLabel("  |  "));
        panelInferior.add(lblIngresos);
        
        return panelInferior;
    }

    private void aplicarFiltroPorFecha() {
        String seleccion = (String) filtroCombo.getSelectedItem();
        Calendar cal = Calendar.getInstance();
        Date fechaFin = cal.getTime();
        Date fechaInicio;

        switch (seleccion) {
            case "Últimos 7 días":
                cal.add(Calendar.DAY_OF_YEAR, -7);
                fechaInicio = cal.getTime();
                this.ventas = gestorVentas.obtenerVentasPorFechas(JFramePrincipal.idFarActual, fechaInicio, fechaFin);
                break;
            case "Último mes":
                cal.add(Calendar.MONTH, -1);
                fechaInicio = cal.getTime();
                this.ventas = gestorVentas.obtenerVentasPorFechas(JFramePrincipal.idFarActual, fechaInicio, fechaFin);
                break;
            case "Último año":
                cal.add(Calendar.YEAR, -1);
                fechaInicio = cal.getTime();
                this.ventas = gestorVentas.obtenerVentasPorFechas(JFramePrincipal.idFarActual, fechaInicio, fechaFin);
                break;
            default: 
                this.ventas = gestorVentas.obtenerVentasPorFarmacia(JFramePrincipal.idFarActual);
                break;
        }

        actualizarTabla();
    }

    private void actualizarTabla() {
        this.datosOriginales = convertirVentasAVector(this.ventas);

        model.setRowCount(0);
        for (Vector<Object> vector : datosOriginales) {
            model.addRow(vector);
        }
        model.fireTableDataChanged();
        
        // Actualizar panel inferior
        remove(getContentPane().getComponent(2)); 
        add(crearPanelInferior(), BorderLayout.SOUTH);
        revalidate();
        repaint();
    }

    private void filtroVenta(String filtro) {
        Vector<Vector<Object>> cargaFiltrada = new Vector<>();
        String filtroLower = filtro.toLowerCase();

        if (filtro.isEmpty()) {
            cargaFiltrada.addAll(datosOriginales);
        } else {
            for (Vector<Object> fila : datosOriginales) {
                String fecha = fila.get(1).toString().toLowerCase();
                String cliente = fila.get(2).toString().toLowerCase();
                String producto = fila.get(3).toString().toLowerCase();

                if (fecha.contains(filtroLower) || cliente.contains(filtroLower) || producto.contains(filtroLower)) {
                    cargaFiltrada.add(fila);
                }
            }
        }

        model.setRowCount(0);
        for (Vector<Object> vector : cargaFiltrada) {
            model.addRow(vector);
        }
        model.fireTableDataChanged();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameVentas());
    }
}
