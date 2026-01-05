package gui;

import javax.swing.*;

import domain.Producto;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class JBuscadorProducto extends JComboBox<Producto> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final List<Producto> listaOriginal;
    private boolean isFiltering = false;

    public JBuscadorProducto(List<Producto> datos) {
        super();
        this.listaOriginal = new ArrayList<>(datos);
        this.setEditable(true);
        this.setModel(new DefaultComboBoxModel<>(datos.toArray(new Producto[0])));
        this.setSelectedIndex(-1);      
        this.getEditor().setItem(null);

        JTextField textoEditor = (JTextField) this.getEditor().getEditorComponent();

        textoEditor.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_ENTER) {
                    return;
                }
                SwingUtilities.invokeLater(() -> filtrarDatos(textoEditor.getText()));
            }
        });
    }

    private void filtrarDatos(String textoBusqueda) {
        if (isFiltering) return;
        isFiltering = true;

        DefaultComboBoxModel<Producto> modelo = (DefaultComboBoxModel<Producto>) this.getModel();
        modelo.removeAllElements();

        if (textoBusqueda.isEmpty()) {
            for (Producto prod : listaOriginal) {
                modelo.addElement(prod);
            }
        } else {
            for (Producto prod : listaOriginal) {
                if (prod.toString().toLowerCase().contains(textoBusqueda.toLowerCase())) {
                    modelo.addElement(prod);
                }
            }
        }

        JTextField textoEditor = (JTextField) this.getEditor().getEditorComponent();
        textoEditor.setText(textoBusqueda);
        
        this.hidePopup();
        if (modelo.getSize() > 0) {
            this.showPopup();
        }
        isFiltering = false;
    }
    
    public Producto getProductoSeleccionado() {
        Object seleccion = this.getSelectedItem();
        if (seleccion instanceof Producto) {
            return (Producto) seleccion;
        }
        return null;
    }
}