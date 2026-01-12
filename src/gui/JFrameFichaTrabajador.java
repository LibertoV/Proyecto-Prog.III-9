package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import domain.Trabajador;
import jdbc.GestorBDInitializerCliente;
import jdbc.GestorBDInitializerTrabajadores;

public class JFrameFichaTrabajador extends JFramePrincipal {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GestorBDInitializerTrabajadores gestorBD = new GestorBDInitializerTrabajadores();
	private Trabajador trabajador;
	private JLabel lblNombre;
	private JLabel lblDNI;
	private JLabel lblTelefono;
	private JLabel lblEmail;
	private JLabel lblDireccion;
	private JLabel lblPuesto;
	private JLabel lblTurno;
	private JLabel lblNss;
	private JLabel lblSalario;
	private JTextField txtNombre;
	private JTextField txtDNI;
	private JTextField txtTelefono;
	private JTextField txtEmail;
	private JTextField txtPuesto;
	private JTextField txtDireccion;
	private JTextField txtNss;
	private JTextField txtTurno;
	private JTextField txtSalario;

	public JFrameFichaTrabajador(Trabajador trabajador) {
		this.trabajador = trabajador;
		this.setTitle("Ficha Trabajadores");
		this.setSize(new Dimension(380,400));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		this.add(crearCabecera(), BorderLayout.NORTH);
	
		this.add(crearCuerpo(),BorderLayout.CENTER);
		this.add(crearParteAbajo(),BorderLayout.SOUTH);
		this.setFocusable(true); //IAG
		this.setVisible(true);
		this.addKeyListener(listenerVolver(JFrameListaTrabajadores.class));
    }

    private JPanel crearCabecera() {
        JPanel panelCabecera = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton salir = new JButton("Salir");
        panelCabecera.add(salir);

        salir.addActionListener((e)->{
			dispose();
			new JFrameListaTrabajadores().setVisible(true);
		
        });
        return panelCabecera;
    }

    private JPanel crearCuerpo() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Datos del Trabajador"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        lblNombre = new JLabel("Nombre:");
        lblDNI = new JLabel("DNI:");
        lblTelefono = new JLabel("Telefono:");
        lblEmail = new JLabel("Email:");
        lblDireccion = new JLabel("Direccion:");
        lblPuesto = new JLabel("Puesto:");
        lblNss = new JLabel("Numer de Seguridad Social:");
        lblTurno = new JLabel("Turno:");
        lblSalario = new JLabel("Salario:");
       
        
        
        txtNombre = new JTextField(trabajador.getNombre());
        txtDNI = new JTextField(trabajador.getDni());
        txtTelefono = new JTextField(trabajador.getTelefono());
        txtEmail = new JTextField(trabajador.getEmail());
        txtDireccion = new JTextField(trabajador.getDireccion());
        txtPuesto = new JTextField(trabajador.getPuesto());
        txtNss = new JTextField(trabajador.getNss());
        txtTurno = new JTextField(trabajador.getTurno());
        txtSalario = new JTextField(trabajador.getSalario());
        
        
        txtNombre.setEnabled(false);
		txtDNI.setEnabled(false);
		txtTelefono.setEnabled(false);
		txtEmail.setEnabled(false);
		txtDireccion.setEnabled(false);
		txtPuesto.setEnabled(false);
		txtNss.setEnabled(false);
		txtTurno.setEnabled(false);
		txtSalario.setEnabled(false);
        
        
        gbc.gridx = 0; 
        gbc.gridy = 0;
        panel.add(lblNombre, gbc);
        gbc.gridx = 1;
        panel.add(txtNombre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(lblDNI, gbc);
        gbc.gridx = 1;
        panel.add(txtDNI, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 2;
        panel.add(lblTelefono, gbc);
        gbc.gridx = 1;
        panel.add(txtTelefono, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(lblEmail, gbc);
        gbc.gridx = 1;
        panel.add(txtEmail, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 4;
        panel.add(lblDireccion, gbc);
        gbc.gridx = 1;
        panel.add(txtDireccion, gbc);

        gbc.gridx = 0; 
        gbc.gridy = 5;
        panel.add(lblPuesto, gbc);
        gbc.gridx = 1;
        panel.add(txtPuesto, gbc);

        
        gbc.gridx = 0; 
        gbc.gridy = 6;
        panel.add(lblNss, gbc);
        gbc.gridx = 1;
        panel.add(txtNss, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 7;
        panel.add(lblTurno, gbc);
        gbc.gridx = 1;
        panel.add(txtTurno, gbc);
        
        gbc.gridx = 0; 
        gbc.gridy = 8;
        panel.add(lblSalario, gbc);
        gbc.gridx = 1;
        panel.add(txtSalario, gbc);

        return panel;
    }

    private JPanel crearParteAbajo() {
        JPanel abajo = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel lblUsuario = new JLabel("Usuario: ");
        JLabel lblNombreUsuario = new JLabel(""+trabajador.getId());
        JButton botonEditar = new JButton("Editar");
        JButton botonGuardar = new JButton("Guardar");
        botonGuardar.setEnabled(false);
        botonEditar.addActionListener(e->{
        	txtNombre.setEnabled(true);
			txtDNI.setEnabled(true);
			txtTelefono.setEnabled(true);
			txtEmail.setEnabled(true);
			txtDireccion.setEnabled(true);
			txtPuesto.setEnabled(true);
			txtNss.setEnabled(true);
			txtTurno.setEnabled(true);
			txtSalario.setEnabled(true);
			botonGuardar.setEnabled(true);
			botonEditar.setEnabled(false);
        });
        botonGuardar.addActionListener(e->{
        	txtNombre.setEnabled(false);
			txtDNI.setEnabled(false);
			txtTelefono.setEnabled(false);
			txtEmail.setEnabled(false);
			txtDireccion.setEnabled(false);
			txtPuesto.setEnabled(false);
			txtNss.setEnabled(false);
			txtTurno.setEnabled(false);
			txtSalario.setEnabled(false);
        	this.gestorBD.actualizarNombre(trabajador, txtNombre.getText());
			this.gestorBD.actualizarDNI(trabajador, txtDNI.getText());
			this.gestorBD.actualizarEmail(trabajador, txtEmail.getText());
			this.gestorBD.actualizarDireccion(trabajador, txtDireccion.getText());
			this.gestorBD.actualizarTelefono(trabajador, txtTelefono.getText());
			this.gestorBD.actualizarPuesto(trabajador, txtPuesto.getText());
			this.gestorBD.actualizarNSS(trabajador, txtNss.getText());
			this.gestorBD.actualizarTurno(trabajador, txtTurno.getText());
			this.gestorBD.actualizarSalario(trabajador, txtSalario.getText());
			botonGuardar.setEnabled(false);
			botonEditar.setEnabled(true);
			JOptionPane.showMessageDialog(this,
					 "Cambios guardados",
					 "Info",
					 JOptionPane.INFORMATION_MESSAGE
					 );
        });
        abajo.add(lblUsuario);
        abajo.add(lblNombreUsuario);
        abajo.add(botonEditar);
        abajo.add(botonGuardar);
        return abajo;
    }

    
}
