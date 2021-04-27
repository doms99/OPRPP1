package hr.fer.zemris.java.gui.charts;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that holds all of the information about the chart like
 * axis labels, min and max shown on y axis, xy values....
 */
public class BarChart {

  /**
   * xy values.
   */
  private final List<XYValue> values;

  /**
   * x and y axis names.
   */
  private final String xAxis, yAxis;

  /**
   * Min, max and step values of y axis.
   */
  private int minY, maxY, step;

  /**
   * Creates new instance of class with passed values.
   * @param values list of xy values
   * @param xAxis x axis name
   * @param yAxis y axis name
   * @param minY y axis min value
   * @param maxY y axis max value
   * @param step y axis step between values
   * @throws IllegalArgumentException if list of xy values contains a y value that is smaller then the y axis min value or
   * y min was smaller then 0 or y max vas smaller then y min
   */
  public BarChart(List<XYValue> values, String xAxis, String yAxis, int minY, int maxY, int step) {
    if(minY < 0)
      throw new IllegalArgumentException("y-min must be larger then 0, was: "+ minY);

    values.forEach(value -> {
      if(value.getY() < minY)
        throw new IllegalArgumentException("Value was given which was smaller then provided minimum. Value:"+ value +", y-min:"+ this.minY);
    });
    this.values = values;
    this.xAxis = xAxis;
    this.yAxis = yAxis;

    this.step = step;
    while ((maxY - minY) % this.step != 0) {
      this.step++;
    }
    this.minY = minY;

    if(maxY <= minY)
      throw new IllegalArgumentException("y-max must be larger then y-min, was y-min:"+ this.minY +", y-max:"+ maxY);
    this.maxY = maxY;

  }

  /**
   * Returns list of xy values.
   * @return list of xy values
   */
  public List<XYValue> getValues() {
    return values;
  }

  /**
   * Return name of x axis.
   * @return name of x axis
   */
  public String getXAxis() {
    return xAxis;
  }

  /**
   * Returns name of y axis.
   * @return name of y axis
   */
  public String getYAxis() {
    return yAxis;
  }

  /**
   * Returns min values of y axis.
   * @return min value of y axis
   */
  public int getMinY() {
    return minY;
  }

  /**
   * Returns max values of y axis.
   * @return max value of y axis
   */
  public int getMaxY() {
    return maxY;
  }

  /**
   * Returns step values of y axis.
   * @return step value of y axis
   */
  public int getStep() {
    return step;
  }

  /**
   * Returns number of xy values
   * @return number of xy values
   */
  public int getValueSize() {
    return values.size();
  }

  /**
   * Expects string of lines read from a file representing data of the chart.
   * First line is the name of x axis,
   * Secund line is the name of y axis,
   * Third line contains xy value pairs.
   * Forth line contains min y shown in y axis.
   * Fifth line contains max y shown on y axis.
   * Sixth line constains step of y values on y axis.
   * @param lines lines read from a file.
   * @return constructed <code>BarChart</code> from sent lines
   * @throws IllegalArgumentException if any value is invalid
   */
  public static BarChart parse(String[] lines) {
    String yAxisLabel, xAxisLabel;
    int minY, maxY, step;
    List<XYValue> values = new ArrayList<>();

    xAxisLabel = lines[0];
    yAxisLabel = lines[1];
    for(String combo : lines[2].split("\\s+")) {
      String[] value = combo.split(",");
      if(value.length != 2)
        throw new IllegalArgumentException("Invalid combo format: "+ combo);

      try {
        int x = Integer.parseInt(value[0]);
        int y = Integer.parseInt(value[1]);
        values.add(new XYValue(x, y));
      } catch (NumberFormatException ex) {
        throw new IllegalArgumentException("Combo contained a non digit character: "+ combo);
      }
    }
    try {
      minY = Integer.parseInt(lines[3]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid y-min format on line 4: "+ lines[3]);
    }
    try {
      maxY = Integer.parseInt(lines[4]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid y-max format on line 5: "+ lines[4]);
    }
    try {
      step = Integer.parseInt(lines[5]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid y-min format on line 6: "+ lines[5]);
    }

    return new BarChart(values, xAxisLabel, yAxisLabel, minY, maxY, step);
  }


}
