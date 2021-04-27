package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja realnu konstantu.
 */
public class ElementConstantDouble extends Element {
    /**
     * Vrijednost realne konstante.
     */
    private double value;

    /**
     * Stavara novu instancu razreda s poslanom vrijednosti.
     * @param value vrijednost realne konstante
     */
    public ElementConstantDouble(double value) {
        this.value = value;
    }

    /**
     * Vraća zapis realne kosntante spremljene u elementu kao String.
     * @return zapis realne kosntante spremljene u elementu
     */
    @Override
    public String asText() {
        return Double.toString(value);
    }
}
