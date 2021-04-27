package hr.fer.zemris.java.fractals;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RootReader {
  public static ComplexRootedPolynomial read() {
    RootReader.writeln("Welcome to Newton-Raphson iteration-based fractal viewer.\n" +
            "Please enter at least two roots, one root per line. Enter 'done' when done.");
    Scanner scan = new Scanner(System.in);
    List<Complex> list = new ArrayList<>();
    int i = 1;
    int min = 2;
    while(true) {
      RootReader.write("Root "+ i +"> ");
      String read = scan.nextLine().trim().toLowerCase();
      if(read.equals("done")) {
        if(i <= min) {
          RootReader.writeln("Minimal number of roots is 2.");
          continue;
        }
        break;
      }
      else if(read.equals("")) continue;
      Complex root = Complex.parse(read);
      if(list.contains(root)) {
        RootReader.writeln("That root was already added.");
        continue;
      }
      try {
        list.add(root);
        i++;
      } catch(IllegalArgumentException ex) {
        RootReader.write(ex.getMessage());
      }
    }
    list.stream().map(Complex::toString).forEach(RootReader::write);
    return new ComplexRootedPolynomial(Complex.ONE, list.toArray(new Complex[] {}));
  }

  public static void write(String message) {
    System.out.print(message);
  }

  public static void writeln(String message) {
    RootReader.write(message+"\n");
  }
}
