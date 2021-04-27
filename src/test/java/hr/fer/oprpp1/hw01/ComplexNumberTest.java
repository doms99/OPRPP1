package hr.fer.oprpp1.hw01;

import static hr.fer.oprpp1.hw01.ComplexNumber.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.net.PortUnreachableException;

import static org.junit.jupiter.api.Assertions.*;

public class ComplexNumberTest {

    @Test
    public void testRealPartDefaultConstructor() {
        ComplexNumber number = new ComplexNumber(1, 1);
        assertEquals(1, number.getReal());
    }

    @Test
    public void testImaginaryPartDefaultConstructor() {
        ComplexNumber number = new ComplexNumber(1, 1);
        assertEquals(1, number.getImaginary());
    }

    @Test
    public void testRoundingRealDefaultConstructor() {
        ComplexNumber number = new ComplexNumber(1.26845, 1.26845);
        assertEquals(1.2685, number.getReal());
    }

    @Test
    public void testRoundingImaginaryDefaultConstructor() {
        ComplexNumber number = new ComplexNumber(1.26845, 1.26845);
        assertEquals(1.2685, number.getImaginary());
    }

    @Test
    public void testRealPartFromReal() {
        ComplexNumber number = fromReal(116.5);
        assertEquals(116.5, number.getReal());
    }

    @Test
    public void testImaginaryPartFromReal() {
        ComplexNumber number = fromReal(116.5);
        assertEquals(0, number.getImaginary());
    }

    @Test
    public void testRealPartFromImaginary() {
        ComplexNumber number = fromImaginary(10.5);
        assertEquals(0, number.getReal());
    }

    @Test
    public void testImaginaryPartFromImaginary() {
        ComplexNumber number = fromImaginary(10.5);
        assertEquals(10.5, number.getImaginary());
    }

    @Test
    public void testAngleFromMagnitudeAndAngle() {
        ComplexNumber number = fromMagnitudeAndAngle(9.9825, 1.1565);
        assertEquals(1.1565, number.getAngle());
    }

    @Test
    public void testBigAngleFromMagnitudeAndAngle() {
        ComplexNumber number = fromMagnitudeAndAngle(9.9825, 7.9654);
        double expectedAngle = (double) Math.round(7.9654 % (2*Math.PI) * 10_000) / 10_000;
        assertEquals(expectedAngle, number.getAngle());
    }

    @Test
    public void testMagnitudeFromMagnitudeAndAngle() {
        ComplexNumber number = fromMagnitudeAndAngle(9.9825, 1.1565);
        assertEquals(9.9825, number.getMagnitude());
    }

    @Test
    public void testRealPartFromMagnitudeAndAngle() {
        ComplexNumber number = fromMagnitudeAndAngle(9.9825, 1.1565);
        double expectedReal = (double) Math.round(9.9825 * Math.cos(1.1565) * 10_000) / 10_000;
        assertEquals(expectedReal, number.getReal());
    }

    @Test
    public void testImaginaryPartFromMagnitudeAndAngle() {
        ComplexNumber number = fromMagnitudeAndAngle(9.9825, 1.1565);
        double expectedReal = (double) Math.round(9.9825 * Math.sin(1.1565) * 10_000) / 10_000;
        assertEquals(expectedReal, number.getImaginary());
    }

    @Test
    public void testRealPartParse() {
        ComplexNumber number = parse("-3.6685+0.1001i");
        assertEquals(-3.6685, number.getReal());
    }

    @Test
    public void testImaginaryPartParse() {
        ComplexNumber number = parse("-3.6685+0.1001i");
        assertEquals(0.1001, number.getImaginary());
    }

    @Test
    public void testInvalidSpacesParse() {
        assertThrows(IllegalArgumentException.class, () -> parse("-3+ 5i"));
    }

    @Test
    public void testInvalidCharactersParse() {
        assertThrows(IllegalArgumentException.class, () -> parse("-3d+5i"));
    }

    @Test
    public void testNullCharactersParse() {
        assertThrows(NullPointerException.class, () -> parse(null));
    }

    @Test
    public void testGetReal() {
        ComplexNumber number = new ComplexNumber(5.6985, 0);
        assertEquals(5.6985, number.getReal());
    }

    @Test
    public void testGetImaginary() {
        ComplexNumber number = new ComplexNumber(0, 5.6985);
        assertEquals(5.6985, number.getImaginary());
    }

