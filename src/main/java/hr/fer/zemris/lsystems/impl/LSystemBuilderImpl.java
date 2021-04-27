package hr.fer.zemris.lsystems.impl;

import hr.fer.oprpp1.collections.Dictionary;
import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.commands.*;

import java.awt.*;

import static java.lang.Math.*;

/**
 * Klasa kojom se gradi <code>LSystem</code>.
 */
public class LSystemBuilderImpl implements LSystemBuilder {

    /**
     * Lista prijelaza za definirane znakove. Svaki znak ne mora imati definiran prijelaz.
     */
    private Dictionary<Character, String> productions;

    /**
     * Lista naredbi za definirane znakove. Svaki znak ne mora imati definiranu naredbu
     */
    private Dictionary<Character, Command> commands;

    /**
     * Početna pozicija kornjače.
     */
    private Vector2D position;

    /**
     * Početno usmjerenje kornjače.
     */
    private Vector2D direction;

    /**
     * Početni niz iz kojeg kreće razvoj sustava.
     */
    private String axiom;

    /**
     * Dužina jediničnog pomaka kornjače.
     */
    private Double unitLength;

    /**
     * Skalar kojim se jedinični pomak skalira ovisno o dubini iteracija.
     * Jedinični pomak se skalira po formuli <code>unitLength * (unitLengthDegreeScaler^d)</code>.
     */
    private Double unitLengthDegreeScaler;

    /**
     * Stvara prazne <code>Dictionary</code>-je.
     */
    public LSystemBuilderImpl() {
        this.productions = new Dictionary<>();
        this.commands = new Dictionary<>();
    }

    /**
     * Postavlja jednini pomak kornjače.
     * @param unitLength duljina jedniničnog pomaka
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     * @throws IllegalArgumentException ako je prosljeđen broj manji od 0
     */
    @Override
    public LSystemBuilder setUnitLength(double unitLength) {
        if(unitLength < 0)
            throw new IllegalArgumentException("Length must be lager then 0");

        this.unitLength = unitLength;
        return this;
    }

    /**
     * Postavlja početnu poziciju kornjače.
     * @param x x koordinata (lijevo prema desno na ekranu) u rasponu [0, 1]
     * @param y y koordinata (dolje prema gore na ekranu) u rasponu [0, 1]
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     * @throws IllegalArgumentException ako je bilo koja koordinata manja od 0 ili veća od 1
     */
    @Override
    public LSystemBuilder setOrigin(double x, double y) {
        if(x<0 || x>1 || y<0 || y>1)
            throw new IllegalArgumentException("Coordinates must be between 0 and 1. Recieved: " + String.format("x:%s y:%s", x, y));

        position = new Vector2D(x, y);
        return this;
    }

    /**
     * Postavlja usmjerenje kornjače.
     * @param angle kut u odnosu na x os
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     */
    @Override
    public LSystemBuilder setAngle(double angle) {
        direction = new Vector2D(1, 0);
        direction.rotate(Math.toRadians(angle));
        return this;
    }

    /**
     * Postavlja početni niz iz kojeg kreće razvoj sustava.
     * @param axiom početni niz
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     */
    @Override
    public LSystemBuilder setAxiom(String axiom) {
        this.axiom = axiom;
        return this;
    }

    /**
     * Postavlja skalar kojim se skalira jedinični pomak kornjače.
     * @param unitLengthDegreeScaler skalar
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     */
    @Override
    public LSystemBuilder setUnitLengthDegreeScaler(double unitLengthDegreeScaler) {
        this.unitLengthDegreeScaler = unitLengthDegreeScaler;
        return this;
    }

    /**
     * Dodaje produkciju za prosljeđeni znak.
     * Svaki znak može imati samo jednu produkciju.
     * @param c znak za koji se produkcija upotrebljava
     * @param s niz kojim se znak zamjenjuje
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     * @throws NullPointerException ako se umjesto Stringa predan <code>null</code>
     */
    @Override
    public LSystemBuilder registerProduction(char c, String s) {
        if(s == null)
            throw new NullPointerException("Production can't produce null");

        productions.put(c, s);
        return this;
    }

