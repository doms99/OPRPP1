package hr.fer.oprpp1.hw05.crypto;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static hr.fer.oprpp1.hw05.crypto.Util.*;

/**
 * Class that implements methods for checksha calcualtion,
 * encryption and decryption.
 */
public class Crypto {

    /**
     * Depending of the program arguments the methode for digest, encrypt or decrypt will be called.
     * @param args program arguments
     */
    public static void main(String[] args) {
        Crypto c = new Crypto();

        if(args.length == 0) {
            System.out.println("No arguments provided!");
            return;
        }

        switch (args[0]) {
            case "checksha" -> {
                if(args.length != 2) {
                    throw new IllegalArgumentException("Expected 2 arguments. 1st type, 2nd file");
                }
                c.digest(new File(args[1]));
            }
            case "encrypt" -> {
                if(args.length != 3) {
                    throw new IllegalArgumentException("Expected 3 arguments. 1st type, 2nd source file, 3rd destination file");
                }
                c.convert(true, new File(args[1]), new File(args[2]));
            }
            case "decrypt" -> {
                if(args.length != 3) {
                    throw new IllegalArgumentException("Expected 3 arguments. 1st type, 2nd source file, 3rd destination file");
                }
                c.convert(false, new File(args[1]), new File(args[2]));
            }
            default -> throw new IllegalArgumentException("Unknown operation");
        }
    }

    /**
     * Calculates the checksha of a passed file.
     * @param file file of which checksha is calculate
     * @throws IllegalArgumentException if an error occurs
     */
    private void digest(File file) {
        if(!file.isFile()) {
            throw new IllegalArgumentException("No file was provided.");
        }

        System.out.print("Please provide expected sha-256 digest for "+ file.getName() +":\n" +
                "> ");

        String provided;
        try(Scanner scan = new Scanner(System.in)) {
            provided = scan.nextLine().trim();
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Digest wasn't provided.");
        }

        String calculated = checksha(file);

        if(calculated.equals(provided)) {
            System.out.println("Digesting completed. Digest of hw05test.bin matches expected digest.\n");
        } else {
            System.out.println("Digesting completed. Digest of hw05test.bin does not match the expected digest. Digest was: " +
                    calculated);
        }

    }

    /**
     * Claculates checksha using SHA-256 algorithm.
     * @param file file of which checksha is calculate
     * @return calculated digest <code>String</code>
     */
    protected String checksha(File file) {
        MessageDigest calculatedDigest;
        try {
            calculatedDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalArgumentException("Invalid algorithm was provided for calculating the digest");
        }

        String calculated;
        try(FileInputStream reader = new FileInputStream(file)) {
            byte[] array = new byte[4000];
            int read;
            while ((read = reader.read(array)) != -1) {
                calculatedDigest.update(array, 0, read);
            }
            calculated = bytetohex(calculatedDigest.digest());
        } catch(IOException ex) {
            throw new IllegalArgumentException("IO error");
        }

        return calculated;
    }

    /**
     * Encrypts or decrypts, depending of <code>encrypt</code>, the source file and saves it to destination file.
     * @param encrypt <code>boolean</code> that decides if the source fle will be encrypted or decrypted
     * @param source source file
     * @param destination destination file
     */
    private void convert(boolean encrypt, File source, File destination) {
        if(!source.isFile()) {
            throw new IllegalArgumentException("No file was provided.");
        }

        System.out.print("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n" +
                "> ");

        String password, vector;
        try(Scanner scan = new Scanner(System.in)) {
            password = scan.nextLine().trim();
            System.out.print("Please provide initialization vector as hex-encoded text (32 hex-digits):\n" +
                    "> ");
            vector = scan.nextLine().trim();
        } catch (NoSuchElementException ex) {
            throw new IllegalArgumentException("Wrong input.");
        }

        try {
            destination.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        convert(encrypt, source, destination, password, vector);

        if(encrypt) {
            System.out.println("Encryption completed. Generated file "+ destination.getName() +" based on file "+ source.getName() +".");
        } else {
            System.out.println("Decryption completed. Generated file "+ destination.getName() +" based on file "+ source.getName() +".");
        }
    }

    /**
     * Encrypts or decrypts the file.
     * @return converted <code>File</code>
     */
    protected void convert(boolean encrypt, File source, File destination, String password, String vector) {
        SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(password), "AES");
        AlgorithmParameterSpec paramSpec = new IvParameterSpec(hextobyte(vector));
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("Invalid algorithm for encryption/decryption");
        } catch (NoSuchPaddingException e) {
            throw new IllegalArgumentException("Invalid padding algorithm");
        } catch (InvalidKeyException e) {
            throw new IllegalArgumentException("Invalid password");
        } catch (InvalidAlgorithmParameterException e) {
            throw new IllegalArgumentException("Invalid algorithm parameter");
        }

        try(FileInputStream reader = new FileInputStream(source);
            FileOutputStream output = new FileOutputStream(destination, false)) {

            byte[] array = new byte[4000];
            int read;
            while ((read = reader.read(array)) != -1) {
                output.write(cipher.update(array, 0, read));
            }
            output.write(cipher.doFinal());
            output.flush();
        } catch(IOException | IllegalBlockSizeException | BadPaddingException ex) {
            throw new IllegalArgumentException("error:" + ex.getClass() +" -> "+ ex.getMessage());
        }
    }
}
