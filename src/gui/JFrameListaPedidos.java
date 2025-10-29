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

public class JFrameListaPedidos extends JFrameFarmaciaSel {
	private static final long serialVersionUID = 1L;
	
	
	public JFrameListaPedidos() {
		this.setTitle("Listado de pedidos");		
		panel = new JPanel(new BorderLayout());
		
		panel.setBackground(new Color(171, 245, 182));
		
		
		
		//CABECERA DE LA PAGINA
		
				//Cambiar ya que si se hereda de la clase padre una cabecera de la que nos sirven cosas no hace falta esto 
				//puediendose modificar directamente en la clase padre
		JPanel cabeza = new JPanel(new BorderLayout());
		cabeza.setBackground(new Color(171, 245, 182));
		panel.add(cabeza,BorderLayout.NORTH);
		ImageIcon logo = new ImageIcon("resources/images/Carrito.png");
		ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		JLabel imagen = new JLabel(logoAjustado);
		cabeza.add(imagen, BorderLayout.EAST);
		
		
		//ESTO IMAGEN SE PUEDE CAMBIAR PERO AL HEREDAR DE SEL TOGGLE MENU HACE QUE AL CLICKAR SE REPINTEE EL MENU SEL
		ImageIcon logo2 = new ImageIcon("resources/images/Casa.png");
		ImageIcon logoAjustado2 = new ImageIcon(logo2.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
		JLabel imagen2 = new JLabel(logoAjustado2);
		cabeza.add(imagen2, BorderLayout.WEST);
		
		
		
		//TABLA
		String[] cabecera = {"numero pedido", "Fecha de la orden", "Fecha de llegada", "Numero poductos","Productos totales"};
		DefaultTableModel modelo = new DefaultTableModel(cabecera, 1);
		JTable pedidos = new JTable(modelo);
		JScrollPane scroll = new JScrollPane(pedidos);
		panel.add(scroll, BorderLayout.CENTER);
		
		modelo.addRow(new Object[]{
			    "P-002", "2025-10-28", "2025-11-05", "4", "125.50 €"
			});

		
		
		this.add(panel);
		
		
	}

	public static void main(String[] args) {
		JFrameListaPedidos lista = new JFrameListaPedidos();
		lista.setVisible(true);

	}
}
