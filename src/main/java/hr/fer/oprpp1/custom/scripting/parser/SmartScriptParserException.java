package hr.fer.oprpp1.custom.scripting.parser;

/**
 * Iznimka koju baca <code>SmartScriptParser</code> ukoliko se dogodila greška prilikom parsiranja.
 *
 */
public class SmartScriptParserException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public SmartScriptParserException() {
    }

    public SmartScriptParserException(String message) {
        super(message);
    }

    public SmartScriptParserException(Throwable cause) {
        super(cause);
    }

    public SmartScriptParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
