package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba kojom se u trenutno aktivno stanju skalira veličina jediničnog pomaka kornjače.
 */
public class ScaleCommand implements Command {

    /**
     * Faktor kojim se jedinični pokam skalira.
     */
    private double factor;

    /**
     * Dobivamo faktor skaliranja i stvaramo novu instancu razreda.
     * @param factor faktor kojim slaliramo jedinični pomak kornjače
     */
    public ScaleCommand(double factor) {
        this.factor = factor;
    }

    /**
     * Skaliramo jedinični pomak u trenutno aktivnom stanju kornjače.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().updateUnitLength(factor);
    }
}
