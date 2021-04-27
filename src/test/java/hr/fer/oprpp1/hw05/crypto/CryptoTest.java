package hr.fer.oprpp1.hw05.crypto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;

public class CryptoTest {

    @Test
    public void digestTest() {
        Crypto c = new Crypto();
        Path file = Path.of("./src/test/resources/hw05part2.bin");
        String expected = "603ce08075a10ea3f781301bfafc01e3e9c9487ba33790d4afa7fd15dffd2b94";
        String calculated = c.checksha(file.toFile());
        assertEquals(expected, calculated);
    }

    @Test
    public void encryptTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.pdf");
        Path dest = Path.of("./src/test/resources/hw05part2_encrypted.bin");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192e2ac7e";
        String vector = "000102030405060708090a0b0c0d0e0f";
        String expected = "603ce08075a10ea3f781301bfafc01e3e9c9487ba33790d4afa7fd15dffd2b94";
        c.convert(true, src.toFile(), dest.toFile(), password, vector);
        String calculated = c.checksha(dest.toFile());
        assertEquals(expected, calculated);
    }

    @Test
    public void encryptWrongPasswordTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.pdf");
        Path dest = Path.of("./src/test/resources/hw05part2_encrypted.bin");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192e28695";
        String vector = "000102030405060708090a0b0c0d0e0f";
        String expected = "603ce08075a10ea3f781301bfafc01e3e9c9487ba33790d4afa7fd15dffd2b94";
        c.convert(true, src.toFile(), dest.toFile(), password, vector);
        String calculated = c.checksha(dest.toFile());
        assertNotEquals(expected, calculated);
    }

    @Test
    public void encryptWrongVectorTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.pdf");
        Path dest = Path.of("./src/test/resources/hw05part2_encrypted.bin");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192e2ac7e";
        String vector = "000102030405060708090a0b0c000000";
        String expected = "603ce08075a10ea3f781301bfafc01e3e9c9487ba33790d4afa7fd15dffd2b94";
        c.convert(true, src.toFile(), dest.toFile(), password, vector);
        String calculated = c.checksha(dest.toFile());
        assertNotEquals(expected, calculated);
    }

    @Test
    public void decryptTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.bin");
        Path dest = Path.of("./src/test/resources/hw05part2_decrypted.pdf");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192e2ac7e";
        String vector = "000102030405060708090a0b0c0d0e0f";
        String expected = "f1255767fc781a7d749f759a67f28121f0efd052519dbb536f918dcb5443e4b6";
        c.convert(false, src.toFile(), dest.toFile(), password, vector);
        String calculated = c.checksha(dest.toFile());
        assertEquals(expected, calculated);
    }

    @Test
    public void decryptWrongPasswordTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.bin");
        Path dest = Path.of("./src/test/resources/hw05part2_decrypted.pdf");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192010101";
        String vector = "000102030405060708090a0b0c0d0e0f";
        String expected = "f1255767fc781a7d749f759a67f28121f0efd052519dbb536f918dcb5443e4b6";
        try {
            c.convert(false, src.toFile(), dest.toFile(), password, vector);
        } catch (IllegalArgumentException ignored) {
            assertTrue(true);
        }
        String calculated = c.checksha(dest.toFile());
        assertNotEquals(expected, calculated);
    }

    @Test
    public void decryptWrongVectorTest() {
        Crypto c = new Crypto();
        Path src = Path.of("./src/test/resources/hw05part2.bin");
        Path dest = Path.of("./src/test/resources/hw05part2_decrypted.pdf");
        if(dest.toFile().exists()) dest.toFile().delete();
        String password = "e52217e3ee213ef1ffdee3a192e2ac7e";
        String vector = "000102030405060708090a0b0c000000";
        String expected = "f1255767fc781a7d749f759a67f28121f0efd052519dbb536f918dcb5443e4b6";
        c.convert(false, src.toFile(), dest.toFile(), password, vector);
        String calculated = c.checksha(dest.toFile());
        assertNotEquals(expected, calculated);
    }
}
