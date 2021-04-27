package hr.fer.zemris.java.gui.charts;

/**
 * Class that models x and y values.
 */
public class XYValue {

  /**
   * X coordinate.
   */
  private final int x;

  /**
   * Y coordinate.
   */
  private final int y;

  /**
   * Creates an instance of a class with passed values.
   * @param x x coordinate
   * @param y y coordinate
   */
  public XYValue(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Return X coordinate.
   * @return X coordinate
   */
  public int getX() {
    return x;
  }

  /**
   * Return Y coordinate.
   * @return Y coordinate
   */
  public int getY() {
    return y;
  }
}
