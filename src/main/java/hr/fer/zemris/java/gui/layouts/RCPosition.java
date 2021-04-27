package hr.fer.zemris.java.gui.layouts;

import java.util.Objects;

/**
 * Class that represents a position of a cell in a grid;
 */
public class RCPosition {

  /**
   * Cells row.
   */
  private final int row;

  /**
   * Cells column.
   */
  private final int column;

  /**
   * Creates new instance of the class.
   * @param row row of a cell
   * @param column column of a cell
   */
  public RCPosition(int row, int column) {
    this.row = row;
    this.column = column;
  }

  /**
   * Returns cells row.
   * @return cells row
   */
  public int getRow() {
    return row;
  }

  /**
   * Return cells column.
   * @return cells column
   */
  public int getColumn() {
    return column;
  }

  /**
   * Creates and return an instance of the class from passed string.
   * String must be in format "<row>,<column>"
   * @param position <code>String</code> representing the position
   * @return new instance of the class with parsed row and column from the passed string
   */
  public static RCPosition parse(String position) {
    String[] split = position.replaceAll("\\s+", "").split(",");
    if(split.length != 2) throw new IllegalArgumentException("Invalid position format: "+ position);
    int row;
    try {
      row = Integer.parseInt(split[0]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid row format:"+ split[0]);
    }

    int col;
    try {
      col = Integer.parseInt(split[1]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("Invalid column format:"+ split[0]);
    }
    return new RCPosition(row, col);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    RCPosition that = (RCPosition) o;
    return row == that.row &&
            column == that.column;
  }

  @Override
  public int hashCode() {
    return Objects.hash(row, column);
  }
}
