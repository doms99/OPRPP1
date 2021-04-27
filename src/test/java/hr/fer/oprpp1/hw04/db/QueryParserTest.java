package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class QueryParserTest {

    @Test
    public void testDirectQuery() {
        QueryParser parser = new QueryParser("jmbag = \"00000001\"");
        assertTrue(parser.isDirectQuery(), "isDirectQuery");
        assertEquals("00000001", parser.getQueriedJMBAG(), "getQueriedJMBAG");
    }

    @Test
    public void testNotDirectQuery() {
        QueryParser parser = new QueryParser("jmbag < \"00000001\"");
        assertFalse(parser.isDirectQuery(), "isDirectQuery");
        assertThrows(IllegalStateException.class, () -> parser.getQueriedJMBAG(), "getQueriedJMBAG");
    }

    @Test
    public void testInvalidAttribute() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmb1ag < \"00000001\""));
    }

    @Test
    public void testMissingOperator() {
        assertThrows(IllegalArgumentException.class, () -> new QueryParser("jmb1ag \"00000001\""));
    }

    @Test
    public void testQueryList() {
        QueryParser parser = new QueryParser("jmbag < \"00000001\" and lastName LIKE \"H*\"");
        List<ConditionalExpression> expected = new ArrayList<>();
        expected.add(new ConditionalExpression(FieldValueGetters.JMBAG, "00000001", ComparisonOperators.LESS));
        expected.add(new ConditionalExpression(FieldValueGetters.LAST_NAME, "H*", ComparisonOperators.LIKE));

        assertArrayEquals(expected.toArray(), parser.getQuery().toArray());
    }
}
