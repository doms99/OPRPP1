package hr.fer.oprpp1.hw04.db;

import java.util.Objects;

/**
 * Pretstavlja zais za jednog studenta u bazi.
 */
public class StudentRecord {

    /**
     * Jmbag studenta.
     */
    private final String jmbag;

    /**
     * Prezime studenta.
     */
    private final String lastName;

    /**
     * Ime studenta.
     */
    private final String firstName;

    /**
     * Ocjena.
     */
    private final int grade;

    /**
     * Stvara instancu razreda s prosljeđenim vrijednostima.
     * @param jmbag jmbag studenta
     * @param lastName prezime studenta
     * @param firstName ime studenta
     * @param grade ocjena
     * @throws NullPointerException ako je kao neka od vrijednosti poslan <code>null</code>
     * @throws IllegalArgumentException ako je poslana ocjena manja od 1 ili veća od 5
     */
    public StudentRecord(String jmbag, String lastName, String firstName, int grade) {
        if(jmbag == null
                || lastName == null
                || firstName == null)
            throw new NullPointerException("JMBAG, last name, first name can't be null");

        if(grade < 1 || grade > 5)
            throw new IllegalArgumentException("Grade must be between 1 and 5");

        this.jmbag = jmbag;
        this.lastName = lastName;
        this.firstName = firstName;
        this.grade = grade;
    }

    /**
     * Provejrava da li su trenutni objekt i prosljedeni objet isti.
     * Objekti se smatraju istim ako imaju isti jmbag.
     * @param o objekt koji ispitujemo
     * @return <code>true</code> ako su im isti jmbag-ovi, inače <code>false</code>
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        StudentRecord that = (StudentRecord) o;
        return Objects.equals(jmbag, that.jmbag);
    }

    /**
     * Hash code objekta se izračunava kao hash code jmbaga
     * @return <code>int</code> izračunati hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(jmbag);
    }

    /**
     * Vraća jmbag studenta.
     * @return jmbag studenta
     */
    public String getJmbag() {
        return jmbag;
    }

    /**
     * Vraća prezime studenta.
     * @return prezime studenta
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Vraća ime studenta.
     * @return ime studenta
     */
    public String getFirstName() {
        return firstName;
    }

    public int getGrade() {
        return grade;
    }
}
