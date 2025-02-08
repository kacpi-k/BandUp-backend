package dev.kkoncki.bandup.interaction.management.repository;

import dev.kkoncki.bandup.interaction.management.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaBlockRepository extends JpaRepository<BlockEntity, String> {
    boolean existsByBlockerIdAndBlockedId(String blockerId, String blockedId);
    List<BlockEntity> findByBlockerId(String userId);
    void deleteByBlockerIdAndBlockedId(String blockerId, String blockedId);
}
