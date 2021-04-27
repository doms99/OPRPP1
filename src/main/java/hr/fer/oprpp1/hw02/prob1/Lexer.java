package hr.fer.oprpp1.hw02.prob1;

/**
 * Razred koji se bavi leksičkom analizom koda.
 * Prolazi kodom <code>char</code> po <code>char</code> i grupira ih te vraća <code>Token</code>e stvorene temeljem isparsiranog dijela koda.
 */
public class Lexer {

    /**
     * Polje u kojem se sprema ulazni <code>String</code> rastavljen na <code>char</code> elemente.
     */
    private char[] data;

    /**
     * Zadnji token koji je bio isparsiran.
     */
    private Token token;

    /**
     * Stvara novu instancu razreda s primljenim <code>String</code>om koji se razdvaja na <code>char</code>ove.
     * @param text ulazni tekst koji se treba tokenizirati
     */
    private int currentIndex;

    /**
     * Stanje u kojem se lexer trenutno nalazi.
     * Moguća stanja su: <code>BASIC</code> i <code>EXTENDED</code>.
     * O promjeni stanja brine se korisnik koji je stvorio Objekt.
     */
    private LexerState state;

    /**
     * Stvara novu instancu razreda s primljenim <code>String</code>om koji se razdvaja na <code>char</code>ove.
     * @param text
     */
    public Lexer(String text) {
        data = text.toCharArray();
        token = null;
        currentIndex = 0;
        state = LexerState.BASIC;
    }

    /**
     * Funckija koja preskače praznine i oznake za novi red.
     * Staje kada naiđe na prvi znak koji nije praznian.
     */
    private void skipSpaces() {
        while(currentIndex < data.length && isSpace(data[currentIndex])) {
            currentIndex++;
        }
    }

    /**
     * Funcija koja provjerava da li je poslani znak praznina ili oznaka za novi red.
     * @param c znak za koji treba provjeriti dali je praznina ili nije
     * @return <code>true</code> ako je znak praznina, inače <code>false</code>
     */
    private boolean isSpace(char c) {
        return c == ' ' ||
                c == '\t' ||
                c == '\r' ||
                c == '\n';
    }

    /**
     * Pomoćna funckija koja parsira ulazni tekst sve do pojave prvog znaka #.
     * Uvijek se stvaraju <code>Token</code>i vrste <code>WORD</code> osim kada se naiđe na znak # kada se stvara <code>SYMBOL</code>.
     * @throws LexerException ukoliko je došlo do greške ili je ulazni niz napisan pogrešno
     */
    private void extendedParse() {
        StringBuilder builder = new StringBuilder();
        if(data[currentIndex] == '#') {
            token = new Token(TokenType.SYMBOL, data[currentIndex++]);
            return;
        }

        while(data[currentIndex] != '#' && !isSpace(data[currentIndex])) {
            builder.append(data[currentIndex++]);
        }

        token = new Token(TokenType.WORD, builder.toString());
    }

    /**
     * Pomoćna funckija koja parsira ulazni tekst sve do pojave prvog znaka #.
     * Stvaraju se <code>Token</code>i vrste <code>WORD, NUMBER</code> ili <code>SYMBOL</code> ako se naišlo na znak #.
     * @throws LexerException ukoliko je došlo do greške ili je ulazni niz napisan pogrešno
     */
    private void basicParse() {
        StringBuilder builder = new StringBuilder();
        if (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\') {
            while (currentIndex < data.length &&
                    (Character.isLetter(data[currentIndex]) || data[currentIndex] == '\\')) {

                if (data[currentIndex] == '\\') {
                    if (++currentIndex >= data.length)
                        throw new LexerException("Invalid escape! No characters after it.");

                    if (!Character.isDigit(data[currentIndex]) && data[currentIndex] != '\\')
                        throw new LexerException("Invalid escape! Character after \\ is not a number.");

                    builder.append(data[currentIndex]);
                    currentIndex++;
                } else {
                    builder.append(data[currentIndex++]);
                }
            }

            token = new Token(TokenType.WORD, builder.toString());

        } else if (Character.isDigit(data[currentIndex])) {

            while (currentIndex < data.length && Character.isDigit(data[currentIndex]))
                builder.append(data[currentIndex++]);

            try {
                token = new Token(TokenType.NUMBER, Long.parseLong(builder.toString()));
            } catch (NumberFormatException ex) {
                throw new LexerException("Number is to big! Number must be in range [" + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "].");
            }

        } else {
            token = new Token(TokenType.SYMBOL, data[currentIndex++]);
        }
    }

    /**
     * Funckija koja kada je pozvana parsira sljedeči dio koda, stvara odgovarajuči <code>Token</code> te ga vraća.
     * @return novostvoreni <code>Token</code>
     * @throws LexerException ukoliko su se isparsirali svi znakoji ulaznog koda, a funkcija se pokuša pozvati ponovno
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("There are no more tokens!");

        skipSpaces();

        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, null);
            return token;
        }

        if(state == LexerState.EXTENDED) extendedParse();
        else basicParse();

        return token;
    }

    /**
     * Vraća <code>Token</code> koji je zadnji bio stvoren.
     * @return zadnje stvoreni <code>Token</code>
     */
    public Token getToken() {
        return this.token;
    }

    /**
     * Funkcija kojom se stanje lexera postavlja na poslano stanje.
     * @param state stanje u koje lexer treba preči
     */
    public void setState(LexerState state) {
        if(state == null)
            throw new NullPointerException("State can't be null!");
        this.state = state;
    }

}
