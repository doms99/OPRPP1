package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja varijablu.
 */
public class ElementVariable extends Element {

    /**
     * Ime varijable.
     */
    private String property;

    /**
     * Stvara novu instancu razreda i poslanim imenom varijable.
     * @param property
     */
    public ElementVariable(String property) {
        this.property = property;
    }

    /**
     * Vraća ime varijable spremljeno u elementu.
     * @return ime varijable
     */
    @Override
    public String asText() {
        return property;
    }
}
