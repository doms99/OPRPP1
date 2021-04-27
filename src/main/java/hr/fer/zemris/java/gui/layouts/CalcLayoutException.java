package hr.fer.zemris.java.gui.layouts;

/**
 * Exception that is thrown when an error occurs positioning of components in <code>CalcLayout</code>
 */
public class CalcLayoutException extends RuntimeException {
  public CalcLayoutException() {
    super();
  }

  public CalcLayoutException(String message) {
    super(message);
  }

  public CalcLayoutException(String message, Throwable cause) {
    super(message, cause);
  }

  public CalcLayoutException(Throwable cause) {
    super(cause);
  }
}
