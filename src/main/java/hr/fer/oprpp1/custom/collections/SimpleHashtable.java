package hr.fer.oprpp1.custom.collections;

import java.util.*;
import static java.lang.Math.*;

/**
 * Implementacija mape koja sprema elemente kao par ključ vrijednost.
 * Za indeksiranje parova koristi se funcija <code>hashCode</code> nad ključem te se tada par
 * sprema u povezanu listu na toj poziciji u polju.
 * Ključevi moraju biti jedinstveni i ne smiju bit <code>null</code> dok vrijednosti mogu biti.
 * @param <K> vrsta ključa koji će se spremati u <code>Dictionary</code>
 * @param <V> vrsta vrijednosti koja će se spremati u <code>Dictionary</code>
 * @author Dominik
 */
public class SimpleHashtable<K, V> implements Iterable<SimpleHashtable.TableEntry<K,V>> {

    /**
     * Privatan razred koji modelira jedan par vrijednosti.
     * @param <K>
     * @param <V>
     */
    public static class TableEntry<K, V> {

        /**
         * Ključ
         */
        private final K key;

        /**
         * Vrijednost
         */
        private V value;

        /**
         * Referenca na sljedeći par u povezanoj listi.
         */
        private TableEntry<K, V> next;

        /**
         * Stvara novi par s poslanim vrijednostima.
         * @param key ključ para
         * @param value vriejdnost para
         */
        public TableEntry(K key, V value, TableEntry<K, V> next) {
            this.key = key;
            this.value = value;
            this.next = next;
        }

        /**
         * Vraća ključ para.
         * @return ključ para
         */
        public K getKey() {
            return key;
        }

        /**
         * Vraća vrijednost para.
         * @return vriejdnost para
         */
        public V getValue() {
            return value;
        }

        /**
         * Vraća sljedeći par u listi
         * @return sljedeći par u listi
         */
        public TableEntry<K, V> getNext() {
            return next;
        }

        /**
         * Postavlja novu vrijednost para
         * @param value nova vrijednost
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Postavlja referencu <code>next</code> na poslani element.
         * @param next nova vrijednost reference
         */
        public void setNext(TableEntry<K, V> next) {
            this.next = next;
        }

        /**
         * Vraća string zapisan kao <code>"ključ=vrijednost"</code>
         * @return string zapis para
         */
        @Override
        public String toString() {
            return String.format("%s=%s", key, value);
        }

        /**
         * Provjerava da li je poslani objekt isti kao i objekt nad kojim se funckija poziva
         * @param obj objekt koji testiramo
         * @return <code>true</code> ako je poslani objekt isti, inače <code>false</code>
         */
        @Override
        public boolean equals(Object obj) {
            if(obj instanceof TableEntry)
                return this.key.equals(((TableEntry<?, ?>) obj).key);
            else
                return false;
        }
    }

    /**
     * Polje u kojem se nalaze liste parova
     */
    private TableEntry<K, V>[] table;

    /**
     * Broj parova spremljenih u kolekciju.
     */
    private int size;

    /**
     * Broj promjena koje su se dogodile od stvaranj objekta.
     */
    private int modificationCount;

    /**
     * Stvara novu instancu razreda i postavlja veličinu polja na 16.
     */
    public SimpleHashtable() {
        this(16);
    }

    /**
     * Stvara novu instancu razreda i postavlja veličinu polja na poslanu vrijednost.
     * @throws IllegalArgumentException ako je poslana inicijalna veličina polja manja od 1
     */
    public SimpleHashtable(int capacity) {
        if(capacity < 1)
            throw new IllegalArgumentException("Capacity must be larger then 1!");

        if(capacity % 2 == 0)
            this.table = (TableEntry<K, V>[]) new TableEntry[capacity];
        else
            this.table = (TableEntry<K, V>[]) new TableEntry[(int) floor(log(capacity)/log(2))];

        this.size = 0;
        modificationCount = 0;
    }

    /**
     * Privatna funckija koja vraća par u kolekciji s poslanim ključem.
     * @param key ključ para koji tražimo
     * @return par s vrijednosti poslanog ključa ako takav postoji, inače vraća <code>null</code>
     */
    private TableEntry<K, V> findEntry(Object key) {

        int index = abs(key.hashCode()) % table.length;

        TableEntry<K, V> entry = table[index];
        while(entry != null) {
            if(entry.key.equals(key)) {
                return entry;
            }
            entry = entry.next;
        }
        return null;
    }

