package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.GroupChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaGroupChatRepository extends JpaRepository<GroupChatMessageEntity, String> {
    @Query("SELECT g FROM GroupChatMessageEntity g WHERE g.bandId = :bandId ORDER BY g.timestamp ASC")
    List<GroupChatMessageEntity> findGroupMessages(String bandId);
}
