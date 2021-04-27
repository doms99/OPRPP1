package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import java.awt.*;

/**
 * Window containing two lists with the same <code>ListModel</code>
 */
public class PrimDemo extends JFrame {

  /**
   * Creates the window and initializes the UI and functionality of the window
   */
  public PrimDemo() {
    super("PrimList");
    setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    setPreferredSize(new Dimension(250,300));
    initGUI();
    pack();
  }

  /**
   * Initializes the UI and functionality of the window
   */
  private void initGUI() {
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(1, 2));

    PrimListModel model = new PrimListModel();

    JList<Integer> list1 = new JList<>();
    list1.setModel(model);
    JScrollPane pane1 = new JScrollPane(list1);
    panel.add(pane1);

    JList<Integer> list2 = new JList<>();
    list2.setModel(model);
    JScrollPane pane2 = new JScrollPane(list2);
    panel.add(pane2);

    cp.add(panel, BorderLayout.CENTER);

    JButton button = new JButton("Sljedeći");
    button.addActionListener(e -> model.next());
    cp.add(button, BorderLayout.PAGE_END);
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(()->{
      new PrimDemo().setVisible(true);
    });
  }
}
