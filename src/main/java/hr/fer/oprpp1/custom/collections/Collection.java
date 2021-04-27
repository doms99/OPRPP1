package hr.fer.oprpp1.custom.collections;

/**
 * Klasa koju nasljeđuju sve ostale kolekcije koje ćemo implementirati
 * Upravljanje i rad s skupom objekata
 * @author Dominik
 */
public class Collection {

    /**
     * Default konstruktor.
     */
    protected Collection() {};

    /**
     * @return broj objekata u kolekciji
     */
    public int size() {
        return 0;
    }

    /**
     * Provjerava da li kolkecija sadrži barem 1 objekt.
     * @return <code>true</code> ako ne sadrži, inače vraća <code>false</code>
     */
    public boolean isEmpty() {
        return this.size() == 0;
    }

    /**
     * Dodaje poslani objekt u kolekciju.
     * @param value objekt koji se dodaje
     */
    public void add(Object value) {
        return;
    }

    /**
     * Provjerava da li kolekcija sadrži poslani objekt.
     * @param value objekt kojeg tražimo
     * @return <code>true</code> ako se objekt nalazi u koleciji, inače <code>false</code>
     */
    public boolean contains(Object value) {
        return false;
    }

    /**
     * Brišemo poslani objekt ako se on nalazi u kolekciji. To provjeravamo s <code>equels</code> metodom.
     * @param value objekt koji želimo obrisati
     * @return  <code>true</code> ako je pronađen poslani objekt, inače <code>false</code>
     */
    public boolean remove(Object value) {
        return false;
    }

    /**
     * @return novi <code>Object[]</code> array u kojem se nalaze svi objekti kolekcije
     * @throws UnsupportedOperationException trenutno nije implementirana
     */
    public  Object[] toArray() {
        throw new UnsupportedOperationException("Haven't implemented the method yet.");
    }

    /**
     * Nad svakim objektom se poziva metoda <code>.process(.)</code> iz poslanog objekta Processor.
     * @param processor objekt u kojem se nalazi metoda <code>process</code>
     */
    public void forEach(Processor processor) {}

    /**
     * Dodaje sve objekte poslane kolekcije u svoju kolekciju.
     * @param other kolekcija koja sadrži elemente koje moramo dodati
     */
    public void addAll(Collection other) {
        class AddingProcessor extends Processor {

            public void process(Object value) {
                add(value);
            }
        }
        other.forEach(new AddingProcessor());
    }

    /**
     * Briše sve objekte kolekcije.
     */
    public void clear() {}
}
