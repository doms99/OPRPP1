package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja String (tekstualni zapis).
 */
public class ElementString extends Element {

    /**
     * Tekstolanog zapisa.
     */
    private String value;

    /**
     * Stvara novu instancu razreda s primljenim Stringom.
     * @param value vrijednost tekstualnog zapisa
     */
    public ElementString(String value) {
        this.value = value;
    }

    /**
     * Vraća tekstualni zapis spremljen u elementu.
     * @return tekstualni zapis
     */
    @Override
    public String asText() {
        return value;
    }
}
