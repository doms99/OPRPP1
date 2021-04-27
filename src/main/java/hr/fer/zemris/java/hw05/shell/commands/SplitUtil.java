package hr.fer.zemris.java.hw05.shell.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class containing static methods for splitting arguments used by <code>ShellCommand</code>s.
 */
public class SplitUtil {

    /**
     * Splits arguments by spaces. Function expects to have between <code>min</code> and <code>max</code>
     * number of arguments after splitting.
     * @param arguments arguments that need to be splitted.
     * @param min minimal number of expected arguments
     * @param max maximal number of expected arguments
     * @param path <code>boolean</code> that needs to be <code>true</code> if it's expected that
     *             a part of the argument will be path
     * @throws IllegalArgumentException if the number of arguments after splitting is less then <code>min</code>
     *                                  or greater then <code>max</code>
     * @return split of arguments as an <code>String</code> array
     */
    public static String[] splitArguments(String arguments, int min, int max, boolean path) {
        String[] result = path ? splitPath(arguments) : split(arguments);
        if(result.length < min || result.length > max)
            throw new IllegalArgumentException("Invalid number of arguments provided. Command takes between "+min+" and "+max+" arguments.");

        return result;
    }

    /**
     * Splits arguments by spaces. Function expects to have <code>num</code>
     * number of arguments after splitting.
     * @param arguments arguments that need to be splitted.
     * @param num expected number of arguments after spliting
     * @param path <code>boolean</code> that needs to be <code>true</code> if it's expected that
     *             a part of the argument will be path
     * @throws IllegalArgumentException if the number of arguments after splitting is <code>num</code>
     * @return split of arguments as an <code>String</code> array
     */
    public static String[] splitExactArguments(String arguments, int num, boolean path) {
        String[] result = path ? splitPath(arguments) : split(arguments);
        if(result.length != num)
            throw new IllegalArgumentException("Invalid number of arguments provided. Command takes "+ num +" arguments.");

        return result;
    }

    /**
     * Private method that is called if path is expected as a part of the argument.
     * Splits arguments by spaces.
     * @param text text that need to be splitted.
     * @throws IllegalArgumentException if the string was opened but was never closed.
     * @return split of arguments as an <code>String</code> array
     */
    private static String[] splitPath(String text) {
        StringBuilder builder = new StringBuilder();
        List<String> result = new ArrayList<>();
        char[] charArray = text.toCharArray();
        for(int i = 0; i < charArray.length; i++) {
            if(charArray[i] == ' ') {
                result.add(builder.toString());
                builder.delete(0, builder.length());
            } else if(charArray[i] == '\"' && builder.length() == 0) {
                i++;
                do {
                    if(charArray[i] == '\\') {
                        if(i < charArray.length - 1 && (charArray[i+1] == '\"' || charArray[i+1] == '\\')) {
                            builder.append(charArray[i+1]);
                            i += 2;
                        } else {
                            builder.append(charArray[i++]);
                        }
                    } else {
                        builder.append(charArray[i++]);
                    }
                } while(charArray[i] != '\"');
                if(i < charArray.length-1 && charArray[i+1] != ' ')
                    throw new IllegalArgumentException("After ending quote must come at least one space.");
            } else {
                builder.append(charArray[i]);
            }
        }
        result.add(builder.toString());
        return Arrays.stream(result.toArray()).filter(s -> !s.equals("")).map(s -> (String) s).toArray(String[]::new);
    }

    /**
     * Splits arguments by spaces.
     * @param text text that need to be splitted.
     * @return split of arguments as an <code>String</code> array
     */
    private static String[] split(String text) {
        return Arrays.stream(text.split("\\s+")).filter(s -> !s.equals("")).toArray(String[]::new);
    }
}
