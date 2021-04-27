package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje implementira <code>Collection</code> te daje dodatne funcionalnosti.
 * Upravljanje i rad s skupom objekata.
 * @author Dominik
 */
public interface List<T> extends  Collection<T> {

    /**
     * Vraća element kolekcije spremljen na poslanoj poziciji.
     * @param index pozicija na kojoj tražimo element
     * @return element spremljen na poslanoj poziciji
     */
    T get(int index);

    /**
     * Briše element koji se trenutno nalazi na poslanoj poziciji i na to mjesto sprema novi element
     * @param value nova vrijednost koja se sprema
     * @param position pozicija na koju dodajemo novi element
     * @param <K> vrsta elemenata koji se sprema, iste klase kao i elementi kolekcije ili je podklasa
     */
    <K extends T> void insert(K value, int position);

    /**
     * Traži poslani objekt u listi.
     * @param value objekt koji se traži
     * @return indeks pozicije na kojem se nalazi prvi objekt koji je jednak poslanom objektu, inače vraća -1 ako objekt nije pronađen
     */
    int indexOf(T value);

    /**
     * Briše element liste na poslanoj poziciji.
     * @param index pozicija s koje brišemo objekt
     */
    void remove(int index);
}