    /**
     * Provjerava popunjenost polja.
     * Ukoliko je popunjenost 75% ili veća, tj. broj elemeata ja u kolekciji je veći od 75% veličine polja
     * tada se stvara novo polje dvostruke veličine i ponovno se smještaju elementi spremljeni elementi prema njihovom hash-u.
     */
    private void checkCapacity() {
        if((float) size < 0.75 * table.length)
            return;

        TableEntry<K, V>[] entryArray = this.toArray();

        table = (TableEntry<K, V>[]) new TableEntry[table.length*2];

        for(TableEntry<K, V> entry : entryArray) {
            entry.next = null;
            int index = abs(entry.key.hashCode()) % table.length;
            if(table[index] == null) {
                table[index] = entry;
            } else {
                TableEntry<K, V> temp = table[index];

                while(temp.next != null) {
                    temp = temp.next;
                }
                temp.next = entry;
            }

        }

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
        if (key == null)
            throw new NullPointerException("Key can't be null!");

        checkCapacity();

        int index = abs(key.hashCode()) % table.length;

        if(table[index] == null) {
            table[index] = new TableEntry<>(key, value, null);
            modificationCount++;
            size++;
            return null;
        }

        TableEntry<K, V> entry = table[index];
        while (entry.next != null) {
            if (entry.key.equals(key)) {
               break;
            }
            entry = entry.next;
        }

        if(entry.key.equals(key)) {
            V oldValue = entry.value;
            entry.value = value;
            modificationCount++;
            return oldValue;
        }

        entry.next = (new TableEntry<K, V>(key, value, null));
        modificationCount++;
        size++;
        return null;
    }

    /**
     * Vraća vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji.
     * @param key ključ para čiju vrijednost tražimo
     * @return vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji, inače vraća <code>null</code>
     */
    public V get(Object key) {
        if(key == null || size == 0)
            return null;

        TableEntry<K, V> entry = findEntry(key);
        return entry == null ? null : entry.value;
    }

    /**
     * Vraća broj spremljenih parova u <code>Dictionary</code>-u.
     * @return broj spremljenih parova
     */
    public int size() {
        return size;
    }

    /**
     * Provjerava da li postoji par s poslanim ključa.
     * @param key ključ koji provjeravamo
     * @return <code>true</code> ako takav par psotoji, inače <code>false</code>
     */
    public boolean containsKey(Object key) {
        if(key == null)
            return false;

        return findEntry(key) != null;
    }

    /**
     * Provjerava da li postoji par s poslanim vrijednosću.
     * @param value vrijednost koji provjeravamo
     * @return <code>true</code> ako takav par psotoji, inače <code>false</code>
     */
    public boolean containsValue(Object value) {
        if(size == 0)
            return false;

        for(TableEntry entry : table) {
            while(entry != null) {
                if(entry.value.equals(value)) {
                    return true;
                }
                entry = entry.next;
            }
        }

        return false;
    }

    /**
     * Briše par s poslanom vrijednosti ključa ako par postoji.
     * @param key ključ para koji želimo izbrisati
     * @return vrijednost spremljenu u paru s poslanom vrijednosti ključa ako par postoji, inače vraća <code>null</code>
     */
    public V remove(Object key) {
        if(key == null)
            return null;

        int index = abs(key.hashCode()) % table.length;

        TableEntry<K, V> entry = table[index];
        V removedValue = null;
        if(entry.key.equals(key)) {
            table[index] = entry.next;
            removedValue = entry.value;
        } else {
            while(entry.next != null) {
                if(entry.next.key.equals(key)) {
                    removedValue = entry.next.value;
                    entry.next = entry.next.next;
                    break;
                }
                entry = entry.next;
            }
        }

        if(removedValue != null) {
            modificationCount++;
            size--;
        }

        return removedValue;
    }

    /**
     * Provejrava da li se u kolekciji nalazi barem jedan par.
     * @return <code>true</code> ako se u kolekciji nalazi barem jedan par, inače vraća <code>false</code>
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Vraća <code>String</code> svih elemenata spremljenih u kolekciji.
     * @return <code>String</code> svih elemenata spremljenih u kolekciji
     */
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        for(TableEntry<K, V> entry : table) {
            while(entry != null) {
                builder.append(entry.toString());
                builder.append( ", ");
                entry = entry.next;
            }
        }
        builder.replace(builder.length() - 2, builder.length(), "");
        builder.append("]");

