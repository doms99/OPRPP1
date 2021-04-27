package hr.fer.zemris.lsystems.impl;

import hr.fer.zemris.lsystems.LSystem;
import hr.fer.zemris.lsystems.LSystemBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LSystemBuilderImplTest {

    @Test
    public void testNullRegisterProduction() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        assertThrows(NullPointerException.class, () -> builder.registerProduction('c', null));
    }

    @Test
    public void testNullRegisterCommand() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        assertThrows(NullPointerException.class, () -> builder.registerCommand('c', null));
    }

    @Test
    public void testInvalidCommand() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        String[] array = {"origin 0.5 0.0", "command G draw f"};
        assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(array));
    }

    @Test
    public void testUnknownIdentifier() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        String[] array = {"sdfas 0.5 0.0", "command G draw f"};
        assertThrows(IllegalArgumentException.class, () -> builder.configureFromText(array));
    }

    @Test
    public void testInvalidOrigin() {
        LSystemBuilder builder = new LSystemBuilderImpl();
        assertThrows(IllegalArgumentException.class, () -> builder.setOrigin(-1, 0));
    }

    @Test
    public void testBuildOnUninitializedVariables() {
        String[] array =  {"origin 0.05 0.4",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "command F draw 1",
                "command + rotate 60",
                "command - rotate -60",
                "",
                "axiom F",
                "",
                "production F F+F--F+F"};
        assertThrows(RuntimeException.class, () -> (new LSystemBuilderImpl()).configureFromText(array).build());


    }

    @Test
    public void testGenerate1() {
        String[] array =  {"origin 0.05 0.4",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "angle 0",
                "command F draw 1",
                "command + rotate 60",
                "command - rotate -60",
                "",
                "axiom F",
                "",
                "production F F+F--F+F"};
        LSystem lSystem = new LSystemBuilderImpl().configureFromText(array).build();
        assertEquals("F", lSystem.generate(0), "Depth 0");
        assertEquals("F+F--F+F", lSystem.generate(1), "Depth 1");
        assertEquals("F+F--F+F+F+F--F+F--F+F--F+F+F+F--F+F", lSystem.generate(2), "Depth 2");

    }

    @Test
    public void testGenerate2() {
        String[] array =  {"origin 0.05 0.4",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "angle 0",
                "command F draw 1",
                "",
                "axiom F",
                "",
                "production F F+G--F+H",
                "production G G--GK",
                "production H H+H"};
        LSystem lSystem = new LSystemBuilderImpl().configureFromText(array).build();
        assertEquals("F", lSystem.generate(0), "Depth 0");
        assertEquals("F+G--F+H", lSystem.generate(1), "Depth 1");
        assertEquals("F+G--F+H+G--GK--F+G--F+H+H+H", lSystem.generate(2), "Depth 2");
        assertEquals("F+G--F+H+G--GK--F+G--F+H+H+H+G--GK--G--GKK--F+G--F+H+G--GK--F+G--F+H+H+H+H+H+H+H", lSystem.generate(3), "Depth 3");
    }

    @Test
    public void testGenerateNoProductions() {
        String[] array =  {"origin 0.05 0.4",
                "unitLength 0.9",
                "unitLengthDegreeScaler 1.0 / 3.0",
                "",
                "angle 0",
                "command F draw 1",
                "",
                "axiom F",
                ""};
        LSystem lSystem = new LSystemBuilderImpl().configureFromText(array).build();
        assertEquals("F", lSystem.generate(0), "Depth 0");
        assertEquals("F", lSystem.generate(1), "Depth 1");
    }
}
