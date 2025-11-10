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
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JDialogAñadirProducto extends JDialog {
	private static final long serialVersionUID = 1L;
	JTextField txtNombre;
	JTextField txtCantidad;
	JTextField txtPrecio;
	Object[] producto = null;
	public JDialogAñadirProducto(JDialogAñadirPedido parent) {
		super(parent, "Añadir producto", true);
		setLayout(new BorderLayout(10,10));
		setSize(new Dimension(200,200));
		setResizable(false);
		setLocationRelativeTo(parent);
		
		add(crearPanel(),BorderLayout.CENTER);
		add(botonesConfirmacion(parent),BorderLayout.SOUTH);
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
        
        txtNombre = new JTextField(30);
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0.0; 

        panel.add(new JLabel("Cantidad:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.0; 

        txtCantidad = new JTextField(15);
        panel.add(txtCantidad, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Precio Unitario:"), gbc);

        gbc.gridx = 1;
        txtPrecio = new JTextField(15);
        panel.add(txtPrecio, gbc);

        return panel;
	}
	
	private Component botonesConfirmacion(JDialogAñadirPedido parent) {
		JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton confirmar = new JButton("Añadir");
		JButton cancelar = new JButton("Cancelar");
		
		confirmar.addActionListener(e -> {
            
			if (txtNombre.getText().length() == 0 ||
            	txtCantidad.getText().length() == 0 ||
            	txtPrecio.getText().length() == 0
            	) {
                 JOptionPane.showMessageDialog(this, "Producto Erroneo, no puede quedar campos en blanco", "Error", JOptionPane.WARNING_MESSAGE);
            } else {
            	producto = new Object[] {txtNombre.getText(),txtCantidad.getText(),txtPrecio.getText()};
            	parent.modelo.addRow(producto);
                
                JOptionPane.showMessageDialog(this, "Pedido guardado con exito");
            }

            

            dispose();
        });
		cancelar.addActionListener(e -> {
			dispose();
		});
		botones.add(confirmar);
		botones.add(cancelar);
		return botones;
		
	}
}
