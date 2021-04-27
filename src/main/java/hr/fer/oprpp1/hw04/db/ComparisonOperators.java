package hr.fer.oprpp1.hw04.db;

/**
 * Klasa koja sadrži statičke varijable s lambda izrazima za uspoređivanje atributa iz <code>StudentRecord</code>a
 */
public class ComparisonOperators {
    public static final IComparisonOperator LESS = (s1, s2) -> s1.compareTo(s2) < 0;
    public static final IComparisonOperator LESS_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) <= 0;
    public static final IComparisonOperator GREATER = (s1, s2) -> s1.compareTo(s2) > 0;
    public static final IComparisonOperator GREATER_OR_EQUALS = (s1, s2) -> s1.compareTo(s2) >= 0;
    public static final IComparisonOperator EQUALS = (s1, s2) -> s1.compareTo(s2) == 0;
    public static final IComparisonOperator NOT_EQUALS = (s1, s2) -> s1.compareTo(s2) != 0;
    public static final IComparisonOperator LIKE = (s1, s2) -> {
                            if(!s2.contains("*")) return s1.compareTo(s2) == 0;

                            if(s1.length() < s2.length() - 1)
                                return false;

                            char[] s1Array = s1.toCharArray();
                            char[] s2Array = s2.toCharArray();
                            int i = 0;

                            while(s2Array[i] != '*') {
                                if(s1Array[i] != s2Array[i])
                                    return false;
                                i++;
                            }
                            i++;

                            while(i < s2Array.length) {
                                int s1Index = s1Array.length - (s2Array.length - i);
                                if(s2Array[i] == '*')
                                    throw new IllegalArgumentException("Expression can't contain more then one * character");
                                if(s1Array[s1Index] != s2Array[i++])
                                    return false;
                            }

                            return true;
                        };
}
