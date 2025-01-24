package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.post.management.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PostManagementRepositoryAdapter implements PostManagementRepository {

    private final JpaPostRepository jpaPostRepository;
    private final JpaCommentRepository jpaCommentRepository;
    private final JpaPostLikeRepository jpaPostLikeRepository;

    public PostManagementRepositoryAdapter(JpaPostRepository jpaPostRepository, JpaCommentRepository jpaCommentRepository, JpaPostLikeRepository jpaPostLikeRepository) {
        this.jpaPostRepository = jpaPostRepository;
        this.jpaCommentRepository = jpaCommentRepository;
        this.jpaPostLikeRepository = jpaPostLikeRepository;
    }


    @Override
    public void savePost(Post post) {
        jpaPostRepository.save(PostMapper.toEntity(post));
    }

    @Override
    public void deletePost(String postId) {
        jpaPostRepository.deleteById(postId);
    }

    @Override
    public Optional<Post> findPostById(String postId) {
        return jpaPostRepository.findById(postId).map(PostMapper::toDomain);
    }

    @Override
    public List<Post> findPostsByUser(String userId) {
        return jpaPostRepository.findAllByUserId(userId).stream()
                .map(PostMapper::toDomain)
                .toList();
    }

    @Override
    public void saveComment(Comment comment) {
        jpaCommentRepository.save(CommentMapper.toEntity(comment));
    }

    @Override
    public void deleteComment(String commentId) {
        jpaCommentRepository.deleteById(commentId);
    }

    @Override
    public void deleteCommentsByPost(String postId) {
        jpaCommentRepository.deleteCommentsByPostId(postId);
    }

    @Override
    public Optional<Comment> findCommentById(String commentId) {
        return jpaCommentRepository.findById(commentId).map(CommentMapper::toDomain);
    }

    @Override
    public List<Comment> findCommentsByPost(String postId) {
        return jpaCommentRepository.findAllByPostId(postId).stream()
                .map(CommentMapper::toDomain)
                .toList();
    }

    @Override
    public void updatePostInteractions(String postId, int likesDelta, int commentsDelta) {
        jpaPostRepository.updatePostInteractions(postId, likesDelta, commentsDelta);
    }

    @Override
    public boolean isPostLikedByUser(String postId, String userId) {
        return jpaPostRepository.isPostLikedByUser(postId, userId);
    }

    @Override
    public void savePostLike(String postId, String userId) {
        jpaPostLikeRepository.save(PostLikeEntity.builder()
                .postId(postId)
                .userId(userId)
                .build());
    }

    @Override
    public void deletePostLike(String postId, String userId) {
        jpaPostLikeRepository.deleteByPostIdAndUserId(postId, userId);
    }
}
