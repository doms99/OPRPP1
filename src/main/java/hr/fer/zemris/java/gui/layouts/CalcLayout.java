package hr.fer.zemris.java.gui.layouts;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom <code>LayoutManager2</code> for positioning components in a 7x5 grid.
 * Component added to position 1,1 spans 5 cells in width.
 */
public class CalcLayout implements LayoutManager2 {

  /**
   * Gap between the cells.
   */
  private final int gridGap;

  /**
   * Map of components.
   * Key is <code>RCPosition</code> of the component.
   */
  private Map<RCPosition, Component> components;

  /**
   * Preferred height and width of cells.
   */
  private int preferredHeight, preferredWidth;

  /**
   * Minimum height and width of cells.
   */
  private int minHeight, minWidth;

  /**
   * Minimum height and width of cells.
   */
  private int maxHeight, maxWidth;

  /**
   * Number of columns and rows of the grid.
   */
  private final int ROWS = 5, COLUMNS = 7;

  /**
   * Creates an instance of the class with passed grid gap.
   * @param gridGap gap between cells
   */
  public CalcLayout(int gridGap) {
    this.gridGap = gridGap;
    components = new HashMap<>();
    preferredHeight = preferredWidth = minWidth = minHeight = maxHeight = maxWidth = 0;
  }

  /**
   * Creates an instance of the class with 0 as grid gap.
   */
  public CalcLayout() {
    this(0);
  }

  /**
   * Adds a passed component to its map with a passed key.
   * Updates min, preferred and max size of the cells deepening of the passed component.
   * @param comp component
   * @param constraints <code>RCPosition</code> position in the grid
   * @throws NullPointerException if passed constraints is <code>null</code>
   * @throws IllegalArgumentException if constraints isn't of a <code>RCPosition</code> or <code>String</code> class
   * @throws CalcLayoutException if invalid <code>RCPosition</code> is passed as an argument or another component was
   * already added to that position
   */
  @Override
  public void addLayoutComponent(Component comp, Object constraints) {
    if(constraints == null)
      throw new NullPointerException("Constraints can't be null");

    RCPosition position;

    if(constraints.getClass().equals(RCPosition.class)) position = (RCPosition) constraints;
    else if(constraints.getClass().equals(String.class)) position = RCPosition.parse((String) constraints);
    else throw new IllegalArgumentException("Constraints must be of RCPosition or String class.");

    int row = position.getRow();
    int col = position.getColumn();
    if(row < 1 || row > ROWS || (row == 1 && col < 6 && col > 1) || col < 1 || col > COLUMNS)
      throw new CalcLayoutException("Invalid row or column position: "+ position.getRow() +","+ position.getColumn());

    if(components.get(position) != null)
      throw new CalcLayoutException("Another component was already added to position "+ position.getRow() +","+ position.getColumn());

    components.put(position, comp);
    preferredHeight = Math.max(preferredHeight, (int) comp.getPreferredSize().getHeight());
    preferredWidth = Math.max(preferredWidth, (int) comp.getPreferredSize().getWidth());
    minHeight = Math.max(minHeight, (int) comp.getMinimumSize().getHeight());
    minWidth = Math.max(minWidth, (int) comp.getMinimumSize().getWidth());
    maxHeight = Math.min(maxHeight, (int) comp.getMaximumSize().getHeight());
    maxWidth = Math.min(maxWidth, (int) comp.getMaximumSize().getWidth());
  }

  /**
   * Calculates max size of the component.
   * @param target <code>Container</code> in which the component will be or already is added
   * @return max size of a component
   */
  @Override
  public Dimension maximumLayoutSize(Container target) {
    return calcSize(maxWidth, maxHeight, target.getInsets());
  }

  @Override
  public float getLayoutAlignmentX(Container target) {
    return 0;
  }

  @Override
  public float getLayoutAlignmentY(Container target) {
    return 0;
  }

