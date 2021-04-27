package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;
import java.awt.*;
import java.util.function.DoubleBinaryOperator;

public class TwoFuncBinaryOperatorButton extends BinaryOperatorButton implements TwoFuncButton {
  private final DoubleBinaryOperator inverse;
  private final DoubleBinaryOperator normal;
  private final String normalText, inverseText;
  private Dimension dimension;

  public TwoFuncBinaryOperatorButton(String normalText, String inverseText, DoubleBinaryOperator normal, DoubleBinaryOperator inverse, CalcModel model) {
    super(normalText, normal, model);
    this.inverse = inverse;
    this.normal = normal;
    this.normalText = normalText;
    this.inverseText = inverseText;
    calculateSize();
  }

  private void calculateSize() {
    Insets insets = super.getInsets();
    int dim = (int) (insets.left + insets.right + new JLabel(normalText.length() > inverseText.length() ? normalText : inverseText).getPreferredSize().getWidth());
    this.dimension = new Dimension(dim, dim);
  }

  @Override
  public void swap() {
    if(super.operator.equals(inverse)) {
      super.operator = normal;
      super.setText(normalText);
    }
    else {
      super.operator = inverse;
      super.setText(inverseText);
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return this.dimension;
  }
}
