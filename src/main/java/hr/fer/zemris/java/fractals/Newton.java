package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.Complex;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.atomic.AtomicBoolean;

public class Newton {
  public static void main(String[] args) {
    ComplexRootedPolynomial crp = RootReader.read();
    System.out.println("Image of fractal will appear shortly. Thank you.");
    FractalViewer.show(new MojProducer(crp));
  }

  public static class MojProducer implements IFractalProducer {
    private final ComplexRootedPolynomial polynomial;

    public MojProducer(ComplexRootedPolynomial polynomial) {
      this.polynomial = polynomial;
    }

    @Override
    public void produce(double reMin, double reMax, double imMin, double imMax,
                        int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {

      int maxIterations = 16 * 16 * 16;
      double convergenceThreshold = 0.001;
      double rootThreshold = 0.002;

      int offset = 0;
      short[] data = new short[width * height];

      for (int y = 0; y < height; y++) {
        if (cancel.get()) break;
        for (int x = 0; x < width; x++) {
          double re = x / (width - 1.0) * (reMax - reMin) + reMin;
          double im = (height - 1.0 - y) * (imMax - imMin) / (height - 1.0) + imMin;
          Complex znOld, zn = new Complex(re, im);
          int iters = 0;
          double module;
          do {
            znOld = zn;
            zn = zn.sub(polynomial.apply(zn).divide(polynomial.toComplexPolynom().derive().apply(zn)));
            module = znOld.sub(zn).module();
            iters++;
          } while (module > convergenceThreshold && iters < maxIterations);

          data[offset++] = (short) (polynomial.indexOfClosestRootFor(zn, rootThreshold) + 1);
        }
      }
      observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
    }
  }
}
