package hr.fer.zemris.java.gui.calc.buttons;

import hr.fer.zemris.java.gui.calc.model.CalcModel;

import javax.swing.*;
import java.awt.*;
import java.util.function.DoubleUnaryOperator;

public class TwoFuncUnaryOperatorButton extends UnaryOperatorButton implements TwoFuncButton {
  private final DoubleUnaryOperator normal;
  private final DoubleUnaryOperator inverse;
  private final String normalText, inverseText;
  private Dimension dimension;

  public TwoFuncUnaryOperatorButton(String normalText, String inverseText, DoubleUnaryOperator normal, DoubleUnaryOperator inverse, CalcModel model) {
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
