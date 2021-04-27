package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of closing the shell.
 */
public class ExitShellCommand implements ShellCommand {

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
    public ExitShellCommand() {
        this.name = "exit";
        description = new ArrayList<>();
        description.add("Exits the program.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Return <code>ShellStatus.TERMINATE</code> which causes the shell to finish and close.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        return ShellStatus.TERMINATE;
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