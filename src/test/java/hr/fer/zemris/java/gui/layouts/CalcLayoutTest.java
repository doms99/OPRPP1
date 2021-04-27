package hr.fer.zemris.java.gui.layouts;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;

public class CalcLayoutTest {

  private class MyFrame extends JFrame {
    public MyFrame() {
      setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
      initGUI();
      pack();
    }

    private void initGUI() {
      Container cp = getContentPane();
      cp.setLayout(new CalcLayout(3));
    }
  }

  @Test
  public void invalidConstraintValues() {
    MyFrame frame = new MyFrame();
    Container cp = frame.getContentPane();
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(0, 2)), "Row = 0");
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(6, 2)), "Row = 6");
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(1, 0)), "Col = 0");
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(1, 8)), "Col = 8");
  }

  @Test
  public void forbiddenFields() {
    MyFrame frame = new MyFrame();
    Container cp = frame.getContentPane();
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(1, 2)), "Row = 1, Col = 2");
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(1, 5)), "Row = 1, Col = 5");
  }

  @Test
  public void existingComponent() {
    MyFrame frame = new MyFrame();
    Container cp = frame.getContentPane();
    cp.add(new JLabel("Test"), new RCPosition(1, 1));
    assertThrows(CalcLayoutException.class, () -> cp.add(new JLabel("Test"), new RCPosition(1, 1)), "Row = 0");
  }

  @Test
  public void testFrameSize1() {
    JPanel p = new JPanel(new CalcLayout(2));
    JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(10,30));
    JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(20,15));
    p.add(l1, new RCPosition(2,2));
    p.add(l2, new RCPosition(3,3));
    Dimension dim = p.getPreferredSize();
    assertEquals(152, dim.width, "width");
    assertEquals(158, dim.height, "height");
  }

  @Test
  public void testFrameSize2() {
    JPanel p = new JPanel(new CalcLayout(2));
    JLabel l1 = new JLabel(""); l1.setPreferredSize(new Dimension(108,15));
    JLabel l2 = new JLabel(""); l2.setPreferredSize(new Dimension(16,30));
    p.add(l1, new RCPosition(1,1));
    p.add(l2, new RCPosition(3,3));
    Dimension dim = p.getPreferredSize();
    assertEquals(152, dim.width, "width");
    assertEquals(158, dim.height, "height");
  }
}
