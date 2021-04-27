package hr.fer.zemris.java.hw05.shell;

/**
 * Exception that is trown by ShellCommand
 */
public class ShellIOException extends RuntimeException {
    public ShellIOException() {
        super();
    }

    public ShellIOException(String message) {
        super(message);
    }
}
