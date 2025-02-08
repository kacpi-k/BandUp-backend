package dev.kkoncki.bandup.user.management.repository;

import dev.kkoncki.bandup.user.management.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserManagementRepository extends JpaRepository<UserEntity, String>, JpaSpecificationExecutor<UserEntity> {
}
