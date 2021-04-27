package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import java.util.function.DoubleUnaryOperator;

public class UnaryOperatorButton extends StyledButton {
  DoubleUnaryOperator operator;

  public UnaryOperatorButton(String text, DoubleUnaryOperator operator, CalcModel model) {
    super(text);
    this.operator = operator;
    super.addActionListener(e -> model.setValue(this.operator.applyAsDouble(model.getValue())));
  }

  void changeOperator(DoubleUnaryOperator operator) {
    if(operator == null)
      throw new NullPointerException("Operator can't be null");

    this.operator = operator;
  }
}
