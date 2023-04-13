import java.util.*;

/**
 * Trie implementation.
 *
 * @author Prabhash Rathore
 */
public class Trie {

    public static class Node {
        private char data;
        private boolean endOfWord;
        private Map<Character, Node> dataMap;

        public Node(char data) {
            this.data = data;
            this.endOfWord = false;
            this.dataMap = new HashMap<>();
        }

        @Override
        public String toString() {
            return "(" + data + "," + endOfWord + "," + dataMap + ")";
        }
    }

    private Node root;

    public Trie() {
        this.root = new Node('0');
    }

    public void addString(String s) {
        if (s == null || s.length() == 0) {
            return;
        }

        addStringHelper(s, 0, root.dataMap);
    }

    private void addStringHelper(String s, int i, Map<Character, Node> map) {
        if (i >= s.length()) {
            return;
        }

        char c = s.charAt(i);
        Node matchedNode = map.get(c);

        if (matchedNode == null) {
            matchedNode = new Node(c);
            map.put(c, matchedNode);
        }

        // end of word
        if (i == s.length() - 1) {
            matchedNode.endOfWord = true;
        }


        addStringHelper(s, i + 1, matchedNode.dataMap);
    }

    public boolean findWord(String input) {
        if (input == null || input.length() == 0) {
            throw new IllegalArgumentException();
        }

        return findWordHelper(input, 0, root.dataMap);
    }

    private boolean findWordHelper(String s, int i, Map<Character, Node> map) {
        if (i >= s.length()) {
            return true;
        }

        char c = s.charAt(i);
        // System.out.println("i: " + i + "; c: " + c + "; nodes: " + nodes);

        Node targetNode = map.get(c);

        if (targetNode == null) {
            return false;
        }

        if (i == s.length() - 1) {
            // System.out.println("LastCharMatched: " + c + " -> " + targetNode);
            return targetNode.endOfWord;
        }

        return findWordHelper(s, i + 1, targetNode.dataMap);
    }

    public List<String> getAllStrings() {
        StringBuilder sb = new StringBuilder();
        List<String> result = new ArrayList<>();
        Map<Character, Node> childMap = root.dataMap;

        getAllStringsHelper(childMap, sb, result);

        return result;
    }

    private void getAllStringsHelper(Map<Character, Node> map, StringBuilder sb, List<String> result) {
        if (map == null || map.size() == 0) {
            return;
        }

        for (Map.Entry<Character, Node> entry : map.entrySet()) {
            char c = entry.getKey();
            Node childNode = entry.getValue();

            sb.append(c);

            if (childNode.endOfWord) {
                result.add(sb.toString());
            }

            getAllStringsHelper(childNode.dataMap, sb, result);

            sb.deleteCharAt(sb.length() - 1); // backtrack and delete last char
        }

    }


    public static void main(String args[]) {
        System.out.println("Trie Program");

        Trie trie = new Trie();
        trie.addString("Carrot");
        trie.addString("Car");
        trie.addString("Grapes");
        trie.addString("america");
        trie.addString("africa");
        trie.addString("europe");


        String wordToFind = "Car";
        boolean foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "Carrot";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "Carrots";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "Carr";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "C";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "grapes";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        wordToFind = "Grapes";
        foundWord = trie.findWord(wordToFind);
        System.out.println("Found " + wordToFind + ": " + foundWord);

        System.out.println("All strings in trie:");
        List<String> allStrings = trie.getAllStrings();
        for (String s : allStrings) {
            System.out.println(s);
        }
    }
}