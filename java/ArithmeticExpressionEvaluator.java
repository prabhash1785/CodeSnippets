import java.util.*;

/**
 * Evaluate mathematical expressions.
 *
 */
public class ArithmeticExpressionEvaluator {

    public static int evaluateExpr(String expr) {

        List<String> tokens = extractAllTokens(expr);

        List<String> divResult = evaulateOP(tokens, "/");

        List<String> prodResult = evaulateOP(divResult, "*");

        List<String> sumResult = evaulateOP(prodResult, "+");


        List<String> subResult = evaulateOP(sumResult, "-");

        return Integer.parseInt(subResult.get(0));
    }

    public static List<String> extractAllTokens(String expr) {

        List<String> tokens = new ArrayList<>();

        int num = 0;

        for (char c : expr.toCharArray()) {
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                tokens.add(String.valueOf(num));
                num = 0;

                tokens.add(String.valueOf(c));
            } else if (c == ' ') {
                continue;
            } else {
                num = num * 10 + Character.getNumericValue(c); // 123 => 1 => 10 + 2 = 12 => 12 * 10 + 3 = 123
            }
        }

        tokens.add(String.valueOf(num));

        return tokens;
    }

    public static List<String> evaulateOP(List<String> tokens, String op) {
        List<String> result = new ArrayList<>();

        for (int i = 0; i < tokens.size(); i++) {

            String curToken = tokens.get(i);
            if (curToken.equals(op)) {

                String num1 = result.remove(result.size() - 1);
                String num2 = tokens.get(i + 1);

                i++;

                String value = evaulateOPHelper(num1, num2, op);
                result.add(value);

            } else {

                result.add(curToken);
            }


        }

        return result;
    }

    private static String evaulateOPHelper(String num1, String num2, String op) {
        int val1 = Integer.parseInt(num1);
        int val2 = Integer.parseInt(num2);

        int result = 0;
        switch (op) {
            case "/":
                result = val1 / val2;
                break;
            case "*":
                result = val1 * val2;
                break;
            case "+":
                result = val1 + val2;
                break;
            case "-":
                result = val1 - val2;
                break;
            default:
                throw new IllegalArgumentException();
        }

        return String.valueOf(result);
    }

    public static void main(String args[]) {

        String input = "2+8/4*5";
        int result = evaluateExpr(input);
        System.out.println("Result: " + result);

        input = " 100 *30/10+     4/2*5-6"; // 100*3+2*5-6 => 300+10-6 = 304
        result = evaluateExpr(input);
        System.out.println("Result: " + result);

    }
}