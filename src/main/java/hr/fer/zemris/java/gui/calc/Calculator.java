package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.buttons.*;
import hr.fer.zemris.java.gui.layouts.CalcLayout;
import hr.fer.zemris.java.gui.layouts.RCPosition;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Calculator extends JFrame {
  private CalculatorModel model;
  private Stack<Double> stack;
  private boolean inverted;
  private List<TwoFuncButton> twoFuncButtons;


  public Calculator() {
    model = new CalculatorModel();
    stack = new Stack<>();
    inverted = false;
    twoFuncButtons = new ArrayList<>();
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    initGUI();
    pack();
  }

  private void initGUI() {
    Container cp = getContentPane();
    cp.setLayout(new CalcLayout(3));

    cp.add(createScreen(), new RCPosition(1, 1));
    addInputButtons(cp);
    addBinaryOperatorButtons(cp);
    addUnaryOperatorButtons(cp);
    addFuncButtons(cp);
  }

  private void addInputButtons(Container cp) {
    cp.add(new FuncButton(".", e -> model.insertDecimalPoint()), new RCPosition(5, 5));
    cp.add(new FuncButton("+/-", e -> model.swapSign()), new RCPosition(5, 4));
    cp.add(new NumberButton(0, model), new RCPosition(5, 3));
    cp.add(new NumberButton(1, model), new RCPosition(4, 3));
    cp.add(new NumberButton(2, model), new RCPosition(4, 4));
    cp.add(new NumberButton(3, model), new RCPosition(4, 5));
    cp.add(new NumberButton(4, model), new RCPosition(3, 3));
    cp.add(new NumberButton(5, model), new RCPosition(3, 4));
    cp.add(new NumberButton(6, model), new RCPosition(3, 5));
    cp.add(new NumberButton(7, model), new RCPosition(2, 3));
    cp.add(new NumberButton(8, model), new RCPosition(2, 4));
    cp.add(new NumberButton(9, model), new RCPosition(2, 5));
  }

  private void addBinaryOperatorButtons(Container cp) {
    TwoFuncBinaryOperatorButton button = new TwoFuncBinaryOperatorButton("x^n", "x^(1/n)", Operations.POWER, Operations.ROOT, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(5, 1));
    cp.add(new BinaryOperatorButton("/", Operations.DIVIDE, model), new RCPosition(2, 6));
    cp.add(new BinaryOperatorButton("*", Operations.MULTIPLY, model), new RCPosition(3, 6));
    cp.add(new BinaryOperatorButton("-", Operations.SUBTRACT, model), new RCPosition(4, 6));
    cp.add(new BinaryOperatorButton("+", Operations.ADD, model), new RCPosition(5, 6));
  }

  private void addUnaryOperatorButtons(Container cp) {
    cp.add(new UnaryOperatorButton("1/x", Operations.INVERT, model), new RCPosition(2, 1));

    TwoFuncUnaryOperatorButton button = new TwoFuncUnaryOperatorButton("sin", "arcsin", Operations.SIN, Operations.ASIN, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(2, 2));

    button = new TwoFuncUnaryOperatorButton("log", "10^x", Operations.LOG, Operations.LOG_INVERSE, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(3, 1));

    button = new TwoFuncUnaryOperatorButton("cos", "arccos", Operations.COS, Operations.ACOS, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(3, 2));

    button = new TwoFuncUnaryOperatorButton("ln", "e^x", Operations.LN, Operations.LN_INVERSE, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(4, 1));

    button = new TwoFuncUnaryOperatorButton("tan", "arctan", Operations.TAN, Operations.ATAN, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(4, 2));

    button = new TwoFuncUnaryOperatorButton("ctg", "arcctg", Operations.CTG, Operations.ACTG, model);
    twoFuncButtons.add(button);
    cp.add(button, new RCPosition(5, 2));
  }

  private void addFuncButtons(Container cp) {
    cp.add(new FuncButton("=", e -> {
      double result = model.getPendingBinaryOperation().applyAsDouble(model.getCurrOperand(), model.getValue());
      model.setValue(result);
    }), new RCPosition(1, 6));
    cp.add(new FuncButton("clr", e -> model.clear()), new RCPosition(1, 7));
    cp.add(new FuncButton("reset", e -> model.clearAll()), new RCPosition(2, 7));
    cp.add(new FuncButton("push", e -> stack.push(model.getValue())), new RCPosition(3, 7));
    cp.add(new FuncButton("pop", e -> model.setValue(stack.pop())), new RCPosition(4, 7));
    cp.add(createCheckbox("Inv"), new RCPosition(5, 7));
  }

  private JCheckBox createCheckbox(String text) {
    JCheckBox checkbox = new JCheckBox(text);
    checkbox.setBackground(Color.darkGray);
    checkbox.addActionListener(e -> notifyButtons());
    checkbox.setForeground(Color.white);
    checkbox.setOpaque(true);
    return checkbox;
  }

  private void notifyButtons() {
    twoFuncButtons.forEach(TwoFuncButton::swap);
  }

  private JLabel createScreen() {
    JLabel label = new JLabel("", SwingConstants.RIGHT);
    label.setBackground(Color.yellow);
    model.addCalcValueListener(calcModel -> {
      label.setText(calcModel.toString());
    });
    label.setOpaque(true);
    return label;
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(()->{
      new Calculator().setVisible(true);
    });
  }
}
