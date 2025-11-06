package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import db.DataAlmacen;

public class JFrameAlmacen extends JFramePrincipal {
    private static final long serialVersionUID = 1L;
    DefaultTableModel modelo;
    Vector<Vector<Object>> datosOriginales;
    JComboBox<String> combo;
    Vector<String> columnNames;

    JFrameAlmacen() {
        this.setTitle("Almacen");
        this.setSize(new Dimension(1200, 850));
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        // ✅ CAMBIO 1: Cargar datos y crear una copia profunda
        Vector<Vector<Object>> datosCargados = DataAlmacen.cargarAlmacen();
        datosOriginales = new Vector<>();
        for (Vector<Object> fila : datosCargados) {
            datosOriginales.add(new Vector<>(fila)); // Copia profunda
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

        String[] tipoFiltro = { "Nombre", "ID","Categoria", "Proveedor" };
        combo = new JComboBox<>(tipoFiltro);

        combo.addActionListener(e -> {
            txtfiltro.setText("");           // Limpia el campo de texto
            filtroMedicamento("");           // Restablece la tabla completa
        });
        
        
        
        JPanel panelAlmacen = new JPanel(new BorderLayout());
        JPanel panelBusqueda = new JPanel(new FlowLayout());

        panelBusqueda.add(txtfiltro);
        panelBusqueda.add(combo);

        panelAlmacen.add(panelBusqueda,BorderLayout.NORTH);
        panelAlmacen.add(tablaAlmacen(datosOriginales));

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

    JScrollPane tablaAlmacen(Vector<Vector<Object>> datos) {
        columnNames = new Vector<String>();
        columnNames.add("ID");
        columnNames.add("Nombre");
        columnNames.add("Categoria");
        columnNames.add("Stock");
        columnNames.add("Precio/u");
        columnNames.add("Proveedor");

        // ✅ CAMBIO 2: Crear modelo con copia independiente
        Vector<Vector<Object>> copiaDatos = new Vector<>();
        for (Vector<Object> fila : datos) {
            copiaDatos.add(new Vector<>(fila));
        }

        modelo = new DefaultTableModel(copiaDatos, columnNames) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tablaAlmacen = new JTable(modelo);
        JScrollPane miScrollPane = new JScrollPane(tablaAlmacen);
        return miScrollPane;
    }

    private void filtroMedicamento(String filtro) {
        System.out.println("actualiza");
        Vector<Vector<Object>> cargaFiltrada = new Vector<>();
        String filtroLower = filtro.toLowerCase().trim();
        int columnaFiltrada = columnNames.indexOf(combo.getSelectedItem());
        System.out.println("Tamaño datosOriginales: " + datosOriginales.size());

        if (columnaFiltrada < 0) columnaFiltrada = 0;

        if (filtroLower.isEmpty()) {
            for (Vector<Object> dato : datosOriginales) {
                cargaFiltrada.add(new Vector<>(dato));
            }
        } else {
            for (Vector<Object> fila : datosOriginales) {
                String id = fila.get(columnaFiltrada).toString().toLowerCase();
                if (id.contains(filtroLower)) {
                    cargaFiltrada.add(new Vector<>(fila));
                }
            }
        }

        modelo.setRowCount(0);
        for (Vector<Object> vector : cargaFiltrada) {
            modelo.addRow(vector);
        }
        modelo.fireTableDataChanged();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameAlmacen());
    }
}

