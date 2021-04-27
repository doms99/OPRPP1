package hr.fer.oprpp1.hw04.db;

/**
 * Iznimka koja se baca kada se dogodila greška unutar <code>QueryLexer</code>a.
 */
public class LexerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public LexerException() {
    }

    public LexerException(String message) {
        super(message);
    }

    public LexerException(Throwable cause) {
        super(cause);
    }

    public LexerException(String message, Throwable cause) {
        super(message, cause);
    }
}
