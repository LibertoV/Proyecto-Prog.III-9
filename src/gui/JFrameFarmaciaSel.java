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
        
        JPanel cabecera = new JPanel(new BorderLayout());
        cabecera.setSize(new Dimension (800,60));
        cabecera.setBackground(Color.gray);
        
        
        ImageIcon logo1 = new ImageIcon("resources/images/imagenMenuTresRayas.png");
        ImageIcon logoAjustado1 = new ImageIcon(logo1.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH));
        
        JButton options = new JButton(logoAjustado1);
        options.setBorder(null);
        options.setFont(new Font("Arial", Font.BOLD,20));
        options.setSize(50,50);
        options.setForeground(Color.black);
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
       
        ImageIcon logo = new ImageIcon("resources/images/logoEmpresa1.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(75, 50, Image.SCALE_SMOOTH));
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
        panel.setBackground(Color.lightGray); // gris claro
        panel.setPreferredSize(new Dimension(200, getHeight())); // Ancho del menú
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        String[] opciones = {"Buscar", "Prescripciones", "Pedidos", "Almacen", "Ventas", "Clientes","Trabajadores","Configuracion","Salir"};
        for (String opcion : opciones) {
            JButton btn = new JButton(opcion);
            btn.setHorizontalAlignment(SwingConstants.LEFT); 
            
            btn.addActionListener(e ->{
            	gestionarMenu(opcion);
            });
            
            panel.add(btn);
        }
        
        panel.setVisible(false); // Inicialmente oculto
        return panel;
    }
    
    private void gestionarMenu(String opcion) {
    	switch(opcion) {
    	case "Pedidos":
    		new JFrameListaPedidos();
    		dispose();
    		break;
    	case "Salir":
    		new JFrameLobby();
    		dispose();
    		break;
    	}
    	
    }
    
    
    private void toggleMenu() {
        menuAbierto = !menuAbierto;
        menu.setVisible(menuAbierto);
        // Esto fuerza al layout a actualizarse y mover el panel central
        revalidate(); 
        repaint();
    }
    
	
}
