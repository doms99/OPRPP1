package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static java.nio.file.StandardOpenOption.*;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of copying a file.
 */
public class CopyShellCommand implements ShellCommand {

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
    public CopyShellCommand() {
        name = "copy";
        description = new ArrayList<>();
        description.add("Copies the file that was provided and gives it a new name.");
        description.add("If directory was provided as a destination then its copied into that directory with the same name as the original file.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation copying a file of which path is passed as first argument.
     * Second argument is the name for the copied file. If passed path is a directory then
     * the file will be copied into it with it's original name.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] split;
        try {
            split = SplitUtil.splitExactArguments(arguments, 2, true);
        } catch (IllegalArgumentException ex) {
            env.writeln(ex.getMessage());
            return ShellStatus.CONTINUE;
        }
        Path src, dest;
        try {
            src = Path.of(split[0]);
            dest = Path.of(split[1]);
        } catch (InvalidPathException ex) {
            throw new ShellIOException(ex.getMessage());
        }

        if(!src.toFile().exists()) {
            env.writeln("Source doesn't exist");
            return ShellStatus.CONTINUE;
        }

        if (!src.toFile().isFile()) {
            env.writeln("Only files can be copied");
            return ShellStatus.CONTINUE;
        }

        if (dest.toFile().isDirectory()) {
            dest = Path.of(dest.toFile().getAbsolutePath(), src.toFile().getName());
        }

        if (dest.toFile().exists()) {
            env.write("Destination file already exists.");
            while (true) {
                env.write(" Do you want to overwrite it? y/n ");
                String answer = env.readLine().toLowerCase().trim();
                if (answer.equals("n")) return ShellStatus.CONTINUE;
                else if (answer.equals("y")) break;
            }
        }

        try (InputStream reader = Files.newInputStream(src);
             OutputStream output = Files.newOutputStream(dest, WRITE, CREATE, TRUNCATE_EXISTING)) {
            byte[] readChar = new byte[4096];
            int numRead = 0;
            while ((numRead = reader.read(readChar)) != -1) {
                output.write(readChar, 0, numRead);
            }
            env.writeln("File successfully copied.");
        } catch (IOException ex) {
            throw new ShellIOException(ex.getMessage());
        }
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