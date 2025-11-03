package domain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import db.DataCliente;
import db.DataPedidos;

public class CajasFarmaciaSel extends JFrame{
	
	private static final long serialVersionUID = 1L;

	public static JPanel UltClientes() {
		JPanel panelP = new JPanel(new BorderLayout());
		JLabel encabezado = new JLabel("Ultimos clientes");
		
		Vector<Vector<Object>> dataClientes = DataCliente.cargaCliente("");
		
		Vector<String> columnNames = new Vector<>();
        columnNames.add("N*");
        columnNames.add("Cliente");
        columnNames.add("Fecha");
        columnNames.add("Producto");
        columnNames.add("Cantidad");
		
		DefaultTableModel model = new DefaultTableModel(dataClientes, columnNames) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }    
            
            
        };
		JTable clientes = new JTable(model);
		JScrollPane miScroll = new JScrollPane(clientes);
		
		panelP.add(encabezado, BorderLayout.NORTH);
		panelP.add(miScroll, BorderLayout.CENTER);
		return panelP;
	}
	
	public static JPanel PedProveedores() {
		JPanel panelP = new JPanel(new BorderLayout());
		JLabel encabezado = new JLabel("Pedidos a proveedores");
		
		Vector<Vector<Object>> dataClientes = DataPedidos.cargarPedidos();
		
		Vector<String> columnNames = new Vector<>();
        columnNames.add("N*");
        columnNames.add("Fecha");
        columnNames.add("Pedido");
        columnNames.add("Factura");
        columnNames.add("Proveedor");
		
		DefaultTableModel model = new DefaultTableModel(dataClientes, columnNames) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
            	if (columnIndex == 4) {
            		return JButton.class;
            	} else {
                return String.class;
            	}
            }       
            
        };
		JTable clientes = new JTable(model);
		JScrollPane miScroll = new JScrollPane(clientes);
		
		panelP.add(encabezado, BorderLayout.NORTH);
		panelP.add(miScroll, BorderLayout.CENTER);
		return panelP;
	}
}
