package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.collections.ObjectStack;

/**
 * Klasa koja omogućuje prikazivanje fraktala.
 */
public class Context {

    /**
     * Stog za spremanje i dohvaćanje stanja kornjače.
     * Aktivno stanje je ono stanje koje se nalazi na vrhu stoga.
     */
    ObjectStack<TurtleState> stack;

    /**
     * Stvara novu instancu razreda i inicijalizira stog.
     */
    public Context() {
        stack = new ObjectStack<>();
    }

    /**
     * Vraća trenutno aktivno stanje, odnosno vraća objekt na vrhu stoga, ali ga ne briše sa stoga.
     * @return trenutno aktivno stanje
     */
    public TurtleState getCurrentState() {
        return stack.peek();
    }

    /**
     * Na vrh stoga sprema predano stanje.
     * @param state stanje koje se sprema na stog
     */
    public void pushState(TurtleState state) {
        stack.push(state);
    }

    /**
     * Briše ativno stanje sa stoga.
     */
    public void popState() {
        stack.pop();
    }

}
