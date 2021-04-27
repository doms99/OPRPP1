package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of outputting all available charsets of the system to the chosen output.
 */
public class CharsetsShellCommand implements ShellCommand {

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
    public CharsetsShellCommand() {
        name = "charsets";
        description = new ArrayList<>();
        description.add("Writes out all available charsets for the platform.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of outputting all available charsets to the passed <code>Environment</code>.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        StringBuilder builder = new StringBuilder();
        for(String name : Charset.availableCharsets().keySet()) {
            builder.append("\t").append(name).append("\n");
        }
        env.writeln("Available charsets:");
        env.write(builder.toString());
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