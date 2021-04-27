package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.*;
import java.awt.*;

public class StyledButton extends JButton {

  public StyledButton(String text) {
    super(text);
    style();
  }

  private void style() {
    super.setBackground(Color.DARK_GRAY);
    int size = (int) super.getPreferredSize().getWidth();
    super.setPreferredSize(new Dimension(size, size));
    super.setForeground(Color.WHITE);
    super.setFocusPainted(false);
    super.setOpaque(true);
  }
}
