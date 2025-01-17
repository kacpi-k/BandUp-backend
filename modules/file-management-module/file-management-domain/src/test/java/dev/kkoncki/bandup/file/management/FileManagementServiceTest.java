package dev.kkoncki.bandup.file.management;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.file.management.repository.FileManagementRepository;
import dev.kkoncki.bandup.file.management.service.FileManagementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileManagementServiceImplTest {

    @Mock
    private FileManagementRepository repository;

    @InjectMocks
    private FileManagementServiceImpl fileManagementService;

    @Test
    void shouldReturnFileWhenExists() {
        String fileId = "file-id";
        File file = File.builder()
                .id(fileId)
                .userId("user-id")
                .extension("txt")
                .size(1024L)
                .url("https://example.com/file-id.txt")
                .uploadedAt(Instant.now())
                .build();

        when(repository.findById(fileId)).thenReturn(Optional.of(file));

        File result = fileManagementService.get(fileId);

        assertNotNull(result);
        assertEquals(fileId, result.getId());
        verify(repository, times(1)).findById(fileId);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFound() {
        String fileId = "non-existent-id";

        when(repository.findById(fileId)).thenReturn(Optional.empty());

        assertThrows(ApplicationException.class, () -> fileManagementService.get(fileId));
        verify(repository, times(1)).findById(fileId);
    }

    @Test
    void shouldSaveFileSuccessfully() {
        File file = File.builder()
                .id("file-id")
                .userId("user-id")
                .extension("txt")
                .size(1024L)
                .url("https://example.com/file-id.txt")
                .uploadedAt(Instant.now())
                .build();

        fileManagementService.save(file);

        verify(repository, times(1)).save(file);
    }

    @Test
    void shouldDeleteFileSuccessfully() {
        String fileId = "file-id";

        fileManagementService.delete(fileId);

        verify(repository, times(1)).delete(fileId);
    }

    @Test
    void shouldReturnFilesForUser() {
        String userId = "user-id";
        List<File> files = List.of(
                File.builder()
                        .id("file-1")
                        .userId(userId)
                        .extension("txt")
                        .size(1024L)
                        .url("https://example.com/file-1.txt")
                        .uploadedAt(Instant.now())
                        .build(),
                File.builder()
                        .id("file-2")
                        .userId(userId)
                        .extension("jpg")
                        .size(2048L)
                        .url("https://example.com/file-2.jpg")
                        .uploadedAt(Instant.now())
                        .build()
        );

        when(repository.findAllByUserId(userId)).thenReturn(files);

        List<File> result = fileManagementService.getAllByUserId(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAllByUserId(userId);
    }
}