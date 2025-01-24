package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.post.management.Comment;
import dev.kkoncki.bandup.post.management.Post;

import java.util.List;
import java.util.Optional;

public interface PostManagementRepository {
    void savePost(Post post);
    void deletePost(String postId);
    Optional<Post> findPostById(String postId);
    List<Post> findPostsByUser(String userId);

    void saveComment(Comment comment);
    void deleteComment(String commentId);
    void deleteCommentsByPost(String postId);
    Optional<Comment> findCommentById(String commentId);
    List<Comment> findCommentsByPost(String postId);

    void updatePostInteractions(String postId, int likesDelta, int commentsDelta);
    boolean isPostLikedByUser(String postId, String userId);
    void savePostLike(String postId, String userId);
    void deletePostLike(String postId, String userId);
}
