package gui;

//IAG Adaptado lo del otro modern en este
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
//IAG
public class ModernPasswordField extends JPasswordField {
  private int arcWidth = 20;
  private int arcHeight = 20;

  public ModernPasswordField(int columns) {
      super(columns);
      setOpaque(false);
      setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
      setFont(new Font("Arial", Font.PLAIN, 14));
      
      // AÑADIR ESTOS LISTENERS PARA REPINTAR
      addFocusListener(new java.awt.event.FocusAdapter() {
          @Override
          public void focusGained(java.awt.event.FocusEvent e) {
              repaint(); // Repintar cuando gana el foco
          }
          
          @Override
          public void focusLost(java.awt.event.FocusEvent e) {
              repaint(); // Repintar cuando pierde el foco
          }
      });
  }

  protected void paintComponent(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      // Sombra sutil
      g2.setColor(new Color(0, 0, 0, 20));
      g2.fillRoundRect(2, 2, getWidth() - 3, getHeight() - 3, arcWidth, arcHeight);
      
      // Fondo
      g2.setColor(getBackground());
      g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
      
      g2.dispose();
      super.paintComponent(g);
  }

  
  protected void paintBorder(Graphics g) {
      Graphics2D g2 = (Graphics2D) g.create();
      g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
      
      // Borde con efecto focus
      if (hasFocus()) {
          g2.setColor(new Color(66, 133, 244)); // Azul cuando tiene foco
          g2.setStroke(new BasicStroke(2));
      } else {
          g2.setColor(new Color(200, 200, 200)); // Gris cuando NO tiene foco
          g2.setStroke(new BasicStroke(1));
      }
      
      g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arcWidth, arcHeight);
      g2.dispose();
  }
}