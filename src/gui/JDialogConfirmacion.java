package gui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class JDialogConfirmacion extends JDialog {

	public JDialogConfirmacion(JFramePrincipal padre) {
		super(padre, "Confirmacion", true);

		JPanel panel = new JPanel(new GridLayout(2, 1));

		JLabel etiquetaPregunta = new JLabel("¿Estás seguro que deseas ver esta farmacia?");
		etiquetaPregunta.setHorizontalAlignment(JLabel.CENTER); // Centra el texto
		panel.add(etiquetaPregunta);

		JPanel panelBotones = new JPanel(new GridLayout(1, 2));
		panelBotones.setOpaque(false);
		JButton siBoton = new JButton("Sí");
		panelBotones.add(siBoton);
		JButton noBoton = new JButton("No");
		panelBotones.add(noBoton);
		panel.add(panelBotones);

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
			}

		});

		this.add(panel);
		
		//Centrado hecho con la ayuda de IA
		this.pack();
		if (padre != null) {
		    int x = padre.getX() + (padre.getWidth() - getWidth()) / 2;
		    int y = padre.getY() + (padre.getHeight() - getHeight()) / 2;
		    setLocation(x, y);
		} else {
		    setLocationRelativeTo(null);
		}

		this.setSize(new Dimension(400, 200));
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);

	};

	public static void main(JFramePrincipal padre) {
		SwingUtilities.invokeLater(() -> {
			JDialogConfirmacion confirmacion = new JDialogConfirmacion(padre);
			confirmacion.setVisible(true);
		});
	}
}
