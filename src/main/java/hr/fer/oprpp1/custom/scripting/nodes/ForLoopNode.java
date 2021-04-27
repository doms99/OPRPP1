package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementFunction;
import hr.fer.oprpp1.custom.scripting.elems.ElementString;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

/**
 * Klasa koja predtavlja <code>for</code> petlju.
 */
public class ForLoopNode extends Node {
    /**
     * Varijabla petlje.
     */
    private ElementVariable variable;

    /**
     * početna vrijednost.
     */
    private Element startExpression;

    /**
     * Završna vrijednost.
     */
    private Element endExpression;

    /**
     * Vrijabla koja prikazuje za koliko ili kako se mijenja varijabla prije svake iteracije petlje.
     */
    private Element stepExpression;

    /**
     * Stavara novu instancu klase sa svim astributim osim <code>stepExpression</code> koji  se postavlja na <code>null</code>.
     * @param variable vaijabla <code>for</code> petlje
     * @param startExpression početna vrijednost
     * @param endExpression završna vrijednost
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression) {
        super();
        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpression = endExpression;
    }

    /**
     * Stavara novu instancu klase sa svim astributim osim <code>stepExpression</code> koji  se postavlja na <code>null</code>.
     * @param variable vaijabla <code>for</code> petlje
     * @param startExpression početna vrijednost
     * @param endExpression završna vrijednost
     * @param stepExpression izraz promijene varijable prije svake iteracije petlje
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpression, Element stepExpression) {
        this(variable, startExpression, endExpression);
        this.stepExpression = stepExpression;
    }

    /**
     * Vraća varijablu <code>for</code> petlje.
     * @return varijablu <code>for</code> petlje
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Vraća početnu vrijednost varijable.
     * @return početnu vrijednost varijable
     */
    public Element getStartExpression() {
        return startExpression;
    }


    /**
     * Vraća završnu vrijednost varijable.
     * @return završnu vrijednost varijable
     */
    public Element getEndExpression() {
        return endExpression;
    }

    /**
     * Vraća izraz promijene vrijednost varijable prije svake iteracije.
     * @return izraz promijene vrijednost varijable prije svake iteracije
     */
    public Element getStepExpression() {
        return stepExpression;
    }

    /**
     * Pretvara <code>Node</code> u tekst.
     * @return <code>FoorLoopNode</code> onako kako je on bio napisan u orginalnom dokumentu
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{$ FOR ");
        builder.append(variable.asText() + " ");

        if(startExpression instanceof ElementString)
            builder.append("\""+ startExpression.asText() + "\" ");
        else
            builder.append(startExpression.asText() + " ");

        if(endExpression instanceof ElementFunction)
            builder.append("@"+ endExpression.asText() +" ");
        else
            builder.append(endExpression.asText() + " ");

        if(stepExpression != null)
            if(stepExpression instanceof ElementString)
                builder.append("\""+ stepExpression.asText() + "\" ");
            else
                builder.append(stepExpression.asText() + " ");
        builder.append("$}");

        return builder.toString().replace("\"", "\\\"").replace("\\", "\\\\");
    }
}
