package dev.kkoncki.bandup.post.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.file.management.service.FileManagementService;
import dev.kkoncki.bandup.post.management.Comment;
import dev.kkoncki.bandup.post.management.MediaType;
import dev.kkoncki.bandup.post.management.Post;
import dev.kkoncki.bandup.post.management.forms.AddCommentForm;
import dev.kkoncki.bandup.post.management.forms.CreatePostForm;
import dev.kkoncki.bandup.post.management.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImpl implements PostService {

    private final PostRepository repository;
    private final FileManagementService fileManagementService;

    public PostServiceImpl(PostRepository repository, FileManagementService fileManagementService) {
        this.repository = repository;
        this.fileManagementService = fileManagementService;
    }

    private String extractFileIdFromUrl(String mediaUrl) {
        String cleanedUrl = mediaUrl.split("\\?")[0];
        String fileName = cleanedUrl.substring(cleanedUrl.lastIndexOf("/") + 1);
        return fileName.contains(".") ? fileName.substring(0, fileName.lastIndexOf(".")) : fileName;
    }

    private MediaType determineMediaType(String extension) {
        return switch (extension) {
            case "jpg", "jpeg", "png", "gif" -> MediaType.IMAGE;
            case "mp4", "mov", "avi" -> MediaType.VIDEO;
            case "mp3", "wav", "flac" -> MediaType.AUDIO;
            default -> throw new ApplicationException(ErrorCode.INVALID_MEDIA_TYPE);
        };
    }

    private Post getOrThrowPost(String postId) {
        return repository.findPostById(postId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.POST_NOT_FOUND));
    }

    private Comment getOrThrowComment(String commentId) {
        return repository.findCommentById(commentId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.COMMENT_NOT_FOUND));
    }

    @Override
    public Post createPost(CreatePostForm form) {
        MediaType mediaType = null;

        if (form.getMediaUrl() != null) {
            String fileId = extractFileIdFromUrl(form.getMediaUrl());
            File file = fileManagementService.get(fileId);
            mediaType = determineMediaType(file.getExtension());
        }

        Post post = Post.builder()
                .id(UUID.randomUUID().toString())
                .userId(form.getUserId())
                .content(form.getContent())
                .mediaUrl(form.getMediaUrl())
                .mediaType(mediaType)
                .timestamp(Instant.now())
                .likesCount(0)
                .commentsCount(0)
                .build();

        repository.savePost(post);

        return post;
    }

    @Override
    public void deletePost(String postId, String userId) {
        Post post = getPostById(postId);
        if (!post.getUserId().equals(userId)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        repository.deletePost(postId);
        repository.deleteCommentsByPost(postId);
    }

    @Override
    public List<Post> getPostsByUserId(String userId) {
        return repository.findPostsByUser(userId);
    }

    @Override
    public Post getPostById(String postId) {
        return getOrThrowPost(postId);
    }

    @Override
    public Comment addComment(AddCommentForm form) {
        Post post = getPostById(form.getPostId());

        Comment comment = Comment.builder()
                .id(UUID.randomUUID().toString())
                .postId(form.getPostId())
                .userId(form.getUserId())
                .content(form.getContent())
                .timestamp(Instant.now())
                .build();

        repository.saveComment(comment);

        post.setCommentsCount(post.getCommentsCount() + 1);
        repository.savePost(post);

        return comment;
    }

    @Override
    public void deleteComment(String commentId, String userId) {
        Comment comment = getCommentById(commentId);
        if (!comment.getUserId().equals(userId)) {
            throw new ApplicationException(ErrorCode.UNAUTHORIZED);
        }

        repository.deleteComment(commentId);
        repository.updatePostInteractions(comment.getPostId(), 0, -1);
    }

    @Override
    public List<Comment> getCommentsByPost(String postId) {
        return repository.findCommentsByPost(postId);
    }

    @Override
    public Comment getCommentById(String commentId) {
        return getOrThrowComment(commentId);
    }

    @Override
    public void likePost(String postId, String userId) {
        Post post = getPostById(postId);
        if (repository.isPostLikedByUser(postId, userId)) {
            throw new ApplicationException(ErrorCode.POST_ALREADY_LIKED);
        }

        repository.savePostLike(postId, userId);
        repository.updatePostInteractions(postId, 1, 0);
    }

    @Override
    public void unlikePost(String postId, String userId) {
        Post post = getPostById(postId);
        if (!repository.isPostLikedByUser(postId, userId)) {
            throw new ApplicationException(ErrorCode.POST_NOT_LIKED);
        }

        repository.deletePostLike(postId);
        repository.updatePostInteractions(postId, -1, 0);
    }
}
