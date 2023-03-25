/**
 * Given a text and regex pattern containing . or * chars, determine if regex matches text.
 *
 * Eg,
 * text="baaabab"
 *
 * pattern = "**ba*ab***" => true
 * pattern = "*baaa?a*b*" => true
 * pattern = "ba*a?*" => true
 * pattern = "a*ab" => false
 *
 * Algorithm:
 * 3 cases:
 *  - match char to char for non-regex char
 *  - For ? char, just move both pointers in text and pattern as it can match any one chat
 *  - For *, it can be 0 or more any char could match so it has 3 cases:
 *      - * match 0 char, only increment pattern index. Eg, s=dog patten=d*og
 *      - * match any number of chars in text, so move text index but not pattern index. Eg, s=dog pattern=*g
 *      - * match exact one chat in text, move botb text and pattern index. Eg, s=dog pattern=d*g
 */
public class RegExMatcher {

    /**
     * Do regex match of text based on given pattern.
     */
    public static boolean patternMatch(String text, String pattern) {
        return patternMatchHelper(text, 0, pattern, 0);
    }

    private static boolean patternMatchHelper(String text, int textIndex, String pattern, int patIndex) {
        if (textIndex >= text.length() && patIndex >= pattern.length()) {
            return true;
        }

        if (patIndex >= pattern.length()) {
            return false;
        }

        if (textIndex >= text.length()) {
            if (pattern.charAt(patIndex) != '*') {
                return false;
            } else {
                return patternMatchHelper(text, textIndex, pattern, patIndex + 1);
            }
        }

        char textChar = text.charAt(textIndex);
        char patternChar = pattern.charAt(patIndex);

        if (patternChar == '?') {
            return patternMatchHelper(text, textIndex + 1, pattern, patIndex + 1);

        } else if (patternChar == '*') {
            boolean match1 = patternMatchHelper(text, textIndex, pattern, patIndex + 1);
            boolean match2 = patternMatchHelper(text, textIndex + 1, pattern, patIndex);
            boolean match3 = patternMatchHelper(text, textIndex + 1, pattern, patIndex + 1);

            return match1 || match2 || match3;

        } else if (textChar == patternChar){
            return patternMatchHelper(text, textIndex + 1, pattern, patIndex + 1);
        }

        return false;
    }

    public static void test(String text, String pattern) {
        boolean result = patternMatch(text, pattern);
        if (result) {
            System.out.println(text + " <=> " + pattern + " --> MATCH" );
        } else {
            System.out.println(text + " <=> " + pattern + " --> NON-MATCH" );
        }
    }

    public static void main(String[] args) {

        test("dog", "dog");
        test("dog", "dig");
        test("dog", "d?g");
        test("dog", "*");
        test("dog", "dog*****");
        test("dog", "dog*****s");
        test("dogs", "dog*****s");

        test("elephant", "elep?h");
        test("fox", "fox**?");

        test("3", "?");
        test("3", "*****?********");

        test("baaabab", "**ba*ab***");
        test("baaabab", "*baaa?a*b*");
        test("baaabab", "ba*a?*");
        test("baaabab", "a*ab");
    }

}
