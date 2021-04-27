package hr.fer.zemris.lsystems.impl.commands;

import hr.fer.zemris.lsystems.Painter;
import hr.fer.zemris.lsystems.impl.Command;
import hr.fer.zemris.lsystems.impl.Context;

/**
 * Naredba kojom trentuno aktivno stanje skidamo sa stoga.
 */
public class PushCommand implements Command {

    /**
     * Skidamo sa stoga prvi element te tako kornjaču vraćamo na zapamćeno stanje.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    @Override
    public void execute(Context ctx, Painter painter) {
        ctx.pushState(ctx.getCurrentState().copy());
    }
}
