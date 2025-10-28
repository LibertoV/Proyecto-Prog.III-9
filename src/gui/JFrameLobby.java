package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector; 
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import domain.FrameManager;

public class JFrameLobby extends JFramePrincipal {
    private static final long serialVersionUID = 1L;

    public JFrameLobby() {
        super();
        panel = super.panel;
        
        panel.setLayout(new BorderLayout()); 
        
        // ... (Configuración de SelectionPanel - Sin cambios) ...
        ImageIcon logo = new ImageIcon("resources/images/logo.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        JButton loginButton = new JButton("Cerrar sesión");
        JPanel SelectionPanel = new JPanel(new GridLayout(1,3));
        
        SelectionPanel.setOpaque(false); 
        SelectionPanel.add(imagen);
        SelectionPanel.add(new JLabel("Farmacias"));
        SelectionPanel.add(new JLabel());//Para ajustar espacios
        SelectionPanel.add(loginButton);
        SelectionPanel.setPreferredSize(new Dimension(SelectionPanel.getPreferredSize().width, 50));
        
        
        
        // 1. Nombres de Columnas
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Nombre");
        columnNames.add("Lugar");
        columnNames.add("EstadoAlmacen");
        columnNames.add("Pedidos");
        columnNames.add("SEL");
        
        //Datos placeholder
        Vector<Vector<Object>> data = new Vector<>();
        
        // Fila 1
        Vector<Object> farmaciaA = new Vector<>();
        farmaciaA.add("Farmacia Central");
        farmaciaA.add("Madrid");
        farmaciaA.add("Stock Bajo");
        farmaciaA.add("10 pendientes");
        farmaciaA.add(Boolean.FALSE);
        data.add(farmaciaA);

        // Fila 2
        Vector<Object> farmaciaB = new Vector<>();
        farmaciaB.add("Farmacia Del Sur");
        farmaciaB.add("Sevilla");
        farmaciaB.add("Stock Óptimo");
        farmaciaB.add("0 pendientes");
        farmaciaB.add(Boolean.FALSE);
        data.add(farmaciaB);
        
        // Fila 3
        Vector<Object> farmaciaC = new Vector<>();
        farmaciaC.add("Farmacia Norte");
        farmaciaC.add("Bilbao");
        farmaciaC.add("Stock Medio");
        farmaciaC.add("5 pendientes");
        farmaciaC.add(Boolean.FALSE);
        data.add(farmaciaC);
        
        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
               return column == 4;
            }
            
            
            //esta funcion de abajo ha sido escrita con la ayuda de una IA generativa
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return Boolean.class; 
                }
                return String.class;
            }   
        };
        
        model.addTableModelListener(new TableModelListener() {
        	 @Override
             public void tableChanged(TableModelEvent e) {
             	int row = e.getFirstRow();
                 int column = e.getColumn();
             	Boolean isChecked = (Boolean) model.getValueAt(row, column);
             	System.out.println("Checkbox en fila " + row + ": " + isChecked);	
             }
        });
        
        
        

        JTable tablaFarmacias = new JTable(model);
        
        JScrollPane scrollPane = new JScrollPane(tablaFarmacias);
        
        panel.add(SelectionPanel, BorderLayout.NORTH); // Barra superior
        
        panel.add(scrollPane, BorderLayout.CENTER); 
        
        
        loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameManager.Relogin();
				System.out.println("deloged");
			}
        });
    }
}