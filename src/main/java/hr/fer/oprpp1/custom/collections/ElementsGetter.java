package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje služi za iteriranje po kolekciji.
 * Svaka kolekcija mora implementirati svoj razred.
 * @param <T> vrsta elemenata kolekcije
 * @author Dominik
 */
public interface ElementsGetter<T> {

    /**
     * Provjerava da li postoji još elemenata kojim nije pronađeno.
     * @return <code>true</code> ako nismo prošli svim elementima, inače <code>false</code>
     */
    boolean hasNextElement();

    /**
     * Vraća sljedeći element kolekcije kojim nismo prošli.
     * @return sljedeći element kolekcije kojim nismo prošli
     */
    T getNextElement();

    /**
     * Nad svakim elementom kojim još nismo prošli poziva funkciju <code>process</code> predanog <code>Processor</code>-a.
     * @param p <code>Processor</code> koji se poziva nad preostalim neprođenim elementima
     */
    default void processRemaining(Processor<? super T> p) {
        while(this.hasNextElement()) {
            p.process(getNextElement());
        }
    }
}