package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of creating a new directory if the path passed as an arguments
 * doesn't represent existing directory.
 */
public class MkdirShellCommand implements ShellCommand {

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
    public MkdirShellCommand() {
        name = "mkdir";
        description = new ArrayList<>();
        description.add("Creates new directory with a name that was provided.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of creating a new directory if the path passed as an arguments
     * doesn't represent existing directory.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] split;
        try {
            split = SplitUtil.splitExactArguments(arguments, 1, true);
        } catch (IllegalArgumentException ex) {
            env.writeln(ex.getMessage());
            return ShellStatus.CONTINUE;
        }
        Path dir;
        try {
            dir = Path.of(split[0]);
        } catch (InvalidPathException ex) {
            throw new ShellIOException(ex.getMessage());
        }
        if(dir.toFile().isDirectory()) {
            env.writeln("Directory with that name already exists.");
            return ShellStatus.CONTINUE;
        }

        try {
            Files.createDirectory(dir);
            env.writeln("Directory successfully created: "+ dir.toFile().getAbsolutePath());
        } catch (IOException e) {
            throw new ShellIOException(e.getMessage());
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