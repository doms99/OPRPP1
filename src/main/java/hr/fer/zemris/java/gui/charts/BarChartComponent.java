package hr.fer.zemris.java.gui.charts;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Component that presents BarChart and it's in charge for it's painting.
 */
public class BarChartComponent extends JComponent {

  /**
   * <code>BarChart</code> containing values for painting.
   */
  private BarChart chart;

  /**
   * Default spacing unit.
   */
  private static int GAP = 15;

  public BarChartComponent(BarChart chart) {
    if(chart == null)
      throw new NullPointerException("chart can't be null");

    this.chart = chart;
    this.setVisible(true);
    this.setOpaque(true);
  }

  /**
   * Paints the component along with the axis, labels and values.
   * @param g <code>Graphic</code> for painting
   */
  public void paint(Graphics g) {
    super.paint(g);

    g.setFont(new Font("Calibri", Font.PLAIN, 16));

    Graphics2D g2d = (Graphics2D) g;
    AffineTransform defaultAt = g2d.getTransform();

    // Drawing axis labels
    AffineTransform at = new AffineTransform();
    at.rotate(-Math.PI / 2);
    g2d.setTransform(at);

    String text = chart.getYAxis();
    int yPos = this.getHeight() / 2 + g.getFontMetrics().stringWidth(text) / 2;
    g2d.drawString(text, -yPos, GAP + g.getFontMetrics().getAscent());

    g2d.setTransform(defaultAt);
    text = chart.getXAxis();
    int xPos = this.getWidth() / 2 - g.getFontMetrics().stringWidth(text) / 2;
    yPos = this.getHeight() - GAP;
    g2d.drawString(text, xPos, yPos);

    // Drawing axis tags
    int height = this.getHeight();  // Component height
    int width = this.getWidth();    // Component width
    int offset = 2 * GAP + g.getFontMetrics().getHeight();  // Space that the axis labels occupy

    g.setFont(new Font("Calibri", Font.BOLD, 12));

    FontMetrics metrics = g.getFontMetrics();
    int fontHeight = metrics.getAscent();

    int hardLeft = offset + metrics.stringWidth(Integer.toString(chart.getMaxY())) + GAP;
    int hardBottom = height - (offset + fontHeight + GAP);
    int hardRight = width - GAP / 2;
    int hardTop = GAP / 2;

    int gridRight = hardRight - GAP/2;
    int gridTop = hardTop + GAP/2;

    int tagMaxWidth = metrics.stringWidth(Integer.toString(chart.getMaxY()));

    int yNumOfValues = (chart.getMaxY() - chart.getMinY()) / chart.getStep();
    int yStart = hardBottom + fontHeight / 2;
    int yStep = (hardBottom - gridTop - fontHeight) / yNumOfValues;
    for (int i = 0; i <= yNumOfValues; i++) {
      g2d.setColor(Color.BLACK);
      g2d.drawString(Integer.toString(chart.getMinY() + i * chart.getStep()), offset, yStart - i * yStep);
      int y = hardBottom - i * yStep;
      if(i == 0) continue;
      g2d.setColor(Color.ORANGE);
      g2d.drawLine(offset + tagMaxWidth + GAP / 2, y, hardRight, y);
    }


    int numOfXs = chart.getValueSize();
    int xStep = (gridRight - hardLeft - fontHeight) / numOfXs;
    int xStart = hardLeft + xStep / 2;

    int minY = chart.getMinY();
    int i = 0;
    int xAxisValueY = height - offset;
    for (XYValue value : chart.getValues()) {
      g2d.setColor(Color.BLACK);
      String currValue = Integer.toString(value.getX());
      g2d.drawString(currValue, xStart + i * xStep - metrics.stringWidth(currValue) / 2, xAxisValueY);
      g2d.setColor(Color.ORANGE);
      int xAxisValueX = hardLeft + (i + 1) * xStep;
      g2d.drawLine(xAxisValueX, hardBottom + GAP / 2, xAxisValueX, gridTop);

      int y;
      if(value.getY() > chart.getMaxY()) {
        y = (int) (hardBottom - Math.round((double) (chart.getMaxY() - minY)/chart.getStep() * yStep)) - GAP;
      } else {
        y  = (int) (hardBottom - Math.round((double)(value.getY() - minY)/chart.getStep() * yStep));
      }
      int x = hardLeft + xStep*i;
      Shape rect = new Rectangle(x, y, xStep, hardBottom-y);

      if(value.getY() > chart.getMaxY()) {
        GradientPaint gradientPaint = new GradientPaint(x+xStep/2, y, Color.WHITE, x+xStep/2, y+2*GAP, Color.RED);
        Paint defaultPaint = g2d.getPaint();
        g2d.setPaint(gradientPaint);
        g2d.fill(rect);
        g2d.setPaint(defaultPaint);
      } else {
        g2d.setColor(Color.RED);
        g2d.fill(rect);
      }

      g2d.setColor(Color.WHITE);
      g2d.setStroke(new BasicStroke(1));
      g2d.draw(rect);
      i++;
    }

    g2d.setColor(Color.gray);
    // Vertical line
    g2d.drawLine(hardLeft, gridTop, hardLeft, hardBottom + GAP / 2);
    Polygon triangleTop = new Polygon(new int[]{hardLeft - 3, hardLeft + 3, hardLeft}, new int[]{hardTop+GAP/2, hardTop+GAP/2, hardTop}, 3);
    g.fillPolygon(triangleTop);
    g2d.drawPolygon(triangleTop);

    // Horizontal line
    g2d.drawLine(hardLeft - GAP / 2, hardBottom, gridRight, hardBottom);
    Polygon triangleRight = new Polygon(new int[]{hardRight - GAP / 2, hardRight - GAP / 2, hardRight}, new int[]{hardBottom - 3, hardBottom + 3, hardBottom}, 3);
    g.fillPolygon(triangleRight);
    g2d.drawPolygon(triangleRight);
  }
}
