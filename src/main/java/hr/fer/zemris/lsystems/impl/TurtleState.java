package hr.fer.zemris.lsystems.impl;

import java.awt.*;

/**
 * Klasa u kojoj se pohranjuje stanje kornjače na zaslonu.
 */
public class TurtleState {

    /**
     * Trenutna pozicija kornjače na ekranu.
     */
    private Vector2D position;

    /**
     * Smjer u kojem kornjača gleda.
     * Vektor je jedinične duljine.
     */
    private Vector2D direction;

    /**
     * Boja kojom kornjača trenutno crta.
     */
    private Color color;

    /**
     * Duljina pomaka kojom se kornjača pomiće svakim pomakom.
     */
    private double unitLength;

    /**
     * Kontruktor kojim stvaramo instancu razreda sa svim predanim vrijednostima.
     * @param position pozicija kornjače
     * @param direction smijer u kojem kornjača gleda
     * @param color boja kojom kornjača crat
     * @param unitLength duljina pomaka kojom se kornjača pomiće za svaki pomak
     */
    public TurtleState(Vector2D position, Vector2D direction, Color color, double unitLength) {
        this.position = position;
        this.direction = direction;
        this.color = color;
        this.unitLength = unitLength;
    }

    /**
     * Funcija vraća novi objekt <code>TurtleState</code> s kopijom stanja objekta nad kojim je funcija pozvana.
     * @return novu instancu razreda s kopijom stanja
     */
    public TurtleState copy() {
        return new TurtleState(this.position.copy(), this.direction.copy(), this.color, this.unitLength);
    }

    /**
     * Vraća trenutnu poziciju kornjače kao vektor.
     * @return trenutnu poziciju kornjače
     */
    public Vector2D getPosition() {
        return position;
    }

    /**
     * Vraća usmjerenje kornjače kao jedinični vektor.
     * @return usmjerenje kornjače
     */
    public Vector2D getDirection() {
        return direction;
    }

    /**
     * Vraća boju kojom kornjača trenutno crta
     * @return boju kojom kornjača trenutno crta
     */
    public Color getColor() {
        return color;
    }

    /**
     * Vraća duljinu pomaka kojom se kornjača pomiče svakim pozivom.
     * @return duljinu pomaka kojom se kornjača pomiče svakim pozivom
     */
    public double getUnitLength() {
        return unitLength;
    }

    /**
     * Postavlja novu poziciju kornjače.
     * @param position nova pozicija kornjače
     * @throws NullPointerException ako je prosljeđena vrijednost null
     */
    public void setPosition(Vector2D position) {
        if(position == null)
            throw new NullPointerException("Position can't be null");
        this.position = position;
    }

    /**
     * Postavlja novu boju kojom kornjača crta.
     * @param color boja
     * @throws NullPointerException ako je prosljeđena vrijednost null
     */
    public void setColor(Color color) {
        if(color == null)
            throw new NullPointerException("Color can't be null");

        this.color = color;
    }

    /**
     * Skaliri pomak kojim se kornjača pomiče
     * @param factor faktor skaliranja pomaka
     */
    public void updateUnitLength(double factor) {
        unitLength *= factor;
    }
}
