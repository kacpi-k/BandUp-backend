package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.post.management.PostLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPostLikeRepository extends JpaRepository<PostLikeEntity, String> {
    @Modifying
    @Query("DELETE FROM PostLikeEntity pl WHERE pl.postId = :postId AND pl.userId = :userId")
    void deleteByPostIdAndUserId(@Param("postId") String postId, @Param("userId") String userId);
}
