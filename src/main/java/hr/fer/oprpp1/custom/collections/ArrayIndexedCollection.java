package hr.fer.oprpp1.custom.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Klasa koja nasljeđuje Collection.
 * Implementira polje u kojem čuva poslane objekte.
 * @author Dominik
 */
public class ArrayIndexedCollection<T> implements List<T> {

    /**
     * Služi nam za brojanje koliko je trenutno pohranjeno objekata u kolekciji.
     */
    private int size;

    /**
     * Stvarno polje u kojem čuvamo objekte.
     */
    private T[] elements;

    /**
     * Broji koliko puta se polje promijenilo
     */
    private long modificationCount;

    /**
     * Default konstruktor.
     * Početna veličina polja postavlja se na 16.
     */
    public ArrayIndexedCollection() {
        this(16);
    }

    /**
     * Konstruktor koji prima početnu veličinu polja.
     * @param initialCapacity početna veličina polja
     * @throws IllegalArgumentException ako je poslani parametar manji od 1
     */
    public ArrayIndexedCollection(int initialCapacity) {
        if(initialCapacity < 1)
            throw new IllegalArgumentException("Initial capacity can't be smaller then 1! Was sent "+ initialCapacity);

        elements = (T[]) new Object[initialCapacity];
        this.modificationCount = 0;
        this.size = 0;
    }

    /**
     * Kontruktor koji prima postojeću kolekciju i kopira elemente.
     * @param other kolkecija koja se kopira
     * @throws NullPointerException ako je poslana null vrjednost umjesto kolekcije
     */
    public ArrayIndexedCollection(Collection<? extends T> other) {
        this(other, 1);
    }

    /**
     * Kontruktor koji prima postojeću kolekciju i početnu veličinu polja.
     * Ukoliko je početna veličina polja manja od veličine poslane kolekcije, polje će imati istu veličinu kao i poslana kolekcija.
     * @param other kolekcija koja se kopira
     * @param initialCapacity željena početna vrijednost polja
     * @throws NullPointerException ako je poslana null vrjednost umjesto kolekcije
     */
    public ArrayIndexedCollection(Collection<? extends T> other, int initialCapacity) {
        this(other.size() > initialCapacity ? other.size() : initialCapacity);
        other.forEach(value ->  elements[size++] = value);
    }

    /**
     * Metoda koja se koristi interno kako bi se poduplala veličina polja
     */
    private void doubleArray() {
        this.elements = Arrays.copyOf(this.elements, this.elements.length*2);
    }

    /**
     * Metoda za testiranje prave veličine polja
     * @return veličinu polja
     */
    int arraySize() { return this.elements.length; }

    /**
     * Provjerava da li je polje prazno, tj. da li je u njemu spremljen ijedan objekt.
     * @return <code>true</code> ako je u polje spremljen barem 1 objekt, inače <code>false</code>
     */
    @Override
    public boolean isEmpty() {                              //jesam
        return this.size() == 0;
    }

    /**
     * Vraća broj objekata spremljenih u polju.
     * @return broj elemenata spremljenih u polju
     */
    @Override
    public int size() {                                       //jesam
        return this.size;
    }

    /**
     * Dodaje novi objekt u polje.
     * @param value objekt koji se dodaje
     * @throws NullPointerException ako se pokuša spremiti null vrijednost
     */
    @Override
    public <K extends T> void add(K value) {
        if(value == null)
            throw new NullPointerException();
        if(size == elements.length) doubleArray();

        this.elements[size++] = value;
        this.modificationCount++;
    }

