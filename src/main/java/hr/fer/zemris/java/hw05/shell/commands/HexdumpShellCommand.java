package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.oprpp1.hw05.crypto.Util;
import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of reading contents of a file and outputting the hex representation of bytes
 * as well as the original contents to the chosen output.
 */
public class HexdumpShellCommand implements ShellCommand {

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
    public HexdumpShellCommand() {
        name = "hexdump";
        description = new ArrayList<>();
        description.add("Produces and outputs provided file in hex notation.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of reading contents of a file and outputting the hex representation of bytes
     * as well as the original contents to the chosen output.
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
        Path src;
        try {
            src = Path.of(split[0]);
        } catch (InvalidPathException ex) {
            throw new ShellIOException(ex.getMessage());
        }
        if(!src.toFile().isFile()) {
            env.writeln("Only files can be hexdumped.");
            return ShellStatus.CONTINUE;
        }

        try(InputStream reader = Files.newInputStream(src)) {
            byte[] readChar = new byte[16];
            int numRead = 0, total = 0;
            while((numRead = reader.read(readChar)) != -1) {
                StringBuilder builder = new StringBuilder();
                builder.append((String.format("%8s", Integer.toHexString(total)).replace(' ', '0'))).append(": ");
                total += numRead;
                String hex = Util.bytetohex(readChar);
                for(int i = 2; i < hex.length(); i += 2) {
                    if(i > numRead*2) builder.append("   ");
                    else builder.append(hex, i-2, i).append(" ");
                }
                for(int i = 0; i < numRead; i++) {
                    char c = (char) readChar[i];
                    if(c < 32 || c > 127) readChar[i] = (byte) '.';
                }
                builder.append("| ").append(new String(readChar, 0, numRead));
                env.writeln(builder.toString());
            }
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