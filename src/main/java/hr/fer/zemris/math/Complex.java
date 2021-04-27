package hr.fer.zemris.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;
import static java.lang.Double.valueOf;
import static java.lang.Math.*;

/**
 * Omogućava spremanje i rad s kompleksnim brojevima.
 * Interno klasa radi s 4 decimale te se ne preporuča korištenje više decimala.
 * Pri stvaranju instance klase varijable se zaokružuju na 4 decimale.
 * @author Dominik Mandić
 */
public class Complex {

  public static final Complex ZERO = new Complex(0,0);
  public static final Complex ONE = new Complex(1,0);
  public static final Complex ONE_NEG = new Complex(-1,0);
  public static final Complex IM = new Complex(0,1);
  public static final Complex IM_NEG = new Complex(0,-1);

  /**
   * Realni dio kompleksnog broja
   */
  private final double re;

  /**
   * Imaginarni dio kompleksnog broja
   */
  private final double im;

  /**
   * Default konstruktor.
   * @param re realni dio kompleksno broja
   * @param im imaginarni dio kompleksnog broja
   */
  public Complex(double re, double im) {
    this.re = re;
    this.im = im;
  }

  /**
   * Stvara novu instancu razreda <code>ComplexNumber</code> s poslanim kompleksnim brojem u polarnim koordinatama
   * @param magnitude magnituda kompleksnog broja u polarnim koordinatama
   * @param angle kut kompleksnog broja u polarnim koordinatama. Kut se svodi na granice od 0 do 2*PI
   * @return novostvorenu instancu razreda
   */
  private static Complex fromMagnitudeAndAngle(double magnitude, double angle) {
    double normalizedAngle = normalizeAngle(angle);
    double real = magnitude * cos(normalizedAngle), imaginary = magnitude * sin(normalizedAngle);
    return new Complex(real, imaginary);
  }

  /**
   * Stvara novu instancu razreda <code>ComplexNumber</code> s poslanim <code>String</code> iz kojeg se parsirau realni i kompleksni dio
   * @param s <code>String</code> iz kojeg se parsiraju realni i kompleksni dio
   * @return novostvorenu instancu razreda
   * @throws NullPointerException ako je poslan <code>null</code>
   * @throws IllegalArgumentException ako poslan prazan <code>String</code>
   * @throws NumberFormatException ako se unutar izraza nalaze praznine (praznine na početku i nakraju su uredu) te znak koji nije broj, +, - ili .
   */
  public static Complex parse(String s) {
    if(s == null)
      throw new NullPointerException("Can't parse null!");

    char[] split = s.trim().replaceAll("\\s+", "").toCharArray();
    checkValid(split);

    StringBuilder builder = new StringBuilder();
    Double real, imaginary;
    int index;
    if(split[0] == '+' || split[0] == '-') {
      builder.append(split[0]);
      index = 1;
    } else index = 0;

    if(split[index] == 'i') {
      for(index++; index < split.length && (Character.isDigit(split[index]) || split[index] == '.'); index++) {
        builder.append(split[index]);
      }
      try {
        real = 0.0;
        String temp = builder.toString();
        if(temp.equals("-") || temp.equals("+")) builder.append("1");
        imaginary = parseDouble(temp.equals("") ? "1" : builder.toString());
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Wrong format of imaginary part: " + builder.toString());
      }
      if(index < split.length) throw new IllegalArgumentException("Imaginary part must be the last part of sequence");

    } else if(Character.isDigit(split[index])) {
      for(; index < split.length && (Character.isDigit(split[index]) || split[index] == '.'); index++) {
        builder.append(split[index]);
      }
      try {
        real = parseDouble(builder.toString());
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Wrong format of real part: " + builder.toString());
      }

      if(index < split.length) {
        if(split[index] == 'i') throw new IllegalArgumentException("Invalid complex number format. Valid format is \"a + ib\"");
        if(split[index] != '+' && split[index] != '-')
          throw new IllegalArgumentException("+ or - expected after real part: "+ split[index] +" was found.");

        builder.delete(0, builder.length());
        builder.append(split[index++]);
        if(index < split.length && split[index] != 'i') throw new IllegalArgumentException("Imaginary i was expected");
        index++;
        do {
          builder.append(split[index++]);
        } while(index < split.length);

        try {
          String temp = builder.toString();
          if(temp.equals("-") || temp.equals("+")) builder.append("1");
          imaginary = parseDouble(temp.equals("") ? "1" : builder.toString());
        } catch (NumberFormatException ex) {
          throw new IllegalArgumentException("Wrong format of imaginary part: " + builder.toString());
        }
      } else imaginary = 0.0;

    } else throw new IllegalArgumentException("Invalid character at "+ index +": "+ split[index]);

    return new Complex(real, imaginary);
  }

  /**
   * Služi za internu provjeru valjanosti poslanog <code>String</code>a
   * @param array vrijednost koje se provjeravaju
   * @throws IllegalArgumentException ukoliko je vrijednost neispravna
   */
  private static void checkValid(char[] array) {
    for(char c : array) {
      if (!Character.isDigit(c) && c != '+' && c != '-' && c != '.' && c != 'i')
        throw new IllegalArgumentException("Invalid sequence: "+ c + " is not an allowed character!");
    }
  }