    /**
     * Vraća objekt koji se nalazi na zadanom poziciji u polju.
     * @param index pozicija u polju
     * @return Objekt na zadanoj poziciji u polju
     * @throws IndexOutOfBoundsException ako se pokušava dohvatiti objekt na poziciji koja nije bila postavljena ili ako je pozicija neispravna
     */
    public T get(int index) {
        if(index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Received "+ index +", should be between 0 and "+ (this.elements.length-1));
        return this.elements[index];
        // Average complexity is 1 because we are not searching trough the array we are just getting the reference that is at that index
        // I didn't check if the index is in range because it will automatically throw an exception IndexOutOfBoundsException
    }

    /**
     * Umeće poslani objekt u polje na poslani poziciji.
     * @param value objekt koji se upisuje u polje
     * @param position pozicija na kojoj se upisuje objekt
     * @throws NullPointerException ako se pokuša spremiti null vrijednost
     * @throws IndexOutOfBoundsException ako je poslani pozicija umetanja nevažeća
     */
    public <K extends T> void insert(K value, int position) {
        if(value == null)
            throw new NullPointerException();
        if(position < 0 || position > this.size)
            throw new IndexOutOfBoundsException("Received "+ position +", should be between 0 and "+ (this.elements.length-1));
        if(this.size == this.elements.length) doubleArray();
        for(int i = size - 1; i >= position; i--) {
            this.elements[i+1] = this.elements[i];
        }
        this.elements[position] = value;
        this.size++;
        this.modificationCount++;
        // This function has linear complexity because it directly depends on the size of the array.
        // On average it well have to move half of the elements in the array
    }

    /**
     * Traži poslani objekt u polju.
     * @param value objekt koji se traži
     * @return indeks pozicije na kojem se nalazi prvi objekt koji je jednak poslani objektu, inače vraća -1 ako objekt nije pronađen
     */
    public int indexOf(Object value) {
        if(elements.length == 0 || value == null)
            return -1;

        int index = -1;
        for(int i = 0; i < size; i++) {
            if(elements[i].equals(value)) {
                index = i;
                break;
            }
        }
        return index;
        // This function has linear complexity because it directly depends on the size of the array.
        // Like the last one on average it will have to go through half of the elements in the array.
    }

    /**
     * Provjerava da li se u polju nalazi poslani u objekt.
     * @param value objekt kojeg tražimo
     * @return  <code>true</code> ako se objekt nalazi u polju, inače <code>false</code>
     */
    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    /**
     * Briše element polja na poslanom indeksu.
     * @param index pozicija s koje brišemo objekt
     * @throws IndexOutOfBoundsException ako je poslana pozicija nevažeća
     */
    public void remove(int index) {
        if(index < 0 || index >= this.size)
            throw new IndexOutOfBoundsException("Received "+ index +", should be between 0 and "+ (this.elements.length-1));
        for(int i = index; i < size; i++) {
            elements[i] = elements[i+1];
        }
        elements[--size] = null;
        this.modificationCount++;
    }

    /**
     * Briše objekt iz polja ako on postoji u polju.
     * @param value objekt koji želimo obrisati
     * @return <code>true<code> ako je objekt pronađen i obrisan, inače <code>false</code>
     */
    @Override
    public boolean remove(Object value) {
        int index = indexOf(value);
        if(index != -1) {
            remove(index);
            return true;
        }
        return false;
    }

    /**
     * Vraća novo polje svih objekatau polja kolekcije.
     * @return novo polje sa objektima
     */
    @Override
    public Object[] toArray() {
        return Arrays.copyOfRange(elements, 0, this.size);
    }

    /**
     * Briše sve elemente polja.
     */
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.elements, null);
        this.modificationCount++;
    }

    /**
     * Razred koji implementira sučelje <code>ElementsGetter</code>.
     * Razred služi za iteriranje nad elementima kolekcije.
     */
    private static class ArrayElementsGetter implements ElementsGetter {

        /**
         * Indeks zadnjeg element u polju kojim smo prošli.
         */
        private int current;

        /**
         * Kolekcija po kojoj se provodi iteracija.
         */
        private ArrayIndexedCollection collection;

        /**
         * Sprema broj modifikacija nad kolekcijom u trenutku kad je kolekcija stvorena.
         */
        private long savedModificationCount;

        /**
         * Stvara novu instancu razreda koja služi za iteriranjem nad poslanom kolekcijom.
         * @param collection kolekcija nad kojom se iterira
         */
        ArrayElementsGetter(ArrayIndexedCollection collection) {
            this.current = 0;
            this.savedModificationCount = collection.modificationCount;
            this.collection = collection;
        }

        /**
         * Provjera da li je kolekcija bila mijenjana za vrijeme iteracijež
         * @throws ConcurrentModificationException ako je kolekcija mijenjana za vrijeme iteriranja
         */
        private void checkModifications() {
            if(savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Can't iterate over and modify the collection at the same time!");
        }

        /**
         * Provjerava da li postoji još elemenata kojim nije pronađeno.
         * @return <code>true</code> ako nismo prošli svim elementima, inače <code>false</code>
         */
        @Override
        public boolean hasNextElement() {
            checkModifications();
            return current < collection.size;
        }

        /**
         * Vraća sljedeći element kolekcije kojim nismo prošli.
         * @return sljedeći element kolekcije kojim nismo prošli
         */
        @Override
        public Object getNextElement() {
            checkModifications();
            if(!this.hasNextElement())
                throw new NoSuchElementException("There are no elements left in collection");
            return collection.elements[current++];
        }
    }

    /**
     * Funkcija stvara i vraća novi <code>ElementsGetter</code> nad kolekcijom
     * @return <code>ElementsGetter</code> nad kolekcijom
     */
    @Override
    public ElementsGetter createElementsGetter() {
        return new ArrayElementsGetter(this);
    }
}
