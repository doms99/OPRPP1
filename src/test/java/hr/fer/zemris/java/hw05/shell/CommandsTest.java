package hr.fer.zemris.java.hw05.shell;

import hr.fer.zemris.java.hw05.shell.commands.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


import java.nio.file.Path;
import java.util.*;

public class CommandsTest {
    private class MyEnvironment implements Environment {
        private Character promptSymbol = '>';
        private Character morelinesSymbol = '\\';
        private Character multilineSymbol = '|';
        private final List<String> input = new ArrayList<>();
        int cur = 0;
        final StringBuilder output = new StringBuilder();

        public void addInputLine(String... lines) {
            this.input.addAll(Arrays.asList(lines));
        }

        public String getOutput() {
            return output.toString();
        }

        @Override
        public String readLine() throws ShellIOException {
            if(cur < input.size()) return input.get(cur++);
            else return "";
        }

        @Override
        public void write(String text) throws ShellIOException {
            if(text != null) {
                output.append(text);
            }
        }

        @Override
        public void writeln(String text) throws ShellIOException {
            if(text != null) {
                output.append(text).append("\n");
            }
        }

        @Override
        public SortedMap<String, ShellCommand> commands() {
            return null;
        }

        @Override
        public Character getMultilineSymbol() {
            return multilineSymbol;
        }

        @Override
        public void setMultilineSymbol(Character symbol) {
            if(symbol != null) multilineSymbol = symbol;
        }

        @Override
        public Character getPromptSymbol() {
            return promptSymbol;
        }

        @Override
        public void setPromptSymbol(Character symbol) {
            if(symbol != null) promptSymbol = symbol;
        }

        @Override
        public Character getMorelinesSymbol() {
            return morelinesSymbol;
        }

        @Override
        public void setMorelinesSymbol(Character symbol) {
            if(symbol != null) morelinesSymbol = symbol;
        }
    };

