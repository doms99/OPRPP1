package hr.fer.oprpp1.custom.collections;

/**
 * Sučelje koje sadrži samo jednu metodu za testiranje poslanog elementa.
 * @param <T> vrsta elementa koji će se testirati
 */
public interface Tester<T> {

    /**
     * Funcija koja testira poslani element
     * @param obj element koji se testira
     * @return <code>true</code> ako je test prošao, inače <code>false</code>
     */
    boolean test(T obj);
}
