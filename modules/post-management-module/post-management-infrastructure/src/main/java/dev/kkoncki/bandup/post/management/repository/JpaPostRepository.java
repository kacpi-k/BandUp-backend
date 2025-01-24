package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.post.management.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPostRepository extends JpaRepository<PostEntity, String> {
    List<PostEntity> findAllByUserId(String userId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount + :likesDelta, p.commentsCount = p.commentsCount + :commentsDelta WHERE p.id = :postId")
    void updatePostInteractions(@Param("postId") String postId, @Param("likesDelta") int likesDelta, @Param("commentsDelta") int commentsDelta);

    @Query("SELECT COUNT(pl) > 0 FROM PostLikeEntity pl WHERE pl.postId = :postId AND pl.userId = :userId")
    boolean isPostLikedByUser(@Param("postId") String postId, @Param("userId") String userId);
}
