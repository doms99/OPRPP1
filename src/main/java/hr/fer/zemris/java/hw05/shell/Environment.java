package hr.fer.zemris.java.hw05.shell;

import java.util.SortedMap;

/**
 * Interface used to write to and read from where ever the interface is configured.
 */
public interface Environment {

    /**
     * Reads a line from the input.
     * @return <code>String</code> representing line read from the input
     * @throws ShellIOException if an error occurs during reading
     */
    String readLine() throws ShellIOException;

    /**
     * Writes the passed text to the output, but doesn't move the cursor to the new line.
     * @param text <code>String</code> that will be outputted
     * @throws ShellIOException if an error occurs during writing
     */
    void write(String text) throws ShellIOException;

    /**
     * Writes the passed text to the output and moves the cursor to the new line.
     * @param text <code>String</code> that will be outputted
     * @throws ShellIOException if an error occurs during writing
     */
    void writeln(String text) throws ShellIOException;

    /**
     * Returns all implemented commands.
     * @return <code>SortedMap</code> containing all implemented commands
     */
    SortedMap<String, ShellCommand> commands();

    /**
     * Returns the symbol that is outputted by the shell when the previous line that was passed to it contained
     * a morelines symbol at the end.
     * @return multiline symbol
     */
    Character getMultilineSymbol();

    /**
     * Sets the multiline symbol.
     * @param symbol new multiline symbol
     */
    void setMultilineSymbol(Character symbol);

    /**
     * Returns the symbol that is outputted by the shell when it's ready to receive the command.
     * @return prompt symbol
     */
    Character getPromptSymbol();

    /**
     * Sets the prompt symbol.
     * @param symbol new prompt symbol
     */
    void setPromptSymbol(Character symbol);

    /**
     * Returns the symbol that signals the shell that the next line is a part of the same command.
     * @return morelines symbol
     */
    Character getMorelinesSymbol();

    /**
     * Sets the prompt symbol.
     * @param symbol new morelines symbol
     */
    void setMorelinesSymbol(Character symbol);
}
