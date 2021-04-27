package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.List;

/**
 * Parser koji parsira primljene upite.
 */
public class QueryParser {

    /**
     * Lista uvjeta koja nastaje nakon parsiranja stringa.
     */
    List<ConditionalExpression> conditions;

    /**
     * Stvara instancu razreda i poziva metodu za parsiranje prosljeđenog stringa
     * @param queryLine string koji se parsira
     */
    public QueryParser(String queryLine) {
        conditions = new ArrayList<>();
        parse(queryLine);
    }

    /**
     * Privatna fununkcija koja prasira primljeni string
     * @param queryLine string koji treba parsirati
     * @throws IllegalArgumentException ako primljeni string nije ispravan
     */
    private void parse(String queryLine) {
        try {
            QueryLexer lexer = new QueryLexer(queryLine);

            while (lexer.nextToken().getType() != TokenType.EOF) {
                Token token = lexer.getToken();

                if(token.getType() == TokenType.ATTRIBUTE) {
                    IFieldValueGetter getter;
                    IComparisonOperator operator;
                    String literal;
                    getter = switch(token.getValue()) {
                        case "jmbag" -> FieldValueGetters.JMBAG;
                        case "lastName" -> FieldValueGetters.LAST_NAME;
                        case "firstName" -> FieldValueGetters.FIRST_NAME;
                        default -> throw new IllegalArgumentException("Unknown attribute: " +token.getValue());
                    };

                    token = lexer.nextToken();
                    if(token.getType() != TokenType.OPERATOR)
                        throw new IllegalArgumentException("Expected to receive an operator. Received: " +token.getType());

                    operator = switch(token.getValue()) {
                        case "<" -> ComparisonOperators.LESS;
                        case "<=" -> ComparisonOperators.LESS_OR_EQUALS;
                        case ">" -> ComparisonOperators.GREATER;
                        case ">=" -> ComparisonOperators.GREATER_OR_EQUALS;
                        case "=" -> ComparisonOperators.EQUALS;
                        case "LIKE" -> ComparisonOperators.LIKE;
                        default -> throw new IllegalArgumentException("Expected to receive an operator. Received: " +token.getValue());
                    };

                    token = lexer.nextToken();
                    if(token.getType() != TokenType.STRING_LITERAL)
                        throw new IllegalArgumentException("Expected to receive an string literal. Received: " +token.getType());

                    literal = token.getValue();

                    conditions.add(new ConditionalExpression(getter, literal, operator));
                } else if(token.getType() == TokenType.AND) {
                    continue;
                } else throw new IllegalArgumentException("Invalid query command");
            }
        } catch (LexerException ex) {
            throw new IllegalArgumentException("Error while parsing.\n" + ex.getMessage());
        }
    }

    /**
     * Funkcija koja ispituje da li je parsirani upite direktni, tj. da li je oblika jmbag="xyz";
     * @return <code>true</code> ako upit je direktan, inače <code>false</code>
     */
    public boolean isDirectQuery() {
        if(conditions.size() != 1)
            return false;

        return conditions.get(0).getComparisonOperator() == ComparisonOperators.EQUALS &&
                conditions.get(0).getFieldGetter() == FieldValueGetters.JMBAG;
    }

    /**
     * Ako je upit direktan vraća jmbag za koji se traži zapis
     * @return jmbag za koji se traži zapis
     * @throws IllegalStateException ako upit nije direktan
     */
    public String getQueriedJMBAG() {
        if(!isDirectQuery())
            throw new IllegalStateException("Query is not direct");

        String result = conditions.get(0).getStringLiteral();
        return result;
    }

    public List<ConditionalExpression> getQuery() {
        return this.conditions;
    }
}
