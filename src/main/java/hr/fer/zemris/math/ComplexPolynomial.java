package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.List;

public class ComplexPolynomial {

  private List<Complex> factors;  // z0, z1, z2, ...

  public ComplexPolynomial(Complex ...factors) {
    if (factors == null)
      throw new NullPointerException("Can't create an object with null argument");
    Arrays.stream(factors).forEach(f -> {
      if(f == null) throw new IllegalArgumentException("One of the factors was null.");
    });

    this.factors = Arrays.asList(factors);
  }

  // returns order of this polynom; eg. For (7+2i)z^3+2z^2+5z+1 returns 3
  public short order() {
    return (short) (factors.size() - 1);
  }

  // computes a new polynomial this*p
  public ComplexPolynomial multiply(ComplexPolynomial p) {
    if(p == null)
      throw new NullPointerException("Null was sent. Can't multiply with null!");

    Complex[] array = new Complex[this.order() + p.order() + 1];
    Arrays.fill(array, Complex.ZERO);
    for(int thisI = 0; thisI < this.factors.size(); thisI++) {
      for(int pI = 0; pI < p.factors.size(); pI++) {
        array[thisI+pI] = array[thisI+pI].add(factors.get(thisI).multiply(p.factors.get(pI)));
      }
    }
    return new ComplexPolynomial(array);
  }

  // computes first derivative of this polynomial; for example, for
  // (7+2i)z^3+2z^2+5z+1 returns (21+6i)z^2+4z+5
  public ComplexPolynomial derive() {
    Complex[] array = new Complex[this.factors.size()-1];
    for(int i = 1; i < factors.size(); i++) {
      array[i-1] = factors.get(i).multiply(new Complex(i, 0));
    }
    return new ComplexPolynomial(array);
  }

  // computes polynomial value at given point z
  public Complex apply(Complex z) {
    if(z == null)
      throw new NullPointerException("Null was sent. Can't apply with null!");

    Complex result = factors.get(0);
    for(int i = 1; i < factors.size(); i++) {
      result = result.add(z.power(i).multiply(factors.get(i)));
    }

    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    for(int i = factors.size()-1; i > 0; i--) {
      builder.append("(").append(factors.get(i)).append(")*z^").append(i).append("+");
    }
    builder.append(factors.get(0));

    return builder.toString();
  }
}
