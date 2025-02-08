package dev.kkoncki.bandup.interaction.management.repository;

import dev.kkoncki.bandup.interaction.management.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFollowRepository extends JpaRepository<FollowEntity, String> {
    boolean existsByFollowerIdAndFollowedId(String followerId, String followedId);
    List<FollowEntity> findByFollowerId(String userId);
    List<FollowEntity> findByFollowedId(String userId);
    void deleteByFollowerIdAndFollowedId(String followerId, String followedId);
}
