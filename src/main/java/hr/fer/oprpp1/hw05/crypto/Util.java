package hr.fer.oprpp1.hw05.crypto;

/**
 * Class containing utility methods for converting bytes to hex and vice versa.
 */
public class Util {

    /**
     * Method that converts a string to byte array.
     * @param keyText string that needs to be converted.
     * @return byte array of converted string
     */
    public static byte[] hextobyte(String keyText) {
        if(keyText.length() == 0) {
            return new byte[0];
        }
        if(keyText.length() % 2 == 1) {
            throw new IllegalArgumentException("Odd numbers of characters provided");
        }
        byte[] bytes = new byte[keyText.length()/2];
        char[] chars = keyText.toCharArray();
        for(int i = 0; i < chars.length; i += 2) {
            bytes[i/2] = (byte) ((toDigit(chars[i]) << 4) + toDigit(chars[i+1]));
        }
        return bytes;
    }

    /**
     * Converts <code>char</code> to <code>int</code>.
     * @param hex hex caraters
     * @return converted <code>int</code>
     */
    private static int toDigit(char hex) {
        int digit = Character.digit(hex, 16);
        if(digit == -1) {
            throw new IllegalArgumentException("Invalid hex character: "+ hex);
        }
        return digit;
    }

    /**
     * Converts <code>byte</code> array to <code>String</code>.
     * @param bytearray <code>byte</code> array that will be converted
     * @return converted <code>String</code>
     */
    public static String bytetohex(byte[] bytearray) {
        StringBuilder hexString = new StringBuilder();
        for (int i = 0; i < bytearray.length; i++) {
            hexString.append(toHex(bytearray[i]));
        }
        return hexString.toString();
    }

    /**
     * converts <code>byte</code> to <code>String</code>.
     * @param num <code>byte</code> that will be converted
     * @return converted <code>String</code>
     */
    private static String toHex(byte num) {
        char[] hexDigits = new char[2];
        hexDigits[0] = Character.forDigit((num >> 4) & 0xF, 16);
        hexDigits[1] = Character.forDigit((num & 0xF), 16);
        return new String(hexDigits);
    }
}
