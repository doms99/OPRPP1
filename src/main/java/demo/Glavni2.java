package demo;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilderProvider;
import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

public class Glavni2 {
    public static void main(String[] args) {
        LSystemViewer.showLSystem(createKochCurve2(LSystemBuilderImpl::new));
    }

    private static LSystem createHilbertCurve(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.05 0.05\n" +
                "angle                  0\n" +
                "unitLength             0.9\n" +
                "unitLengthDegreeScaler 1.0 / 2.0\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 90\n" +
                "command - rotate -90\n" +
                "\n" +
                "axiom L\n" +
                "\n" +
                "production L +RF-LFL-FR+\n" +
                "production R -LF+RFR+FL-").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createKochCurve(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.05 0.4\n" +
                "angle                  0\n" +
                "unitLength             0.9\n" +
                "unitLengthDegreeScaler 1.0 / 3.0\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 60\n" +
                "command - rotate -60\n" +
                "\n" +
                "axiom F\n" +
                "\n" +
                "production F F+F--F+F").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createKochCurve2(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.3 0.75\n" +
                "angle                  0\n" +
                "unitLength             0.45\n" +
                "unitLengthDegreeScaler 1.0 / 3.0\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 90\n" +
                "command - rotate -90\n" +
                "\n" +
                "axiom F-F-F-F\n" +
                "\n" +
                "production F FF-F-F-F-FF").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createKochIsland(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.3 0.75\n" +
                "angle                  0\n" +
                "unitLength             0.45\n" +
                "unitLengthDegreeScaler 1.0 / 4.0\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 90\n" +
                "command - rotate -90\n" +
                "\n" +
                "axiom F-F-F-F\n" +
                "\n" +
                "production F F-F+F+FF-F-F+F").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createPlant1(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.5 0.0\n" +
                "angle                  90\n" +
                "unitLength             0.1\n" +
                "unitLengthDegreeScaler 1.0 /2.05\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 25.7\n" +
                "command - rotate -25.7\n" +
                "command [ push\n" +
                "command ] pop\n" +
                "command G color 00FF00\n" +
                "\n" +
                "axiom GF\n" +
                "\n" +
                "production F F[+F]F[-F]F").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createPlant2(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.5 0.0\n" +
                "angle                  90\n" +
                "unitLength             0.3\n" +
                "unitLengthDegreeScaler 1.0 /2.05\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 25\n" +
                "command - rotate -25\n" +
                "command [ push\n" +
                "command ] pop\n" +
                "command G color 00FF00\n" +
                "\n" +
                "axiom GF\n" +
                "\n" +
                "production F FF+[+F-F-F]-[-F+F+F]").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createPlant3(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.5 0.0\n" +
                "angle                  90\n" +
                "unitLength             0.5\n" +
                "unitLengthDegreeScaler 1.0 /2.05\n" +
                "\n" +
                "command F draw 1\n" +
                "command + rotate 20\n" +
                "command - rotate -20\n" +
                "command [ push\n" +
                "command ] pop\n" +
                "command G color 00FF00\n" +
                "\n" +
                "axiom GB\n" +
                "\n" +
                "production B F[+B]F[-B]+B\n" +
                "production F FF").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }

    private static LSystem createSierpinskiGasket(LSystemBuilderProvider provider) {
        String[] split = ("origin                 0.15 0.6\n" +
                "angle                  0\n" +
                "unitLength             0.5\n" +
                "unitLengthDegreeScaler 1.0 / 2.2\n" +
                "\n" +
                "command R draw 1\n" +
                "command L draw 1\n" +
                "command + rotate 60\n" +
                "command - rotate -60\n" +
                "\n" +
                "axiom R\n" +
                "\n" +
                "production R L-R-L\n" +
                "production L R+L+R").split("\n");
        return provider.createLSystemBuilder().configureFromText(split).build();
    }
}
