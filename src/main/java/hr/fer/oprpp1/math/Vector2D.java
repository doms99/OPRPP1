package hr.fer.oprpp1.math;

import static java.lang.Math.*;


/**
 * Klasa koja modelira dvodimenzijonalni vektor i omogućava nam rad s njima.
 */
public class Vector2D {

    /**
     * x koordinata vektora
     */
    private double x;

    /**
     * y koordinata vektora
     */
    private double y;

    /**
     * Stvara novu instancu razreda s poslanim vrijednostima
     * @param x x koordinata vektora
     * @param y y koordinata vektora
     */
    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Vraća x koordinatu vektora
     * @return x koordinatu vektora
     */
    public double getX() {
        return x;
    }

    /**
     * Vraća y koordinatu vektora
     * @return y koordinatu vektora
     */
    public double getY() {
        return y;
    }

    /**
     * Dodaje poslani vektor onom vektoru nad kojim je funcija pozvana
     * @param offset vektor koji se dodaje
     */
    public void add(Vector2D offset) {
        y += offset.y;
        x += offset.x;
    }

    /**
     * Stvara novu instancu razreda sa vrijednostima vektora nad kojim je funkcija poslana
     * te na taj vektora dodaje poslani i vraća ga.
     * @param offset vektor koji se dodaje
     * @return novi vektor sa dodanim vrijednostima
     */
    public Vector2D added(Vector2D offset) {
        Vector2D newVector = new Vector2D(x, y);
        newVector.add(offset);

        return newVector;
    }

    /**
     * Rotira vektor za poslani kut
     * @param angle kut rotacije
     */
    public void rotate(double angle) {
        double oldAngle = x < 0 ? atan(y/x) + PI : atan(y/x);
        double newAngle = oldAngle + angle;
        double magnitude = hypot(x, y);

        x = magnitude * cos(newAngle);
        y = magnitude * sin(newAngle);
    }


    /**
     * Stvara novu instancu razreda sa vrijednostima vektora nad kojim je funkcija poslana
     * te taj vektor rotira i vraća ga.
     * @param angle kut za koji se vektor rotira
     * @return novi vektor sa rotiranim vrijednostima
     */
    public Vector2D rotated(double angle) {
        Vector2D newVector = new Vector2D(x, y);
        newVector.rotate(angle);

        return newVector;
    }

    /**
     * Skalira vektor za poslanu vrijednost.
     * @param scalar vrijednost kojom se vektor skalira
     */
    public void scale(double scalar) {
        x *= scalar;
        y *= scalar;
    }

    /**
     * Stvara novu instancu razreda sa vrijednostima vektora nad kojim je funkcija poslana
     * te taj vektor skalira i vraća ga.
     * @param scalar vrijednost kojom se vektor skalira
     * @return novi vektor sa skaliranim vrijednostima
     */
    public Vector2D scaled(double scalar) {
        Vector2D newVector = new Vector2D(x, y);
        newVector.scale(scalar);

        return newVector;
    }

    /**
     * Vraća novu instancu razreda sa istim vrijednostima kao i vektor nad kojim je funcija pozvana.
     * @return novu instancu razreda sa istim vrijednostima kao i vektor nad kojim je funcija pozvana
     */
    public Vector2D copy() {
        return new Vector2D(x, y);
    }
}
