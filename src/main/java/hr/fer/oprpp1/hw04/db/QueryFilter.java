package hr.fer.oprpp1.hw04.db;

import java.util.List;

/**
 * Klasa koja će filtrirati sve studente koji ne zadovoljavaju uvjete predane u listi tijekom stvaranja.
 */
public class QueryFilter implements IFilter {

    /**
     * Lista uvjeta
     */
    private List<ConditionalExpression> conditions;

    /**
     * Stvara instancu razreda i sprema listu uvjeta.
     * @param conditions lista uvjeta
     */
    public QueryFilter(List<ConditionalExpression> conditions) {
        this.conditions = conditions;
    }

    /**
     * Za prosljeđeni zapis testira da li on zadovoljava sve uvjete iz liste
     * @param record element baze koji se ispituje
     * @return <code>true</code> ako je zapapis zadovoljava sve uvjete, inače <code>false</code>
     */
    @Override
    public boolean accepts(StudentRecord record) {
        boolean result = true;

        for(ConditionalExpression cond : conditions) {
            if(!cond.getComparisonOperator().satisfied(
                    cond.getFieldGetter().get(record),
                    cond.getStringLiteral())) {
                result = false;
                break;
            }
        }

        return result;
    }
}
