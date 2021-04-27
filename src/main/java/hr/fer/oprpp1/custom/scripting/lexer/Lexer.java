package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.*;

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
     * Trenutna pozicija na kojoj se nalazimo unutar <code>data</code> polja.
     */
    private int currentIndex;

    /**
     * Stanje u kojem se lexer trenutno nalazi.
     * Moguća stanja su: <code>TEXT</code> i <code>TAG</code>.
     * O promjeni stanja brine se korisnik koji je stvorio Objekt.
     */
    private LexerState state;

    /**
     * Stvara novu instancu razreda s primljenim <code>String</code>om koji se razdvaja na <code>char</code>ove.
     * @param text ulazni tekst koji se treba tokenizirati
     */
    public Lexer(String text) {
        data = text.toCharArray();
        token = null;
        currentIndex = 0;
        state = LexerState.TEXT;
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

    /**
     * Funckija koja preskače praznine i oznake za novi red.
     * Staje kada naiđe na prvi znak koji nije praznian.
     */
    private void skipSpacesTAG() {
        while(currentIndex < data.length && (data[currentIndex] == ' ' ||
                data[currentIndex] == '\t' ||
                data[currentIndex] == '\r' ||
                data[currentIndex] == '\n')) {
            currentIndex++;
        }
    }

    /**
     * Funckija koja preskače samo praznine.
     * Staje kada naiđe na prvi znak koji nije praznian.
     */
    private void skipSpacesTEXT() {
        while(currentIndex < data.length && (data[currentIndex] == ' ' ||
                data[currentIndex] == '\t')) {
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
                c == '\t';
    }

    /**
     * Funkcija koja parsira kod i stvara <code>Token</code>e kada se lexer nalazi u stanju <code>TEXT</code>.
     * Ukoliko funkcija naiđe na <code>{$</code> šalje <code>Token</code> korisniku kojim ga obavještava da je otvoren novi tag te da bi se trebalo preči u
     * stanje <code>TAG</code>.
     * @throws LexerException ako je ulazni kod pogrešno napisan
     */
    private void textParse() {
        StringBuilder builder = new StringBuilder();
        if(data[currentIndex] == '{' && currentIndex + 1 < data.length && data[currentIndex+1] == '$') {
            token = new Token(TokenType.TAG_OPEN, new Element());
            currentIndex += 2;
            return;
        }

        while(currentIndex < data.length && !isSpace(data[currentIndex])) {
            if(data[currentIndex] == '{' && currentIndex + 1 < data.length && data[currentIndex+1] == '$') {
                break;
            }
            if(data[currentIndex] == '\\') {
                if(currentIndex + 1 < data.length &&  data[currentIndex+1] != '\\' && data[currentIndex+1] != '{')
                    throw new LexerException("Invalid escape! \\"+ data[currentIndex+1] +" is not allowed!");

                builder.append(data[++currentIndex]);
                currentIndex++;
            } else {
                builder.append(data[currentIndex++]);
            }
        }

        token = new Token(TokenType.STRING, new ElementString(builder.toString()));
    }

    /**
     * Funkcija koja parsira kod i stvara <code>Token</code>e kada se lexer nalazi u stanju <code>TAG</code>.
     * Ukoliko funkcija naiđe na <code>$}</code> šalje <code>Token</code> korisniku kojim ga obavještava da je otvoren novi tag te da bi se trebalo preči u
     * stanje <code>TEXT</code>.
     * @throws LexerException ako je ulazni kod pogrešno napisan
     */
    private void tagParse() {
        StringBuilder builder = new StringBuilder();
        if (data[currentIndex] == '$' && currentIndex + 1 < data.length && data[currentIndex + 1] == '}') {
            token = new Token(TokenType.TAG_CLOSE, new Element());
            currentIndex += 2;
            return;
        }

        if(data[currentIndex] == '=') {
            token = new Token(TokenType.ECHO_TAG, new Element());
            currentIndex++;
        } else if (data[currentIndex] == '\"') {
            currentIndex++;
            parseString();
        } else if(data[currentIndex] == '-' && currentIndex + 1 < data.length && Character.isDigit(data[currentIndex+1]) || Character.isDigit(data[currentIndex])) {
            parseDigit();
        } else if(Character.isLetter(data[currentIndex])) {
            parseName(TokenType.VARIABLE);
        } else if(data[currentIndex] == '@' && currentIndex + 1 < data.length && Character.isLetter(data[currentIndex+1])) {
            currentIndex++;
            parseName(TokenType.FUNCTION);
        } else if(data[currentIndex] == '+' ||
                data[currentIndex] == '-' ||
                data[currentIndex] == '*' ||
                data[currentIndex] == '/' ||
                data[currentIndex] == '^') {
            token = new Token(TokenType.OPERATOR, new ElementOperator(Character.toString(data[currentIndex])));
            currentIndex++;
        } else throw new LexerException("Invalid expression!");

    }

    /**
     * Pomoćna funkcija koja se poziva ako je potrebo parsirati brojčanu konstantu.
     * Ovisno o ulazu, stvara <code>Token</code> s elementom <code>ElementConstantDouble</code> ili <code>ElementConstantInteger</code>
     * @throws LexerException ako je ulazni kod pogrešno napisan
     */
    private void parseDigit() {
        StringBuilder builder = new StringBuilder();
        if(data[currentIndex] == '-')
            builder.append(data[currentIndex++]);
        boolean dotFound = false;
        while (currentIndex < data.length && (Character.isDigit(data[currentIndex]) || data[currentIndex] == '.')) {
            if(data[currentIndex] == '.')
                if(dotFound)
                    throw new LexerException("Invalid number format!");
                else
                    dotFound = true;
            builder.append(data[currentIndex++]);
        }
        if(dotFound)
            token = new Token(TokenType.DOUBLE_CONST, new ElementConstantDouble(Double.parseDouble(builder.toString())));
        else
            token = new Token(TokenType.INTEGER_CONST, new ElementConstantInteger(Integer.parseInt(builder.toString())));
    }

    /**
     * Pomoćna funkcija koja se poziva ako je potrebo parsirati ime varijable ili ime funkcije.
     * Stvara <code>Token</code> s elementom tipa <code>ElementVariable</code>.
     */
    private void parseName(TokenType type) {
        StringBuilder builder = new StringBuilder();
        while (Character.isLetterOrDigit(data[currentIndex]) || data[currentIndex] == '_') {
            builder.append(data[currentIndex++]);
        }
        if(type == TokenType.VARIABLE)
            token = new Token(type, new ElementVariable(builder.toString()));
        else
            token = new Token(type, new ElementFunction(builder.toString()));
    }

    /**
     * Pomoćna funkcija koja se poziva ako je potrebo parsirati Stringa.
     * Stvara <code>Token</code> s elementom tipa <code>ElementString</code>.
     * @throws LexerException ako je ulazni kod pogrešno napisan
     */
    private void parseString() {
        StringBuilder builder = new StringBuilder();
        while(data[currentIndex] != '\"') {
            if(data[currentIndex] == '\\') {
                if(currentIndex + 1 < data.length && data[currentIndex+1] == '\"' || data[currentIndex+1] == '\\')
                    currentIndex++;
                else
                    throw new LexerException("Invalid escape! \\"+ data[currentIndex+1] +" is not allowed!");
            }
            builder.append(data[currentIndex++]);
            if(currentIndex == data.length)
                throw new LexerException("String was never closed!");
        }
        currentIndex++;
        token = new Token(TokenType.STRING, new ElementString(builder.toString()));
    }

    /**
     * Funckija koja kada je pozvana parsira sljedeči dio koda, stvara odgovarajuči <code>Token</code> te ga vraća.
     * @return novostvoreni <code>Token</code>
     * @throws LexerException ukoliko su se isparsirali svi znakoji ulaznog koda, a funkcija se pokuša pozvati ponovno
     */
    public Token nextToken() {
        if (token != null && token.getType() == TokenType.EOF)
            throw new LexerException("There are no more tokens!");

        if(state == LexerState.TAG) skipSpacesTAG();
        else skipSpacesTEXT();

        if (currentIndex >= data.length) {
            token = new Token(TokenType.EOF, new Element());
            return token;
        }

        if(state == LexerState.TAG) tagParse();
        else textParse();

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
     * Služi za testiranje u kojem se stanju trenutno nalazi lexer.
     * @return <code>LexerState</code> stanje u kojem se lexer trenutno nalazi
     */
    LexerState getState() {
        return this.state;
    }
}
