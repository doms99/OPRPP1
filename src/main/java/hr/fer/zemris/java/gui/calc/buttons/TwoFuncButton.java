package hr.fer.zemris.java.gui.calc.buttons;

/**
 * Interface that is implemented by all of the buttons that can do two operations depending of
 * the inversion.
 */
public interface TwoFuncButton {

  /**
   * Function that swaps the active operation of the component that implements it.
   */
  void swap();
}
