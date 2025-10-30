package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

//LISTADO DE PEDIDOS

public class JFrameListaPedidos extends JFramePrincipal {
	private static final long serialVersionUID = 1L;
	
	
	public JFrameListaPedidos() {
		this.setTitle("Listado de pedidos");		
		panel = new JPanel(new BorderLayout());
		
		//CABECERA DE LA PAGINA
		
				
		JPanel cabeza = new JPanel(new BorderLayout());
		panel.add(cabeza,BorderLayout.NORTH);
		
		JPanel panel2 = new JPanel(new BorderLayout());
		
		ImageIcon logo = new ImageIcon("resources/images/Carrito.png");
		ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JLabel imagen = new JLabel(logoAjustado);
		cabeza.add(imagen, BorderLayout.EAST);
		
		
		ImageIcon logo2 = new ImageIcon("resources/images/Casa.png");
		ImageIcon logoAjustado2 = new ImageIcon(logo2.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
		JLabel imagen2 = new JLabel(logoAjustado2);
		cabeza.add(imagen2, BorderLayout.WEST);
		
		
		
		//TABLA
		String[] cabecera = {"numero pedido", "Fecha de la orden", "Fecha de llegada", "Numero poductos","Productos totales"};
		
		
		DefaultTableModel modelo = new DefaultTableModel(cabecera, 1) {
		    @Override
		    public boolean isCellEditable(int row, int column) {
		        return false;
		    }
		};
		
		JTable pedidos = new JTable(modelo);
		
		pedidos.getTableHeader().setReorderingAllowed(false);
		JScrollPane scroll = new JScrollPane(pedidos);
		 	
		panel2.add(scroll, BorderLayout.CENTER);
				
		modelo.addRow(new Object[]{
			    "P-002", "2025-10-28", "2025-11-05", "4", "125.50 €"
			});
		
		
		//panel.add(panel2, BorderLayout.CENTER);
		this.add(panel);
			
	}

	public static void main(String[] args) {
		JFrameListaPedidos lista = new JFrameListaPedidos();
		lista.setVisible(true);

	}
}
