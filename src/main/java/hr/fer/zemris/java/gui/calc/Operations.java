package hr.fer.zemris.java.gui.calc;

import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleUnaryOperator;

/**
 * Class containing all of the operations that the calculator uses
 */
public class Operations {
  public static DoubleBinaryOperator ADD = (o1, o2) -> o1 + o1;
  public static DoubleBinaryOperator SUBTRACT = (o1,o2) -> o1 - o2;
  public static DoubleBinaryOperator MULTIPLY = (o1,o2) -> o1*o2;
  public static DoubleBinaryOperator DIVIDE = (o1, o2) -> o1/o2;
  public static DoubleBinaryOperator POWER = Math::pow;
  public static DoubleBinaryOperator ROOT = (o1, o2) -> Math.pow(o1, 1/o2);
  public static DoubleUnaryOperator INVERT = o1 -> 1/o1;
  public static DoubleUnaryOperator LOG = Math::log10;
  public static DoubleUnaryOperator LOG_INVERSE = o1 -> Math.pow(10, o1);
  public static DoubleUnaryOperator LN = Math::log;
  public static DoubleUnaryOperator LN_INVERSE = o1 -> Math.pow(Math.E, o1);
  public static DoubleUnaryOperator SIN = Math::sin;
  public static DoubleUnaryOperator ASIN = Math::asin;
  public static DoubleUnaryOperator COS = Math::cos;
  public static DoubleUnaryOperator ACOS = Math::acos;
  public static DoubleUnaryOperator TAN = Math::tan;
  public static DoubleUnaryOperator ATAN = Math::atan;
  public static DoubleUnaryOperator CTG = o1 -> 1/Math.tan(o1);
  public static DoubleUnaryOperator ACTG = o1 -> Math.atan(1/o1);
}
