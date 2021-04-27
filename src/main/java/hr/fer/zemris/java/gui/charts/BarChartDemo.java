package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

/**
 *
 */
public class BarChartDemo extends JFrame {
  public BarChartDemo(BarChart chart, String path) {
    super("BarChart");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setMinimumSize(new Dimension(100, 180));
    setLocation(20, 50);
    initGUI(chart, path);
    pack();
  }

  private void initGUI(BarChart chart, String path) {
    JLabel label = new JLabel(path);
    label.setHorizontalAlignment(SwingConstants.CENTER);
    label.setPreferredSize(new Dimension(150, 30));
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(label, BorderLayout.PAGE_START);
    cp.add(new BarChartComponent(chart), BorderLayout.CENTER);
    cp.setPreferredSize(new Dimension(500, 500));
  }

  public static void main(String[] args) {
    if(args.length != 1) {
      System.out.println("Invalid number of arguments provided");
      return;
    }

    Path path = Path.of(args[0]);
    if(!path.toFile().isFile()) {
      System.out.println("Provided path is not a file");
      return;
    }

    try(Scanner scanner = new Scanner(path)) {
      String[] lines = new String[6];
      for(int i = 0 ; i < 6; i++) {
        if(!scanner.hasNextLine())
          throw new IllegalArgumentException("Not enough lines in provided file");

        lines[i] = scanner.nextLine();
      }
      BarChart chart = BarChart.parse(lines);
      SwingUtilities.invokeLater(()->{
        new BarChartDemo(chart, args[0]).setVisible(true);
      });
    } catch (IOException | IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
}
