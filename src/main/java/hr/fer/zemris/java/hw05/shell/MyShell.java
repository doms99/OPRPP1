package hr.fer.zemris.java.hw05.shell;
import hr.fer.zemris.java.hw05.shell.commands.*;

import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Shell that implements <code>Environment</code> for writing to configured output.
 */
public class MyShell implements Environment {

    /**
     * Map of commands
     */
    private SortedMap<String, ShellCommand> commands;

    /**
     * Symbol that is outputted by the shell when the previous line that was passed to it contained
     */
    private Character multilineSymbol;

    /**
     * Symbol that signals the shell that the next line is a part of the same command.
     */
    private Character morelinesSymbol;

    /**
     * Symbol that is outputted by the shell when it's ready to receive the command.
     */
    private Character promptSymbol;

    /**
     * <code>Scanner</code> used to read and write
     */
    private final Scanner scan;

    public static void main(String[] args) {
        MyShell m = new MyShell();
        m.start();
    }

    /**
     * Creates new instance of the shell
     */
    public MyShell() {
        this.buildCommands();
        this.promptSymbol = '>';
        this.morelinesSymbol = '\\';
        this.multilineSymbol = '|';
        this.scan = new Scanner(System.in);
    }

    public MyShell(String source) {
        this.buildCommands();
        this.promptSymbol = '>';
        this.morelinesSymbol = '\\';
        this.multilineSymbol = '|';
        this.scan = new Scanner(source);
    }

    /**
     * Creates the <code>Map</code> and fills it with <code>ShellCommand</code>s.
     */
    private void buildCommands() {
        commands = new TreeMap<>();
        commands.put("charsets", new CharsetsShellCommand());
        commands.put("cat", new CatShellCommand());
        commands.put("copy", new CopyShellCommand());
        commands.put("exit", new ExitShellCommand());
        commands.put("hexdump", new HexdumpShellCommand());
        commands.put("ls", new LsShellCommand());
        commands.put("mkdir", new MkdirShellCommand());
        commands.put("symbol", new SymbolShellCommand());
        commands.put("tree", new TreeShellCommand());
        commands.put("help", new HelpShellCommand());
    }

    /**
     * Starts the shell and reads available input then calls the matching command if the command exists.
     */
    public void start() {
        this.writeln("Welcome to MyShell v 1.0");

        while(true) {
            this.write(this.getPromptSymbol() + " ");
            String[] lineSplit;
            if(scan.hasNextLine()) {
                lineSplit = this.readLine().split("\\s+");
            } else {
                lineSplit = new String[] {""};
            }
            ShellCommand command = commands.get(lineSplit[0].toLowerCase());
            if(command == null) {
                if(!lineSplit[0].equals("")) this.writeln("Unrecognized command");
                continue;
            }
            String arguments = collectArguments(lineSplit);

            try {
                ShellStatus status = command.executeCommand(this, arguments.toString().trim());
                if(status == ShellStatus.TERMINATE) break;
            } catch (ShellIOException ex) {
                this.writeln("Error occurred: "+ ex.getMessage());
                break;
            }
        }
    }

    protected String collectArguments(String[] split) {
        StringBuilder arguments = new StringBuilder();
        if(split[split.length-1].charAt(0) == this.getMorelinesSymbol()) {
            for(int i = 1; i < split.length-1; i++) arguments.append(" "+ split[i]);

            outer:
            while (true) {
                this.write(this.getMultilineSymbol() +" ");
                String[] nextSplit = this.readLine().split("\\s+");
                for(int i = 0; i < nextSplit.length; i++) {
                    if(i == nextSplit.length-1 && !(nextSplit[i].length() != 0 &&nextSplit[i].charAt(0) == this.getMorelinesSymbol())) {
                        arguments.append(" ").append(nextSplit[i]);
                        break outer;
                    }
                }
            }
        } else {
            for(int i = 1; i < split.length; i++) arguments.append(" ").append(split[i]);
        }
        return arguments.toString();
    }

    @Override
    public String readLine() throws ShellIOException {
        if(scan.hasNextLine()) return scan.nextLine();
        else return null;
    }

    @Override
    public void write(String text) throws ShellIOException {
        if(text != null) {
            System.out.print(text);
        }
    }

    @Override
    public void writeln(String text) throws ShellIOException {
        if(text != null) {
            System.out.println(text);
        }
    }

    @Override
    public SortedMap<String, ShellCommand> commands() {
        return commands;
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
}
