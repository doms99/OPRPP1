package hr.fer.oprpp1.hw04.db;

/**
 * Klasa koja sadrži statičke varijable s lambda izrazima za dohvaćanje atributa iz <code>StudentRecord</code>a
 */
public class FieldValueGetters {
    public static final IFieldValueGetter FIRST_NAME = s -> s.getFirstName();
    public static final IFieldValueGetter LAST_NAME = s -> s.getLastName();
    public static final IFieldValueGetter JMBAG = s -> s.getJmbag();
}
