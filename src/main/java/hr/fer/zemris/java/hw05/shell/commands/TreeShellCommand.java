package hr.fer.zemris.java.hw05.shell.commands;

import hr.fer.zemris.java.hw05.shell.Environment;
import hr.fer.zemris.java.hw05.shell.ShellCommand;
import hr.fer.zemris.java.hw05.shell.ShellIOException;
import hr.fer.zemris.java.hw05.shell.ShellStatus;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class that implements <code>ShellCommand</code> interface.
 * Class represents the operation of outputting a file tree starting from given directory to the chosen output.
 */
public class TreeShellCommand implements ShellCommand {

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
    public TreeShellCommand() {
        name = "tree";
        description = new ArrayList<>();
        description.add("Prints ot a tree of files starting from the provided directory");
        description = Collections.unmodifiableList(description);
    }

    /**
     * Executes the operation of outputting a file tree starting from given directory to the chosen output.
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
        try {

            Files.walkFileTree(dir, new SimpleFileVisitor<>() {
                int lvl = 0;
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    write(dir.toFile().getName());
                    lvl++;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    write(file.toFile().getName());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    lvl--;
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    write("//////////");
                   return FileVisitResult.CONTINUE;
                }

                private void write(String name) {
                    env.writeln(" ".repeat(lvl*2)+"|--"+name);
                }
            });
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