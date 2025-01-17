package dev.kkoncki.bandup.file.management.repository;

import dev.kkoncki.bandup.file.management.File;

import java.util.List;
import java.util.Optional;

public interface FileManagementRepository {
    Optional<File> findById(String id);
    void save(File file);
    void delete(String id);
    List<File> findAllByUserId(String userId);
}
