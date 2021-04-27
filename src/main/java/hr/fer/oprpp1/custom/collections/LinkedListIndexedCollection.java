package hr.fer.oprpp1.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;

/**
 * Klasa koja nasljeđuje Collection.
 * Implementira povezanu listu u kojem čuva poslane objekte.
 * Lista se sastoji od čvorova u kojoj se nalazi vrijednost i pokazivači.
 * @author Dominik
 */
public class LinkedListIndexedCollection<T> implements List<T> {

    /**
     * Služi nam za brojanje koliko je trenutno pohranjeno objekata u kolekciji.
     */
    private int size;

    /**
     * Pokazivač na prvi čvor u listi.
     */
    private Node<T> first;

    /**
     * Pokazivač na posljednji čvor u listi.
     */
    private Node<T> last;

    /**
     * Broji koliko puta se polje promijenilo
     */
    private long modificationCount;

    /**
     * Privatna klasa koja nam služi za povezivanje i nizanje elemenata u listu.
     * Svaki čvor pokazuje na prethodog i na sljedećeg.
     * Ukoliko nema sljedečeg ili prethodnog čvora, pokazuje na <code>null</code>.
     * @author Dominik
     */
    private static class Node<T> {

        /**
         * Vrijednost koja je spremljena u čvoru.
         */
        T value;

        /**
         * Pokazivač na sljedeći čvor.
         */
        Node next;

        /**
         * Pokazivač na prethodni čvor.
         */
        Node prev;

        /**
         * Kostruktor za stvaranje čvora.
         * @param value vrijednost koju ćemo spremamo u čvor
         * @param next pokazivač na sljedeći čvor
         * @param prev pokazivač na prethodni čvor
         */
        public Node(T value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }
    }

    /**
     * Default Konstruktor.
     * Stvara kolekciju s nula elemenata.
     */
    public LinkedListIndexedCollection() {
        this.size = 0;
        this.first = this.last = null;
        this.modificationCount = 0;
    }

    /**
     * Konstruktor koji prima kolekciju te iz nje kopira sve elemente te veličinu postavlja na veličinu poslane kolekcije
     * @param other kolekcija koja se kopira
     * @throws NullPointerException ako je poslano <code>null</code> umjesto <code>Collection</code>
     */
    public LinkedListIndexedCollection(Collection<? extends T> other) {
        this();

        if(other.size() > 1) {
            Node<T>[] arrayOfNodes = new Node[other.size()];
            for(Object o : other.toArray()) {
                arrayOfNodes[size++] = new Node<T>((T) o, null, null);
            }
            for(int i = 1; i < this.size-1; i++) {
                arrayOfNodes[i].prev = arrayOfNodes[i-1];
                arrayOfNodes[i].next = arrayOfNodes[i+1];
            }
            arrayOfNodes[0].next = arrayOfNodes[1];
            arrayOfNodes[this.size-1].prev = arrayOfNodes[this.size-2];
            first = arrayOfNodes[0];
            last = arrayOfNodes[this.size-1];
        }
    }

    /**
     * Metoda koja se koristi interno da se pronađe čvor na željenoj poziciji.
     * @param position pozicija na kojoj se nalazi čvor
     * @return <code>Node</code> na poslanoj poziciji
     */
    private Node<T> findNode(int position) {
        Node<T> current;
        if(position - this.size/2 > 0) { // if this is true then the node we are looking for is closer to the last node and we start from it
            current = last;
            for(int i = this.size - 1; i > position; i--) {
                current = current.prev;
            }
        } else {
            current = first;
            for(int i = 0; i < position; i++) {
                current = current.next;
            }
        }

        return current;
    }

    /**
     * Metoda za testiranje na koju vrijednost pokazuje interna varijabla <code>first</code>.
     * @return vrijednost spremljena u prvom čvoru
     */
    T getFirst() {
        return this.first == null ? null : this.first.value;
    }

