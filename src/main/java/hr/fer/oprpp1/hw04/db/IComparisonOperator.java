package hr.fer.oprpp1.hw04.db;

/**
 * Sučelje koje će implementirati svaki razred koji će provjeravati da li nešto vrijedi za poslane elemente.
 */
public interface IComparisonOperator {

    /**
     * Provodi testiranje nad predanim elmentima i vraća rezultat
     * @param value1 prva vrijednost
     * @param value2 druga vrijednost
     * @return <code>true</code> ako su uvjeti zadovoljeni, inače vraća <code>false</code>
     */
    public boolean satisfied(String value1, String value2);
}
