package hr.fer.oprpp1.custom.collections;

/**
 * Model klase koja sadrži metodu koja će nešto raditi sa prosljeđenjim objektom
 * @author Dominik
 */
public interface Processor {

    /**
     * Metoda koja će nešto raditi nad prosljeđenim objektom
     * @param value objekt nad kojim se provodi radnja
     */
    void process(Object value);
}
