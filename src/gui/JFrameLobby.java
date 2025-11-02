package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.EventObject;
import java.util.Vector;
import javax.swing.AbstractCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import domain.DataLoad;

public class JFrameLobby extends JFramePrincipal {
    private static final long serialVersionUID = 1L;
    private JTextField txtFiltro;
	private Vector<Vector<Object>> datosOriginales;
	private DefaultTableModel model;
	protected int filaTablaFarmacia = -1;
    public JFrameLobby() {
        super();
        
        panel.setLayout(new BorderLayout()); 
        
        ImageIcon logo = new ImageIcon("resources/images/logoEmpresa1.png");
        ImageIcon logoAjustado = new ImageIcon(logo.getImage().getScaledInstance(90, 60, Image.SCALE_SMOOTH));
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
        datosOriginales = data;
        
        model = new DefaultTableModel(datosOriginales, columnNames) {
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
        
        CustomRowRenderer rowRenderer = new CustomRowRenderer();
        for (int i = 0; i < tablaFarmacias.getColumnCount() -1; i++) {
            tablaFarmacias.getColumnModel().getColumn(i).setCellRenderer(rowRenderer);
        }
        
        tablaFarmacias.setRowHeight(25);
        tablaFarmacias.getTableHeader().setReorderingAllowed(false);
        

        
        MouseMotionListener motionListener = new MouseMotionListener() {


    		@Override
    		public void mouseDragged(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseMoved(MouseEvent e) {
    			
    			Point puntosRaton = new Point(e.getX(),e.getY());
    			filaTablaFarmacia = tablaFarmacias.rowAtPoint(puntosRaton);
    			tablaFarmacias.repaint();
    			
    		}
    		
    	};
        
    	MouseListener miMouseListener = new MouseListener() {

    		@Override
    		public void mouseClicked(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mousePressed(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseReleased(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseEntered(MouseEvent e) {
    			// TODO Auto-generated method stub
    			
    		}

    		@Override
    		public void mouseExited(MouseEvent e) {
    			filaTablaFarmacia=-1;
    			tablaFarmacias.repaint();
    			
    		}
    		
    	};
    	
    	tablaFarmacias.addMouseMotionListener(motionListener);
    	tablaFarmacias.addMouseListener(miMouseListener);
        
        
        JScrollPane scrollPane = new JScrollPane(tablaFarmacias);
        
        
        panel.add(SelectionPanel, BorderLayout.NORTH); 
        
        datosOriginales = new Vector<>();
        for (Vector<Object> fila : data) {
            datosOriginales.add(new Vector<>(fila));
        }
        
        txtFiltro = new JTextField(20);
        DocumentListener doclistener = new DocumentListener() {
			
			
			
			@Override
			public void insertUpdate(DocumentEvent e) {
				filtroFarmacia(txtFiltro.getText());
				
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				filtroFarmacia(txtFiltro.getText());
				
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				System.out.println("Cambiando Texto");
				
			}
			
		};
        
		txtFiltro.getDocument().addDocumentListener(doclistener);
		JPanel panelFiltro = new JPanel();
		panelFiltro.add(new JLabel("Busqueda de Farmacia: "));
		panelFiltro.add(txtFiltro);
		
		JPanel FiltroPanel = new JPanel(new BorderLayout());
        FiltroPanel.setBackground(panel.getBackground());
        FiltroPanel.setOpaque(false);
		FiltroPanel.add(panelFiltro,BorderLayout.NORTH);
		FiltroPanel.add(scrollPane, BorderLayout.CENTER); 
		panel.add(FiltroPanel,BorderLayout.CENTER);
		
		
        loginButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new JFrameLogin();
				System.out.println("deloged");
			}
        });
    }
    
    
    
    private void filtroFarmacia(String filtro) {
    	Vector<Vector<Object>> cargaFiltrada = new Vector<Vector<Object>>();
    	String filtroLower = filtro.toLowerCase();
    	
    	if(filtro.isEmpty()) {
    		cargaFiltrada.addAll(datosOriginales);
    	}else {
    		for(Vector<Object> fila : datosOriginales) {
        		String nombreFarmacia = fila.get(0).toString().toLowerCase();
        		
        		if(nombreFarmacia.contains(filtroLower)) {
        			cargaFiltrada.add(fila);
        			
        		}
    		}
    	
    	}
    	
    	model.setRowCount(0);
    	for (Vector<Object> vector : cargaFiltrada) {
			model.addRow(vector);
		}
    	model.fireTableDataChanged();
    	
    	
    	
    };
    

    
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
        private JFrameLobby parent;

        public ButtonEditor(JTable table) {
            this.table = table;
            this.parent = parent;
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
            JDialogConfirmacion nuevaVentana = new JDialogConfirmacion(parent);
            nuevaVentana.setVisible(true);	
        }
    }
    
    //Realizado con IA generativa
    private class CustomRowRenderer extends JLabel implements TableCellRenderer {
        private static final long serialVersionUID = 1L;

        public CustomRowRenderer() {
            setOpaque(true); 
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, // <-- Ignoraremos 'isSelected'
                                                       int row, int column) {
            
            setText(value != null ? value.toString() : "");
            
            // 💡 Lógica para pintar SOLO si el ratón está encima
            if (row == filaTablaFarmacia) {
                // Fila actual sobre la que está el ratón (pintar de azul claro)
                setBackground(new Color(173, 216, 230)); 
                setForeground(table.getSelectionForeground()); // Color de texto claro/blanco
            } else {
                // Resto de filas (color por defecto)
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }
            
            // Aunque la tabla no resalte la selección, el foco puede ser útil
            if (hasFocus) {
                 // Opcional: puedes poner un borde para indicar el foco
                 setBorder(UIManager.getBorder("Table.focusCellHighlightBorder")); 
            } else {
                 // Borde estándar de celda
                 setBorder(UIManager.getBorder("TableHeader.cellBorder")); 
            }
            
            return this;
        }
    }
}