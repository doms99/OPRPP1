package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComparisonOperatorsTest {

    @Test
    public void testLESS() {
        assertTrue(ComparisonOperators.LESS.satisfied("aas", "zsd"), "testing true");
        assertFalse(ComparisonOperators.LESS.satisfied("faa", "eaa"), "testing false");
    }

    @Test
    public void testLESS_OR_EQUALS() {
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("a", "z"), "testing true");
        assertFalse(ComparisonOperators.LESS_OR_EQUALS.satisfied("f", "e"), "testing false");
        assertTrue(ComparisonOperators.LESS_OR_EQUALS.satisfied("fasd", "fasd"), "testing equal");
    }

    @Test
    public void testGREATER() {
        assertTrue(ComparisonOperators.GREATER.satisfied("zsd", "aas"), "testing true");
        assertFalse(ComparisonOperators.GREATER.satisfied("eaa", "faa"), "testing false");
    }

    @Test
    public void testGREATER_OR_EQUALS() {
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("z", "a"), "testing true");
        assertFalse(ComparisonOperators.GREATER_OR_EQUALS.satisfied("aba", "baa"), "testing false");
        assertTrue(ComparisonOperators.GREATER_OR_EQUALS.satisfied("dfsdsf", "dfsdsf"), "testing equal");
    }

    @Test
    public void testEQUALS() {
        assertTrue(ComparisonOperators.EQUALS.satisfied("fdssdf sdf", "fdssdf sdf"), "testing true");
        assertFalse(ComparisonOperators.EQUALS.satisfied("fdssdf sdf", "fdssdf sfd"), "testing false");
    }

    @Test
    public void testNOT_EQUALS() {
        assertFalse(ComparisonOperators.NOT_EQUALS.satisfied("fdssdf sdf", "fdssdf sdf"), "testing false");
        assertTrue(ComparisonOperators.NOT_EQUALS.satisfied("fdssdf sdf", "fdssdf sfd"), "testing true");
    }

    @Test
    public void testLIKE() {
        assertFalse(ComparisonOperators.LIKE.satisfied("AAA", "AA*AA"), "first");
        assertTrue(ComparisonOperators.LIKE.satisfied("AAAA", "AA*AA"), "second");
        assertTrue(ComparisonOperators.LIKE.satisfied("AaA", "*"), "third");
    }
}
