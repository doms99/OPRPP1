package hr.fer.oprpp1.custom.scripting.lexer;

import hr.fer.oprpp1.custom.scripting.elems.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class LexerTest {


    @Test
    public void testNotNullTEXT() {
        Lexer lexer = new Lexer("");

        assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
    }

    @Test
    public void testNotNullTAG() {
        Lexer lexer = new Lexer("");
        lexer.setState(LexerState.TAG);

        assertNotNull(lexer.nextToken(), "Token was expected but null was returned.");
    }


    @Test
    public void testNullInput() {
        // must throw!
        assertThrows(NullPointerException.class, () -> new Lexer(null));
    }

    @Test
    public void testNullState() {
        assertThrows(NullPointerException.class, () -> new Lexer("").setState(null));
    }

    @Test
    public void testEmpty() {
        Lexer lexer = new Lexer("");

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }


    @Test
    public void testGetReturnsLastNextTEXT() {
        // Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
        Lexer lexer = new Lexer("");

        Token token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }

    @Test
    public void testGetReturnsLastNextTAG() {
        // Calling getToken once or several times after calling nextToken must return each time what nextToken returned...
        Lexer lexer = new Lexer("");
        lexer.setState(LexerState.TAG);

        Token token = lexer.nextToken();
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
        assertEquals(token, lexer.getToken(), "getToken returned different token than nextToken.");
    }


    @Test
    public void testRadAfterEOFTEXT() {
        Lexer lexer = new Lexer("");

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void testRadAfterEOFTAG() {
        Lexer lexer = new Lexer("");
        lexer.setState(LexerState.TAG);

        // will obtain EOF
        lexer.nextToken();
        // will throw!
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    // Helper method that reads the files placed in the folder resources/extra.
    private String readExample(int n) {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
            if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }


    @Test
    public void testEmptyInTEXT() {
        Lexer lexer = new Lexer("");

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }

    @Test
    public void testEmptyInTAG() {
        Lexer lexer = new Lexer("");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Empty input must generate only EOF token.");
    }


    @Test
    public void testNoActualContentTEXT() {
        // When input is only of spaces, tabs, newlines, etc...
        Lexer lexer = new Lexer("   \t    ");

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
    }

    @Test
    public void testNoActualContentTAG() {
        // When input is only of spaces, tabs, newlines, etc...
        Lexer lexer = new Lexer("   \r\n\t    ");
        lexer.setState(LexerState.TAG);

        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "Input had no content. Lexer should generated only EOF token.");
    }

    @Test
    public void testUnclosedStringTAG() {
        // When input is only of spaces, tabs, newlines, etc...
        Lexer lexer = new Lexer("   \"hhhhhh    ");
        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void testTextInputTEXT() {
        Lexer lexer = new Lexer("Kako smo alooo 123");

        Token[] correctTokens = {
                new Token(TokenType.STRING, new ElementString("Kako")),
                new Token(TokenType.STRING, new ElementString("smo")),
                new Token(TokenType.STRING, new ElementString("alooo")),
                new Token(TokenType.STRING, new ElementString("123")),
                new Token(TokenType.EOF, new Element())
        };

        checkTokenStream(lexer, correctTokens);
    }

    @Test
    public void testTextInputTAG() {
        Lexer lexer = new Lexer("Kako smo alooo 123");
        lexer.setState(LexerState.TAG);

        Token[] correctTokens = {
                new Token(TokenType.VARIABLE, new ElementVariable("Kako")),
                new Token(TokenType.VARIABLE, new ElementVariable("smo")),
                new Token(TokenType.VARIABLE, new ElementVariable("alooo")),
                new Token(TokenType.INTEGER_CONST, new ElementConstantInteger(123)),
                new Token(TokenType.EOF, new Element())
        };

        checkTokenStream(lexer, correctTokens);
    }

    @Test
    public void testEchoTag() {
        Lexer lexer = new Lexer("dio teksta \n {$= i \"Ovo je sada novi tag\" -12.3 @sin $}");
        Token[] beforeOpeningTag = {
                new Token(TokenType.STRING, new ElementString("dio")),
                new Token(TokenType.STRING, new ElementString("teksta")),
                new Token(TokenType.STRING, new ElementString("\n")),
                new Token(TokenType.TAG_OPEN, new Element()),
        };

        Token[] inTag = {
                new Token(TokenType.ECHO_TAG, new Element()),
                new Token(TokenType.VARIABLE, new ElementVariable("i")),
                new Token(TokenType.STRING, new ElementString("Ovo je sada novi tag")),
                new Token(TokenType.DOUBLE_CONST, new ElementConstantDouble(-12.3)),
                new Token(TokenType.FUNCTION, new ElementFunction("sin")),
                new Token(TokenType.TAG_CLOSE, new Element())
        };

        Token finalToken = new Token(TokenType.EOF, new Element());

        checkTokenStream(lexer, beforeOpeningTag);
        lexer.setState(LexerState.TAG);
        checkTokenStream(lexer, inTag);
        lexer.setState(LexerState.TEXT);
        checkToken(lexer.nextToken(), finalToken);
    }

    @Test
    public void testForLoopTag() {
        Lexer lexer = new Lexer("dio teksta \n" +
                "{$FOR i \"12\" -12.3 bbb $}" +
                "neki tekst između" +
                "{$END$}");
        Token[] beforeOpeningTag = {
                new Token(TokenType.STRING, new ElementString("dio")),
                new Token(TokenType.STRING, new ElementString("teksta")),
                new Token(TokenType.STRING, new ElementString("\n")),
                new Token(TokenType.TAG_OPEN, new Element()),
        };

        Token[] inForTag = {
                new Token(TokenType.VARIABLE, new ElementVariable("FOR")),
                new Token(TokenType.VARIABLE, new ElementVariable("i")),
                new Token(TokenType.STRING, new ElementString("12")),
                new Token(TokenType.DOUBLE_CONST, new ElementConstantDouble(-12.3)),
                new Token(TokenType.VARIABLE, new ElementVariable("bbb")),
                new Token(TokenType.TAG_CLOSE, new Element())
        };

        Token[] inBetweenForAndEndTag = {
                new Token(TokenType.STRING, new ElementString("neki")),
                new Token(TokenType.STRING, new ElementString("tekst")),
                new Token(TokenType.STRING, new ElementString("između")),
                new Token(TokenType.TAG_OPEN, new Element()),
        };

        Token[] inEndTag = {
                new Token(TokenType.VARIABLE, new ElementVariable("END")),
                new Token(TokenType.TAG_CLOSE, new Element())
        };

        Token finalToken = new Token(TokenType.EOF, new Element());

        checkTokenStream(lexer, beforeOpeningTag);
        lexer.setState(LexerState.TAG);
        checkTokenStream(lexer, inForTag);
        lexer.setState(LexerState.TEXT);
        checkTokenStream(lexer, inBetweenForAndEndTag);
        lexer.setState(LexerState.TAG);
        checkTokenStream(lexer, inEndTag);
        lexer.setState(LexerState.TEXT);
        checkToken(lexer.nextToken(), finalToken);
    }

    @Test
    public void testEscapesTEXT() {
        Lexer lexer = new Lexer("\\{$ \\\\");

        checkToken(new Token(TokenType.STRING, new ElementString("{$")), lexer.nextToken());
        checkToken(new Token(TokenType.STRING, new ElementString("\\")), lexer.nextToken());
    }

    @Test
    public void testInvalidEscapesTEXT() {
        Lexer lexer = new Lexer("\\n");

        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void testEscapesTAG() {
        Lexer lexer = new Lexer("\"\\\"\" \"\\\\\"");
        lexer.setState(LexerState.TAG);

        checkToken(new Token(TokenType.STRING, new ElementString("\"")), lexer.nextToken());
        checkToken(new Token(TokenType.STRING, new ElementString("\\")), lexer.nextToken());
    }

    @Test
    public void testInvalidEscapesTAG() {
        Lexer lexer = new Lexer(" \"\\{\"");
        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, () ->  lexer.nextToken());
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void testDigitTAG() {
        Lexer lexer = new Lexer("-1.2");
        lexer.setState(LexerState.TAG);

        checkToken(lexer.nextToken(), new Token(TokenType.DOUBLE_CONST, new ElementConstantDouble(-1.2)));
    }

    @Test
    public void testInvalidDigitTAG() {
        Lexer lexer = new Lexer("-1.2.2");
        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, () ->  lexer.nextToken());
    }

    @Test
    public void testNotDefinedCharacterTAG() {
        Lexer lexer = new Lexer("%");
        lexer.setState(LexerState.TAG);

        assertThrows(LexerException.class, () ->  lexer.nextToken());
    }

    // Helper method for checking if lexer generates the same stream of tokens
    // as the given stream.
    private void checkTokenStream(Lexer lexer, Token[] correctData) {
        int counter = 0;
        for (Token expected : correctData) {
            Token actual = lexer.nextToken();
            String msg = "Checking token " + counter + ":";
            assertEquals(expected.getType(), actual.getType(), msg);
            assertEquals(expected.getValue().getClass(), actual.getValue().getClass(), msg);
            assertEquals(expected.getValue().asText(), actual.getValue().asText(), msg);
            counter++;
        }
    }

    private void checkToken(Token actual, Token expected) {
        assertEquals(expected.getType(), actual.getType(), "Token types are not equal");
        assertEquals(expected.getValue().getClass(), actual.getValue().getClass(), "Token value classes are not equal");
        assertEquals(expected.getValue().asText(), actual.getValue().asText(), "Token values are not equal");
    }
}