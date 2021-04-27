package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexPolynomialTest {

  @Test
  public void deriveTest() {
    ComplexPolynomial poly =
            new ComplexPolynomial(Complex.ONE, new Complex(5, 2), new Complex(1, 2), new Complex(2, 2));
    double expectedIndex = 0;
    ComplexPolynomial expected = new ComplexPolynomial(new Complex(5, 2), new Complex(2, 4), new Complex(6, 6));
    assertEquals(expected, poly.derive());
  }
}