    @Test
    public void catCommandTest() {
        MyEnvironment env = new MyEnvironment();
        CatShellCommand command = new CatShellCommand();
        assertEquals("cat", command.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, command.executeCommand(null, ""), "return");
        String expected = "this is a \n" +
                "\n" +
                "\n" +
                "test text for\n" +
                "\n" +
                "commands tests!!!!!!\n";
        command.executeCommand(env, "\"./src/test/resources/test text.txt\"");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void catCommandNonExistingFileTest() {
        MyEnvironment env = new MyEnvironment();
        CatShellCommand command = new CatShellCommand();
        assertEquals("cat", command.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, command.executeCommand(null, ""), "return");
        String expected = "Provided path isn't a file\n";
        command.executeCommand(env, "\"./src/test/resources/test text missing.txt\"");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void catCommandNonSupportedCharsetTest() {
        MyEnvironment env = new MyEnvironment();
        CatShellCommand command = new CatShellCommand();
        assertEquals("cat", command.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, command.executeCommand(null, ""), "return");
        String expected = "Selected charset isn't supported by this instance of Java Virtual Machine\n";
        command.executeCommand(env, "\"./src/test/resources/test text.txt\" wrong");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void catCommandToManyArgumentsTest() {
        MyEnvironment env = new MyEnvironment();
        CatShellCommand command = new CatShellCommand();
        assertEquals("cat", command.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, command.executeCommand(null, ""), "return");
        String expected = "Invalid number of arguments provided. Command takes between 1 and 2 arguments.\n";
        command.executeCommand(env, "\"./src/test/resources/test text.txt\" wrong wrong");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void copyCommandToFileTest() {
        MyEnvironment env = new MyEnvironment();
        CopyShellCommand copy = new CopyShellCommand();
        CatShellCommand cat = new CatShellCommand();

        String src = "./src/test/resources/test text.txt";
        String dest = "./src/test/resources/test text copied.txt";
        Path destPath = Path.of(dest);

        if(destPath.toFile().exists()) destPath.toFile().delete();
        assertEquals("copy", copy.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, copy.executeCommand(null, ""), "return");

        copy.executeCommand(env, String.format("\"%s\" \"%s\"", src, dest));
        String expected = "File successfully copied.\n"+
                "this is a \n" +
                "\n" +
                "\n" +
                "test text for\n" +
                "\n" +
                "commands tests!!!!!!\n";
        cat.executeCommand(env, "\""+ dest +"\"");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void copyCommandNonExistingFileTest() {
        MyEnvironment env = new MyEnvironment();
        CopyShellCommand copy = new CopyShellCommand();
        CatShellCommand cat = new CatShellCommand();

        String src = "./src/test/resources/test text non existing.txt";
        String dest = "./src/test/resources/test text copied.txt";
        assertEquals("copy", copy.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, copy.executeCommand(null, ""), "return");

        copy.executeCommand(env, String.format("\"%s\" \"%s\"", src, dest));
        String expected = "Source file doesn't exist\n";
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void copyCommandToExistingFileTest() {
        MyEnvironment env = new MyEnvironment();
        env.addInputLine("y");
        CopyShellCommand copy = new CopyShellCommand();
        CatShellCommand cat = new CatShellCommand();

        String src = "./src/test/resources/test text.txt";
        String dest = "./src/test/resources/test text exists.txt";
        assertEquals("copy", copy.getCommandName(), "name");
        copy.executeCommand(env, String.format("\"%s\" \"%s\"", src, dest));
        assertEquals(ShellStatus.TERMINATE, copy.executeCommand(null, ""), "return");

        String expected = "Destination file already exists. Do you want to overwrite it? y/n File successfully copied.\n"+
                "this is a \n" +
                "\n" +
                "\n" +
                "test text for\n" +
                "\n" +
                "commands tests!!!!!!\n";
        cat.executeCommand(env, "\""+ dest +"\"");
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void copyCommandToDirectoryTest() {
        MyEnvironment env = new MyEnvironment();
        CopyShellCommand copy = new CopyShellCommand();
        CatShellCommand cat = new CatShellCommand();

        String fileName = "test text.txt";
        String src = "./src/test/resources";
        String dest = "./src/test/resources/test dir";
        Path destPath = Path.of(String.format("%s/%s", dest, fileName));

        if(destPath.toFile().exists()) destPath.toFile().delete();
        assertEquals("copy", copy.getCommandName(), "name");
        assertEquals(ShellStatus.TERMINATE, copy.executeCommand(null, ""), "return");

        copy.executeCommand(env, String.format("\"%s/%s\" \"%s\"", src, fileName, dest));
        String expected = "File successfully copied.\n"+
                "this is a \n" +
                "\n" +
                "\n" +
                "test text for\n" +
                "\n" +
                "commands tests!!!!!!\n";
        cat.executeCommand(env, String.format("\"%s/%s\"", dest, fileName));
        assertEquals(expected, env.getOutput().replace("\r\n", "\n"));
    }

    @Test
    public void testExitCommand() {
        ShellCommand exit = new ExitShellCommand();
        assertEquals(ShellStatus.TERMINATE, exit.executeCommand(new MyEnvironment(), ""), "return");
    }

    @Test
    public void symbolCommandTest() {
        SymbolShellCommand command = new SymbolShellCommand();
        MyEnvironment env = new MyEnvironment();
        command.executeCommand(env, "prompt x");
        assertEquals('x', env.getPromptSymbol(), "prompt");
        command.executeCommand(env, "morelines y");
        assertEquals('y', env.getMorelinesSymbol(), "morelines");
        command.executeCommand(env, "multiline z");
        assertEquals('z', env.getMultilineSymbol(), "multiline");
    }

    @Test
    public void testSplitUtil() {
        String original = "first second         third   forth";
        assertArrayEquals(new String[] {"first", "second", "third", "forth"}, SplitUtil.splitExactArguments(original, 4, false), "exact no path split");
        assertArrayEquals(new String[] {"first", "second", "third", "forth"}, SplitUtil.splitExactArguments(original, 4, true), "exact path split");
        assertArrayEquals(new String[] {"first", "second", "third", "forth"}, SplitUtil.splitArguments(original, 4, 4, false), "range no path split");
        assertArrayEquals(new String[] {"first", "second", "third", "forth"}, SplitUtil.splitArguments(original, 4, 4, true), "range path split");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitArguments(original, 1, 3, false), "To much arguments range no path split");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitArguments(original, 1, 3, true), "To much arguments range path split");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitExactArguments(original, 1, false), "To much arguments range exact split");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitExactArguments(original,3, true), "To much arguments exact path split");

        String original2 = "\"first second\"        third     forth";
        assertArrayEquals(new String[] {"\"first", "second\"", "third", "forth"}, SplitUtil.splitExactArguments(original2, 4, false), "exact no path split with path");
        assertArrayEquals(new String[] {"first second", "third", "forth"}, SplitUtil.splitExactArguments(original2, 3, true), "exact path split with path");
        assertArrayEquals(new String[] {"\"first", "second\"", "third", "forth"}, SplitUtil.splitArguments(original2, 1, 4, false), "range no path split with path");
        assertArrayEquals(new String[] {"first second", "third", "forth"}, SplitUtil.splitArguments(original2, 1, 4, true), "range path split with path");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitArguments(original2, 1, 2, false), "To much arguments range no path split with path");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitArguments(original2, 4, 5, true), "To much arguments range path split with path");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitExactArguments(original2, 1, false), "To much arguments range exact split with path");
        assertThrows(IllegalArgumentException.class, () -> SplitUtil.splitExactArguments(original2,4, true), "To much arguments exact path split with path");
    }
}
