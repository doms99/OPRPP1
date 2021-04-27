package hr.fer.oprpp1.hw04.db;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentDatabaseTest {

    @Test
    public void testForJMBAG() {
        StudentDatabase db = new StudentDatabase(Arrays.asList("00000001\tLjika\tLuka\t5",
                "00023625\tBlanko\tKika\t1"));
        assertEquals(new StudentRecord("00023625", "Blanko", "Kika", 1), db.forJMBAG("00023625"));
    }

    @Test
    public void testForBUMBAGUnexciting() {
        StudentDatabase db = new StudentDatabase(Arrays.asList("00000001\tLjika\tLuka\t5",
                "00023625\tBlanko\tKika\t1"));
        assertEquals(null, db.forJMBAG("02652825"));
    }

    @Test
    public void testFilterTrue() {
        IFilter filter = s -> true;
        StudentDatabase db = new StudentDatabase(Arrays.asList("00000001\tLjika\tLuka\t5",
                "00023625\tBlanko\tKika\t1",
                "00000002\tJoka\tMiki\t2"));
        List<StudentRecord> expected = new ArrayList<>();
        expected.add(new StudentRecord("00000001", "Ljika", "Luka", 5));
        expected.add(new StudentRecord("00023625", "Blanko", "Kika", 1));
        expected.add(new StudentRecord("00000002", "Joka", "Miki", 2));

        assertEquals(expected, db.filter(filter));
    }

    @Test
    public void testFilterFalse() {
        IFilter filter = s -> false;
        StudentDatabase db = new StudentDatabase(Arrays.asList("00000001\tLjika\tLuka\t5",
                "00023625\tBlanko\tKika\t1",
                "00000002\tJoka\tMiki\t2"));
        List<StudentRecord> expected = new ArrayList<>();

        assertEquals(expected, db.filter(filter));
    }

    @Test
    public void testNullConstructor() {
        assertThrows(NullPointerException.class, () -> new StudentDatabase(null));
    }

    @Test
    public void testCritical() {
        StudentDatabase db = new StudentDatabase(Arrays.asList("0000000001\tLjika\tLuka\t5",
                "00023625\tBlanko\tKika\t1"));
        StudentRecord sr = new StudentRecord("0000000001", "Ljika", "Luka" ,5);
        assertEquals(sr, db.forJMBAG(sr.getJmbag()));
    }
}
