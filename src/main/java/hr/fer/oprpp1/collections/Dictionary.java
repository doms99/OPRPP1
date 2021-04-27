package hr.fer.oprpp1.collections;

/**
 * Razred koji modelira jednostavan <code>Map</code> u kojem se podatci spremaju parovi ključ vrijednost.
 * Ključevi moraju biti jedinstveni i ne smiju bit <code>null</code> dok vrijednosti mogu biti.
 * @param <K> vrsta ključa koji će se spremati u <code>Dictionary</code>
 * @param <V> vrsta vrijednosti koja će se spremati u <code>Dictionary</code>
 *
 * @author Dominik
 */
public class Dictionary<K, V> {

    /**
     * Privatan razred koji modelira jedan par vrijednosti.
     * @param <K>
     * @param <V>
     */
    private static class Combo<K, V> {

        /**
         * Ključ para
         */
        private final K key;

        /**
         * Vrijednost para
         */
        private V value;

        /**
         * Stvara novi par s poslanim vrijednostima.
         * @param key ključ para
         * @param value vriejdnost para
         */
        public Combo(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Parovi se smatraju jednakim ako su im ključevi jednaki.
         * @param obj <code>Object</code> koji provjeravamo
         * @return <code>true</code> ako su jednaki, inače <code>false</code>
         */
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof Combo)
                return this.key.equals(((Combo<?, ?>) obj).key);
            else
                return false;
        }
    }

    /**
     * Kolekcija u kojoj se spremaju parovi.
     */
    private ArrayIndexedCollection<Combo<K, V>> elements;

    /**
     * Stvara novu instancu razreda i inicijalizira kolekciju.
     */
    public Dictionary() {
        elements = new ArrayIndexedCollection<>();
    }

    /**
     * Provjerava da li se u <code>Dictionary</code>-u nalazi barem jedan par.
     * @return <code>true</code> ako <code>Dictionary</code> nije prazan, inače <code>false</code>
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * Vraća broj spremljenih parova u <code>Dictionary</code>-u.
     * @return broj spremljenih parova
     */
    public int size() {
        return elements.size();
    }

    /**
     * Briše sve parove dodane u <code>Dictionary</code>.
     */
    public void clear() {
        elements.clear();
    }

    /**
     * Stvara novi par s poslanim vrijednostima i sprema ga u <code>Dictionary</code>.
     * Ako već postoji par s istim ključem, njegova vrijednost se postavlja na poslanu vrijednost.
     * @param key ključ para
     * @param value vrijednost para
     * @param <X> vrsta ključ koja može biti podklase ili iste klase kao i vrsta ključa <code>Dictionary</code>-a
     * @param <Y> vrsta vrijednosti koja može biti podklase ili iste klase kao i vrsta vrijednosti <code>Dictionary</code>-a
     * @return <code>null</code> ako u <code>Dictionary</code>-u ne postoji par s istim ključem, inače staru vrijdnost para
     * @throws NullPointerException ako je poslani ključ <code>null</code>
     */
    public <X extends K, Y extends V> V put(X key, Y value) {
        if(key == null)
            throw new NullPointerException("Key can't be null!");

        Combo<K, V> combo = new Combo<>(key, value);
        int index = elements.indexOf(combo);
        if(index != -1) {
            V oldValue = elements.get(index).value;
            elements.remove(index);
            elements.insert(combo, index);
            return oldValue;
        } else {
            elements.add(combo);
            return null;
        }
    }

    /**
     * Vraća vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji.
     * @param key ključ para čiju vrijednost tražimo
     * @return vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji, inače vraća <code>null</code>
     */
    public V get(Object key) {
        if(elements.size() == 0 ||
                key == null)
            return null;

        int index = elements.indexOf(new Combo<>(key, null));
        if(index == -1)
            return null;

        return elements.get(index).value;
    }

    /**
     * Briše par s poslanom vrijednosti ključa ako par postoji.
     * @param key ključ para koji želimo izbrisati
     * @return vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji, inače vraća <code>null</code>
     */
    public V remove(K key) {
        if(key == null)
            return null;

        int index = elements.indexOf(new Combo<>(key, null));
        if(index == -1)
            return null;

        V oldValue = elements.get(index).value;
        elements.remove(index);

        return oldValue;
    }
}
