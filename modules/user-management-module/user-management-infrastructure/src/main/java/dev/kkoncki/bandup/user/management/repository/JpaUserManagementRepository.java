package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.user.management.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface JpaUserManagementRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.bio = :bio WHERE u.id = :userId")
    void updateBio(String userId, String bio);

    @Modifying
    @Transactional
    @Query("UPDATE UserEntity u SET u.imageUrl = :imageUrl WHERE u.id = :userId")
    void updateImageUrl(String userId, String imageUrl);
}
