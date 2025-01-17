package dev.kkoncki.bandup.file.management.repository;

import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.file.management.FileEntity;
import dev.kkoncki.bandup.file.management.FileMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FileManagementRepositoryAdapter implements FileManagementRepository {

    private final JpaFileManagementRepository jpaFileManagementRepository;

    public FileManagementRepositoryAdapter(JpaFileManagementRepository jpaFileManagementRepository) {
        this.jpaFileManagementRepository = jpaFileManagementRepository;
    }

    @Override
    public Optional<File> findById(String id) {
        Optional<FileEntity> entity = jpaFileManagementRepository.findById(id);
        return entity.map(FileMapper::toDomain);
    }

    @Override
    public void save(File file) {
        FileEntity entity = FileMapper.toEntity(file);
        jpaFileManagementRepository.save(entity);
    }

    @Override
    public void delete(String id) {
        jpaFileManagementRepository.deleteById(id);
    }

    @Override
    public List<File> findAllByUserId(String userId) {
        return jpaFileManagementRepository.findAllByUserId(userId).stream()
                .map(FileMapper::toDomain)
                .toList();
    }
}