    /**
     * Dodaje naredbu za prosljeđeni znak.
     * Svaki znak može imati samo jednu naredbu.
     * @param c znak za koji se naredba upoterebljava
     * @param s naredba <code>Command</code>
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     * @throws IllegalArgumentException ako je primljena nepostoječa deklaracija naredbe ili vrijednosti naredbi nisu ispravni
     * @throws NullPointerException ako je umjesto String naredbe predan <code>null</code>
     */
    @Override
    public LSystemBuilder registerCommand(char c, String s) {

        String[] split = s.split(" ");
        switch (split[0]) {
            case "draw" -> {
                try {
                    commands.put(c, new DrawCommand(Double.parseDouble(split[1])));
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Invalid draw command sent");
                }
            }
            case "skip" -> {
                try {
                    commands.put(c, new SkipCommand(Double.parseDouble(split[1])));
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Invalid skip command sent");
                }
            }
            case "scale" -> {
                try {
                    commands.put(c, new ScaleCommand(Double.parseDouble(split[1])));
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Invalid scale command sent");
                }
            }
            case "rotate" -> {
                try {
                    commands.put(c, new RotateCommand(Double.parseDouble(split[1])));
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Invalid rotate command sent");
                }
            }
            case "push" -> commands.put(c, new PushCommand());
            case "pop" -> commands.put(c, new PopCommand());
            case "color" -> {
                try {
                    commands.put(c, new ColorCommand(Color.decode("#"+split[1])));
                } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                    throw new IllegalArgumentException("Invalid color command sent");
                }
            }
            default -> throw new IllegalArgumentException("Unexciting command");
        }
        return this;
    }

    /**
     * Parsira poslano polje stringova i inicijalizira sve vrijednosti koje pronalazi.
     * @param rows <code>String</code>ovi s vrijednostima
     * @return vraća <code>this</code>, tj. referencu na objekt nad kojim je funkcija pozvana
     * @throws IllegalArgumentException ako je vrijednosti naredbi nisu ispravne
     */
    @Override
    public LSystemBuilder configureFromText(String[] rows) {
        loop:
        for(String row : rows) {
            String[] split = row.trim().split("\\s+|\\t+|/");
            switch (split[0]) {
                case "command" -> {
                    try {
                        if(split[1].toCharArray().length != 1)
                            throw new IllegalArgumentException("Invalid command");

                        this.registerCommand(split[1].toCharArray()[0], split[2] + (split.length < 4 ? "" : " "+split[3]));
                    } catch (IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid command: " + row);
                    }
                }
                case "production" -> {
                    try {
                        if(split[1].toCharArray().length != 1)
                            throw new IllegalArgumentException("Invalid command");

                        this.registerProduction(split[1].toCharArray()[0], split[2]);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid command");
                    }
                }
                case "origin" -> {
                    try {
                        this.setOrigin(Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid origin coordinates");
                    }
                }
                case "angle" -> {
                    try {
                        this.setAngle(Double.parseDouble(split[1]));
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid angle");
                    }
                }
                case "unitLength" -> {
                    try {
                        this.setUnitLength(Double.parseDouble(split[1]));
                    } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid unitLength");
                    }
                }
                case "unitLengthDegreeScaler" -> {
                    if(row.contains("/")) {
                        split = row.trim().replace("/", " ").split("\\s+|\\t+");
                        try {
                            this.setUnitLengthDegreeScaler(Double.parseDouble(split[1]) / Double.parseDouble(split[2]));
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            throw new IllegalArgumentException("Invalid unitLengthDegreeScaler");
                        }
                    } else {
                        try {
                            this.setUnitLengthDegreeScaler(Double.parseDouble(split[1]));
                        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
                            throw new IllegalArgumentException("Invalid unitLengthDegreeScaler");
                        }
                    }
                }
                case "axiom" -> {
                    try {
                        this.setAxiom(split[1]);
                    } catch (IndexOutOfBoundsException ex) {
                        throw new IllegalArgumentException("Invalid axiom definition");
                    }
                }
                case "" -> {}
                default -> throw new IllegalArgumentException("Unknown directive: " + split[0]);
            }

        }
        return this;
    }

    /**
     * Pomoćna funkcija koja provjerava da li su sve vrijednosti inicijalizirane.
     * @throws RuntimeException ukoliko je se pokuša stvoriti <code>LSystem</code>, a sve vrijednosti nisu inicijalizirane
     */
    private void checkIfReady() {
        if(position == null ||
            direction == null ||
            axiom == null ||
            unitLength == null ||
            unitLengthDegreeScaler == null) throw new RuntimeException("Function was called before all vales were initialized");
    }

    /**
     * Stvara i vraća <code>LSystem</code> iz prethodno inicijaliziranih varijabli.
     * @return <code>LSystem</code> iz prethodno inicijaliziranih varijabli
     * @throws RuntimeException ako sve vrijednosti nisu bile inicijalizirane prije poziva
     */
    @Override
    public LSystem build() {
        checkIfReady();

        /**
         * Klasa koji implementira sučelje <code>LSystem</code> te je reprezentacija jednog L-sistema
         */
        class LSystemImpl implements LSystem {

            /**
             * Kontekst sistema.
             */
            Context context;

            /**
             * Ganerira niz iz aksioma ovisno o dubini prema kojom se izvršavaju naredbe
             * @param i dubina odnosno broj iteracija
             * @return generirani niz
             */
            @Override
            public String generate(int i) {
                String output = axiom;
                StringBuilder builder = new StringBuilder();
                while(i>0) {
                    for(char c : output.toCharArray()) {
                        String temp = productions.get(c);
                        if(temp != null)
                            builder.append(temp);
                        else
                            builder.append(c);
                    }
                    output = builder.toString();
                    builder.delete(0, builder.length());
                    i--;
                }
                return output;
            }

            /**
             * Funkcija poziva funkciju <code>generate</code> te iz dobivenog niza provodi naredbe koje odgovaraju znakovima niza.
             * @param i dubina
             * @param painter objekt koji crta po ekranu
             */
            @Override
            public void draw(int i, Painter painter) {
                context = new Context();
                context.pushState(new TurtleState(position.copy(), direction.copy(), Color.BLACK, unitLength * pow(unitLengthDegreeScaler, i)));
                String generated = generate(i);
                System.out.println(generated);

                for(char c : generated.toCharArray()) {
                    Command command = commands.get(c);
                    if(command != null) {
                        command.execute(context, painter);
                    }
                }
            }
        }
        return new LSystemImpl();
    }
}
