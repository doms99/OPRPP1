package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Klasa koja predstavlja dijelove dokumenta koji nisu izrazi.
 */
public class TextNode extends Node {

    /**
     * Tekst dokumenta koji se nalazi između 2 izraza.
     */
    private String text;

    /**
     * Stvara instancu razreda s poslanim tekstom.
     * @param text tekst dokumenta koji se nalazi između 2 izraza
     */
    public TextNode(String text) {
        super();
        this.text = text;
    }

    /**
     * Vraća tekst dokumenta koji se nalazi između 2 izraza.
     * @return tekst dokumenta koji se nalazi između 2 izraza
     */
    public String getText() {
        return text;
    }

    /**
     * Vraća <code>String</code> onakav kakav je orginalno pisao unutar dokumenta te ga je moguće ponovno isparsirati bez greške.
     * @return <code>String</code> onakav kakav je orginalno pisao unutar dokumenta
     */
    @Override
    public String toString() {
        return text.replace("\\", "\\\\").replace("{$", "\\{$");
    }
}
