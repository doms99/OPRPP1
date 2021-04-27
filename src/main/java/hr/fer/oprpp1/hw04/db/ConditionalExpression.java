package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Klasa kojom će se testirati zapis za određeni upit.
 */
public class ConditionalExpression {

    /**
     * Vrijednost iz zapisa koju ćemo testirati.
     */
    private IFieldValueGetter fieldGetter;

    /**
     * Vrijednost s kojom se uspoređuje zapis.
     */
    private String value;

    /**
     * Operator usporedbe.
     */
    private IComparisonOperator comparisonOperator;

    /**
     * Stvara novu instancu razreda s prosljeđenim atributima.
     * @param fieldGetter Vrijednost iz zapisa koju ćemo testirati
     * @param value Vrijednost s kojom se uspoređuje zapis
     * @param comparisonOperator Operator usporedbe
     */
    public ConditionalExpression(IFieldValueGetter fieldGetter, String value, IComparisonOperator comparisonOperator) {
        this.fieldGetter = fieldGetter;
        this.value = value;
        this.comparisonOperator = comparisonOperator;
    }

    /**
     * Vraća vrijednost iz zapisa koju ćemo testirati
     * @return vrijednost iz zapisa koju ćemo testirati
     */
    public IFieldValueGetter getFieldGetter() {
        return fieldGetter;
    }

    /**
     * Vraća vrijednost s kojom se uspoređuje zapis
     * @return vrijednost s kojom se uspoređuje zapis
     */
    public String getStringLiteral() {
        return value;
    }

    /**
     * Vraća operator usporedbe
     * @return operator usporedbe
     */
    public IComparisonOperator getComparisonOperator() {
        return comparisonOperator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConditionalExpression that = (ConditionalExpression) o;
        return fieldGetter.equals(that.fieldGetter) &&
                value.equals(that.value) &&
                comparisonOperator.equals(that.comparisonOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fieldGetter, value, comparisonOperator);
    }
}
