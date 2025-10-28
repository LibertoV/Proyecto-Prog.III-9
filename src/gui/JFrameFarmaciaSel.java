package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;





public class JFrameFarmaciaSel extends  JFrameLobby{
	private static final long serialVersionUID = 1L;
	private JPanel menu; //IA
	private boolean menuAbierto = false; //IA
	
	
	public JFrameFarmaciaSel() {
		super();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(63,180,190));
		
       
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.red);
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setSize(new Dimension (800,60));
        cabecera.setBackground(new Color(171, 245, 182));
        
        JButton options = new JButton("☰");
        options.setFont(new Font("Arial", Font.BOLD,20));
        options.setSize(60,60);
        options.setForeground(Color.white);
        options.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				System.out.println("Seleccionado");
				toggleMenu();
			}
		});
        
        cabecera.add(options,BorderLayout.WEST);
        
        JLabel tituloLabel = new JLabel("Nombre Farmacia");
        cabecera.setBackground(Color.white);
        cabecera.setFont(new Font("Arial",Font.BOLD,22));
        cabecera.add(tituloLabel,BorderLayout.CENTER);
       
        ImageIcon logo = new ImageIcon("resources/images/Perfil.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        cabecera.add(imagen,BorderLayout.EAST);
        
        mainPanel.add(cabecera, BorderLayout.NORTH);
        JPanel centerPanel = new JPanel(new BorderLayout());
        menu = createMenuPanel();
        centerPanel.add(menu,BorderLayout.WEST);
        
      
        JPanel gridLayoutPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        JTable pantalla1 = new JTable();
        JTable pantalla2 = new JTable();
        JTable pantalla3 = new JTable();
        JTable pantalla4 = new JTable();
        gridLayoutPanel.add(pantalla1);
        gridLayoutPanel.add(pantalla2);
        gridLayoutPanel.add(pantalla3);
        gridLayoutPanel.add(pantalla4);
		centerPanel.add(gridLayoutPanel,BorderLayout.CENTER);
    
		mainPanel.add(centerPanel,BorderLayout.CENTER);
		setVisible(true);
        
        panel.add(mainPanel);
	}
	
	//Ayuda de IA generativa
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(9, 1, 0, 10)); // 9 filas, 1 columna, 10px de espacio
        panel.setBackground(new Color(220, 240, 255)); // Azul claro
        panel.setPreferredSize(new Dimension(200, getHeight())); // Ancho del menú
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        
        String[] opciones = {"Buscar", "Prescripciones", "Pedidos", "Almacen", "Ventas", "Clientes","Trabajadores","Configuracion","Salir"};
        for (String opcion : opciones) {
            JButton btn = new JButton(opcion);
            btn.setHorizontalAlignment(SwingConstants.LEFT); 
            panel.add(btn);
        }
        
        panel.setVisible(false); // Inicialmente oculto
        return panel;
    }
    
    
    private void toggleMenu() {
        menuAbierto = !menuAbierto;
        menu.setVisible(menuAbierto);
        // Esto fuerza al layout a actualizarse y mover el panel central
        revalidate(); 
        repaint();
    }
    
	public static void main(String[] args) {
        // Crear la ventana en el hilo de eventos de Swing
		SwingUtilities.invokeLater(() -> {
    		// Crear una instancia de EjemploLayouts y hacerla visible
            JFrameFarmaciaSel farmacia = new JFrameFarmaciaSel();
            farmacia.setVisible(true);
        });
    }
}
