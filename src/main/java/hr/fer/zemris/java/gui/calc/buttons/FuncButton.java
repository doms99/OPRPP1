package hr.fer.zemris.java.gui.calc.buttons;

import javax.swing.*;
import java.awt.event.ActionListener;

public class FuncButton extends StyledButton {

  public FuncButton(String text, ActionListener a) {
    super(text);
    super.addActionListener(a);
  }
}