  @Override
  public void invalidateLayout(Container target) {

  }

  /**
   * @throws UnsupportedOperationException because operation is not implemented
   */
  @Override
  public void addLayoutComponent(String name, Component comp) {
    throw new UnsupportedOperationException("Method is not implemented");
  }

  /**
   * Removes the passed component from the layout if thw component was added to it.
   * @param comp component that need to be removed
   */
  @Override
  public void removeLayoutComponent(Component comp) {
    for(RCPosition pos : components.keySet()) {
      if(components.get(pos).equals(comp)) {
        components.remove(pos);
        break;
      }
    }
  }

  /**
   * Calculates preferred size of the component.
   * @param parent <code>Container</code> in which the component will be or already is added
   * @return max size of a component
   */
  @Override
  public Dimension preferredLayoutSize(Container parent) {
    return calcSize(preferredWidth, preferredHeight, parent.getInsets());
  }

  /**
   * Calculates minimum size of the component.
   * @param parent <code>Container</code> in which the component will be or already is added
   * @return max size of a component
   */
  @Override
  public Dimension minimumLayoutSize(Container parent) {
    return calcSize(minWidth, minHeight, parent.getInsets());
  }

  /**
   * Private helper method that calculates the size of the component depending on the passed width and height of the cells and
   * <code>Insets</code> of the parent component.
   * @param width cell width
   * @param height cell height
   * @param border <code>Insets</code> aog the parent component
   * @return calculated <code>Dimension</code> object
   */
  private Dimension calcSize(int width, int height, Insets border) {
    int resWidth = COLUMNS* width + (COLUMNS - 1)*gridGap + border.left + border.right;
    int resHeight = ROWS* height + (ROWS - 1)*gridGap + border.bottom + border.top;

    return new Dimension(resWidth, resHeight);
  }

  /**
   * Lays out the specified container.
   * @param parent the container to be laid out
   */
  @Override
  public void layoutContainer(Container parent) {
    Insets border = parent.getInsets();
    int usableWidth = parent.getWidth() - border.left - border.right - (COLUMNS - 1)*gridGap;
    int usableHeight = parent.getHeight() - border.bottom - border.top - (ROWS - 1)*gridGap;

    int rowHeight = (int) Math.ceil((double) usableHeight/ROWS);
    int colWidth = (int) Math.ceil((double) usableWidth/COLUMNS);

    int[] row = new int[ROWS], col = new int[COLUMNS];

    Arrays.fill(row, rowHeight);
    Arrays.fill(col, colWidth);

    int excess = usableHeight - ROWS*rowHeight;
    if(excess > 0) {
      int space = ROWS/excess;
      for(int i = 0; i < ROWS && excess > 0; i += space) {
        row[i]--;
        excess--;
      }
    }

    excess = usableWidth - COLUMNS*colWidth;
    if(excess > 0) {
      int space = COLUMNS/excess;
      for(int i = 0; i < COLUMNS && excess > 0; i += space) {
        col[i]--;
        excess--;
      }
    }
    int[] x = new int[COLUMNS], y = new int[ROWS];
    x[0] = border.left;
    for(int i = 1; i < COLUMNS; i++) {
      x[i] = x[i-1] + col[i-1] + gridGap;
    }

    y[0] = border.top;
    for(int i = 1; i < ROWS; i++) {
      y[i] = y[i-1] + row[i-1] + gridGap;
    }

    for(RCPosition pos : components.keySet()) {
      Component comp = components.get(pos);
      int xIndex = pos.getColumn(), yIndex = pos.getRow();
      if(xIndex == 1 && yIndex == 1) {
        comp.setBounds(x[xIndex-1], y[yIndex-1], x[5]-gridGap, row[yIndex-1]);
      } else {
        comp.setBounds(x[xIndex-1], y[yIndex-1], col[xIndex-1], row[yIndex-1]);
      }
    }
  }
}
