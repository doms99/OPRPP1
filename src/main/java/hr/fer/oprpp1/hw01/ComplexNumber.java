package hr.fer.oprpp1.hw01;

import static java.lang.Math.*;
import static java.lang.Double.*;

/**
 * Omogućava spremanje i rad s kompleksnim brojevima.
 * Interno klasa radi s 4 decimale te se ne preporuča korištenje više decimala.
 * Pri stvaranju instance klase varijable se zaokružuju na 4 decimale.
 * @author Dominik Mandić
 */
public class ComplexNumber {

    /**
     * Realni dio kompleksnog broja
     */
    private final double real;

    /**
     * Imaginarni dio kompleksnog broja
     */
    private final double imaginary;

    /**
     * Default konstruktor.
     * @param real realni dio kompleksno broja
     * @param imaginary imaginarni dio kompleksnog broja
     */
    public ComplexNumber(double real, double imaginary) {
        this.real = normalize(real);
        this.imaginary = normalize(imaginary);
    }

    /**
     * Stvora novu instancu reazreda <code>ComplexNumber</code> s poslanim realnim dijelom i 0 za imaginarni dio.
     * @param real realni dio kompleksnog broja
     * @return novostvorenu instancu razreda
     */
    public static ComplexNumber fromReal(double real) {
        return new ComplexNumber(normalize(real), 0);
    }

    /**
     * Stvora novu instancu reazreda <code>ComplexNumber</code> s 0 za realni dio i poslanim imaginarnim dijelom.
     * Creates new instance of <code>ComplexNumber</code> class with 0 for real part and received imaginary part.
     * @param imaginary imaginarni dio kompleksnog broja
     * @return novostvorenu instancu razreda
     */
    public static ComplexNumber fromImaginary(double imaginary) {
        return new ComplexNumber(0, normalize(imaginary));
    }

    /**
     * Stvara novu instancu razreda <code>ComplexNumber</code> s poslanim kompleksnim brojem u polarnim koordinatama
     * @param magnitude magnituda kompleksnog broja u polarnim koordinatama
     * @param angle kut kompleksnog broja u polarnim koordinatama. Kut se svodi na granice od 0 do 2*PI
     * @return novostvorenu instancu razreda
     */
    public static ComplexNumber fromMagnitudeAndAngle(double magnitude, double angle) {
        double normalizedAngle = normalizeAngle(angle);
        double real = magnitude * cos(normalizedAngle), imaginary = magnitude * sin(normalizedAngle);
        return new ComplexNumber(normalize(real), normalize(imaginary));
    }

    /**
     * Stvara novu instancu razreda <code>ComplexNumber</code> s poslanim <code>String</code> iz kojeg se parsirau realni i kompleksni dio
     * @param s <code>String</code> iz kojeg se parsiraju realni i kompleksni dio
     * @return novostvorenu instancu razreda
     * @throws NullPointerException ako je poslan <code>null</code>
     * @throws NumberFormatException ako se unutar izraza nalaze praznine (praznine na početku i nakraju su uredu) te znak koji nije broj, +, - ili .
     */
    public static ComplexNumber parse(String s) {
        if(s == null)
            throw new NullPointerException("Can't parse null!");
        char[] array = s.trim().toCharArray();
        if(array.length == 0)
            throw new IllegalArgumentException("Can't parse an empty string!");
        String real = "";
        String imaginary = "";
        int index = array.length - 1;
        if(array[index] == 'i') {
            imaginary = "i";
            while(index > 0 && array[index] != '+' && array[index] != '-') {
                index--;

                checkValid(array[index]);

                imaginary = array[index] + imaginary;
            }
        }
        if(index != 0) {
            while(index > 0) {
                index--;

                checkValid(array[index]);

                real = array[index] + real;
            }
        }

        if(real.equals("")) real = "0";
        imaginary = switch (imaginary) {
            case "" -> "0";
            case "i" -> "1";
            case "-i" -> "-1";
            default -> imaginary.substring(0, imaginary.length() - 1);
        };

        return new ComplexNumber(parseDouble(real), parseDouble(imaginary));
    }

    /**
     * Služi za internu provjeru valjanosti poslanog <code>String</code>a
     * @param c vrijednost koju provjeravamo
     * @throws NumberFormatException ukoliko je vrijednost neispravna
     */
    private static void checkValid(char c) {
        if(Character.isWhitespace(c))
            throw new NumberFormatException("Can't have spaces in the middle of the expression!");
        else if(!Character.isDigit(c) && c != '+' && c != '-' && c != '.')
            throw new NumberFormatException(c +" is not a digit!");
    }

    /**
     * Privatna metoda za zaokruživanje brojeva na četiri decimale
     * @param value vrijednost koju treba zaokružiti
     * @return poslanu vrijednost zaokruženu na četiri decimale
     */
    private static double normalize(double value) {
       return (double) Math.round(value * 10_000) / 10_000;
    }

    /**
     * poslani kut svodi na pozitivne granice od 0 do 2*PI te ga zaokružuje na četiri decimale
     * @param angle kut koji treba normalizirati
     * @return normaliziranu poslanu vrijednost
     */
    private static double normalizeAngle(double angle) {
        double newAngle = normalize(angle % (2 * PI));
        if(newAngle < 0)
            newAngle = angle + 2*PI;
        return normalize(newAngle);
    }

