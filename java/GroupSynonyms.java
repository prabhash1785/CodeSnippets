import java.util.*;

/**
 * Given a set of tuples representing synonyms. Group all synonyms in one bucket.
 *
 * @author Prabhash Rathore
 */
public class GroupSynonyms {

    public static class Pair {
        private String first;
        private String second;

        public Pair(String first, String second) {
            this.first = first;
            this.second = second;
        }
    }

    /**
     * Since synonym pairs can have transitive dependencies, this method uses DFS algorithm to group all related
     * synonyms in one group.
     *
     */
    public static Map<String, Set<String>> groupSynonymsUsingDFS(List<Pair> synonyms) {

        Map<String, Set<String>> map = new HashMap<>();

        for (Pair pair : synonyms) {
            String first = pair.first;
            String second = pair.second;

            Set<String> set = map.getOrDefault(first, new HashSet<>());
            set.add(second);
            map.put(first, set);

            Set<String> set2 = map.getOrDefault(second, new HashSet<>());
            set2.add(first);
            map.put(second, set2);
        }

        Map<String, Set<String>> group = new HashMap<>();
        Set<String> visitedSet = new HashSet<>();
        for (Pair pair : synonyms) {
            String word = pair.first;
            if (visitedSet.contains(word)) {
                continue;
            }

            if (!group.containsKey(word)) {
                Set<String> relatedWordSet = new HashSet<>();
                dfs(word, map, visitedSet, relatedWordSet);
                group.put(word, relatedWordSet);
            }
        }

        return group;
    }

    private static void dfs(String word, Map<String, Set<String>> wordMap, Set<String> visitedSet,
                            Set<String> relatedWordsSet) {
        if (visitedSet.contains(word)) {
            return;
        }

        visitedSet.add(word);

        relatedWordsSet.add(word);

        Set<String> words = wordMap.get(word);
        for (String s : words) {
            dfs(s, wordMap, visitedSet, relatedWordsSet);
        }
    }

    public static void main(String[] args) {
        List<Pair> synonyms = List.of(new Pair("main", "primary"),
                new Pair("main", "first"),
                new Pair("first", "primary"),
                new Pair("first", "initial"),
                new Pair("rating", "score"),
                new Pair("count", "number"),
                new Pair("score", "grade"),
                new Pair("performance", "grade")
        );

        Map<String, Set<String>> group = groupSynonymsUsingDFS(synonyms);
        System.out.println("Group: " + group);
    }
}
