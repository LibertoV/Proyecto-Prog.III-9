package gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import domain.DataLoad;
import domain.FrameManager;

public class JFrameLobby extends JFramePrincipal {
    private static final long serialVersionUID = 1L;

    public JFrameLobby() {
        super();
        
        panel.setLayout(new BorderLayout()); 
        
        ImageIcon logo = new ImageIcon("resources/images/logo.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(75, 75, Image.SCALE_SMOOTH));
        JLabel imagen = new JLabel(logoAjustado);
        
        JButton loginButton = new JButton("Cerrar sesión");
        JPanel SelectionPanel = new JPanel(new GridLayout(1,3));
        
        SelectionPanel.setOpaque(false);
        SelectionPanel.setPreferredSize(new Dimension(SelectionPanel.getPreferredSize().width, 50));
   
        SelectionPanel.add(imagen);
        SelectionPanel.add(new JLabel("Farmacias"));
        SelectionPanel.add(new JLabel());
        SelectionPanel.add(loginButton);
        
        
        Vector<String> columnNames = new Vector<>();
        columnNames.add("Nombre");
        columnNames.add("Lugar");
        columnNames.add("EstadoAlmacen");
        columnNames.add("Pedidos");
        columnNames.add("SEL");
        
        Vector<Vector<Object>> data = DataLoad.cargaFarmacia("src/db/farmacias.csv");
        

        
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            private static final long serialVersionUID = 1L;
            
            @Override
            public boolean isCellEditable(int row, int column) {
               return column == 4 ;
            }
            
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 4) {
                    return JButton.class; 
                }
                return String.class;
            }    
            
        };
        
        
        
        
        

        JTable tablaFarmacias = new JTable(model);
        tablaFarmacias.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer()); 
        tablaFarmacias.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(tablaFarmacias)); 
        
        tablaFarmacias.setRowHeight(25);
        tablaFarmacias.getTableHeader().setReorderingAllowed(false);
        
        
        JScrollPane scrollPane = new JScrollPane(tablaFarmacias);
        
        
        panel.add(SelectionPanel, BorderLayout.NORTH); 
        panel.add(scrollPane, BorderLayout.CENTER); 
        
        
        
        loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				FrameManager.Relogin();
				System.out.println("deloged");
			}
        });
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        private static final long serialVersionUID = 1L;
        
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                                                       boolean isSelected, boolean hasFocus, 
                                                       int row, int column) {
            setText(value.toString()); 
            return this;
        }
    }

    private static class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
    	
    	//esta entidad ha sido creada con ayuda y guia de una ia generativa
        private static final long serialVersionUID = 1L;
        private final JButton button;
        private String label;
        private JTable table;
        private int currentRow; 

        public ButtonEditor(JTable table) {
            this.table = table;
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(this);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            this.currentRow = row;
            if (value != null) {
                label = (String) value;
                button.setText(label);
            } else {
                label = "";
                button.setText("");
            }
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            return label; 
        }

        @Override
        public boolean isCellEditable(EventObject e) {
            return true; 
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped(); 
            
            String nombreFarmacia = (String) table.getModel().getValueAt(currentRow, 0); 
            
            System.out.println("Botón 'SEL' presionado en la fila: " + currentRow);
            System.out.println("Farmacia seleccionada: " + nombreFarmacia);
            JFrameConfirmacion nuevaVentana = new JFrameConfirmacion();
            nuevaVentana.setVisible(true);	
        }
    }
}