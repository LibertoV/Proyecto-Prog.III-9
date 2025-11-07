package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class JFrameFichaTrabajador extends JFrame {

    public JFrameFichaTrabajador() {
        this.setTitle("Ficha del Trabajador");
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLayout(new BorderLayout());

        this.add(crearCabecera(), BorderLayout.NORTH);
        this.add(crearCuerpo(), BorderLayout.CENTER);
        this.add(crearParteAbajo(), BorderLayout.SOUTH);

        this.pack();
        this.setSize(400, this.getHeight() + 50); // 👈 Un poco más grande
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setVisible(true);
    }

    private JPanel crearCabecera() {
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton salir = new JButton("Salir");
        panelCabecera.add(salir);

        salir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return panelCabecera;
    }

    private JPanel crearCuerpo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Trabajador"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblApellidos = new JLabel("Apellidos:");
        JLabel lblDNI = new JLabel("DNI:");
        JLabel lblPuesto = new JLabel("Puesto:");
        JLabel lblFechaContratacion = new JLabel("Fecha Contratación (aaaa-mm-dd):");
        JLabel lblFinContrato = new JLabel("Fin de Contrato (aaaa-mm-dd):");
        JLabel lblTelefono = new JLabel("Teléfono:");

        
        
        JTextField txtNombre = new JTextField(15);
        JTextField txtApellidos = new JTextField(15);
        JTextField txtDNI = new JTextField(10);
        JTextField txtPuesto = new JTextField(15);
        JTextField txtFechaContratacion = new JTextField(10);
        JTextField txtFinContrato = new JTextField(10);
        JTextField txtTelefono = new JTextField(10);

        
        
        gbc.gridx = 0; 
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblApellidos, gbc);
        gbc.gridx = 1;
        panel.add(txtApellidos, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 2;
        panel.add(lblDNI, gbc);
        gbc.gridx = 1;
        panel.add(txtDNI, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblPuesto, gbc);
        gbc.gridx = 1;
        panel.add(txtPuesto, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 4;
        panel.add(lblFechaContratacion, gbc);
        gbc.gridx = 1;
        panel.add(txtFechaContratacion, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 5;
        panel.add(lblFinContrato, gbc);
        gbc.gridx = 1;
        panel.add(txtFinContrato, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 6;
        panel.add(lblTelefono, gbc);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        return panel;
    }

    private JPanel crearParteAbajo() {
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario: ");
        JLabel lblNombreUsuario = new JLabel("XXX");
        abajo.add(lblUsuario);
        abajo.add(lblNombreUsuario);
        return abajo;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new JFrameFichaTrabajador());
    }
}
