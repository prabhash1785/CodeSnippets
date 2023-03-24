/**
 *
 * Given a number n, print a diamond as follows:
 *
 * Example:
 * n = 3
 *
 *       *
 *      ***
 *     *****
 *      ***
 *       *
 *
 */
public class DiamondGenerator {

    public static void printDiamond(int n) {
        if (n < 1) {
            throw new RuntimeException("Invalid input: " + n);
        }

        int maxStars = 2 * n - 1;

        // print top half of diamond
        for (int i = 1; i <= n; i++) {
            int starCount = 2 * i - 1;
            int spaceCount = (maxStars - starCount) / 2;

            printStars(starCount, spaceCount);
        }

        // print bottom half of diamond
        for (int i = n - 1; i > 0; i--) {
            int starCount = 2 * i - 1;
            int spaceCount = (maxStars - starCount) / 2;

            printStars(starCount, spaceCount);
        }
    }

    private static void printStars(int starCount, int spaceCount) {
        while (spaceCount > 0) {
            System.out.print(" ");
            spaceCount--;
        }

        while (starCount > 0) {
            System.out.print("*");
            starCount--;
        }

        System.out.print("\n");
    }

    public static void main(String[] args) {
        int n = 5;
        System.out.println("Diamond of size: " + n);
        printDiamond(n);

        n = 3;
        System.out.println("Diamond of size: " + n);
        printDiamond(n);

        n = 10;
        System.out.println("Diamond of size: " + n);
        printDiamond(n);
    }
}
