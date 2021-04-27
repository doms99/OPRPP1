package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.collections.ArrayIndexedCollection;
import hr.fer.oprpp1.custom.collections.EmptyStackException;
import hr.fer.oprpp1.custom.collections.ObjectStack;
import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.lexer.Lexer;
import hr.fer.oprpp1.custom.scripting.lexer.LexerException;
import hr.fer.oprpp1.custom.scripting.lexer.LexerState;
import hr.fer.oprpp1.custom.scripting.lexer.TokenType;
import hr.fer.oprpp1.custom.scripting.nodes.*;


/**
 * Klasa koja je zadužena za parsiaranje <code>Token</code>a dobivenih iz <code>Lexer</code>a te stavaranje <code>Node</code>-ova od koji se sastoji poslani kod.
 */
public class SmartScriptParser {

    /**
     * <code>Lexer</code> koji tokenizira ulazni kod.
     */
    private Lexer lexer;

    /**
     * Pomoćni stog na kojeg se postavljaju <code>Node</code>-ovi.
     * <code>Nnode</code> na vrhu stoga predstavlja <code>Node</code> unutar kojeg se trenutno nalazimo i kojem pripadaju novonastala djeca.
     */
    private ObjectStack stack;

    /**
     * Korijenski <code>Node</code> svakog dokumenta.
     */
    private DocumentNode document;

    /**
     * Stavara novu instancu razreda s poslanim dokumentom te poziva privatne funkcije koje započinju parsiranje.
     * @param docBody dokument koji želimo parsirati
     */
    public SmartScriptParser(String docBody) {
        lexer = new Lexer(docBody);
        stack = new ObjectStack();
        document = new DocumentNode();
        stack.push(document);
        try {
            parse();
        } catch (LexerException ex) {
            throw new SmartScriptParserException("Parsing failed!");
        }
    }

    /**
     * Funkcija koja se bavi parsiranjem ulaznog dukemnta.
     * Provjerava dobivene tokene te poziva odgovarajuče funckije za prasiranje različitih <code>Node</code>-ova.
     * @throws SmartScriptParserException ukoliko je ulazni dokument pogrešno napisan
     */
    private void parse() {
        lexer.nextToken();
        Node currentNode;
        while(lexer.getToken().getType() != TokenType.EOF) {
            try {
                 currentNode = (Node) stack.peek();
            } catch (EmptyStackException ex) {
                throw new SmartScriptParserException("To many END tags!");
            }
            if(lexer.getToken().getType() != TokenType.TAG_OPEN) {
                parseTextNode(currentNode);
            } else if(lexer.getToken().getType() == TokenType.TAG_OPEN) {
                lexer.setState(LexerState.TAG);
                if(lexer.nextToken().getType() == TokenType.ECHO_TAG) {
                    parseEchoNode(currentNode);
                } else if(lexer.getToken().getType() == TokenType.VARIABLE && lexer.getToken().getValue().asText().toUpperCase().equals("END")) {
                    parseEndTag();
                } else if(lexer.getToken().getType() == TokenType.VARIABLE && lexer.getToken().getValue().asText().toUpperCase().equals("FOR")) {
                    parseForNode(currentNode);
                } else throw new SmartScriptParserException("Invalid tag!");

                lexer.setState(LexerState.TEXT);
                lexer.nextToken();
            }
        }

        stack.pop();
        if(!stack.isEmpty())
            throw new SmartScriptParserException("Some tags weren't closed!");
    }

    /**
     * Pomoćna funkcija koja parsira kod i stvara <code>TextNode</code>
     * @param currentNode <code>Node</code> kojem će se dodati svoreni <code>TextNode</code>
     */
    private void parseTextNode(Node currentNode) {
        StringBuilder builder = new StringBuilder();
        while(lexer.getToken().getType() != TokenType.TAG_OPEN && lexer.getToken().getType() != TokenType.EOF) {
            builder.append(lexer.getToken().getValue().asText() + " ");
            lexer.nextToken();
        }

        String result = builder.toString();
        currentNode.addChildNode(new TextNode(result.substring(0, result.length() - 1)));
    }

