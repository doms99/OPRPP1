package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;

/**
 * Klasa koja predstavlja tag koji služi za ispis izraza koji je unutar njega.
 */
public class EchoNode extends Node {

    /**
     * Polje elemenata koji se nalaze unutar taga.
     */
    private Element[] elements;

    /**
     * Stava novu instancu razreda s primljenim elementima.
     * @param elements
     */
    public EchoNode(Element[] elements) {
        super();
        this.elements = elements;
    }

    /**
     * Vraća sve elemente koji se nalaze unutar taga.
     * @return svi elementi koji se nalaze unutar taga
     */
    public Element[] getElements() {
        return elements;
    }

    /**
     * Pretvara <code>Node</code> u tekst.
     * @return <code>EchoNode</code> onako kako je on bio napisan u orginalnom dokumentu
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{$= ");
        for(Element e : elements) {
            if(e instanceof ElementString)
                builder.append("\""+ e.asText().replace("\"", "\\\"").replace("\\", "\\\\") + "\" ");
            else if(e instanceof ElementFunction)
                builder.append("@"+ e.asText() + " ");
            else
                builder.append(e.asText() + " ");
        }
        builder.append("$}");
        return builder.toString();
    }
}
