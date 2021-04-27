package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

import java.awt.Color;

/**
 * Naredba za mijenjanje boje kojom kornjača crta.
 */
public class ColorCommand implements Command {

    /**
     * Boja koju želimo postaviti.
     */
    private Color color;

    /**
     * Dobivamo boju i stvaramo instancu razreda.
     * @param color boja koju želimo postaviti
     */
    public ColorCommand(Color color) {
        this.color = color;
    }

    /**
     * Mijenja boju trenutno aktivnom stanju u kontekstu.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.getCurrentState().setColor(color);
    }
}
