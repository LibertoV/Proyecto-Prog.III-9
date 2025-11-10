package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import com.toedter.calendar.JDateChooser;


public class JDialogAñadirPedido extends JDialog {
	
	private JTable tablaProductos;
    public DefaultTableModel modelo;
	
	public JDialogAñadirPedido(JFrameListaPedidos parent) {
		super(parent, "Añadir nuevo pedido", true);
		setLayout(new BorderLayout(10,10));
		setSize(new Dimension(500,600));
		setLocationRelativeTo(parent);
		setResizable(false);
		
        add(crearPanelDetalles(), BorderLayout.NORTH);

        add(crearPanelTabla(), BorderLayout.CENTER);

        add(crearParteAbajo(), BorderLayout.SOUTH);
		
        this.setFocusable(true); //IAG
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				boolean ctrlPresionado = e.isControlDown();
				if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
		            dispose();
		            SwingUtilities.invokeLater(() -> new JFrameFarmaciaSel().setVisible(true)); 
		        }
			}
		});
		
		
	}
	private Component crearPanelDetalles() {
	    JPanel panel = new JPanel(new GridBagLayout());
	    panel.setBorder(BorderFactory.createTitledBorder("Datos del Pedido"));

	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.anchor = GridBagConstraints.WEST;

	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.weightx = 0.0;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(new JLabel("Proveedor:"), gbc);

	    gbc.gridx = 1;
	    gbc.weightx = 1.0;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    JTextField txtProveedor = new JTextField(30);
	    panel.add(txtProveedor, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.weightx = 0.0; 
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(new JLabel("Fecha Pedido:"), gbc); 

	    gbc.gridx = 1;
	    gbc.weightx = 1.0;
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    JDateChooser dateChooserPedido = new JDateChooser();
	    dateChooserPedido.setDateFormatString("yyyy-MM-dd");
	    panel.add(dateChooserPedido, gbc);

	    gbc.gridx = 0;
	    gbc.gridy = 2;
	    gbc.weightx = 0.0;
	    gbc.fill = GridBagConstraints.NONE;
	    panel.add(new JLabel("Fecha Estimada:"), gbc);

	    gbc.gridx = 1;
	    gbc.weightx = 1.0; 
	    gbc.fill = GridBagConstraints.HORIZONTAL;
	    JDateChooser dateChooserEstimada = new JDateChooser();
	    dateChooserEstimada.setDateFormatString("yyyy-MM-dd");
	    panel.add(dateChooserEstimada, gbc);

	    return panel;
	}	
	private Component crearPanelTabla() {
		JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Productos del Pedido"));

        String[] columnas = {"Producto", "Cantidad", "Precio Unitario (€)"};
        Object[][] datos = {

        };
        
        modelo = new DefaultTableModel(datos, columnas);
        tablaProductos = new JTable(modelo);

        JScrollPane scroll = new JScrollPane(tablaProductos);
        panel.add(scroll, BorderLayout.CENTER);

        JPanel panelBotonesTabla = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAnadirFila = new JButton("Añadir Producto");
        JButton btnEliminarFila = new JButton("Eliminar Producto");
        
        panelBotonesTabla.add(btnAnadirFila);
        panelBotonesTabla.add(btnEliminarFila);
        btnAnadirFila.addActionListener(e -> {
        	JDialogAñadirProducto dialog = new JDialogAñadirProducto(this);
        	dialog.setVisible(true);
        });
        
        btnEliminarFila.addActionListener(e -> {
            int filaSel = tablaProductos.getSelectedRow();
            if (filaSel != -1) {
                modelo.removeRow(filaSel);
            } else {
                JOptionPane.showMessageDialog(this, 
                        "Seleccione un producto de la tabla para eliminar.", 
                        "Error", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        panel.add(panelBotonesTabla, BorderLayout.SOUTH);
		return panel;
	}
	
	private Component crearParteAbajo() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnGuardar = new JButton("Guardar");
        JButton btnCancelar = new JButton("Cancelar");

        panel.add(btnGuardar);
        panel.add(btnCancelar);

        btnGuardar.addActionListener(e -> {

            int filas = modelo.getRowCount();
            if (filas == 0) {
                 JOptionPane.showMessageDialog(this, "Debe añadir al menos un producto.", "Error", JOptionPane.WARNING_MESSAGE);
                 
            }

            
            JOptionPane.showMessageDialog(this, "Pedido guardado con exito");

            dispose();
        });

        btnCancelar.addActionListener(e -> {
            dispose();
        });
		return panel;
	}


	public static void main(JFrameListaPedidos parent) {
		SwingUtilities.invokeLater(()->new JDialogAñadirPedido(parent));
	}
}
