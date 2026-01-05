package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import domain.Pedido;
import domain.Producto;
import jdbc.GestorBDInitializerPedido;

public class JFrameAlmacen extends JFramePrincipal {
    private static final long serialVersionUID = 1L;
    DefaultTableModel modelo;
    JTable tablaAlmacen;
    List<Pedido> datosFiltrados;
    JComboBox<String> combo;
    Vector<String> columnNames;

    JFrameAlmacen() {
        this.setTitle("Almacen");
        this.setSize(new Dimension(1200, 850));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        this.addKeyListener(listenerVolver(JFrameFarmaciaSel.class));

        // Cargar datos
        GestorBDInitializerPedido pedidosBD = new GestorBDInitializerPedido();
        List<Pedido> datosCargados = pedidosBD.obtenerDatos();
        datosFiltrados = new ArrayList<>();

        for (Pedido pedido : datosCargados) {
            LocalDate fechaLlegada = ((java.sql.Date) pedido.getFechaLlegada()).toLocalDate();
            if (fechaLlegada.isBefore(LocalDate.now()) && pedido.getIdFarmacia() == idFarActual) {
                datosFiltrados.add(pedido);
            }
        }

        this.add(panelCabecera(), BorderLayout.NORTH);

        JTextField txtfiltro = new JTextField(15);
        DocumentListener filtro = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filtroMedicamento(txtfiltro.getText());
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                filtroMedicamento(txtfiltro.getText());
            }
            @Override
            public void changedUpdate(DocumentEvent e) {}
        };
        txtfiltro.getDocument().addDocumentListener(filtro);

        String[] tipoFiltro = { "Nombre", "ID", "Proveedor" };
        combo = new JComboBox<>(tipoFiltro);

        combo.addActionListener(e -> {
            txtfiltro.setText("");
            filtroMedicamento("");
        });

        JPanel panelAlmacen = new JPanel(new BorderLayout());
        JPanel panelBusqueda = new JPanel(new FlowLayout());

        panelBusqueda.add(txtfiltro);
        panelBusqueda.add(combo);

        panelAlmacen.add(panelBusqueda, BorderLayout.NORTH);
        panelAlmacen.add(tablaAlmacen(datosFiltrados));

        this.add(panelAlmacen, BorderLayout.CENTER);
    }

    JPanel panelCabecera() {
        JPanel panelCabecera = new JPanel(new BorderLayout());
        panelCabecera.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        ImageIcon logo1 = new ImageIcon("resources/images/Casa.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
        JButton Salir = new JButton(logoAjustado1);
        Salir.setBorder(null);
        panelCabecera.add(Salir, BorderLayout.WEST);

        Salir.addActionListener(e -> {
            dispose();
            new JFrameFarmaciaSel();
        });

        return panelCabecera;
    };

    JScrollPane tablaAlmacen(List<Pedido> datos) {
        columnNames = new Vector<String>();
        columnNames.add("ID");
        columnNames.add("Nombre");
        columnNames.add("Stock");
        columnNames.add("Precio/u");
        columnNames.add("Proveedor");

        Vector<Vector<Object>> copiaDatos = new Vector<>();
        obtenerDatos(copiaDatos, datos);

        modelo = new DefaultTableModel(copiaDatos, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaAlmacen = new JTable(modelo);
        tablaAlmacen.getTableHeader().setReorderingAllowed(false);
        JScrollPane miScrollPane = new JScrollPane(tablaAlmacen);

        int columnIndex = columnNames.indexOf("Proveedor");
        if (columnIndex != -1) {
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setCellRenderer(new CustomImageRenderer());
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setMaxWidth(120);
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setMinWidth(120);
        }
        return miScrollPane;
    }

    private void filtroMedicamento(String filtro) {
        Vector<Vector<Object>> cargaFiltrada = new Vector<>();
        String filtroLower = filtro.toLowerCase().trim();
        String criterio = (String) combo.getSelectedItem();

        if (filtroLower.isEmpty()) {
            obtenerDatos(cargaFiltrada, datosFiltrados);
        } else {
            for (Pedido pedido : datosFiltrados) {
                for (Producto producto : pedido.getProductos().keySet()) {

                    String valorAComparar = "";

                    if (criterio.equals("ID")) {
                        valorAComparar = String.valueOf(producto.getId());
                    } else if (criterio.equals("Nombre")) {
                        valorAComparar = producto.getNombre();
                    } else if (criterio.equals("Proveedor")) {
                        valorAComparar = pedido.getProveedor();
                    }

                    if (valorAComparar != null && valorAComparar.toLowerCase().contains(filtroLower)) {
                        Vector<Object> fila = new Vector<>();
                        fila.add(producto.getId());
                        fila.add(producto.getNombre());
                        fila.add(pedido.getProductos().get(producto)); // Stock
                        fila.add(producto.getPrecioUnitario());
                        fila.add(pedido.getProveedor());

                        cargaFiltrada.add(fila);
                    }
                }
            }
        }

        modelo.setDataVector(cargaFiltrada, columnNames);

        // Reasignar renderer
        int columnIndex = columnNames.indexOf("Proveedor");
        if (columnIndex != -1) {
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setCellRenderer(new CustomImageRenderer());
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setMaxWidth(120);
            tablaAlmacen.getColumnModel().getColumn(columnIndex).setMinWidth(120);
        }
    }

    private void obtenerDatos(Vector<Vector<Object>> linea, List<Pedido> datos) {
        for (Pedido pedido : datos) {
            for (Producto producto : pedido.getProductos().keySet()) {
                // ✅ CAMBIO: Eliminado el " " de Categoria en Arrays.asList
                linea.add(new Vector<>(Arrays.asList(
                        producto.getId(),
                        producto.getNombre(),
                        pedido.getProductos().get(producto),
                        producto.getPrecioUnitario(),
                        pedido.getProveedor()
                )));
            }
        }
    }

    class CustomImageRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 1L;

        public CustomImageRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER);
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            setIcon(null);
            setText(null);

            if (value instanceof String && !((String) value).isEmpty()) {
                ImageIcon icono = getCachedIcon((String) value);
                if (icono != null)
                    setIcon(icono);
                else
                    setText("IMG ERROR");
            } else {
                setText("N/A");
            }

            return this;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameAlmacen());
    }
}

