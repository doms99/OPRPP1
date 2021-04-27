package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;

/**
 * Koja koja služi za oblikovanje strukture dokumenta.
 * Nasljeđuju ju svi ostali <code>Node</code>-ovi.
 */
public abstract class Node {

    /**
     * Djeca klase.
     */
    private ArrayIndexedCollection collection;

    /**
     * Dodaje djecu. Ukoliko se funckija prvi puta poziva ona inicijalizira unutarnju kolkeciju.
     * @param child
     */
    public void addChildNode(Node child) {
        if(collection == null)
            collection = new ArrayIndexedCollection();
        collection.add(child);
    }

    /**
     * Vraća broj dodane djece.
     * @return broj dodane djece
     */
    public int numberOfChildren() {
        return collection.size();
    }

    /**
     * Vraća dijete na poslanoj poziciji unutar kolekcije.
     * @param index pozicija s koje želimo dohvatiti dijete
     * @return <code>Node</code> dijete s poslane pozicije
     * @throws IllegalArgumentException ako je index nevažeći
     */
    public Node getChild(int index) {
        if(index >= collection.size() || index < 0)
            throw new IllegalArgumentException("No child was added to that position yet. Index should be between 0 and " + collection.size() + ".");
        return (Node) collection.get(index);
    }

    /**
     * Pretvara node u tekst.
     * @return uvijek vraća prazan <code>String</code> jer je ovo generički <code>Node</code>
     */
    @Override
    public String toString() {
        return "";
    }
}
