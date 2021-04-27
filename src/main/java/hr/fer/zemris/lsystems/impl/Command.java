package hr.fer.zemris.lsystems.impl;
import hr.fer.zemris.lsystems.Painter;

/**
 * Sučelje koje sadrži samo jednu funkciju kojom se izvodi implementirana naredba.
 * Implementiraju ju sve klase kojima će se raditi.
 */
public interface Command {

    /**
     * Funkcija koja izvodi implementiranu naredbu.
     * @param ctx kontekst u kojem su spremljena stanja
     * @param painter <code>Painter</code> koji sadrži funkcije za crtanje po platnu
     */
    void execute(Context ctx, Painter painter);
}
