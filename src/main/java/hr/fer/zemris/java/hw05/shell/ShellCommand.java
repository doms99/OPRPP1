package hr.fer.zemris.java.hw05.shell;

import java.util.List;

/**
 * Interface that is implemented by all the classes that can be called from <code>MyShell</code>
 */
public interface ShellCommand {

    /**
     * Methode that executes the operation implemented by the command class.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    ShellStatus executeCommand(Environment env, String arguments);

    /**
     * Returns the name of the command.
     * @return name of the command
     */
    String getCommandName();

    /**
     * Returns the commands description.
     * @return <code>List</code> of <code>Strings</code>. Each entry is one line of the description.
     */
    List<String> getCommandDescription();
}
