package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of reading contents of a file and outputting it to the chosen output.
 */
public class CatShellCommand implements ShellCommand {

    /**
     * Command name.
     */
    private String name;

    /**
     * Command description.
     */
    private List<String> description;

    /**
     * Creates an instance of the class.
     */
    public CatShellCommand() {
        name = "cat";
        description = new ArrayList<>();
        description.add("Writes the file, given as the first argument, to the console.");
        description.add("The second argument is optional and it represents the name of the charset that is used to interpret the file.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of reading from a file and passing it to the passed <code>Environment</code>.
     * First arguments represents the path to the file and the second one the charset that will be used to
     * interpret the file. If the second argument isn't passed then the default charset for that system will be used.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] split;
        try {
            split = SplitUtil.splitArguments(arguments, 1, 2, true);
        } catch (IllegalArgumentException ex) {
            env.writeln(ex.getMessage());
            return ShellStatus.CONTINUE;
        }
        Path file;
        try {
            file = Path.of(split[0]);
        } catch (InvalidPathException ex) {
            throw new ShellIOException(ex.getMessage());
        }
        if(!file.toFile().isFile()) {
            env.writeln("Provided path isn't a file");
            return ShellStatus.CONTINUE;
        }

        Charset charsetName;
        if(split.length == 2) {
            try {
                charsetName = Charset.forName(split[1]);
            } catch (IllegalCharsetNameException ex) {
                env.writeln("Charset with that name doesn't exist.");
                return ShellStatus.CONTINUE;
            } catch (UnsupportedCharsetException ex) {
                env.writeln("Selected charset isn't supported by this instance of Java Virtual Machine");
                return ShellStatus.CONTINUE;
            }
        }
        else charsetName = Charset.defaultCharset();

        try(BufferedReader reader = Files.newBufferedReader(file, charsetName)) {
            char[] readChar = new char[4096];
            int numRead = 0;
            while((numRead = reader.read(readChar)) != -1) {
                env.write(new String(readChar).substring(0, numRead));
            }
        } catch (IOException ex) {
            throw new ShellIOException(ex.getMessage());
        }
        env.writeln("");
        return ShellStatus.CONTINUE;
    }

    @Override
    public String getCommandName() {
        return name;
    }

    @Override
    public List<String> getCommandDescription() {
        return description;
    }
}
