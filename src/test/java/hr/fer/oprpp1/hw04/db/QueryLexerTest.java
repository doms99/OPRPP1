package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryLexerTest {

    @Test
    public void testParsing() {
        QueryLexer lexer = new QueryLexer("jmbag=\"00000001\"");

        assertEquals(TokenType.ATTRIBUTE, lexer.nextToken().getType(), "Attribute type");
        assertEquals("jmbag", lexer.getToken().getValue(), "Attribute value");
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType(), "Operator type");
        assertEquals("=", lexer.getToken().getValue(), "Operator value");
        assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getType(), "String STRING_LITERAL type");
        assertEquals("00000001", lexer.getToken().getValue(), "String STRING_LITERAL value");
        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "EOF type");
    }

    @Test
    public void testSpaceParsing() {
        QueryLexer lexer = new QueryLexer("     jmbag       =\t\"00000001\"     ");
        assertEquals(TokenType.ATTRIBUTE, lexer.nextToken().getType(), "Attribute type");
        assertEquals("jmbag", lexer.getToken().getValue(), "Attribute value");
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType(), "Operator type");
        assertEquals("=", lexer.getToken().getValue(), "Operator value");
        assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getType(), "String STRING_LITERAL type");
        assertEquals("00000001", lexer.getToken().getValue(), "String STRING_LITERAL value");
        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "EOF type");
    }

    @Test
    public void testInvalidAttributeParsing() {
        QueryLexer lexer = new QueryLexer("1jmbag=\"00000001");
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }

    @Test
    public void testMultipleConditions() {
        QueryLexer lexer = new QueryLexer("jmbag=\"00000001\" anD lastName>\"Ana\"");

        assertEquals(TokenType.ATTRIBUTE, lexer.nextToken().getType(), "First attribute type");
        assertEquals("jmbag", lexer.getToken().getValue(), "First attribute value");
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType(), "First operator type");
        assertEquals("=", lexer.getToken().getValue(), "First operator value");
        assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getType(), "First string STRING_LITERAL type");
        assertEquals("00000001", lexer.getToken().getValue(), "First string STRING_LITERAL value");
        assertEquals(TokenType.AND, lexer.nextToken().getType(), "AND type");
        assertEquals("and", lexer.getToken().getValue(), "and value");
        assertEquals(TokenType.ATTRIBUTE, lexer.nextToken().getType(), "Second attribute type");
        assertEquals("lastName", lexer.getToken().getValue(), "Second attribute value");
        assertEquals(TokenType.OPERATOR, lexer.nextToken().getType(), "Second operator type");
        assertEquals(">", lexer.getToken().getValue(), "Second operator value");
        assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getType(), "Second string STRING_LITERAL type");
        assertEquals("Ana", lexer.getToken().getValue(), "Second string STRING_LITERAL value");
        assertEquals(TokenType.EOF, lexer.nextToken().getType(), "EOF type");
    }

    @Test
    public void testMissingElement() {
        QueryLexer lexer = new QueryLexer("lastName \"Ana\"");
        assertEquals(TokenType.ATTRIBUTE, lexer.nextToken().getType(), "attribute type");
        assertEquals("lastName", lexer.getToken().getValue(), "attribute value");
        assertEquals(TokenType.STRING_LITERAL, lexer.nextToken().getType(), "attribute type");
        assertEquals("Ana", lexer.getToken().getValue(), "attribute value");
    }

    @Test
    public void testUnclosedStringLiteral() {
        QueryLexer lexer = new QueryLexer("\"nikad nije zatvoren");
        assertThrows(LexerException.class, () -> lexer.nextToken());
    }
}
