package hr.fer.oprpp1.hw04.db;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Razred koji implementira bazu podataka.
 */
public class StudentDatabase {

    /**
     * Polje svih studenata u bazi.
     */
    ArrayList<StudentRecord> students;

    /**
     * Indeks napravljen prema jmbagu studenata.
     * Za svaki jmbag spremljena je pozicija u polju.
     */
    HashMap<String, StudentRecord> index;

    /**
     * Prima polje zapisa studenta u obliku jmbag   lastName firstName  grade.
     * Zapisi su odvojeni znakom \t.
     * @param students polje zapisa studenata
     */
    public StudentDatabase(List<String> students) {
        this.students = new ArrayList<>();
        this.index = new HashMap<>();
        parseStudents(students);
    }

    /**
     * Pomoćna funkcija koja parsira primljeno polje studenata i dodaje ih u listu objekta te stvara indeks nad jmbagom.
     * @param students polje zapisa studenata
     * @throws NullPointerException ako je umjesto polja predan <code>null</code>
     */
    private void parseStudents(List<String> students) {
        for(String s : students) {
            String[] attributes = s.trim().split("\\t+");
            if(attributes[3].compareTo("1") < 0 || attributes[3].compareTo("5") > 0)
                throw new IllegalArgumentException("Grade must be between 1 and 5. Was received: " +s);

            StudentRecord student = new StudentRecord(attributes[0], attributes[1], attributes[2], Integer.parseInt(attributes[3]));
            this.index.put(attributes[0], student);
            this.students.add(student);

        }
    }

    /**
     * Vraća zapisu u listi kojem je jmbag jednak prosljeđenom jmbagu.
     * @param jmbag jmbag studenta kojeg tražimo.
     * @return <code>StudentRecord</code> kojem je jmbag jednak prosljeđenom jmbagu ako postoji, inače vraća <code>null</code>
     */
    public StudentRecord forJMBAG(String jmbag) {
        int hash = jmbag.hashCode();
        int hash2 = Objects.hash(jmbag);
        int h;
        int hash3 = (h = jmbag.hashCode()) ^ (h >>> 16);
        StudentRecord student = index.get(jmbag);
        return student;
    }

    /**
     * Funcija vraća one elemente polja za koje će <code>filter</code> vratiti true nakon poziva njegove metode.
     * @param filter objekt kojim se testira svaki element polja
     * @return <code>List</code>u svih elemenata koji zadovoljavaju zadani uvijet
     * @throws NullPointerException ako je umjeto <code>IFilter</code> predan null
     */
    public List<StudentRecord> filter(IFilter filter) {
        return students.stream().filter(filter::accepts).collect(Collectors.toList());
    }
}
