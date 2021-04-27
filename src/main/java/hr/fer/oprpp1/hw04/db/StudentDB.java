package hr.fer.oprpp1.hw04.db;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Klasa preko koje ćemo pretraživati bazu.
 */
public class StudentDB {

    /**
     * Baza studenata.
     */
    private StudentDatabase database;

    /**
     * Stvara bazu i učitava podatke iz datoteke database.txt u resursima.
     */
    public StudentDB() {
        database = new StudentDatabase(loadFromResources());
    }

    /**
     * Učitava podatke i datoteke.
     * @return listu u kojoj su spremljeni redovi datoteke
     */
    private static List<String> loadFromResources() {
        List<String> students = new ArrayList<>();

        Scanner scanner = new Scanner(StudentDatabase.class.getClassLoader().getResourceAsStream("database.txt"));
        if(scanner==null) throw new RuntimeException("Datoteka database.txt je nedostupna.");
        while(scanner.hasNextLine()) {
            students.add(scanner.nextLine());
        }

        return students;
    }

    /**
     * Funcija prima željeni upit te ga parsira i vraća listu svih sudenata koji zadovoljavaju uvijete iz upita.
     * Ako niti jedan student ne zadovoljava upit vraća praznu listu.
     * @param query upit
     * @return listu svih sudenata koji zadovoljavaju uvijete iz upita
     * @throws IllegalArgumentException ako se dogodila greška tijekom parsiranja
     */
    private List<StudentRecord> query(String query) {
        QueryParser parser = new QueryParser(query);

        List<StudentRecord> filteredStudents;
        if(parser.isDirectQuery()) {
            StudentRecord student = database.forJMBAG(parser.getQueriedJMBAG());
            if(student != null)
                filteredStudents = Arrays.asList(student);
            else
                filteredStudents = List.of();
        } else filteredStudents = database.filter(new QueryFilter(parser.getQuery()));

        return filteredStudents;
    }

    /**
     * Formatira sve zapise iz liste tako da se oni mogu ispisati svaki u istom formatu.
     * @param students lista zapisa studenta
     * @return formatiran zapis
     */
    private String formatOutput(List<StudentRecord> students) {
        if(students.size() == 0) {
            return "Records selected: 0";
        }

        int jmbagSpace = 10;
        int lastNameSpaces = students.stream().map(StudentRecord::getLastName).map(String::length).max(Integer::compare).get();
        int firstNameSpaces = students.stream().map(StudentRecord::getFirstName).map(String::length).max(Integer::compare).get();
        int gradeSpaces = 1;

        StringBuilder builder = new StringBuilder();
        if(students.size() > 0) {
            builder.append("+")
                    .append("=".repeat(jmbagSpace+2))
                    .append("+")
                    .append("=".repeat(lastNameSpaces+2))
                    .append("+")
                    .append("=".repeat(firstNameSpaces+2))
                    .append("+")
                    .append("=".repeat(gradeSpaces+2))
                    .append("+\n");
        }

        int borderLength = builder.toString().length();

        for(StudentRecord record : students) {
            builder.append("| ")
                    .append(record.getJmbag())
                    .append(" | ")
                    .append(record.getLastName())
                    .append(" ".repeat(lastNameSpaces-record.getLastName().length()))
                    .append(" | ")
                    .append(record.getFirstName())
                    .append(" ".repeat(firstNameSpaces-record.getFirstName().length()))
                    .append(" | ")
                    .append(record.getGrade())
                    .append(" |\n");
        }

        builder.append(builder.substring(0, borderLength));

        builder.append("Records selected: "+ students.size() +"\n");
        return builder.toString();
    }

    /**
     * Glavna funcija koja stvara instancu <code>StudentDB</code> i učitava upite sa standardnog ulaza.
     * @param args argumenti pokretanja
     */
    public static void main(String[] args) {
        StudentDB database = new StudentDB();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Awaiting query");
        while(true) {
            System.out.print("> ");
            String line = scanner.nextLine();
            if(line.toUpperCase().startsWith("EXIT")) {
                System.out.println("Goodbye!");
                break;
            }

            if(!line.startsWith("query")) {
                System.out.println("Unknown command");
                continue;
            }

            List<StudentRecord> students;
            try {
                students = database.query(line.replace("query", ""));
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                continue;
            }

            String output = database.formatOutput(students);
            System.out.println(output);
        }
    }


}


