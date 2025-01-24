package dev.kkoncki.bandup.post.management.service;

import dev.kkoncki.bandup.post.management.Comment;
import dev.kkoncki.bandup.post.management.Post;
import dev.kkoncki.bandup.post.management.forms.AddCommentForm;
import dev.kkoncki.bandup.post.management.forms.CreatePostForm;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
public interface PostService {
    Post createPost(@Valid CreatePostForm form);
    void deletePost(String postId, String userId);
    List<Post> getPostsByUserId(String userId);
    Post getPostById(String postId);

    Comment addComment(@Valid AddCommentForm form);
    void deleteComment(String commentId, String userId);
    List<Comment> getCommentsByPost(String postId);
    Comment getCommentById(String commentId);

    void likePost(String postId, String userId);
    void unlikePost(String postId, String userId);
}
