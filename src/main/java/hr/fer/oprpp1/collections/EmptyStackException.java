package hr.fer.oprpp1.collections;

/**
 * Iznimka koja će se bacati svaki puta kada će se pokušati čitati ili skidati element s praznog stoga
 * @author Dominik
 */
public class EmptyStackException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public EmptyStackException() {
    }

    public EmptyStackException(String message) {
        super(message);
    }

    public EmptyStackException(Throwable cause) {
        super(cause);
    }

    public EmptyStackException(String message, Throwable cause) {
        super(message, cause);
    }
}
