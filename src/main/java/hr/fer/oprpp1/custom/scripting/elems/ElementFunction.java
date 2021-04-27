package hr.fer.oprpp1.custom.scripting.elems;

/**
 * Element koji predstavlja funkciju.
 */
public class ElementFunction extends Element {
    /**
     * Ime funkcije.
     */
    private String name;

    /**
     * stvara novu instancu elementa s poslanom vrijednosti.
     * @param name ime funkcije
     */
    public ElementFunction(String name) {
        this.name = name;
    }

    /**
     * Vraća ime funkcije.
     * @return ime funckije
     */
    @Override
    public String asText() {
        return name;
    }
}
