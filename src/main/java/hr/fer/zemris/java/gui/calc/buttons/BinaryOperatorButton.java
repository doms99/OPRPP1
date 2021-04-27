package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import java.util.function.DoubleBinaryOperator;

public class BinaryOperatorButton extends StyledButton {
  DoubleBinaryOperator operator;

  public BinaryOperatorButton(String text, DoubleBinaryOperator operator, CalcModel model) {
    super(text);
    this.operator = operator;
    super.addActionListener(e -> model.setPendingBinaryOperation(this.operator));
  }

  void changeOperator(DoubleBinaryOperator operator) {
    if(operator == null)
      throw new NullPointerException("Operator can't be null");

    this.operator = operator;
  }
}
