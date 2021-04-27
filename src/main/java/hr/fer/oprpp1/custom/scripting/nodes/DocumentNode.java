package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * Korijenski <code>Node</code> svake datoteke
 */
public class DocumentNode extends Node {

    /**
     * Varijabla koja služi za praćenje dubine ugnježđivanja prilikom ispisa.
     */
    private static int forDepth = 0;

    /**
     * Stvara novu isntancu razreda.
     */
    public DocumentNode() {
        super();
    };

    /**
     * Vraća sadražaj dokumenta nad kojim je objekt bio stvoren.
     * Vraća ga poštivajuči sva pravila kojim je dokument pisan tako da ako se vraćeni <code>String</code> ponovno parsira trebali bi dobiti jednaku strukturu unutar dokumenta.
     * @return sadržaj dokumenta nad kojim je bojekt bio stvoren
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.numberOfChildren(); i++) {
            Node child = this.getChild(i);
            if(child instanceof ForLoopNode)
                builder.append(printForLoopNode((ForLoopNode) child));
            else
                builder.append(printNode(child));
        }
        return builder.toString();
    }

    /**
     * Pomoćna funckija koja pretvara u tekst sve <code>Node</code>ove osim <code>ForLoopNode</code>-a.
     * @param node <code>Node</code> koji treba pretvorit u tekst
     * @return poslani <code>Node</code> pretvoren u tekst
     */
    private String printNode(Node node) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t".repeat(forDepth));
        builder.append(node.toString());
        return builder.toString();
    }

    /**
     * Pomoćna funckija koja pretvara u tekst <code>ForLoopNode</code>.
     * @param node <code>ForLoopNode</code> koji treba pretvorit u tekst
     * @return poslani <code>ForLoopNode</code> pretvoren u tekst
     */
    private String printForLoopNode(ForLoopNode node) {
        StringBuilder builder = new StringBuilder();
        builder.append("\t".repeat(forDepth));
        builder.append(node.toString());
        forDepth++;
        for(int index = 0; index < node.numberOfChildren(); index++) {
            Node child = node.getChild(index);
            if(child instanceof ForLoopNode)
                builder.append(printForLoopNode((ForLoopNode) child));
            else
                builder.append(printNode(child));
        }
        forDepth--;
        builder.append("\t".repeat(forDepth));
        builder.append("{$END$}\r\n");
        return builder.toString();
    }
}
