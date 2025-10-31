package gui;

import java.awt.Dimension;

import javax.swing.SwingUtilities;

public class JFrameSelPedido extends JFrameListaPedidos {
	private static final long serialVersionUID = 1L;
	public JFrameSelPedido() {
		this.setTitle("Pedido");
		this.setSize(new Dimension (500,400));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
				
		
		
		
	}
	
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JFrameSelPedido());
	}

}
