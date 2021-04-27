package hr.fer.oprpp1.custom.collections;

/**
 * Klasa koja daje mogućnosti stoga kao što su metode <code>push</code>, <code>pop</code> i <code>peek</code>.
 * Interno koristi neku kolekciju za spremanje podataka
 * @author Dominik
 */
public class ObjectStack {

    /**
     * Kolekcija koja se koristi za spremanje objekata.
     */
    private ArrayIndexedCollection stack;

    /**
     * Default kostruktor.
     * Stvara praznu kolekciju.
     */
    public ObjectStack() {
        this.stack = new ArrayIndexedCollection();
    }

    /**
     * Provjerava da li je stog prazan.
     * @return <code>true</code> ako se na stogu ne nalazi niti jedan objekt, inače <code>false</code>
     */
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    /**
     * Vraća broj elemenata spremljenih na stog.
     * @return broj elemenata spremljenih na stogu
     */
    public int size() {
        return stack.size();
    }

    /**
     * Sprema objekt na vrh stoga.
     * @param value vrijednost koja se sprema na vrh stoga
     */
    public void push(Object value) {
        if(value == null)
            throw new NullPointerException("Null can't be pushed to the stack!");
        stack.add(value);
    }

    /**
     * Skida zadnji dodani objekt i briše ga sa stoga.
     * @return zadnji dodani element na stoga
     */
    public Object pop() {
        if(size() == 0)
            throw new EmptyStackException("Can't call method pop on an empty stack!");
        Object result = stack.get(size() - 1);
        stack.remove(size() - 1);

        return result;
    }

    /**
     * Skida zadnji dodani objekt sa stoga, ali ga ne briše
     * @return zadnji dodani objekt na stoga
     */
    public Object peek() {
        if(size() == 0)
            throw new EmptyStackException("Can't call method peek on an empty stack!");
        return stack.get(size() - 1);
    }

    /**
     * Prazni stog.
     */
    public void clear() {
        stack.clear();
    }
}
