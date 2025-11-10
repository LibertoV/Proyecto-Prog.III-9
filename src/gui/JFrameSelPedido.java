package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class JFrameSelPedido extends JFramePrincipal {

	private static final long serialVersionUID = 1L;

	public JFrameSelPedido() {
		this.setTitle("Informacion del pedido");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
				.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_DOWN_MASK), "volverAtras");

		getRootPane().getActionMap().put("volverAtras", new AbstractAction() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Cabecera
		this.add(crearCabecera(), BorderLayout.NORTH);

		// Cuerpo
		this.add(crearCuerpo(), BorderLayout.CENTER);

		// Parte de abajo
		this.add(crearAbajo(), BorderLayout.SOUTH);

		this.setVisible(true);
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

	public JPanel crearCabecera() {
		JPanel main = new JPanel(new BorderLayout());

		JPanel izq = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel nPed = new JLabel("Nº pedido: ");
		izq.add(nPed);

		JLabel numero = new JLabel("000", SwingConstants.RIGHT);
		numero.setPreferredSize(new Dimension(70, 20));
		numero.setBackground(Color.white);
		numero.setBorder(BorderFactory.createLineBorder(Color.gray));
		numero.setOpaque(true);
		izq.add(numero);
		main.add(izq, BorderLayout.WEST);

		JPanel dcha = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JLabel estado = new JLabel("Pendiente", SwingConstants.CENTER);
		estado.setPreferredSize(new Dimension(150, 20));
		estado.setOpaque(true);
		estado.setBackground(Color.LIGHT_GRAY);
		estado.setBorder(BorderFactory.createLineBorder(Color.BLACK));

		dcha.add(estado);
		main.add(dcha, BorderLayout.EAST);

		return main;

	}

	public JPanel crearCuerpo() {
		JPanel cuerpo = new JPanel();
		cuerpo.setLayout(new BoxLayout(cuerpo, BoxLayout.Y_AXIS));
		cuerpo.setBorder(new EmptyBorder(10, 15, 10, 15));
		cuerpo.setBackground(Color.WHITE);

		JPanel infoGeneral = new JPanel(new GridLayout(4, 2, 5, 5));
		infoGeneral.setBorder(BorderFactory.createTitledBorder("Información general"));
		infoGeneral.setBackground(Color.WHITE);

		infoGeneral.add(new JLabel("Fecha de pedido:"));
		infoGeneral.add(new JLabel("2025-11-06"));

		infoGeneral.add(new JLabel("Fecha de llegada estimada:"));
		infoGeneral.add(new JLabel("2025-11-10"));

		infoGeneral.add(new JLabel("Proveedor:"));
		infoGeneral.add(new JLabel("Amazon"));

		infoGeneral.add(new JLabel("Método de envío:"));
		infoGeneral.add(new JLabel("Urgente (24h)"));

		JPanel panelProductos = new JPanel(new BorderLayout());
		panelProductos.setBorder(BorderFactory.createTitledBorder("Productos del pedido"));
		panelProductos.setBackground(Color.WHITE);

		String[] columnas = { "Producto", "Cantidad", "Precio unitario (€)" };
		Object[][] datos = { { "Ibuprofeno M3", 200, 1 }, { "Paracetamol", 10, 2 }, { "Aspirinas", 50, 5 },
				{ "Enantium", 100, 2 } };

		JTable tabla = new JTable(new DefaultTableModel(datos, columnas)) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}

		};
		tabla.getTableHeader().setReorderingAllowed(false);

		final int[] filaHover = { -1 };

		DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
			@Override
			public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
					boolean hasFocus, int row, int column) {
				Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

				if (isSelected) {
					c.setBackground(new Color(173, 216, 230));
				} else if (row == filaHover[0]) {
					c.setBackground(new Color(220, 240, 255));
				} else {
					c.setBackground(Color.WHITE);
				}

				return c;
			}
		};
		
		for (int i = 0; i < tabla.getColumnCount(); i++) {
			tabla.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}

		JScrollPane scroll = new JScrollPane(tabla);
		
		tabla.addMouseMotionListener(new MouseMotionAdapter() {
		    @Override
		    public void mouseMoved(MouseEvent e) {
		        int fila = tabla.rowAtPoint(e.getPoint());
		        if (fila != filaHover[0]) {
		            filaHover[0] = fila;
		            tabla.repaint();
		        }
		    }
		});

		tabla.addMouseListener(new MouseAdapter() {
		    @Override
		    public void mouseExited(MouseEvent e) {
		        filaHover[0] = -1;
		        tabla.repaint();
		    }
		});

		
		panelProductos.add(scroll, BorderLayout.CENTER);

		JPanel resumen = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		resumen.setBackground(Color.WHITE);
		JLabel totalProd = new JLabel("Total productos: ");
		JLabel prod = new JLabel("360");

		JLabel totalPrec = new JLabel("Precio total: ");
		JLabel prec = new JLabel("1000€");

		resumen.add(totalProd);
		resumen.add(prod);
		resumen.add(Box.createRigidArea(new Dimension(20, 0)));
		resumen.add(totalPrec);
		resumen.add(prec);

		cuerpo.add(infoGeneral);
		cuerpo.add(Box.createRigidArea(new Dimension(0, 10)));
		cuerpo.add(panelProductos);
		cuerpo.add(Box.createRigidArea(new Dimension(0, 5)));
		cuerpo.add(resumen);

		return cuerpo;

	}

	public JPanel crearAbajo() {
		JPanel abajo = new JPanel(new FlowLayout(FlowLayout.LEFT));

		JLabel nombreUsuario = new JLabel("Usuario: ");
		abajo.add(nombreUsuario);

		JLabel nombre = new JLabel("XXX");
		abajo.add(nombre);

		return abajo;

	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> new JFrameSelPedido());

	}

}
