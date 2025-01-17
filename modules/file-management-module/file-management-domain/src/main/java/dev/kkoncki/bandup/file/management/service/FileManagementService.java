package dev.kkoncki.bandup.file.management.service;

import dev.kkoncki.bandup.file.management.File;

import java.util.List;

public interface FileManagementService {
    File get(String id);
    void save(File file);
    void delete(String id);
    List<File> getAllByUserId(String userId);
}
