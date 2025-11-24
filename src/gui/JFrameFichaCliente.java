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

import domain.CajasClientePedido;
import domain.FrameManager;


public class JFrameFichaCliente extends JFramePrincipal{

	
	private static final long serialVersionUID = 1L;
	
	public JFrameFichaCliente(){
		this.setTitle("Ficha Clientes");
		this.setSize(new Dimension(600,800));
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		
		this.add(crearPanelPrincipal(), BorderLayout.CENTER);
		JButton cerrar = new JButton("Cerrar");
		cerrar.addActionListener((e)->{
			dispose();
		});
		this.add(cerrar,BorderLayout.SOUTH);
		this.setFocusable(true); //IAG
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
		JTextField panelNombre = new JTextField(10);
		panelPrincipal.add(panelNombre,BorderLayout.WEST);
		panelNombre.setEnabled(false);
		
		panelPrincipal.add(new JLabel("DNI:"));
		JTextField panelDni = new JTextField(10);
		panelPrincipal.add(panelDni,BorderLayout.WEST);
		panelDni.setEnabled(false);
		
		panelPrincipal.add(new JLabel("Teléfono:"));
		JTextField panelTelefono = new JTextField(10);
		panelPrincipal.add(panelTelefono);
		panelTelefono.setEnabled(false);
		
		panelPrincipal.add(new JLabel("Email:"));
		JTextField panelEmail = new JTextField(10);
		panelPrincipal.add(panelEmail);
		panelEmail.setEnabled(false);
		
		
		panelPrincipal.add(new JLabel("Dirección"));
		JTextField panelDireccion = new JTextField(10);
		panelPrincipal.add(panelDireccion,BorderLayout.WEST);
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
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		SwingUtilities.invokeLater(() -> new JFrameFichaCliente());
		
	}

}
