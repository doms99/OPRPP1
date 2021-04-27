package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;
import hr.fer.zemris.lsystems.impl.Vector2D;

/**
 * Naredba za pomicanje kornjače tako da ostavlja liniju putem kojim je prošla.
 */
public class DrawCommand implements Command {

    /**
     * Broj s kojim se skalira jedinični pomak kornjače.
     */
    private double step;

    /**
     * Dobivamo skalar i stvaramo instancu razreda.
     * @param step skalar kojim skaliramo jedinični pomak kornjače
     */
    public DrawCommand(double step) {
        this.step = step;
    }

    /**
     * Izračunava novu poziciju kornjače, poziva funciju za crtanje po ekranu i ažurira poziciju u trenutno aktivnom stanju kornjače.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        Vector2D currentPosition = ctx.getCurrentState().getPosition();
        Vector2D direction = ctx.getCurrentState().getDirection();
        Vector2D newPosition = currentPosition.added(direction.scaled(ctx.getCurrentState().getUnitLength() * step));
        painter.drawLine(currentPosition.getX(),
                            currentPosition.getY(),
                            newPosition.getX(),
                            newPosition.getY(),
                            ctx.getCurrentState().getColor(),
                            1f);
        ctx.getCurrentState().setPosition(newPosition);
    }
}
