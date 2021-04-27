package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of outputting and/or changing the symbols of the environment.
 */
public class SymbolShellCommand implements ShellCommand {

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
    public SymbolShellCommand() {
        this.name = "symbol";
        this.description = new ArrayList<>();
        description.add("If only only one argument is provided then it prints out what character represents that symbol which name was provided as an argument.");
        description.add("If two arguments are provided then it sets the character that represents the symbol which name was provided as first argument");
        description.add("to the value of the second argument.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of outputting and/or changing the symbols of the environment.
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
        if(split.length == 1) {
            switch (split[0].toLowerCase()) {
                case "prompt" -> env.writeln("Symbol for PROMPT is '"+ env.getPromptSymbol() +"'");
                case "morelines" -> env.writeln("Symbol for MORELINES is '"+ env.getMorelinesSymbol() +"'");
                case "multiline" -> env.writeln("Symbol for MULTILINE is '"+ env.getMultilineSymbol() +"'");
                default -> env.writeln(split[0] +" symbol doesn't exist");
            }
        } else {
            if(split[1].length() != 1) throw new ShellIOException(split[1] +" can't be a symbol because it to long. Must be the 1 character");
            switch (split[0].toLowerCase()) {
                case "prompt" -> {
                    env.writeln("Symbol for PROMPT changed from '"+ env.getPromptSymbol() +"' to '"+ split[1].charAt(0) +"'");
                    env.setPromptSymbol(split[1].charAt(0));
                }
                case "morelines" -> {
                    env.writeln("Symbol for MORELINES changed from '"+ env.getMorelinesSymbol() +"' to '"+ split[1].charAt(0) +"'");
                    env.setMorelinesSymbol(split[1].charAt(0));
                }
                case "multiline" -> {
                    env.writeln("Symbol for MULTILINE changed from '"+ env.getMultilineSymbol() +"' to '"+ split[1].charAt(0) +"'");
                    env.setMultilineSymbol(split[1].charAt(0));
                }
                default -> env.writeln(split[0] +" symbol doesn't exist");
            }
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