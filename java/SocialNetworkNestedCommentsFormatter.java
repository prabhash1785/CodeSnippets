import java.util.*;

/**
 * Given an unordered list of Comments with unique comment id and it's parent comment id. Create a tree structure of
 * comments from this list and then pretty print. For printing, each nested comment, needs to be indented by a tab. See
 * below example.
 *
 * Example:
 * CommentList: [(1, null), (50, null), (100, null), (2, 1), (10, 1), (3, 2), (5, 2), (20, 10), (75, 100),
 * (4, 3), (200, 4)]
 *
 *
 * 1
 *      2
 *          3
 *              4
 *                  200
 *          5
 *      10
 *          20
 *    50
 *    100
 *          75
 *
 *
 */
public class SocialNetworkNestedCommentsFormatter {

    public static class Comment {
        private long commentId;
        private Long parentCommentId;

        public Comment(long commentId, Long parentCommentId) {
            this.commentId = commentId;
            this.parentCommentId = parentCommentId;
        }
    }

    public static class CommentNode {
        private Comment comment;
        private CommentNode parent;
        private List<CommentNode> children;

        public CommentNode(Comment comment, CommentNode parent) {
            this.comment = comment;
            this.parent = parent;
            this.children = new ArrayList<>();
        }
    }

    /**
     * Create a nested tree structure of comments from flat list of comments.
     *
     * @param comments
     */
    public static List<CommentNode> createCommentTreeFromList(List<Comment> comments) {
        List<CommentNode> topLevelCommentList = new ArrayList<>();

        Map<Long, CommentNode> commentMap = new HashMap<>();

        for (Comment comment : comments) {
            long commentId = comment.commentId;
            Long parentCommentId = comment.parentCommentId;

            CommentNode parentCommentNode = null;

            if (commentMap.containsKey(parentCommentId)) {
                parentCommentNode = commentMap.get(parentCommentId);
            } else {
                if (parentCommentId == null) {
                    parentCommentId = Long.MAX_VALUE;
                }
                Comment parComment = new Comment(parentCommentId, null);
                parentCommentNode = new CommentNode(parComment, null);
                commentMap.put(parentCommentId, parentCommentNode);
            }

            CommentNode commentNode = null;
            if (commentMap.containsKey(commentId)) {
                commentNode = commentMap.get(commentId);

                // possibly fix commentNode created earlier during parent linking
                commentNode.comment.parentCommentId = parentCommentId;
                commentNode.parent = parentCommentNode;

            } else {

                commentNode = new CommentNode(comment, parentCommentNode);
                commentMap.put(commentId, commentNode);
            }

            parentCommentNode.children.add(commentNode); // link child to Parent

            if (parentCommentId == Long.MAX_VALUE) {
                topLevelCommentList.add(commentNode);
            }
        }

        return topLevelCommentList;
    }

    /**
     * Pretty print nested comments using a tab per depth level.
     *
     * @param commentList
     */
    public static void printFormattedComments(List<CommentNode> commentList) {
        for (CommentNode comment : commentList) {
            printFormattedCommentsHelper(comment, 0);
        }
    }

    private static void printFormattedCommentsHelper(CommentNode comment, int tabCount) {
        if (comment == null) {
            return;
        }

        printComment(comment, tabCount);

        List<CommentNode> children = comment.children;
        tabCount++;

        for (CommentNode child : children) {
            printFormattedCommentsHelper(child, tabCount);
        }

        tabCount--;
    }

    private static void printComment(CommentNode comment, int tabCount) {
        StringBuilder sb = new StringBuilder();
        while (tabCount > 0) {
            sb.append("\t");
            tabCount--;
        }

        sb.append(comment.comment.commentId);

        System.out.println(sb);
    }

    public static void main(String args[]) {

        List<Comment> commentList = new ArrayList<>();

        Comment one = new Comment(1, null);
        Comment fifty = new Comment(50, null);
        Comment hundred = new Comment(100, null);

        Comment two = new Comment(2, 1L);
        Comment ten = new Comment(10, 1L);

        Comment three = new Comment(3, 2L);
        Comment five = new Comment(5, 2L);
        Comment twenty = new Comment(20, 10L);
        Comment seventyFive = new Comment(75, 100L);

        Comment four = new Comment(4, 3L);

        Comment twoHundred = new Comment(200, 4L);

        commentList.add(one);
        commentList.add(fifty);
        commentList.add(hundred);
        commentList.add(two);
        commentList.add(ten);
        commentList.add(three);
        commentList.add(five);
        commentList.add(twenty);
        commentList.add(seventyFive);
        commentList.add(four);
        commentList.add(twoHundred);

        List<CommentNode> commentNodeList = createCommentTreeFromList(commentList);

        printFormattedComments(commentNodeList);
    }
}


