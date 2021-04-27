package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonCalculate {

  public NewtonCalculate() {
  }

  public static void showSequential(ComplexRootedPolynomial polynomial) {
    FractalViewer.show(getSequentialFractalProducer(polynomial));
  }

  public static void calculate(double reMin, double reMax, double imMin, double imMax,
                               int width, int height, int maxIterations, int ymin, int ymax, short[] data,
                               AtomicBoolean cancel, ComplexRootedPolynomial polynomial) {
    int offset = ymin * width;
    double convergenceThreshold = 0.001;
    double rootThreshold = 0.002;

    for(int y = ymin; y <= ymax && !cancel.get(); ++y) {
      for(int x = 0; x < width; ++x) {
        double re = x / (width - 1.0) * (reMax - reMin) + reMin;
        double im = (height - 1.0 - y) * (imMax - imMin) / (height - 1.0) + imMin;
        Complex znOld, zn = new Complex(re, im);
        int iters = 0;

        do {
          znOld = zn;
          zn = zn.sub(polynomial.apply(zn).divide(polynomial.toComplexPolynom().derive().apply(zn)));
          iters++;
        } while (znOld.sub(zn).module() > convergenceThreshold && iters < maxIterations);

        data[offset++] = (short) (polynomial.indexOfClosestRootFor(zn, rootThreshold) + 1);
      }
    }

  }

  private static IFractalProducer getSequentialFractalProducer(ComplexRootedPolynomial polynomial) {
    return new IFractalProducer() {
      public void produce(double reMin, double reMax, double imMin, double imMax, int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
        System.out.println("Započinjem izračune...");
        int m = 4096;
        short[] data = new short[width * height];
        NewtonCalculate.calculate(reMin, reMax, imMin, imMax, width, height, m, 0, height - 1, data, cancel, polynomial);
        System.out.println("Izračuni gotovi...");
        observer.acceptResult(data, (short)m, requestNo);
        System.out.println("Dojava gotova...");
      }
    };
  }
}
