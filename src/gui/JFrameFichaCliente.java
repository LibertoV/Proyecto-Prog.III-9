package gui;

import java.awt.BorderLayout;


import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Scrollbar;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import db.Cliente;
import domain.CajasClientePedido;
import domain.FrameManager;
import jdbc.GestorBDInitializerCliente;


public class JFrameFichaCliente extends JFramePrincipal{

	private Cliente cliente;
	private JTextField panelNombre,panelDni, panelTelefono, panelEmail, panelDireccion;
	private GestorBDInitializerCliente gestorBD = new GestorBDInitializerCliente();
	private static final long serialVersionUID = 1L;
	
	public JFrameFichaCliente(Cliente cliente){
		this.cliente = cliente;
		this.setTitle("Ficha Clientes");
		this.setSize(new Dimension(600,800));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		this.add(crearPanelPrincipal(), BorderLayout.CENTER);
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener((e)->{
			dispose();
			new JFrameListaClientes().setVisible(true);
		});
		this.add(cerrar,BorderLayout.SOUTH);
		this.setFocusable(true); //IAG
		this.setVisible(true);
		this.addKeyListener(listenerVolver(JFrameListaClientes.class));
	}
	


	private JPanel crearPanelPrincipal() {
		JPanel total = new JPanel();
		JPanel panelPrincipal = new JPanel(new FlowLayout(FlowLayout.LEFT,0,0));
		panelPrincipal.setBorder(null);
		panelPrincipal.add(crearPanelUsuario());
		JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
		panelInferior.add(crearPanelRecetas());
		JPanel panelTablaPedidos = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
		panelTablaPedidos.setBorder(null);
		panelTablaPedidos.add(crearPanelTabla(),BorderLayout.EAST);
		
		total.add(panelPrincipal);
		total.add(panelInferior);
		total.add(panelTablaPedidos);
		
		return total;
	}

	private JPanel crearPanelTabla() {
		JPanel panelTabla = CajasClientePedido.TableUtl();
		
		return panelTabla;
	}



	private JPanel crearPanelRecetas() {
		JList<String> recetasList = new JList<>();//aqui vendria el modelo de recetas de cada cliente
		recetasList.setBorder(BorderFactory.createTitledBorder("Recetas activas"));//IAG
		JPanel recetas = new JPanel(new BorderLayout(8,8));
		recetas.add(new JScrollPane(recetasList),BorderLayout.CENTER);
		return recetas;
	}

	private JPanel crearPanelUsuario() {
		JPanel panelPrincipal = new JPanel(new GridLayout(6,2,5,5));
		JPanel botones = new JPanel(new FlowLayout());
		
		panelPrincipal.add(new JLabel("Nombre Usuario:"));
		panelNombre = new JTextField(cliente.getNombre(),10);
		panelPrincipal.add(panelNombre);
		panelNombre.setEnabled(false);
		
		panelPrincipal.add(new JLabel("DNI:"));
		panelDni = new JTextField(cliente.getDni(),10);
		panelPrincipal.add(panelDni);
		panelDni.setEnabled(false);
		
		panelPrincipal.add(new JLabel("Teléfono:"));
		panelTelefono = new JTextField(cliente.getTlf(),10);
		panelPrincipal.add(panelTelefono);
		panelTelefono.setEnabled(false);
		
		panelPrincipal.add(new JLabel("Email:"));
		panelEmail = new JTextField(cliente.getEmail(),10);
		panelPrincipal.add(panelEmail);
		panelEmail.setEnabled(false);
		
		
		panelPrincipal.add(new JLabel("Dirección"));
		panelDireccion = new JTextField(cliente.getDireccion(),10);
		panelPrincipal.add(panelDireccion);
		panelDireccion.setEnabled(false);
		
		
		
		JButton editar = new JButton("Editar");
		
		JButton guardar = new JButton("Guardar");
		guardar.setEnabled(false);
		editar.addActionListener((e)->{
			panelNombre.setEnabled(true);
			panelDni.setEnabled(true);
			panelTelefono.setEnabled(true);
			panelEmail.setEnabled(true);
			panelDireccion.setEnabled(true);
			guardar.setEnabled(true);
			editar.setEnabled(false);
			
		});
		guardar.addActionListener((e)->{
			panelNombre.setEnabled(false);
			panelDni.setEnabled(false);
			panelTelefono.setEnabled(false);
			panelEmail.setEnabled(false);
			panelDireccion.setEnabled(false);
			this.gestorBD.actualizarNombre(cliente, panelNombre.getText());
			this.gestorBD.actualizarDNI(cliente, panelDni.getText());
			this.gestorBD.actualizarEmail(cliente, panelEmail.getText());
			this.gestorBD.actualizarDireccion(cliente, panelDireccion.getText());
			this.gestorBD.actualizarTelefono(cliente, panelTelefono.getText());
			guardar.setEnabled(false);
			editar.setEnabled(true);
			JOptionPane.showMessageDialog(this,
					 "Cambios guardados",
					 "Info",
					 JOptionPane.INFORMATION_MESSAGE
					 );
			
		});
		botones.add(editar);
		botones.add(guardar);
		panelPrincipal.add(botones);
		return panelPrincipal;
	}
	
	
	

}
