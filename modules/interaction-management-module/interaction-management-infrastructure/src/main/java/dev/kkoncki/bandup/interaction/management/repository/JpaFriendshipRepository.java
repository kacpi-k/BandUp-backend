package dev.kkoncki.bandup.interaction.management.repository;

import dev.kkoncki.bandup.interaction.management.FriendshipEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaFriendshipRepository extends JpaRepository<FriendshipEntity, String> {
    Optional<FriendshipEntity> findByRequesterIdAndAddresseeId(String requesterId, String addresseeId);
    List<FriendshipEntity> findByRequesterIdOrAddresseeId(String userId1, String userId2);

    @Query("SELECT COUNT(f) > 0 FROM FriendshipEntity f WHERE (f.requesterId = :user1 AND f.addresseeId = :user2) OR (f.requesterId = :user2 AND f.addresseeId = :user1)")
    boolean existsFriendship(@Param("user1") String user1, @Param("user2") String user2);

    Optional<FriendshipEntity> findByRequesterIdAndAddresseeIdOrAddresseeIdAndRequesterId(String loggedUser, String userId, String loggedUser1, String userId1);
}
