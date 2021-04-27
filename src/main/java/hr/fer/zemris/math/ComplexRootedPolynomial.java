package hr.fer.zemris.math;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.*;

public class ComplexRootedPolynomial {
  private Complex constant;  // z0
  private List<Complex> roots;  //  z1, z2, z3 ....

  public ComplexRootedPolynomial(Complex constant, Complex ... roots) {
    if (constant == null || roots == null)
      throw new NullPointerException("Can't create an object with null arguemnts");
    this.constant = constant;
    this.roots = Arrays.asList(roots);
  }

  // computes polynomial value at given point z
  public Complex apply(Complex z) {
    if(z == null)
      throw new NullPointerException("Null was sent. Can't apply with null!");

    Complex result = constant;
    for(Complex c : roots) {
      result = result.multiply(z.sub(c));
    }

    return result;
  }
  // converts this representation to ComplexPolynomial type
  public ComplexPolynomial toComplexPolynom() {
    ComplexPolynomial result = new ComplexPolynomial(constant);
    for(Complex c : roots) {
      result = result.multiply(new ComplexPolynomial(c.negate(), Complex.ONE));
    }
    return result;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();

    builder.append(constant);
    for(int i = 0; i < roots.size(); i++) {
      builder.append("*(z-(").append(roots.get(i)).append("))");
    }

    return builder.toString();
  }

  // finds index of closest root for given complex number z that is within
  // treshold; if there is no such root, returns -1
  // first root has index 0, second index 1, etc
  public int indexOfClosestRootFor(Complex z, double treshold) {
    if (z == null)
      throw new NullPointerException("Can't apply with null!");

    int closest = -1;
    double closestDistance = treshold + 0.001;
    for (int i = 0; i < roots.size(); i++) {
      double distance = z.sub(roots.get(i)).module();
      if (distance < closestDistance) {
        closest = i;
        closestDistance = distance;
      }
    }
    return closest;
  }
}