    /**
     * Vraća realni dio kompleksnog broja
     * @return realni dio kompleksnog broja
     */
    public double getReal() { return this.real; }

    /**
     * Vraća imaginarni dio kompleksnog broja
     * @return imaginarni dio kompleksnog broja
     */
    public double getImaginary() { return this.imaginary; }

    /**
     * Vraća magnitudu (polarnu kordinatu) kompleksnog broja
     * @return magnitudu kompleksnog broja
     */
    public double getMagnitude() {
        double magnitude = hypot(this.real, this.imaginary);
        return normalize(magnitude);
    }

    /**
     * Vraća kut (polarnu kordinatu) kompleksnog broja
     * @return kut kompleksnog broja
     */
    public double getAngle() {
        double result;
        if(real == 0) {
            if (imaginary > 0) result = PI / 2;
            else result = -PI / 2;
        } else if(real < 0) result = atan(this.imaginary / this.real) + PI ;
        else result = atan(this.imaginary / this.real);

        return normalizeAngle(result);
    }

    /**
     * Zbraja dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param c kompleksni broj s kojim se zbraja
     * @return novostvorenu instancu razreda
     * @throws NullPointerException ako je poslan <code>null</code>
     */
    public ComplexNumber add(ComplexNumber c) {
        if(c == null)
            throw new NullPointerException("Null was sent. Can't add with null!");
        return new ComplexNumber(this.real + c.real, this.imaginary + c.imaginary);
    }

    /**
     * Oduzima dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param c kompleksni broj kojeg treba oduzeti
     * @return novostvorenu instancu razreda
     * @throws NullPointerException ako je poslan <code>null</code>
     */
    public ComplexNumber sub(ComplexNumber c) {
        if(c == null)
            throw new NullPointerException("Null was sent. Can't subtract with null!");
        return new ComplexNumber(this.real - c.real, this.imaginary - c.imaginary);
    }

    /**
     * Množi dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param c kompleksni broj s kojimse treba pomnožiti
     * @return novostvorenu instancu razreda
     * @throws NullPointerException ako je poslan <code>null</code>
     */
    public ComplexNumber mul(ComplexNumber c) {
        if(c == null)
            throw new NullPointerException("Null was sent. Can't multiply with null!");
        double newReal = this.real * c.real - this.imaginary * c.imaginary;
        double newImaginary = this.imaginary * c.real + this.real * c.imaginary;

        return new ComplexNumber(newReal, newImaginary);
    }

    /**
     * Dijeli dva broja i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param c kompleksni broj s kojim se treba podijeliti (djelitelj)
     * @return novostvorenu instancu razreda
     * @throws NullPointerException ako je poslan <code>null</code>
     */
    public ComplexNumber div(ComplexNumber c) {
        if(c == null)
            throw new NullPointerException("Null was sent. Can't divide with null!");
        if(c.real == 0 && c.imaginary == 0)
            throw new ArithmeticException("Can't divide by zero!");
        double newMagnitude = this.getMagnitude() / c.getMagnitude();
        double newAngle = this.getAngle() - c.getAngle();

        return fromMagnitudeAndAngle(newMagnitude, newAngle);
    }

    /**
     * Potencira kompleksni broj i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param n eksponenst
     * @return novostvorenu instancu razreda
     * @throws IllegalArgumentException ako je poslani broj manji od 0
     */
    public ComplexNumber power(int n) {
        if(n < 0)
            throw new IllegalArgumentException("n must be larger or equal then 0");
        double newMagnitude = pow(this.getMagnitude(), n);
        double newAngle = n * this.getAngle();

        return fromMagnitudeAndAngle(newMagnitude, newAngle);
    }

    /**
     * Korjenuje kompleksni broj i vraća novu instancu <code>ComplexNumber</code> razreda s tim vrijednostima
     * @param n potencija korjena
     * @return novostvorenu instancu razreda
     * @throws IllegalArgumentException ako je poslani broj manji od 1
     */
    public ComplexNumber[] root(int n) {
        if(n < 1)
            throw new IllegalArgumentException("n must be larger then 0");

        double newMagnitude = Math.pow(this.getMagnitude(), 1.0/n);
        double angle = this.getAngle();
        ComplexNumber[] result = new ComplexNumber[n];
        for(int k = 0; k < n; k++) {
            double newAngle = (angle + 2*k*PI)/n;
            result[k] = fromMagnitudeAndAngle(newMagnitude, newAngle);
        }
        return result;
    }

    /**
     * Zapisuje kompleksni broj u <code>String</code>
     * @return zapis kompleksnog broja u <code>String</code>u
     */
    @Override
    public String toString() {
        String result = "";
        if(this.real != 0) result = String.format("%.2f", (double) round(this.real * 100) / 100);
        if(this.imaginary != 0) {
            if(this.imaginary > 0) result = result + String.format("+%.2fi", (double) round(this.imaginary * 100) / 100);
            else result = result + String.format("%.2fi", (double) round(this.imaginary * 100) / 100);
        }
        return result;
    }
}
