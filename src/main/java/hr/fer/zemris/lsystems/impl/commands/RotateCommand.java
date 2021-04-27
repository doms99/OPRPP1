package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import static java.lang.Math.PI;

/**
 * Naredba kojojm mijenjamo usmjerenje kornjače.
 */
public class RotateCommand implements Command {

    /**
     * Kut za koji roziramo kornjaču.
     */
    private double angle;

    /**
     * Dobivamo željeni kut rotacije i stvaramo novu instsancu klase.
     * @param angle kot za koji će se kornjača rotirati
     */
    public RotateCommand(double angle) {
        this.angle = angle;
    }

    /**
     * Rotira kornjaču tako da trenutno aktivnom stanju rotira vektor usmjerenja za odabrani kut.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().getDirection().rotate(Math.toRadians(angle));
    }
}
