package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;

public class NumberButton extends StyledButton {

  public NumberButton(int digit, CalcModel model) {
    super(Integer.toString(digit));
    super.addActionListener(e -> model.insertDigit(digit));
  }
}
