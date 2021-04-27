package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ConditionalExpressionTest {

    @Test
    public void testConstructor() {
        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Bos*",
                ComparisonOperators.LIKE
        );
        assertEquals(FieldValueGetters.LAST_NAME, expr.getFieldGetter(), "Field getter");
        assertEquals("Bos*", expr.getStringLiteral(), "String literal");
        assertEquals(ComparisonOperators.LIKE, expr.getComparisonOperator(), "Comparison operator");
    }

    @Test
    public void testFunctionality() {
        ConditionalExpression expr = new ConditionalExpression(
                FieldValueGetters.LAST_NAME,
                "Bos*",
                ComparisonOperators.LIKE
        );

        boolean falseSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(new StudentRecord("00225561", "Parica", "Pero", 3)),
                expr.getStringLiteral()
        );
        boolean trueSatisfies = expr.getComparisonOperator().satisfied(
                expr.getFieldGetter().get(new StudentRecord("00225561", "Bosić", "Pero", 3)),
                expr.getStringLiteral()
        );


        assertFalse(falseSatisfies);
        assertTrue(trueSatisfies);
    }
}
