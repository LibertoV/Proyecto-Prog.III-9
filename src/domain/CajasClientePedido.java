package domain;

import java.awt.BorderLayout;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

import db.DataPedidos;
import gui.JFrameFichaCliente;

public class CajasClientePedido extends JFrame {

	
	private static final long serialVersionUID = 1L;
	
	public static JPanel TableUtl() {
		JPanel panelPrincipal = new JPanel(new BorderLayout());
		//JLabel encabezado = new JLabel("Ultimos clientes");
		
		Vector<Vector<Object>> dataPedidos = DataPedidos.cargarPedidos();
		
		Vector<String> columnNames = new Vector<>();
        columnNames.add("Fecha");
        columnNames.add("Producto");
        columnNames.add("Cantidad");
        columnNames.add("Precio");
        columnNames.add("Farmacia");
		
        DefaultTableModel model = new DefaultTableModel(dataPedidos, columnNames) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }    
            
            
        };
        JTable pedidos = new JTable(model);
        pedidos.getTableHeader().setReorderingAllowed(false);
		JScrollPane miScroll = new JScrollPane(pedidos);
		
		//panelPrincipal.add(encabezado, BorderLayout.NORTH);
		panelPrincipal.add(miScroll, BorderLayout.CENTER);
        
		return panelPrincipal;
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(() -> CajasClientePedido.TableUtl());
		
	}

}
