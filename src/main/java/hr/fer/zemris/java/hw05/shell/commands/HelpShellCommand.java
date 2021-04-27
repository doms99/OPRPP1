package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of outputting names and descriptions of all the implemented commands.
 */
public class HelpShellCommand implements ShellCommand {

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
    public HelpShellCommand() {
        this.name = "help";
        description = new ArrayList<>();
        description.add("Prints out names of commands that are available.");
        description.add("If name of the command is passed then it outputs the description of the command.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of outputting names and descriptions of all the implemented commands.
     * @param env environment used for reading the input and writing the output
     * @param arguments command arguments
     * @return <code>ShellStatus</code> telling the shell to terminate or continue working
     */
    @Override
    public ShellStatus executeCommand(Environment env, String arguments) {
        String[] split;
        try {
            split = SplitUtil.splitArguments(arguments, 0, 1, true);
        } catch (IllegalArgumentException ex) {
            env.writeln(ex.getMessage());
            return ShellStatus.CONTINUE;
        }
        StringBuilder builder = new StringBuilder();
        Collection<ShellCommand> commands;
        if(split.length == 0) {
            commands = env.commands().values();
        } else {
            commands = new ArrayList<>();
            commands.add(env.commands().get(split[0]));
        }
        for(ShellCommand command : commands) {
            boolean first = true;
            for(String des : command.getCommandDescription()) {
                if(first) {
                    first = false;
                    builder.append(String.format("%-15s", command.getCommandName()));
                    builder.append(des);
                    continue;
                }

                builder.append("\n").append(" ".repeat(15)).append(des);
            }
            env.writeln(builder.append("\n").toString());
            builder.delete(0, builder.length());
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
