package dev.kkoncki.bandup.post.management;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.file.management.service.FileManagementService;
import dev.kkoncki.bandup.post.management.forms.AddCommentForm;
import dev.kkoncki.bandup.post.management.forms.CreatePostForm;
import dev.kkoncki.bandup.post.management.repository.PostRepository;
import dev.kkoncki.bandup.post.management.service.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository repository;

    @Mock
    private FileManagementService fileManagementService;

    @InjectMocks
    private PostServiceImpl postService;

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
        assertNull(result.getMediaType());
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
    void shouldAddCommentToPost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        doAnswer(invocation -> invocation.getArgument(0))
                .when(repository).saveComment(any(Comment.class));

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
    void shouldLikePost() {
        when(repository.findPostById("post-id")).thenReturn(Optional.of(post));
        when(repository.isPostLikedByUser("post-id", "user-id")).thenReturn(false);

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

