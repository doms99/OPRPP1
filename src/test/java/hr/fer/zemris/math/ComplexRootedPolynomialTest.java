package hr.fer.zemris.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComplexRootedPolynomialTest {

  @Test
  public void indexOfClosestRootForTest() {
    ComplexRootedPolynomial rooted =
            new ComplexRootedPolynomial(Complex.ONE, Complex.ONE, Complex.ONE.negate(), Complex.IM, Complex.IM_NEG);
    double expectedIndex = 0;
    Complex z = new Complex(1, 1);
    assertEquals(expectedIndex, rooted.indexOfClosestRootFor(z, 10));
  }


}
