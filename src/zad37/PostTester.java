package zad37;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//Да се имплементира класа Post во која ќе се чуваат информациите за објава на една социјална мрежа. Во класата да се имплементираат следните методи:
//
//Конструктор Post(String username, String postContent)
//void addComment (String username, String commentId, String content, String replyToId) - метод за додавање на коментар со ИД commentId и содржина content од корисникот со корисничко име username. Коментарот може да биде директно на самата објава (replyToId=null во таа ситуација) или да биде reply на веќе постоечки коментар/reply. **
//void likeComment (String commentId) - метод за лајкнување на коментар.
//String toString() - toString репрезентација на една објава во форматот прикажан подолу. Коментарите се листаат во опаѓачки редослед според бројот на лајкови (во вкупниот број на лајкови се сметаат и лајковите на replies на коментарите, како и на replies na replies итн.)
class Comment implements Comparable<Comment>{
    private final String username;
    private final String commentId;
    private final String commentContent;
    private final String replyToId;
    private List<Comment> replies;
    private int likes;

    public Comment(String username, String commentId, String commentContent, String replyToId) {
        this.username = username;
        this.commentId = commentId;
        this.commentContent = commentContent;
        this.replyToId = replyToId;

        this.replies = new ArrayList<>();
        this.likes = 0;
    }

    public String getCommentId() {
        return commentId;
    }

    public String getUsername() {
        return username;
    }

    public String getReplyToId() {
        return replyToId;
    }

    public void addLike() {
        likes++;
    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public int getTotalLikes() {
        return likes + replies.stream().mapToInt(Comment::getTotalLikes).sum();
    }

    public String getCommentContent() {
        return commentContent;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    @Override
    public int compareTo(Comment o) {
        return Comparator.comparingInt(Comment::getTotalLikes)
                //.reversed()
                .thenComparingInt(com -> com.getReplies().size()).reversed()
                .thenComparing(Comment::getCommentId)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return toStringWithTab(2);
    }

    public String toStringWithTab(int tabLevel) {
        String tab = "    ".repeat(tabLevel);
        return String.format("%sComment: %s\n%sWritten by: %s\n%sLikes: %d\n",
                tab, commentContent, tab, username, tab, likes) +
                replies.stream()
                        .sorted()
                        .map(reply -> reply.toStringWithTab(tabLevel + 1))
                        .collect(Collectors.joining());
    }
}


class Post {
    private String username;
    private String postContent;
    private Map<String, Comment> comments;

    public Post(String username, String postContent) {
        this.username = username;
        this.postContent = postContent;

        comments = new HashMap<>();
    }

    public void addComment (String username, String commentId, String content, String replyToId) {
        Comment newComment = new Comment(username, commentId, content, replyToId);
        comments.put(commentId, newComment);

        comments.computeIfPresent( replyToId, (key, parentComment) -> {
            parentComment.addReply(newComment);
            return parentComment;
        });
    }

    public void likeComment (String commentId) {
        comments.computeIfPresent(commentId, (key, comment) -> {
            comment.addLike();
            return comment;
        });
    }

    @Override
    public String toString() {
        return String.format("Post: %s\nWritten by: %s\nComments:\n", postContent, username) +
                comments.values().stream()
                        .filter(comment -> comment.getReplyToId() == null)
                        .sorted()
                        .map(Comment::toString)
                        .collect(Collectors.joining());
    }
}

public class PostTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String postAuthor = sc.nextLine();
        String postContent = sc.nextLine();

        Post p = new Post(postAuthor, postContent);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(";");
            String testCase = parts[0];

            if (testCase.equals("addComment")) {
                String author = parts[1];
                String id = parts[2];
                String content = parts[3];
                String replyToId = null;
                if (parts.length == 5) {
                    replyToId = parts[4];
                }
                p.addComment(author, id, content, replyToId);
            } else if (testCase.equals("likes")) { //likes;1;2;3;4;1;1;1;1;1 example
                for (int i = 1; i < parts.length; i++) {
                    p.likeComment(parts[i]);
                }
            } else {
                System.out.println(p);
            }

        }
    }
}

