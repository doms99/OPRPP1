package hr.fer.oprpp1.custom.collections;

/**
 * Klasa koju nasljeđuju sve ostale kolekcije koje ćemo implementirati
 * Upravljanje i rad s skupom objekata
 * @author Dominik
 */
public interface Collection {

    /**
     * @return broj objekata u kolekciji
     */
    int size();

    /**
     * Provjerava da li kolkecija sadrži barem 1 objekt.
     * @return <code>true</code> ako ne sadrži, inače vraća <code>false</code>
     */
    default boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Dodaje poslani objekt u kolekciju.
     * @param value objekt koji se dodaje
     */
    void add(Object value);

    /**
     * Provjerava da li kolekcija sadrži poslani objekt.
     * @param value objekt kojeg tražimo
     * @return <code>true</code> ako se objekt nalazi u koleciji, inače <code>false</code>
     */
    boolean contains(Object value);

    /**
     * Brišemo poslani objekt ako se on nalazi u kolekciji. To provjeravamo s <code>equels</code> metodom.
     * @param value objekt koji želimo obrisati
     * @return  <code>true</code> ako je pronađen poslani objekt, inače <code>false</code>
     */
    boolean remove(Object value);

    /**
     * @return novi <code>Object[]</code> array u kojem se nalaze svi objekti kolekcije
     * @throws UnsupportedOperationException trenutno nije implementirana
     */
    Object[] toArray();

    /**
     * Nad svakim objektom se poziva metoda <code>.process(.)</code> iz poslanog objekta Processor.
     * @param processor objekt u kojem se nalazi metoda <code>process</code>
     */
    default void forEach(Processor processor) {
        ElementsGetter getter = this.createElementsGetter();
        while(getter.hasNextElement()) {
            processor.process(getter.getNextElement());
        }
    }

    /**
     * Dodaje sve objekte poslane kolekcije u svoju kolekciju.
     * @param other kolekcija koja sadrži elemente koje moramo dodati
     */
    default void addAll(Collection other) {
        other.forEach(new Processor() {
            public void process(Object value) {
                add(value);
            }
        });
    }

    /**
     * Briše sve objekte kolekcije.
     */
    void clear();

    ElementsGetter createElementsGetter();

    default void addAllSatisfying(Collection col, Tester tester) {
        col.forEach(new Processor() {
            @Override
            public void process(Object value) {
                if(tester.test(value)) add(value);
            }
        });
    }
}
