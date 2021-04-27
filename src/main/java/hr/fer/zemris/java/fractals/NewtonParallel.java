package hr.fer.zemris.java.fractals;

import hr.fer.zemris.java.fractals.viewer.FractalViewer;
import hr.fer.zemris.java.fractals.viewer.IFractalProducer;
import hr.fer.zemris.java.fractals.viewer.IFractalResultObserver;
import hr.fer.zemris.math.ComplexRootedPolynomial;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class NewtonParallel {
  public static void main(String[] args) {
    int workers = Runtime.getRuntime().availableProcessors(), tracks = 4;

    String formatMessage = "Program arguments ar invalid. Allowed formats:" +
            "--workers=x or -w x | --tracks=x or -t x";
    int i = 0;
    while (i < args.length) {
      if (args[i].contains("--")) {
        String[] split = args[i].split("=");
        switch (split[0]) {
          case "--workers" -> {
            try {
              workers = Integer.parseInt(split[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
              RootReader.write(formatMessage);
              return;
            }
          }
          case "--tracks" -> {
            try {
              tracks = Integer.parseInt(split[1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
              RootReader.write(formatMessage);
              return;
            }
          }
          default -> {
            RootReader.write("Unknown flag: "+ split[0]);
            return;
          }
        }
        i++;
      } else if (args[i].contains("-")) {
        switch (args[i]) {
          case "-w" -> {
            try {
              workers = Integer.parseInt(args[i + 1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
              RootReader.writeln(formatMessage);
              return;
            }
          }
          case "-t" -> {
            try {
              tracks = Integer.parseInt(args[i + 1]);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException ignore) {
              RootReader.writeln(formatMessage);
              return;
            }
          }
          default -> {
            RootReader.writeln("Unknown flag: "+ args[i]);
            return;
          }
        }
        i += 2;
      }
    }
    ComplexRootedPolynomial crp = RootReader.read();
    RootReader.writeln("Image of fractal will appear shortly. Thank you.");
    FractalViewer.show(new MojProducer(crp, workers, tracks));
  }

  public static class PosaoIzracuna implements Runnable {
    ComplexRootedPolynomial polynomial;
    double reMin;
    double reMax;
    double imMin;
    double imMax;
    int width;
    int height;
    int yMin;
    int yMax;
    int m;
    short[] data;
    AtomicBoolean cancel;
    public static PosaoIzracuna NO_JOB = new PosaoIzracuna();

    private PosaoIzracuna() {

    }

    public PosaoIzracuna(double reMin, double reMax, double imMin,
                         double imMax, int width, int height, int yMin, int yMax,
                         int m, short[] data, AtomicBoolean cancel, ComplexRootedPolynomial polynomial) {
      super();
      this.reMin = reMin;
      this.reMax = reMax;
      this.imMin = imMin;
      this.imMax = imMax;
      this.width = width;
      this.height = height;
      this.yMin = yMin;
      this.yMax = yMax;
      this.m = m;
      this.data = data;
      this.cancel = cancel;
      this.polynomial = polynomial;
    }

    @Override
    public void run() {

      NewtonCalculate.calculate(reMin, reMax, imMin, imMax, width, height, m, yMin, yMax, data, cancel, polynomial);

    }
  }


  public static class MojProducer implements IFractalProducer {
    private final ComplexRootedPolynomial polynomial;
    private final int workers, tracks;

    public MojProducer(ComplexRootedPolynomial polynomial, int workers, int tracks) {
      this.polynomial = polynomial;
      this.workers = workers;
      this.tracks = tracks;
    }

    @Override
    public void produce(double reMin, double reMax, double imMin, double imMax,
                        int width, int height, long requestNo, IFractalResultObserver observer, AtomicBoolean cancel) {
      RootReader.writeln("Zapocinjem izracun...");
      int m = 16 * 16 * 16;
      short[] data = new short[width * height];
      int brojYPoTraci = tracks > height ? 1 : (height / tracks);

      final BlockingQueue<PosaoIzracuna> queue = new LinkedBlockingQueue<>();

      Thread[] radnici = new Thread[workers > 0 ? workers : 4];
      for (int i = 0; i < radnici.length; i++) {
        radnici[i] = new Thread(new Runnable() {
          @Override
          public void run() {
            while (true) {
              PosaoIzracuna p = null;
              try {
                p = queue.take();
                if (p == PosaoIzracuna.NO_JOB) break;
              } catch (InterruptedException e) {
                continue;
              }
              p.run();
            }
          }
        });
      }
      for (Thread thread : radnici) {
        thread.start();
      }

      for (int i = 0; i < tracks; i++) {
        int yMin = i * brojYPoTraci;
        int yMax = (i + 1) * brojYPoTraci - 1;
        if (i == tracks - 1) {
          yMax = height - 1;
        }
        PosaoIzracuna posao = new PosaoIzracuna(reMin, reMax, imMin, imMax, width, height, yMin, yMax, m, data, cancel, polynomial);
        while (true) {
          try {
            queue.put(posao);
            break;
          } catch (InterruptedException ignored) {
          }
        }
      }
      for (int i = 0; i < radnici.length; i++) {
        while (true) {
          try {
            queue.put(PosaoIzracuna.NO_JOB);
            break;
          } catch (InterruptedException ignored) {
          }
        }
      }

      for (Thread thread : radnici) {
        while (true) {
          try {
            thread.join();
            break;
          } catch (InterruptedException ignored) {
          }
        }
      }

      RootReader.writeln("Racunanje gotovo. Idem obavijestiti promatraca tj. GUI!");
      observer.acceptResult(data, (short) (polynomial.toComplexPolynom().order() + 1), requestNo);
    }
  }
}
