package dev.kkoncki.bandup.post.management.repository;

import dev.kkoncki.bandup.interaction.management.FriendshipStatus;
import dev.kkoncki.bandup.post.management.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaPostRepository extends JpaRepository<PostEntity, String> {
    List<PostEntity> findAllByUserId(String userId);

    @Modifying
    @Query("UPDATE PostEntity p SET p.likesCount = p.likesCount + :likesDelta, p.commentsCount = p.commentsCount + :commentsDelta WHERE p.id = :postId")
    int updatePostInteractions(@Param("postId") String postId, @Param("likesDelta") int likesDelta, @Param("commentsDelta") int commentsDelta);

    @Query("SELECT COUNT(pl) > 0 FROM PostLikeEntity pl WHERE pl.postId = :postId AND pl.userId = :userId")
    boolean isPostLikedByUser(@Param("postId") String postId, @Param("userId") String userId);

    @Query("""
    SELECT p FROM PostEntity p
    WHERE p.userId IN (
        SELECT CASE
            WHEN f.requesterId = :userId THEN f.addresseeId
            ELSE f.requesterId
        END
        FROM FriendshipEntity f
        WHERE (f.requesterId = :userId OR f.addresseeId = :userId)
        AND f.status = :status
    )
    ORDER BY p.timestamp DESC
    """)
    List<PostEntity> findPostsByFriendsAndStatus(@Param("userId") String userId, @Param("status") FriendshipStatus status);
}
