package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.post.management.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaCommentRepository extends JpaRepository<CommentEntity, String> {
    List<CommentEntity> findAllByPostId(String postId);

    @Modifying
    @Query("DELETE FROM CommentEntity c WHERE c.postId = :postId")
    void deleteCommentsByPostId(@Param("postId") String postId);
}
