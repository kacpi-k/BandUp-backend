package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.PrivateChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPrivateChatRepository extends JpaRepository<PrivateChatMessageEntity, String> {
    @Query("SELECT p FROM PrivateChatMessageEntity p WHERE " +
            "(p.senderId = :senderId AND p.receiverId = :receiverId) " +
            "OR (p.senderId = :receiverId AND p.receiverId = :senderId) " +
            "ORDER BY p.timestamp DESC")
    Page<PrivateChatMessageEntity> findPrivateMessages(String senderId, String receiverId, Pageable pageable);

    List<PrivateChatMessageEntity> findAllBySenderIdOrReceiverId(String userId, String userId1);
}