    /**
     * Pomoćna funkcija koja parsira kod i stvara <code>EchoNode</code>
     * @param currentNode <code>Node</code> kojem će se dodati svoreni <code>EchoNode</code>
     * @throws SmartScriptParserException ukoliko je tag pogrešno definiran
     */
    private void parseEchoNode(Node currentNode) {
        ArrayIndexedCollection collection = new ArrayIndexedCollection();
        while(lexer.nextToken().getType() != TokenType.TAG_CLOSE) {
            if(lexer.getToken().getType() == TokenType.ECHO_TAG)
                throw new SmartScriptParserException("Invalid tag expression!");
            collection.add(lexer.getToken().getValue());
        }
        Element[] elemArray = new Element[collection.size()];
        for(int i = 0; i < elemArray.length; i++) {
            elemArray[i] = (Element) collection.get(i);
        }
        currentNode.addChildNode(new EchoNode(elemArray));
    }

    /**
     * Pomoćna funkcija koja parsira <code>END</code> tagove.
     * Ona sa stoga skida posljednji dodani <code>Node</code>.
     * @throws SmartScriptParserException ukoliko se unutar taga nalazi previše elemenata ili se pokušala pozvati <code>pop</code> metoda nad praznim stogom
     */
    private void parseEndTag() {
        try {
            stack.pop();
        } catch (EmptyStackException ex) {
            throw new SmartScriptParserException("To many END tags!");
        }
        if(lexer.nextToken().getType() != TokenType.TAG_CLOSE)
            throw new SmartScriptParserException("Invalid END tag!");
    }

    /**
     * Pomoćna funkcija koja parsira kod i stvara <code>ForLoopNode</code>
     * @param currentNode <code>Node</code> kojem će se dodati svoreni <code>ForLoopNode</code>
     * @throws SmartScriptParserException ukoliko je tag pogrešno definiran
     */
    private void parseForNode(Node currentNode) {
        ElementVariable variable;
        Element startExpression;
        Element endExpression;
        Element stepExpression;
        if (lexer.nextToken().getType() != TokenType.VARIABLE || lexer.getToken().getType() == TokenType.TAG_CLOSE)
            throw new SmartScriptParserException("Invalid number od expression in for loop!");
        if(lexer.getToken().getType() == TokenType.ECHO_TAG)
            throw new SmartScriptParserException("Invalid tag expression!");
        variable = (ElementVariable) lexer.getToken().getValue();

        if (lexer.nextToken().getType() == TokenType.TAG_CLOSE)
            throw new SmartScriptParserException("Invalid number od expression in for loop!");
        if(lexer.getToken().getType() == TokenType.ECHO_TAG)
            throw new SmartScriptParserException("Invalid tag expression!");
        startExpression = lexer.getToken().getValue();

        if (lexer.nextToken().getType() == TokenType.TAG_CLOSE)
            throw new SmartScriptParserException("Invalid number od expression in for loop!");
        if(lexer.getToken().getType() == TokenType.ECHO_TAG)
            throw new SmartScriptParserException("Invalid tag expression!");
        endExpression = lexer.getToken().getValue();

        if (lexer.nextToken().getType() == TokenType.TAG_CLOSE) {
            stepExpression = null;
        } else {
            if(lexer.getToken().getType() == TokenType.ECHO_TAG)
                throw new SmartScriptParserException("Invalid tag expression!");

            stepExpression = lexer.getToken().getValue();

            if (lexer.nextToken().getType() != TokenType.TAG_CLOSE)
                throw new SmartScriptParserException("Invalid number od expression in for loop!");
        }

        Node node = new ForLoopNode(variable, startExpression, endExpression, stepExpression);
        currentNode.addChildNode(node);
        stack.push(node);
    }

    /**
     * Vraća korijenski <code>Node</code> ulaznog dokumenta.
     * @return korijenski <code>Node</code> ulaznog dokumenta
     */
    public DocumentNode getDocumentNode() {
        return this.document;
    }
}
