package hr.fer.zemris.java.gui.calc;

import hr.fer.zemris.java.gui.calc.model.CalcModel;
import hr.fer.zemris.java.gui.calc.model.CalcValueListener;
import hr.fer.zemris.java.gui.calc.model.CalculatorInputException;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Stack;
import java.util.function.DoubleBinaryOperator;

public class CalculatorModel implements CalcModel {
  private List<CalcValueListener> listeners;
  private boolean editable;
  private boolean negativeNum;
  private String currInput, frozenValue;
  private OptionalDouble activeOperand;
  private double currOperand;
  private DoubleBinaryOperator pendingOperation;
  private Stack<Double> stack;

  public CalculatorModel() {
    this.listeners = new ArrayList<>();
    stack = new Stack<>();
    freshStart();
  }

  private void freshStart() {
    editable = true;
    negativeNum = false;
    currInput = "";
    currOperand = 0.0;
    activeOperand = OptionalDouble.empty();
    pendingOperation = null;
    stack.empty();
  }

  @Override
  public void addCalcValueListener(CalcValueListener l) {
    listeners.add(l);
    l.valueChanged(this);
  }

  @Override
  public void removeCalcValueListener(CalcValueListener l) {
    int index = listeners.indexOf(l);
    if(index != -1) listeners.remove(index);
  }

  @Override
  public double getValue() {
    return negativeNum ? -currOperand : currOperand;
  }

  private void notifyListeners() {
    for(CalcValueListener listener : listeners) {
      listener.valueChanged(this);
    }
  }

  @Override
  public void setValue(double value) {
    if(Double.isNaN(value)) {
      currInput = "NaN";
    } else if(Double.isFinite(value)) {
      currInput = Double.toString(value);
    } else {
      if(value == Double.NEGATIVE_INFINITY) {
        currInput = "-Infinity";
      } else {
        currInput = "Infinity";
      }
    }
    currOperand = value;
    editable = false;
    negativeNum = false;
    frozenValue = null;
    notifyListeners();
  }

  @Override
  public boolean isEditable() {
    return editable;
  }

  @Override
  public void clear() {
    if(!isEditable()) {
      clearAll();
      return;
    }
    currInput = "";
    negativeNum = false;
    notifyListeners();
  }

  @Override
  public String toString() {
    if(frozenValue != null) return frozenValue;
    else if(currInput.equals("")) return negativeNum ? "-0" : "0";
    else return negativeNum ? ("-"+currInput) : currInput;
  }

  @Override
  public void clearAll() {
    freshStart();
    notifyListeners();
  }

  @Override
  public void swapSign() throws CalculatorInputException {
    if(!isEditable())
      throw new CalculatorInputException("Model is not editable");

    negativeNum = !negativeNum;
    notifyListeners();
  }

  @Override
  public void insertDecimalPoint() throws CalculatorInputException {
    if(!isEditable())
      throw new CalculatorInputException("Model is not editable");

    if(currInput.contains("."))
      throw new CalculatorInputException("Decimal point already exists");

    if(currInput.equals(""))
      throw new CalculatorInputException("No number was added before a decimal point");

    currInput += ".";
    notifyListeners();
  }

  @Override
  public void insertDigit(int digit) throws CalculatorInputException, IllegalArgumentException {
    if(!isEditable())
      throw new CalculatorInputException("Model is not editable");

    String newCurrInput;
    if(currInput.equals("0")) {
      if(digit == 0) return;
      else newCurrInput = Integer.toString(digit);
    } else  newCurrInput = currInput + digit;

    if(newCurrInput.length() > 308)
      throw new CalculatorInputException("Number is to big");

    try {
      currOperand = Double.parseDouble(newCurrInput);
      currInput = newCurrInput;
    } catch (NumberFormatException ex) {
      throw new CalculatorInputException(ex.getMessage());
    }
    if(frozenValue != null)
      frozenValue = null;

    notifyListeners();
  }

  @Override
  public boolean isActiveOperandSet() {
    return activeOperand.isPresent();
  }

  @Override
  public double getCurrOperand() throws IllegalStateException {
    if(activeOperand.isEmpty())
      throw new IllegalStateException("Active operand wasn't set");

    return activeOperand.getAsDouble();
  }

  @Override
  public void setCurrOperand(double currOperand) {
    activeOperand = OptionalDouble.of(currOperand);
    notifyListeners();
  }

  @Override
  public void clearActiveOperand() {
    activeOperand = OptionalDouble.empty();
    notifyListeners();
  }

  @Override
  public DoubleBinaryOperator getPendingBinaryOperation() {
    return pendingOperation;
  }

  @Override
  public void setPendingBinaryOperation(DoubleBinaryOperator op) {
    if(currInput.equals(""))
      throw new CalculatorInputException("First operand wasn't added");

    if(pendingOperation != null) {
      double result = pendingOperation.applyAsDouble(activeOperand.getAsDouble(), currOperand);
      activeOperand = OptionalDouble.of(negativeNum ? -result : result);
      frozenValue = Double.toString(activeOperand.getAsDouble());
    } else {
      activeOperand = OptionalDouble.of(negativeNum ? -currOperand : currOperand);
      frozenValue = negativeNum ? "-"+currInput : currInput;
    }

    clear();
    editable = true;
    pendingOperation = op;
    notifyListeners();
  }
}
