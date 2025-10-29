package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class JFrameConfirmacion extends JFramePrincipal{
	private static final long serialVersionUID = 1L;
	public JFrameConfirmacion() {
		super();
		
		panel.setLayout(new GridLayout(2,1));
		
		JLabel etiquetaPregunta = new JLabel("¿Estás seguro que deseas ver esta farmacia?");
        etiquetaPregunta.setHorizontalAlignment(JLabel.CENTER); // Centra el texto
        panel.add(etiquetaPregunta);
		
		JPanel panelBotones = new JPanel(new GridLayout(1,2));
		panelBotones.setOpaque(false);
		JButton siBoton = new JButton("Sí"); 
		panelBotones.add(siBoton);
		JButton noBoton = new JButton("No");
        panelBotones.add(noBoton);
		panel.add(panelBotones);
		
        
        
		
		
		Dimension dimension = new Dimension(400,200);
		this.setMinimumSize(dimension);
		this.setSize(dimension);
		this.setResizable(false);
	    this.setLocationRelativeTo(null);
	    this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    
	    
	    noBoton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
        	
        });
	    
	    siBoton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new JFrameFarmaciaSel();
				dispose();
				//Debe actualizar el JFramePrincipal
			}
        	
        });
	}

}
