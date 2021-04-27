package hr.fer.oprpp1.custom.scripting.parser;

import hr.fer.oprpp1.custom.scripting.elems.*;
import hr.fer.oprpp1.custom.scripting.nodes.*;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

public class SmartScriptParserTest {

    @Test
    public void testExamplesFromResources() {
        // primjer1.txt
        String docBody = readExample(1);
        SmartScriptParser parser = new SmartScriptParser(docBody);

        DocumentNode document = parser.getDocumentNode();
        String originalDocumentBody = document.toString();
        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), originalDocumentBody.replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing primjer1");

        // primjer2.txt
        docBody = readExample(2);
        parser = new SmartScriptParser(docBody);

        document = parser.getDocumentNode();
        originalDocumentBody = document.toString();
        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), originalDocumentBody.replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing primjer2");

        // primjer3.txt
        docBody = readExample(3);
        parser = new SmartScriptParser(docBody);

        document = parser.getDocumentNode();
        originalDocumentBody = document.toString();
        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), originalDocumentBody.replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing primjer3");

        // primjer4.txt
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(readExample(4)), "Testing primjer4");

        // primjer5.txt
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(readExample(5)), "Testing primjer5");

        // primjer6.txt
        docBody = readExample(6);
        parser = new SmartScriptParser(docBody);

        document = parser.getDocumentNode();
        originalDocumentBody = document.toString();
        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), originalDocumentBody.replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing primjer6");

        // primjer7.txt
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(readExample(7)), "Testing primjer7");

        // primjer8.txt
        docBody = readExample(8);
        parser = new SmartScriptParser(docBody);

        document = parser.getDocumentNode();
        originalDocumentBody = document.toString();
        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), originalDocumentBody.replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing primjer8");

        // primjer9.txt
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(readExample(9)), "Testing primjer9");
    }

    @Test
    public void testNodes() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 10 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n" +
                "{$FOR i 0 10 2 $}\r\n" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n" +
                "{$END$}";

        SmartScriptParser parser = new SmartScriptParser(docBody);

        DocumentNode document = parser.getDocumentNode();

        assertEquals(TextNode.class, document.getChild(0).getClass());
        assertEquals(ForLoopNode.class, document.getChild(1).getClass());
        assertEquals(TextNode.class, document.getChild(2).getClass());
        assertEquals(ForLoopNode.class, document.getChild(3).getClass());

        Node forLoop1 = document.getChild(1);
        assertEquals(TextNode.class, forLoop1.getChild(0).getClass());
        assertEquals(EchoNode.class, forLoop1.getChild(1).getClass());
        assertEquals(TextNode.class, forLoop1.getChild(2).getClass());

        Node forLoop2 = document.getChild(3);
        assertEquals(TextNode.class, forLoop2.getChild(0).getClass());
        assertEquals(EchoNode.class, forLoop2.getChild(1).getClass());
        assertEquals(TextNode.class, forLoop2.getChild(2).getClass());
        assertEquals(EchoNode.class, forLoop2.getChild(3).getClass());
        assertEquals(TextNode.class, forLoop2.getChild(4).getClass());

        assertEquals(docBody.replaceAll("\\s*\\n*\\t*\\r*", ""), document.toString().replaceAll("\\s*\\n*\\t*\\r*", ""), "Testing document.toString(");
    }

    @Test
    public void testUnclosedForLoop() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 10 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n" +
                "{$FOR i 0 10 2 $}\r\n" +
                " sin({$=i$}^2) = {$= i i * @sin \"0.000\" @decfmt $}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testToManyEndTags() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 10 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n" +
                "{$END$}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testMultipleEchoSymbols() {
        String docBody = "This is sample text.\r\n" +
                " This is {$= i @sin = 3 $}-th time this message is generated.\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testInvalidEndTag() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 10 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END i $}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testInvalidForTag() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR 6 1 10 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testToLittleElementsInForTag() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }

    @Test
    public void testToMuchElementsInForTag() {
        String docBody = "This is sample text.\r\n" +
                "{$ FOR i 1 3 6 \"bbb\" $}\r\n" +
                " This is {$= i $}-th time this message is generated.\r\n" +
                "{$END$}\r\n";
        assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
    }


    // Helper class that reads the specified files in resources/extra.
    // the files must be named primjerN.txt where N is the number of the example.
    private String readExample(int n) {
        try(InputStream is = this.getClass().getClassLoader().getResourceAsStream("extra/primjer"+n+".txt")) {
            if(is==null) throw new RuntimeException("Datoteka extra/primjer"+n+".txt je nedostupna.");
            byte[] data = is.readAllBytes();
            String text = new String(data, StandardCharsets.UTF_8);
            return text;
        } catch(IOException ex) {
            throw new RuntimeException("Greška pri čitanju datoteke.", ex);
        }
    }
}
