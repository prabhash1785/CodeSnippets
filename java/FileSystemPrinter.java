import java.util.*;

/**

Given a list of absolute paths, generate a directory structure

 Input:
"/Users/xyz/src/test-project/README.md",
"/Users/xyz/src/test-project/contexts/production.yaml",
"/Users/xyz/src/test-project/contexts/staging.yaml",
"/Users/xyz/src/test-project/deploy.yaml",
"/Users/xyz/src/test-project/docker-compose.yml",
"/Users/xyz/src/test-project/scripts/queue_utils.py",
"/Users/xyz/src/test-project/src/consumer.js",
"/Users/xyz/src/test-project/src/entrypoint.sh",
"/Users/xyz/src/test-project/src/pass.js",
"/Users/xyz/src/test-project/src/logger.js",
"/Users/xyz/src/test-project/src/main.js",
"/Users/xyz/src/test-project/src/permissions.js",
"/Users/xyz/src/test-project/src/pubsub.js",
"/Users/xyz/src/test-project/src/subscriber.js",
"/Users/xyz/src/test-project/src/tests/pass_test.js",
"/Users/xyz/src/test-project/src/stats.js",
"/Users/xyz/src/test-project/package.json",
"/tmp/bar.txt",
"/extra/foo/dat.py"
"/Users/xyz/src/test-project/README23.md",

Output:

Users
-xyz
--src
---test-project
----README.md
----contexts
-----production.yaml
-----staging.yaml
----deploy.yaml
----docker-compose.yml
----scripts
-----queue_utils.py
----src
-----consumer.js
-----entrypoint.sh
-----pass.js
-----logger.js
-----main.js
-----permissions.js
-----pubsub.js
-----subscriber.js
-----tests
------pass_test.js
-----stats.js
----package.json
tmp
-bar.txt
extra
-foo
--dat.py

*/
public class FileSystemPrinter {

    /**
     * Abstraction to represent a File object.
     */
    public static class File {
        private String name;

        private List<File> child;

        public File(String name) {
            this.name = name;
            this.child = new ArrayList<>();
        }

    }

    /**
     * This method accepts a list of file names, creates a file system tree in memory and then prints all the files
     * in file system in formatted/indented manner.
     *
     * @param files
     */
    public static void printFileHierarchy(List<String> files) {

        File file = createFileSystem(files);
        printFileSystem(file);
    }


    private static File createFileSystem(List<String> files) {

        File file = new File("/");

        for (String fileName : files) {

            List<String> tokens = Arrays.asList(fileName.substring(1).split("/"));

            addToFileTree(file, tokens, 0);
        }

        return file;
    }

    private static void addToFileTree(File file, List<String> tokens, int index) {

        if (index >= tokens.size()) {
            return;
        }

        String token = tokens.get(index);
        File newFile = new File(token);

        List<File> children = file.child;
        File childMatch = null;
        for(File child : children) {
            if (child.name.equals(token)) {
                childMatch = child;
                break;
            }
        }

        if (childMatch == null) {
            file.child.add(newFile);

            addToFileTree(newFile, tokens, index + 1);
        } else {

            addToFileTree(childMatch, tokens, index + 1);
        }
    }

    private static void printFileSystem(File file) {

        List<File> children = file.child;

        for (File child : children) {
            printFileSystemHelper(child, 0);
        }
    }

    private static void printFileSystemHelper(File file, int level) {

        if (file == null) {
            return;
        }

        printFormattedName(file.name, level);

        for (File child : file.child) {
            printFileSystemHelper(child, level + 1);
        }
    }

    private static void printFormattedName(String name, int dashCount) {

        while(dashCount > 0)    {
            System.out.print("-");
            dashCount--;
        }

        System.out.print(name + "\n");
    }

    public static void main(String[] args) {

        List<String> files = List.of("/a/b/c", "/x/y/z/1.pdf", "/a/b/foo", "/bar", "/a/100/200/300/400/500");
        printFileHierarchy(files);
    }
}