  /**
   * poslani kut svodi na pozitivne granice od 0 do 2*PI te ga zaokružuje na četiri decimale
   * @param angle kut koji treba normalizirati
   * @return normaliziranu poslanu vrijednost
   */
  private static double normalizeAngle(double angle) {
    double newAngle = angle % (2 * PI);
    if(newAngle < 0)
      newAngle = angle + 2*PI;
    return newAngle;
  }

  /**
   * Vraća realni dio kompleksnog broja
   * @return realni dio kompleksnog broja
   */
  public double getReal() { return this.re; }

  /**
   * Vraća imaginarni dio kompleksnog broja
   * @return imaginarni dio kompleksnog broja
   */
  public double getImaginary() { return this.im; }

  /**
   * Vraća magnitudu (polarnu kordinatu) kompleksnog broja
   * @return magnitudu kompleksnog broja
   */
  public double module() {
    return hypot(this.re, this.im);
  }

  /**
   * Vraća kut (polarnu kordinatu) kompleksnog broja
   * @return kut kompleksnog broja
   */
//   private double angle() {
//    double result;
//    if(re == 0) {
//      if (im > 0) result = PI / 2;
//      else result = -PI / 2;
//    } else if(re < 0) result = atan(this.im / this.re) + PI ;
//    else result = atan(this.im / this.re);
//
//    return normalizeAngle(result);
//  }
  private double angle() {
    double angle = Math.atan2(im, re);

    if (angle < 0)
      angle = 2 * Math.PI + angle;

    return angle;
  }

  /**
   * Zbraja dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param c kompleksni broj s kojim se zbraja
   * @return novostvorenu instancu razreda
   * @throws NullPointerException ako je poslan <code>null</code>
   */
  public Complex add(Complex c) {
    if(c == null)
      throw new NullPointerException("Null was sent. Can't add with null!");
    return new Complex(this.re + c.re, this.im + c.im);
  }

  /**
   * Oduzima dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param c kompleksni broj kojeg treba oduzeti
   * @return novostvorenu instancu razreda
   * @throws NullPointerException ako je poslan <code>null</code>
   */
  public Complex sub(Complex c) {
    if(c == null)
      throw new NullPointerException("Null was sent. Can't subtract with null!");
    return new Complex(this.re - c.re, this.im - c.im);
  }

  /**
   * Množi dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param c kompleksni broj s kojimse treba pomnožiti
   * @return novostvorenu instancu razreda
   * @throws NullPointerException ako je poslan <code>null</code>
   */
  public Complex multiply(Complex c) {
    if(c == null)
      throw new NullPointerException("Null was sent. Can't multiply with null!");
    double newReal = this.re * c.re - this.im * c.im;
    double newImaginary = this.im * c.re + this.re * c.im;

    return new Complex(newReal, newImaginary);
  }

  /**
   * Dijeli dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param c kompleksni broj s kojim se treba podijeliti (djelitelj)
   * @return novostvorenu instancu razreda
   * @throws NullPointerException ako je poslan <code>null</code>
   */
  public Complex divide(Complex c) {
    if(c == null)
      throw new NullPointerException("Null was sent. Can't divide with null!");
    if(c.re == 0 && c.im == 0)
      throw new ArithmeticException("Can't divide by zero!");
    double newMagnitude = this.module() / c.module();
    double newAngle = this.angle() - c.angle();

    return fromMagnitudeAndAngle(newMagnitude, newAngle);
  }

  /**
   * Potencira kompleksni broj i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param n eksponenst
   * @return novostvorenu instancu razreda
   * @throws IllegalArgumentException ako je poslani broj manji od 0
   */
  public Complex power(int n) {
    if(n < 0)
      throw new IllegalArgumentException("n must be larger or equal then 0");
    if(n == 1)
      return new Complex(this.re, this.im);

    double newMagnitude = pow(this.module(), n);
    double newAngle = n * this.angle();

    return fromMagnitudeAndAngle(newMagnitude, newAngle);
  }

  /**
   * Korjenuje kompleksni broj i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
   * @param n potencija korjena
   * @return novostvorenu instancu razreda
   * @throws IllegalArgumentException ako je poslani broj manji od 1
   */
  public List<Complex> root(int n) {
    if(n < 1)
      throw new IllegalArgumentException("n must be larger then 0");

    double newMagnitude = Math.pow(this.module(), 1.0/n);
    double angle = this.angle();
    List<Complex> result = new ArrayList<>();
    for(int k = 0; k < n; k++) {
      double newAngle = (angle + 2*k*PI)/n;
      result.add(fromMagnitudeAndAngle(newMagnitude, newAngle));
    }
    return result;
  }

  public Complex negate() {
    return new Complex(-re, -im);
  }

  /**
   * Zapisuje kompleksni broj u <code>String</code>
   * @return zapis kompleksnog broja u <code>String</code>u
   */
  @Override
  public String toString() {
    if(im < 0) return String.format("%s-i%s", (double) round(this.re * 10) / 10,(double) abs(round(this.im * 10 / 10)));
    return String.format("%s+i%s", (double) round(this.re * 10) / 10,(double) round(this.im * 10 / 10));
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Complex complex = (Complex) o;
    return Double.compare(complex.re, re) == 0 &&
            Double.compare(complex.im, im) == 0;
  }

  @Override
  public int hashCode() {
    return Objects.hash(re, im);
  }
}
