package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.Element;

/**
 * Klasa čije objekte stvara i šalje <code>Lexer</code>.
 * Sadrže informacije o tipu <code>Token</code>a te odgovarajuči <code>Element</code>.
 */
public class Token {

    /**
     * Tip <code>Token</code>a.
     */
    private TokenType type;

    /**
     * <code>Element</code> koji odgovara tipu.
     */
    private Element value;

    /**
     * Stvaara novu instancu klase s poslanim tipom i <code>Element</code>om.
     * @param type tip <code>Token</code>a
     * @param value <code>Element</code> koji odgovara tipu
     */
    public Token(TokenType type, Element value) {
        this.type = type;
        this.value = value;
    }

    /**
     * Vraća <code>Element</code> spremljen unutar <code>Token</code>a
     * @return <code>Element</code> unutar <code>Token</code>a
     */
    public Element getValue() { return this.value; }

    /**
     * Vraća vrstu tokena.
     * @return <code>TokenType</code> vrsta tokena
     */
    public TokenType getType() { return this.type; }
}
