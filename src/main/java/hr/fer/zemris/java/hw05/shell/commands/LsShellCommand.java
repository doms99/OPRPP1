package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of outputting the contents of the directory to the chosen output.
 */
public class LsShellCommand implements ShellCommand {

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
    public LsShellCommand() {
        name = "ls";
        description = new ArrayList<>();
        description.add("Writes out all the files and directories that are in provided directory.");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of outputting the contents of the directory to the chosen output.
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
        if(!dir.toFile().isDirectory()) {
            env.writeln("Provided path isn't a directory.");
            return ShellStatus.CONTINUE;
        }

        try {
            for(File f : dir.toFile().listFiles()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                BasicFileAttributeView faView = Files.getFileAttributeView(
                        dir, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
                );
                BasicFileAttributes attributes = faView.readAttributes();
                FileTime fileTime = attributes.creationTime();
                String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));

                StringBuilder idn = new StringBuilder();
                if(f.isDirectory()) idn.append('d');
                else idn.append('-');

                if(f.canRead()) idn.append('r');
                else idn.append('-');

                if(f.canWrite()) idn.append('w');
                else idn.append('-');

                if(f.canExecute()) idn.append('x');
                else idn.append('-');

                env.writeln(String.format("%s %10d %s %s", idn.toString(), f.length(), formattedDateTime, f.getName()));
            }
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