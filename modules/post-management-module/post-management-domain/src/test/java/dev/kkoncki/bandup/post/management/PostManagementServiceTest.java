package dev.kkoncki.bandup.post.management;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.file.management.service.FileManagementService;
import dev.kkoncki.bandup.post.management.forms.AddCommentForm;
import dev.kkoncki.bandup.post.management.forms.CreatePostForm;
import dev.kkoncki.bandup.post.management.repository.PostManagementRepository;
import dev.kkoncki.bandup.post.management.service.PostManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostManagementServiceTest {

    @Mock
    private PostManagementRepository repository;

    @Mock
    private FileManagementService fileManagementService;

    @InjectMocks
    private PostManagementServiceImpl postService;

    private CreatePostForm createPostForm;
    private AddCommentForm addCommentForm;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        createPostForm = new CreatePostForm("user-id", "Sample content", null);
        addCommentForm = new AddCommentForm("post-id", "user-id", "Nice post!");
        post = Post.builder()
                .id("post-id")
                .userId("user-id")
                .content("Sample content")
                .mediaUrl(null)
                .mediaType(null)
                .timestamp(Instant.now())
                .likesCount(0)
                .commentsCount(0)
                .build();

        comment = Comment.builder()
                .id("comment-id")
                .postId("post-id")
                .userId("user-id")
                .content("Nice post!")
                .timestamp(Instant.now())
                .build();
    }

    @Test
    void shouldCreatePostWithoutMedia() {
        doAnswer(invocation -> invocation.getArgument(0))
                .when(repository).savePost(any(Post.class));

        Post result = postService.createPost(createPostForm);

        assertNotNull(result.getId());
        assertEquals(createPostForm.getUserId(), result.getUserId());
        assertEquals(createPostForm.getContent(), result.getContent());
        assertNull(result.getMediaUrl());
        assertEquals(MediaType.TEXT, result.getMediaType());
        verify(repository, times(1)).savePost(any(Post.class));
    }


    @Test
    void shouldCreatePostWithMedia() {
        createPostForm.setMediaUrl("https://storage.googleapis.com/file-id.png");
        File file = new File("file-id", "user-id", "png", 1024, "https://example.com/file-id.png", Instant.now());

        when(fileManagementService.get("file-id")).thenReturn(file);
        doAnswer(invocation -> invocation.getArgument(0))
                .when(repository).savePost(any(Post.class));

        Post result = postService.createPost(createPostForm);

        assertNotNull(result.getId());
        assertEquals("IMAGE", result.getMediaType().name());
        verify(fileManagementService, times(1)).get("file-id");
        verify(repository, times(1)).savePost(any(Post.class));
    }

    @Test
    void shouldDeletePostAndItsComments() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));

        postService.deletePost("post-id", "user-id");

        verify(repository, times(1)).deletePost("post-id");
        verify(repository, times(1)).deleteCommentsByPost("post-id");
    }

    @Test
    void shouldNotDeletePostIfUnauthorized() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));

        assertThrows(ApplicationException.class, () -> postService.deletePost("post-id", "another-user-id"));
        verify(repository, never()).deletePost(anyString());
    }

    @Test
    void shouldGetPostsByUserId() {
        List<Post> expectedPosts = List.of(
                Post.builder().id("post-id-1").userId("user-id").content("Content 1").build(),
                Post.builder().id("post-id-2").userId("user-id").content("Content 2").build()
        );
        when(repository.findPostsByUser("user-id")).thenReturn(expectedPosts);

        List<Post> result = postService.getPostsByUserId("user-id");

        assertEquals(expectedPosts.size(), result.size());
        assertEquals(expectedPosts, result);
        verify(repository, times(1)).findPostsByUser("user-id");
    }

    @Test
    void shouldGetPostById() {
        Post expectedPost = Post.builder()
                .id("post-id")
                .userId("user-id")
                .content("Content")
                .build();
        when(repository.findPostById("post-id")).thenReturn(Optional.of(expectedPost));

        Post result = postService.getPostById("post-id");

        assertEquals(expectedPost, result);
        verify(repository, times(1)).findPostById("post-id");
    }

    @Test
    void shouldThrowExceptionWhenPostNotFound() {
        when(repository.findPostById("non-existing-id")).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> postService.getPostById("non-existing-id"));

        verify(repository, times(1)).findPostById("non-existing-id");
    }

    @Test
    void shouldAddCommentToPost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        doAnswer(invocation -> invocation.getArgument(0))
                .when(repository).saveComment(any(Comment.class));

        when(repository.updatePostInteractions("post-id", 0, 1)).thenReturn(1);

        Comment result = postService.addComment(addCommentForm);

        assertNotNull(result.getId());
        assertEquals(addCommentForm.getPostId(), result.getPostId());
        assertEquals(addCommentForm.getContent(), result.getContent());
        verify(repository, times(1)).saveComment(any(Comment.class));
        verify(repository, times(1)).updatePostInteractions("post-id", 0, 1);
    }


    @Test
    void shouldDeleteComment() {
        when(repository.findCommentById("comment-id")).thenReturn(Optional.of(comment));

        postService.deleteComment("comment-id", "user-id");

        verify(repository, times(1)).deleteComment("comment-id");
        verify(repository, times(1)).updatePostInteractions("post-id", 0, -1);
    }

    @Test
    void shouldNotDeleteCommentIfUnauthorized() {
        when(repository.findCommentById("comment-id")).thenReturn(Optional.of(comment));

        assertThrows(ApplicationException.class, () -> postService.deleteComment("comment-id", "another-user-id"));
        verify(repository, never()).deleteComment(anyString());
    }

    @Test
    void shouldGetCommentsByPost() {
        List<Comment> expectedComments = List.of(
                Comment.builder().id("comment-id-1").postId("post-id").content("Comment 1").build(),
                Comment.builder().id("comment-id-2").postId("post-id").content("Comment 2").build()
        );
        when(repository.findCommentsByPost("post-id")).thenReturn(expectedComments);

        List<Comment> result = postService.getCommentsByPost("post-id");

        assertEquals(expectedComments.size(), result.size());
        assertEquals(expectedComments, result);
        verify(repository, times(1)).findCommentsByPost("post-id");
    }

    @Test
    void shouldGetCommentById() {
        Comment expectedComment = Comment.builder()
                .id("comment-id")
                .postId("post-id")
                .content("Comment content")
                .build();
        when(repository.findCommentById("comment-id")).thenReturn(Optional.of(expectedComment));

        Comment result = postService.getCommentById("comment-id");

        assertEquals(expectedComment, result);
        verify(repository, times(1)).findCommentById("comment-id");
    }

    @Test
    void shouldThrowExceptionWhenCommentNotFound() {
        when(repository.findCommentById("non-existing-id")).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> postService.getCommentById("non-existing-id"));

        verify(repository, times(1)).findCommentById("non-existing-id");
    }

    @Test
    void shouldLikePost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        when(repository.isPostLikedByUser("post-id", "user-id")).thenReturn(false);

        when(repository.updatePostInteractions("post-id", 1, 0)).thenReturn(1);

        postService.likePost("post-id", "user-id");

        verify(repository, times(1)).savePostLike("post-id", "user-id");
        verify(repository, times(1)).updatePostInteractions("post-id", 1, 0);
    }


    @Test
    void shouldNotLikeAlreadyLikedPost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        when(repository.isPostLikedByUser("post-id", "user-id")).thenReturn(true);

        assertThrows(ApplicationException.class, () -> postService.likePost("post-id", "user-id"));
        verify(repository, never()).savePostLike(anyString(), anyString());
    }

    @Test
    void shouldUnlikePost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        when(repository.isPostLikedByUser("post-id", "user-id")).thenReturn(true);

        postService.unlikePost("post-id", "user-id");

        verify(repository, times(1)).deletePostLike("post-id", "user-id");
        verify(repository, times(1)).updatePostInteractions("post-id", -1, 0);
    }

    @Test
    void shouldNotUnlikeNotLikedPost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        when(repository.isPostLikedByUser("post-id", "user-id")).thenReturn(false);

        assertThrows(ApplicationException.class, () -> postService.unlikePost("post-id", "user-id"));
        verify(repository, never()).deletePostLike(anyString(), anyString());
    }
}