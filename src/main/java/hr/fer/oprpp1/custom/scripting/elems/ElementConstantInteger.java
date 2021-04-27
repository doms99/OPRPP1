package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja cijelobrojnu konstantu.
 */
public class ElementConstantInteger extends Element {

    /**
     * Vrijednost cijelobrojne konstante.
     */
    private int value;

    /**
     * Stavara novu instancu razreda s poslanom vrijednosti.
     * @param value vrijednost cijelobrojne konstante
     */
    public ElementConstantInteger(int value) {
        this.value = value;
    }

    /**
     * Vraća zapis cijelobrojne kosntante spremljene u elementu kao String.
     * @return zapis cijelobrojne kosntante spremljene u elementu
     */
    @Override
    public String asText() {
        return Integer.toString(value);
    }
}
