package hr.fer.oprpp1.custom.scripting.lexer;

/**
 * Mogući tipovi <code>Token</code>a
 */
public enum TokenType {
    OPERATOR, VARIABLE, INTEGER_CONST, DOUBLE_CONST, STRING, FUNCTION, TAG_OPEN, TAG_CLOSE, ECHO_TAG, EOF
}
