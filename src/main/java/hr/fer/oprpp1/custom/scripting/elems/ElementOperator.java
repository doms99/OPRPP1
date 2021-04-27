package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja operator.
 */
public class ElementOperator extends Element {

    /**
     * Operator zapisan kao String.
     */
    private String symbol;

    /**
     * Stvara novu instancu razreda s poslanim operatorom.
     * @param symbol operator
     */
    public ElementOperator(String symbol) {
        this.symbol = symbol;
    }

    /**
     * Vraća operator zapisan kao String.
     * @return operator spremljen u lementu
     */
    @Override
    public String asText() {
        return symbol;
    }
}