    @Test
    public void testGetMagnitude() {
        ComplexNumber number = new ComplexNumber(1.5, 6.2);
        double expectedMagnitude = 6.3789;  // izračunato na zasebnom kalulatoru
        assertEquals(expectedMagnitude, number.getMagnitude());
    }

    @Test
    public void testGetAnglePositiveReal() {
        ComplexNumber number = new ComplexNumber(1.5, 6.2);
        double expectedAngle = 1.3334; // izračunato na zasebnom kalulatoru
        assertEquals(expectedAngle, number.getAngle());
    }

    @Test
    public void testGetAngleNegativeReal() {
        ComplexNumber number = new ComplexNumber(-1.5, 6.2);
        double expectedAngle = 1.8082; // izračunato na zasebnom kalulatoru
        assertEquals(expectedAngle, number.getAngle());
    }

    @Test
    public void testRealPartAdd() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        ComplexNumber b = a.add(new ComplexNumber(2.8999, 0.5521));
        assertEquals(4.4267, b.getReal());
    }

    @Test
    public void testRealImaginaryAdd() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        ComplexNumber b = a.add(new ComplexNumber(2.8999, 0.5521));
        assertEquals(4.1187, b.getImaginary());
    }

    @Test
    public void testAddNull() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        assertThrows(NullPointerException.class, () -> a.add(null));
    }

    @Test
    public void testRealPartSub() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        ComplexNumber b = a.sub(new ComplexNumber(8.5264, 0.8512));
        assertEquals(-6.9996, b.getReal());
    }

    @Test
    public void testRealImaginarySub() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        ComplexNumber b = a.sub(new ComplexNumber(8.5264, 0.8512));
        assertEquals(2.7154, b.getImaginary());
    }

    @Test
    public void testSubNull() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        assertThrows(NullPointerException.class, () -> a.sub(null));
    }

    @Test
    public void testRealPartMul() {
        ComplexNumber a = new ComplexNumber(2.8971, 4.8965);
        ComplexNumber b = a.mul(new ComplexNumber(4.8526, 0.8512));
        assertEquals(9.8906, b.getReal());
    }

    @Test
    public void testRealImaginaryMul() {
        ComplexNumber a = new ComplexNumber(2.8971, 4.8965);
        ComplexNumber b = a.mul(new ComplexNumber(4.8526, 0.8512));
        assertEquals(26.2268, b.getImaginary());
    }

    @Test
    public void testMulNull() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        assertThrows(NullPointerException.class, () -> a.mul(null));
    }

    @Test
    public void testRealPartDiv() {
        ComplexNumber a = new ComplexNumber(2.8971, -4.8965);
        ComplexNumber b = a.div(new ComplexNumber(4.8526, 0.8512));
        assertEquals(0.4076, b.getReal());
    }

    @Test
    public void testRealImaginaryDiv() {
        ComplexNumber a = new ComplexNumber(2.8971, -4.8965);
        ComplexNumber b = a.div(new ComplexNumber(4.8526, 0.8512));
        assertEquals(-1.0805, b.getImaginary());
    }

    @Test
    public void testDivWithZero() {
        ComplexNumber a = new ComplexNumber(2.8971, -4.8965);
        assertThrows(ArithmeticException.class, () -> a.div(new ComplexNumber(0, 0)));
    }

    @Test
    public void testDivNull() {
        ComplexNumber a = new ComplexNumber(1.5268, 3.5666);
        assertThrows(NullPointerException.class, () -> a.div(null));
    }

    @Test
    public void testRealPartPower() {}

    @Test
    public void testImaginaryPartPower() {}

    @Test
    public void testToString() {
        ComplexNumber number = new ComplexNumber(-1.5268, 2.5698);
        assertArrayEquals("-1,53+2,57i".toCharArray(), number.toString().toCharArray());
    }

    @Test
    public void testToStringOnlyReal() {
        ComplexNumber number = new ComplexNumber(1.5268, 0);
        assertArrayEquals("1,53".toCharArray(), number.toString().toCharArray());
    }

    @Test
    public void testToStringOnlyImaginary() {
        ComplexNumber number = new ComplexNumber(0, -2.1564);
        assertArrayEquals("-2,16i".toCharArray(), number.toString().toCharArray());
    }
}
