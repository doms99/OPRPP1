package hr.fer.oprpp1.hw04.db;

/**
 * Sučelje koje će implementirati svaka klasa koja će provjeravati nešto nad elementom baze
 */
public interface IFilter {

    /**
     * Funcija koja ispituje da li su ispunjeni neki uvjeti za taj element baze
     * @param record element baze koji se ispituje
     * @return <code>true</code> ako element ispunjava zadane uvjete, inače <code>false</code>
     */
    public boolean accepts(StudentRecord record);
}