    /**
     * Metoda za testiranje na koju vrijednost pokazuje interna varijabla <code>last</code>.
     * @return vrijednost spremljena u zadnjem čvoru
     */
    T getLast() {
        return this.last == null ? null : this.last.value;
    }

    /**
     * Provjerava da li je lista prazno, tj. da li je u njoj spremljen ijedan objekt.
     * @return <code>true</code> ako je u listi spremljen barem 1 objekt, inače <code>false</code>
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Vraća broj objekata spremljenih u listi.
     * @return broj elemenata spremljenih u listi
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Dodaje novi objekt u listu.
     * @param value objekt koji se dodaje
     * @throws NullPointerException ako se pokuša spremiti null vrijednost
     */
    public <K extends T> void add(K value) {
        if(value == null) throw new NullPointerException();
        Node<T> newNode = new Node<T>(value, null, last);
        if(last == null) {
            first = last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        this.size++;
        this.modificationCount++;
    }

    /**
     * Vraća objekt koji se nalazi na zadanom poziciji u listi.
     * @param index pozicija u listi
     * @return Objekt na zadanoj poziciji u listi
     * @throws IndexOutOfBoundsException ako je poslani index nevažeći
     */
    public T get(int index) {
        if(index < 0 || index > this.size-1)
            throw new IndexOutOfBoundsException("Received "+ index +", should be between 0 and "+ (this.size-1));
        return findNode(index).value;
    }

    /**
     * Umeće poslani objekt u listu na poslanoj poziciji.
     * @param value objekt koji se upisuje u listu
     * @param position pozicija na kojoj se upisuje objekt
     * @throws NullPointerException ako se pokuša spremiti null vrijednost
     * @throws IndexOutOfBoundsException ako je poslana pozicija umetanja nevažeća
     */
    public <K extends T> void insert(K value, int position) {
        if(position < 0 || position > this.size)
            throw new IndexOutOfBoundsException("Received "+ position +", should be between 0 and "+ this.size);
        if(value == null)
            throw new NullPointerException();
        if(first == null || position == this.size) { //if there are no elements in the collection or the position we are inserting is the first available position
            add(value);
            return;
        }
        Node<T> current = findNode(position);
        Node<T> newNode = new Node<>(value, current, current.prev);

        if(current.prev != null) { // if this is false then we are inserting to the first position
            current.prev.next = newNode;
        } else {
            first = newNode;
        }
        if(current.next != null) { //if this is false then we are inserting to the last position
            current.next.prev = newNode;
        } else {
            last = newNode;
        }

        this.size++;
        this.modificationCount++;
        // Average complexity of this function is n/2 + 1 because the method findNode ensures it will go trough
        // max n/2 + 1 element to find the one we are looking for
    }

    /**
     * Traži poslani objekt u listi.
     * @param value objekt koji se traži
     * @return indeks pozicije na kojem se nalazi prvi objekt koji je jednak poslanom objektu, inače vraća -1 ako objekt nije pronađen
     */
    public int indexOf(Object value) {
        if(size == 0 || value == null)
            return -1;

        Node currentFront = first;
        Node currentBack = last;
        int indexFront = 0;
        int indexBack = this.size-1;
        int resultIndex = -1;
        for(int i = 0; i < (int) Math.ceil((float) this.size/2); i++) {
            if(currentFront.value.equals(value)) {
                resultIndex = indexFront;
                break;
            } else if (currentBack.value.equals(value)) {
                resultIndex = indexBack;
                break;
            }
            currentBack = currentBack.prev;
            currentFront = currentFront.next;
            indexBack--;
            indexFront++;
        }
        return resultIndex;
        // Average complexity of this function is n/2 + 1 because we are searching from both sides.
    }

    /**
     * Provjerava da li se u listi nalazi poslani u objekt.
     * @param value objekt kojeg tražimo
     * @return  <code>true</code> ako se objekt nalazi u listi, inače <code>false</code>
     */
    @Override
    public boolean contains(Object value) {
        return indexOf(value) != -1;
    }

    /**
     * Briše element liste na poslanoj poziciji.
     * @param index pozicija s koje brišemo objekt
     * @throws IndexOutOfBoundsException ako je poslana pozicija brisanja nevažeća
     */
    public void remove(int index) {
        if(index < 0 || index > this.size-1)
            throw new IndexOutOfBoundsException("Received "+ index +", should be between 0 and "+ (this.size-1));
        Node<T> current = findNode(index);
        if(this.size == 1) {
            first = last = null;
            this.size--;
            return;
        }
        if(current.prev != null) { // if this is false then we are removing from the first position
            current.prev.next = current.next;
        } else {
            first = current.next;
            first.prev = null;
        }
        if(current.next != null) { //if this is false then we are removing from the last position
            current.next.prev = current.prev;
        } else {
            last = current.prev;
            last.next = null;
        }
        this.size--;
        this.modificationCount++;
    }

    /**
     * Briše objekt iz liste ako on postoji u listi.
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
     * Vraća polje svih objekata liste kolekcije.
     * @return novo polje sa objektima
     */
    @Override
    public Object[] toArray() {
        Object[] result = new Object[this.size];
        if(this.size == 0) {
            return result;
        }
        Node<T> current = first;
        for(int i = 0; i < this.size; i++) {
            result[i] = current.value;
            current = current.next;
        }
        return result;
    }

    /**
     * Briše sve elemente liste.
     */
    @Override
    public void clear() {
        this.size = 0;
        first = last = null;
        this.modificationCount++;
    }

    /**
     * Razred koji implementira sučelje <code>ElementsGetter</code>.
     * Razred služi za iteriranje nad elementima kolekcije.
     */
    private static class ListElementsGetter<T> implements ElementsGetter<T> {

        /**
         * Zadnji element kojim smo prošli.
         */
        private Node<T> current;

        /**
         * Kolekcija po kojoj se provodi iteracija.
         */
        private LinkedListIndexedCollection<T> collection;

        /**
         * Sprema broj modifikacija nad kolekcijom u trenutku kad je kolekcija stvorena.
         */
        private long savedModificationCount;

        /**
         * Stvara novu instancu razreda koja služi za iteriranjem nad poslanom kolekcijom.
         * @param collection kolekcija nad kojom se iterira
         */
        ListElementsGetter(LinkedListIndexedCollection<T> collection) {
            current = collection.first;
            this.collection = collection;
            this.savedModificationCount = collection.modificationCount;
        }

        /**
         * Provjera da li je kolekcija bila mijenjana za vrijeme iteracijež
         * @throws ConcurrentModificationException ako je kolekcija mijenjana za vrijeme iteriranja
         */
        private void checkModifications() {
            if(savedModificationCount != collection.modificationCount)
                throw new ConcurrentModificationException("Can't modify and iterate over the collection at the same time!");
        }

        /**
         * Provjerava da li postoji još elemenata kojim nije pronađeno.
         * @return <code>true</code> ako nismo prošli svim elementima, inače <code>false</code>
         */
        @Override
        public boolean hasNextElement() {
            checkModifications();
            return !(current == null);
        }

        /**
         * Vraća sljedeći element kolekcije kojim nismo prošli.
         * @return sljedeći element kolekcije kojim nismo prošli
         */
        @Override
        public T getNextElement() {
            checkModifications();
            if(!this.hasNextElement())
                throw new NoSuchElementException("There are no elements left in collection");
            T result = current.value;
            current = current.next;

            return result;
        }
    }

    /**
     * Funkcija stvara i vraća novi <code>ElementsGetter</code> nad kolekcijom
     * @return <code>ElementsGetter</code> nad kolekcijom
     */
    @Override
    public ElementsGetter<T> createElementsGetter() {
        return new ListElementsGetter<T>(this);
    }
}
