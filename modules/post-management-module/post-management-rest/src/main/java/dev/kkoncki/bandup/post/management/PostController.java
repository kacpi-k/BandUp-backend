package dev.kkoncki.bandup.post.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.post.management.forms.AddCommentForm;
import dev.kkoncki.bandup.post.management.forms.CreatePostForm;
import dev.kkoncki.bandup.post.management.service.PostManagementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final PostManagementService postManagementService;
    private final LoggedUser loggedUser;

    public PostController(PostManagementService postManagementService, LoggedUser loggedUser) {
        this.postManagementService = postManagementService;
        this.loggedUser = loggedUser;
    }

    @PostMapping
    public Post createPost(CreatePostForm form) {
        return postManagementService.createPost(form);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable String postId) {
        postManagementService.deletePost(postId, loggedUser.getUserId());
    }

    @GetMapping("/{postId}")
    public Post getPostById(@PathVariable String postId) {
        return postManagementService.getPostById(postId);
    }

    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable String userId) {
        return postManagementService.getPostsByUserId(userId);
    }

    @PostMapping("/comment")
    public Comment addComment(@RequestBody AddCommentForm form) {
        return postManagementService.addComment(form);
    }

    @GetMapping("/comment/{commentId}")
    public Comment getCommentById(@PathVariable String commentId) {
        return postManagementService.getCommentById(commentId);
    }

    @DeleteMapping("/comment/{commentId}")
    public void deleteComment(@PathVariable String commentId) {
        postManagementService.deleteComment(commentId, loggedUser.getUserId());
    }

    @GetMapping("/{postId}/comment")
    public List<Comment> getCommentsByPost(@PathVariable String postId) {
        return postManagementService.getCommentsByPost(postId);
    }

    @PostMapping("/{postId}/like")
    public void likePost(@PathVariable String postId) {
        postManagementService.likePost(postId, loggedUser.getUserId());
    }

    @DeleteMapping("/{postId}/like")
    public void unlikePost(@PathVariable String postId) {
        postManagementService.unlikePost(postId, loggedUser.getUserId());
    }
}