        return builder.toString();
    }

    /**
     * Vraća polje svih spremljenih parova onim redom kojim se nalaze u kolekciji.
     * @return polje svih parova
     */
    public TableEntry<K,V>[] toArray() {
        TableEntry<K, V>[] result = (TableEntry<K, V>[]) new TableEntry[size];
        int i = 0;
        for(TableEntry<K, V> entry : table) {
            while(entry != null) {
                result[i++] = entry;
                entry = entry.next;
            }
        }

        return result;
    }

    /**
     * Briše sve elemente kolekvije.
     */
    public void clear() {
        Arrays.fill(table, null);
        modificationCount++;
    }

    /**
     * Razred koji implementira iterator za iteriranje po kolekciji
     */
    private class IteratorImpl implements Iterator<SimpleHashtable.TableEntry<K,V>> {

        /**
         * Trenutna pozicija na kojoj se nalazimo u polju kolekcije.
         */
        private int currentIndex;

        /**
         * Zadnje par koji smo prošli.
         */
        private TableEntry<K,V> currentEntry;

        /**
         * Sprema broj modifikacija nad kolekcijom u trenutku kad je kolekcija stvorena.
         */
        private int savedModificationCount;

        /**
         * Varijabala kojom kontroliramo pozivanje funcije remove.
         */
        private boolean removed;

        /**
         * Stvara novu instancu razreda koja služi za iteriranjem nad kolekcijom.
         */
        public IteratorImpl() {
            currentIndex = 0;
            removed = true;
            savedModificationCount = modificationCount;
        }

        /**
         * Provjera da li je kolekcija bila mijenjana za vrijeme iteracijež
         * @throws ConcurrentModificationException ako je kolekcija mijenjana za vrijeme iteriranja
         */
        private void checkModifications() {
            if(savedModificationCount != modificationCount)
                throw new ConcurrentModificationException("Can't modify and iterate over the collection at the same time!");
        }

        /**
         * Provjerava da li postoji još elemenata kojim nije pronađeno.
         * @return <code>true</code> ako nismo prošli svim elementima, inače <code>false</code>
         */
        @Override
        public boolean hasNext() {
            checkModifications();

            if(currentEntry != null && currentEntry.next != null )
                return true;
            else if(currentIndex == table.length - 1)
                return false;

            int tempCurrent = currentIndex;
            while(tempCurrent < table.length && table[tempCurrent] == null)
                tempCurrent++;

            return tempCurrent != table.length;
        }

        /**
         * Vraća sljedeći element kolekcije kojim nismo prošli.
         * @return sljedeći element kolekcije kojim nismo prošli
         * @throws NoSuchElementException ako se funkcija pozove nakon što smo prošli sve elemente
         */
        @Override
        public TableEntry<K, V> next() {
            checkModifications();

            removed = false;

            // checks for the first call of the function
            if(currentEntry == null && table[currentIndex] != null) {
                currentEntry = table[currentIndex];
                return currentEntry;
            }

            if(currentEntry != null && currentEntry.next != null) {
                currentEntry = currentEntry.next;
                return currentEntry;
            }

            do {
                currentIndex++;
            } while(currentIndex < table.length && table[currentIndex] == null);

            if(currentIndex == table.length)
                throw new NoSuchElementException("There are no more elements!");

            currentEntry = table[currentIndex];
            return currentEntry;
        }

        /**
         * Briše zadnje poslani par iz kolekcije i ažurira interni broj promjena kolekcije.
         * @throws IllegalStateException ako se funcija pozove više puta nakon istog poziva <code>next</code> funkcije
         */
        @Override
        public void remove() {
            checkModifications();

            if(removed)
                throw new IllegalStateException("Remove can only be called once after every next() code");

            SimpleHashtable.this.remove(currentEntry.key);
            savedModificationCount = modificationCount;
            removed = true;
        }
    }

    /**
     * Funkcija stvara i vraća novi <code>Iterator</code> nad kolekcijom
     * @return <code>Iterator</code> nad kolekcijom
     */
    @Override
    public Iterator<TableEntry<K, V>> iterator() {
        return new IteratorImpl();
    }

}
