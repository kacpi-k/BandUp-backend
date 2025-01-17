package dev.kkoncki.bandup.file.management.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.file.management.repository.FileManagementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileManagementServiceImpl implements FileManagementService {

    private final FileManagementRepository repository;

    public FileManagementServiceImpl(FileManagementRepository repository) {
        this.repository = repository;
    }

    private File getOrThrowFile(String id) {
        return repository.findById(id).orElseThrow(() -> new ApplicationException(ErrorCode.FILE_NOT_FOUND));
    }

    @Override
    public File get(String id) {
        return getOrThrowFile(id);
    }

    @Override
    public void save(File file) {
        repository.save(file);
    }

    @Override
    public void delete(String id) {
        repository.delete(id);
    }

    @Override
    public List<File> getAllByUserId(String userId) {
        return repository.findAllByUserId(userId);
    }
}
