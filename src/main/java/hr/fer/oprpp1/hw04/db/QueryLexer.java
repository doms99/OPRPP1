package hr.fer.oprpp1.hw04.db;

/**
 * Lexer koji će se koristiti za parsiranje <code>query</code>ja.
 */
public class QueryLexer {

    /**
     * Uvijet razdjeljen na <code>char</code>ove
     */
    private char[] data;

    /**
     * Zadnje parsirani token
     */
    private Token token;

    /**
     * Trenutna pozicij unutar polja podataka
     */
    private int currentIndex;

    public QueryLexer(String queryLine) {
        this.data = queryLine.toCharArray();
        this.currentIndex = 0;
    }

    /**
     * Preskače sve razmake.
     */
    private void skipSpaces() {
        while(currentIndex < data.length && Character.isWhitespace(data[currentIndex]))
            currentIndex++;
    }

    /**
     * Parsira i vraća novi token.
     * @return novonastali token
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("There are no more tokens!");

        skipSpaces();

        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, "");
            return token;
        }

        parseQuery();

        return token;
    }

    /**
     * pomoćna metoda koja parsira query i sprema ga u privatnu varijablu token
     * @throws LexerException ako zapis koji se parsira nije ispravan
     */
    private void parseQuery() {
        StringBuilder builder = new StringBuilder();

        if(Character.isLetter(data[currentIndex])) {
            while(currentIndex < data.length && !Character.isWhitespace(data[currentIndex]) && Character.isLetterOrDigit(data[currentIndex]))
                builder.append(data[currentIndex++]);

            if(builder.toString().equals("LIKE"))
                token = new Token(TokenType.OPERATOR, "LIKE");
            else if(builder.toString().toUpperCase().equals("AND"))
                token = new Token(TokenType.AND, "AND");
            else
                token = new Token(TokenType.ATTRIBUTE, builder.toString());
        } else if(data[currentIndex] >= '<' && data[currentIndex] <= '>') {
            while(data[currentIndex] >= '<' && data[currentIndex] <= '>')
                builder.append(data[currentIndex++]);

            token = new Token(TokenType.OPERATOR, builder.toString());
        } else if(data[currentIndex] == '\"') {
            currentIndex++;
            while(currentIndex < data.length && data[currentIndex] != '\"')
                builder.append(data[currentIndex++]);
            if(currentIndex == data.length)
                throw new LexerException("Unclosed string literal");
            currentIndex++;
            token = new Token(TokenType.STRING_LITERAL, builder.toString());
        } else {
            throw new LexerException("Invalid character: " + data[currentIndex]);
        }
    }

    /**
     * Vraća <code>Token</code> koji je zadnji bio stvoren.
     * @return zadnje stvoreni <code>Token</code>
     */
    public Token getToken() {
        return this.token;
    }
}
