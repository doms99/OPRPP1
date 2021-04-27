package hr.fer.oprpp1.hw04.db;

/**
 * Sučelje koje ćee implementirati klase čija će zdaća biti dohvaćanje vrijednosti atributa iz zapisa studenata.
 */
public interface IFieldValueGetter {

    /**
     * Vraća zadani vrijednost atributa iz prosljeđenog elementa.
     * @param record element iz kojeg se dohvaća vrijednost
     * @return zadanu vrijednosti iz elementa
     */
    public String get(StudentRecord record);
}
