package dev.kkoncki.bandup.chat.repository;

import dev.kkoncki.bandup.chat.GroupChatMessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaGroupChatRepository extends JpaRepository<GroupChatMessageEntity, String> {
    @Query("SELECT g FROM GroupChatMessageEntity g WHERE " +
            "g.bandId = :bandId " +
            "ORDER BY g.timestamp DESC")
    Page<GroupChatMessageEntity> findGroupMessages(@Param("bandId") String bandId, Pageable pageable);
    @Query("SELECT g FROM GroupChatMessageEntity g WHERE g.bandId IN :bandIds ORDER BY g.timestamp DESC")
    List<GroupChatMessageEntity> findAllByBandIdIn(@Param("bandIds") List<String> bandIds);
}
