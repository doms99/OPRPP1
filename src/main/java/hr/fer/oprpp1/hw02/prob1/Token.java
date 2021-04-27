package hr.fer.oprpp1.hw02.prob1;

/**
 * Klasa čije objekte stvara i šalje <code>Lexer</code>.
 * Sadrže informacije o tipu <code>Token</code>a te odgovarajuči <code>Object</code>.
 */
public class Token {

    /**
     * Tip <code>Token</code>a.
     */
    private TokenType type;

    /**
     * <code>Object</code> koji se spremljen u <code>Token</code>u.
     */
    private Object value;

    /**
     * Stvaara novu instancu klase s poslanim tipom i <code>Object</code>om.
     * @param type tip <code>Token</code>a
     * @param value <code>Object</code> koji se sprema u <code>Token</code>
     */
    public Token(TokenType type, Object value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Vraća tip <code>Token</code>a
     * @return tip <code>Token</code>a
     */
    public Object getValue() { return this.value; }

    /**
     * Vraća <code>object</code> spremljen unutar <code>Token</code>a
     * @return <code>Object</code> unutar <code>Token</code>a
     */
    public Object getType() { return this.type; }
}
