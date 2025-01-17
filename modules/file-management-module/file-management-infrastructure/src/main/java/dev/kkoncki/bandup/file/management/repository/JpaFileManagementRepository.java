package dev.kkoncki.bandup.file.management.repository;

import dev.kkoncki.bandup.file.management.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaFileManagementRepository extends JpaRepository<FileEntity, String> {
    List<FileEntity> findAllByUserId(String userId);
}
