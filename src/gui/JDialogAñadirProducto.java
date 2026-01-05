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
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import domain.Producto;
import jdbc.GestorBDInitializerProducto;

public class JDialogAñadirProducto extends JDialog {
	private static final long serialVersionUID = 1L;
	private JBuscadorProducto txtNombre;
	private JTextField txtPrecio;
	private JSpinner spinnerCantidad;
	
	public JDialogAñadirProducto(JDialogAñadirPedido parent) {
		super(parent, "Añadir producto", true);
		setLayout(new BorderLayout(10,10));
		setSize(new Dimension(450,200));
		setResizable(false);
		setLocationRelativeTo(parent);
		
		add(crearPanel(),BorderLayout.CENTER);
		add(botonesConfirmacion(parent),BorderLayout.SOUTH);
		this.setFocusable(true);
		
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}
			@Override
			public void keyPressed(KeyEvent e) {
				boolean ctrlPresionado = e.isControlDown();
				if (ctrlPresionado && e.getKeyCode() == KeyEvent.VK_E) {
		            dispose();
		        }
			}
		});
	}
	
	private Component crearPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del producto"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.WEST; 

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        GestorBDInitializerProducto prodBD = new GestorBDInitializerProducto();
        txtNombre =  new JBuscadorProducto(prodBD.obtenerDatos());
        txtNombre.addActionListener(e -> {
			Object seleccion = txtNombre.getSelectedItem();
			
			if (seleccion instanceof Producto) {
				Producto p = (Producto) seleccion;
				txtPrecio.setText(String.valueOf(p.getPrecioUnitario())); 
			} else {
				txtPrecio.setText("");
			}
		});
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0; 
        panel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 1;
        SpinnerNumberModel modeloCantidad = new SpinnerNumberModel(1, 1, 10000, 1);
        spinnerCantidad = new JSpinner(modeloCantidad);
        panel.add(spinnerCantidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Precio Unitario:"), gbc);

        gbc.gridx = 1;
        txtPrecio = new JTextField(15);
        txtPrecio.setEditable(false);
        panel.add(txtPrecio, gbc);

        return panel;
	}
	
	private Component botonesConfirmacion(JDialogAñadirPedido parent) {
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton confirmar = new JButton("Añadir");
		JButton cancelar = new JButton("Cancelar");
		
		confirmar.addActionListener(e -> {
			
			int cantidad = (Integer) spinnerCantidad.getValue();
            
			if (!(txtNombre.getProductoSeleccionado() instanceof Producto)|| txtPrecio.getText().trim().isEmpty()) {
                 JOptionPane.showMessageDialog(this, "Producto Erroneo, no puede quedar campos en blanco", "Error", JOptionPane.WARNING_MESSAGE);
                 return;
            } 
			
			try {
				GestorBDInitializerProducto prodBD = new GestorBDInitializerProducto();
				parent.recibirProducto(prodBD.obtenerProductoPorId(txtNombre.getProductoSeleccionado().getId()),cantidad);
				dispose();
				
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(this, "El precio debe ser numérico", "Error", JOptionPane.ERROR_MESSAGE);
			}
        });
		
		cancelar.addActionListener(e -> {
			dispose();
		});
		
		botones.add(confirmar);
		botones.add(cancelar);
		return botones;
	}
}