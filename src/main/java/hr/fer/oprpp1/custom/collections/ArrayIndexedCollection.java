package hr.fer.oprpp1.custom.collections;

import java.util.Arrays;

/**
 * Klasa koja nasljeđuje Collection.
 * Implementira polje u kojem čuva poslane objekte.
 * @author Dominik
 */
public class ArrayIndexedCollection extends Collection {

    /**
     * Služi nam za brojanje koliko je trenutno pohranjeno objekata u kolekciji.
     */
    private int size;

    /**
     * Stvarno polje u kojem čuvamo objekte.
     */
    private Object[] elements;

    /**
     * Default konstruktor.
     * Početna veličina polja postavlja se na 16.
     */
    public ArrayIndexedCollection() {                                   //jesam
        this.size = 0;
        elements = new Object[16];
    }

    /**
     * Konstruktor koji prima početnu veličinu polja.
     * @param initialCapacity početna veličina polja
     * @throws IllegalArgumentException ako je poslani parametar manji od 1
     */
    public ArrayIndexedCollection(int initialCapacity) {                //jesam
        if(initialCapacity < 1)
            throw new IllegalArgumentException("Initial capacity can't be smaller then 1! Was sent "+ initialCapacity);
        this.size = 0;
        elements = new Object[initialCapacity];
    }

    /**
     * Kontruktor koji prima postojeću kolekciju i kopira elemente.
     * @param other kolkecija koja se kopira
     * @throws IllegalArgumentException ako je poslana prazna kolekcija
     * @throws NullPointerException ako je poslana null vrjednost umjesto kolekcije
     */
    public ArrayIndexedCollection(Collection other) {                                       //jesam
        if(other.size() == 0)
            throw new IllegalArgumentException("Can't add objects from an empty collection. Size was: "+ other.size());
        this.size = 0;
        elements = new Object[other.size()];
        other.forEach(new Processor() {
            public void process(Object value) {
                elements[size++] = value;
            }
        });
    }

    /**
     * Kontruktor koji prima postojeću kolekciju i početnu veličinu polja.
     * Ukoliko je početna veličina polja manja od veličine poslane kolekcije, polje će imati istu veličinu kao i poslana kolekcija.
     * @param other kolekcija koja se kopira
     * @param initialCapacity željena početna vrijednost polja
     * @throws NullPointerException ako je poslana null vrjednost umjesto kolekcije
     */
    public ArrayIndexedCollection(Collection other, int initialCapacity) {               //jesam
        this(other.size() > initialCapacity ? other.size() : initialCapacity);
        other.forEach(new Processor() {
            public void process(Object value) {
                elements[size++] = value;
            }
        });
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
    protected int arraySize() { return this.elements.length; }

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
    public void add(Object value) {                            //jesam
        if(value == null)
            throw new NullPointerException();
        if(size == elements.length) doubleArray();

        this.elements[size++] = value;
    }

    /**
     * Vraća objekt koji se nalazi na zadanom poziciji u polju.
     * @param index pozicija u polju
     * @return Objekt na zadanoj poziciji u polju
     * @throws IndexOutOfBoundsException ako se pokušava dohvatiti objekt na poziciji koja nije bila postavljena ili ako je pozicija neispravna
     */
    public Object get(int index) {                                                       //jesam
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
    public void insert(Object value, int position) {                                //jesam
        if(value == null)
            throw new NullPointerException();
        if(position < 0 || position > this.size)
            throw new IndexOutOfBoundsException("Received "+ position +", should be between 0 and "+ (this.elements.length-1));
        if(size == elements.length) doubleArray();
        for(int i = size - 1; i >= position; i--) {
            elements[i+1] = elements[i];
        }
        elements[position] = value;
        size++;
        // This function has linear complexity because it directly depends on the size of the array.
        // On average it well have to move half of the elements in the array
    }

    /**
     * Traži poslani objekt u polju.
     * @param value objekt koji se traži
     * @return indeks pozicije na kojem se nalazi prvi objekt koji je jednak poslani objektu, inače vraća -1 ako objekt nije pronađen
     */
    public int indexOf(Object value) {                          //jesam
        if(value == null) return -1;
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
    public boolean contains(Object value) {                                 //jesam
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
    }

    /**
     * Briše objekt iz polja ako on postoji u polju.
     * @param value objekt koji želimo obrisati
     * @return <code>true<code> ako je objekt pronađen i obrisan, inače <code>false</code>
     */
    @Override
    public boolean remove(Object value) {                                       //jesam
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
    public Object[] toArray() {                                             //jesam
        return Arrays.copyOfRange(elements, 0, this.size);
    }

    /**
     * Prolazi svim elementima polja i na svakog od njih primjenjuje metodu Processor-a.
     * @param processor objekt u kojem se nalazi metoda <code>process</code>
     * @throws NullPointerException ako je poslan <code>null</code> umjesto <code>Processor</code>
     */
    @Override
    public void forEach(Processor processor) {
        for(int i = 0; i < this.size; i++) {
            processor.process(elements[i]);
        }
    }

    /**
     * Briše sve elemente polja.
     */
    @Override
    public void clear() {
        this.size = 0;
        Arrays.fill(this.elements, null);
    }
}
