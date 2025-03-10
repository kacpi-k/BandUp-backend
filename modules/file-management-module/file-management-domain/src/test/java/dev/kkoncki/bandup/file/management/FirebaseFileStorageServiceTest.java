package dev.kkoncki.bandup.file.management;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.file.management.service.FileManagementService;
import dev.kkoncki.bandup.file.management.service.FirebaseFileStorageService;
import dev.kkoncki.bandup.firebase.FirebaseProps;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FirebaseFileStorageServiceTest {

    @Mock
    private Storage storage;

    @Mock
    private Bucket bucket;

    @Mock
    private Blob blob;

    @Mock
    private FirebaseProps firebaseProps;

    @Mock
    private FileManagementService fileManagementService;

    @InjectMocks
    private FirebaseFileStorageService fileStorageService;

    @BeforeEach
    void setUp() {
        when(firebaseProps.getBucket()).thenReturn("test-bucket");
        when(storage.get("test-bucket")).thenReturn(bucket);
    }

    @Test
    void shouldUploadFileSuccessfully() {
        byte[] file = "file-content".getBytes();
        String userId = "user-id";
        String originalFileName = "test.txt";
        String contentType = "text/plain";

        when(bucket.create(anyString(), eq(file), eq(contentType))).thenReturn(blob);
        when(bucket.get(anyString())).thenReturn(blob);
        when(blob.exists()).thenReturn(true);

        ArgumentCaptor<String> fileNameCaptor = ArgumentCaptor.forClass(String.class);

        String mediaLink = fileStorageService.uploadFile(file, userId, originalFileName, contentType);

        verify(bucket).create(fileNameCaptor.capture(), eq(file), eq(contentType));
        String generatedFileName = fileNameCaptor.getValue();

        String expectedFileUrl = "https://storage.googleapis.com/" + "test-bucket/" + generatedFileName;

        assertNotNull(mediaLink);
        assertEquals(expectedFileUrl, mediaLink);
        verify(bucket, times(1)).create(anyString(), eq(file), eq(contentType));
        verify(fileManagementService, times(1)).save(any(File.class));
    }

    @Test
    void shouldThrowExceptionWhenUploadFails() {
        byte[] file = "file-content".getBytes();
        String userId = "user-id";
        String originalFileName = "test.txt";
        String contentType = "text/plain";

        when(bucket.create(anyString(), eq(file), eq(contentType))).thenThrow(new RuntimeException());

        assertThrows(ApplicationException.class, () ->
                fileStorageService.uploadFile(file, userId, originalFileName, contentType));

        verify(fileManagementService, never()).save(any(File.class));
    }

    @Test
    void shouldDownloadFileSuccessfully() {
        String fileName = "test.txt";
        byte[] fileContent = "file-content".getBytes();

        when(bucket.get(fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);
        when(blob.getContent()).thenReturn(fileContent);

        byte[] result = fileStorageService.downloadFile(fileName);

        assertArrayEquals(fileContent, result);
        verify(bucket, times(1)).get(fileName);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFoundDuringDownload() {
        String fileName = "test.txt";

        when(bucket.get(fileName)).thenReturn(null);

        assertThrows(ApplicationException.class, () ->
                fileStorageService.downloadFile(fileName));
    }

    @Test
    void shouldDeleteFileSuccessfully() {
        String fileName = "test.txt";

        when(bucket.get(fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);

        fileStorageService.deleteFile(fileName);

        verify(blob, times(1)).delete();
        verify(fileManagementService, times(1)).delete("test");
    }

    @Test
    void shouldThrowExceptionWhenFileNotFoundDuringDelete() {
        String fileName = "test.txt";

        when(bucket.get(fileName)).thenReturn(null);

        assertThrows(ApplicationException.class, () ->
                fileStorageService.deleteFile(fileName));

        verify(fileManagementService, never()).delete(anyString());
    }

    @Test
    void shouldGenerateFileUrlSuccessfully() {
        String fileName = "test.txt";

        when(bucket.get(fileName)).thenReturn(blob);
        when(blob.exists()).thenReturn(true);

        String url = fileStorageService.generateFileUrl(fileName);

        assertEquals("https://storage.googleapis.com/test-bucket/test.txt", url);
        verify(bucket, times(1)).get(fileName);
    }

    @Test
    void shouldThrowExceptionWhenFileNotFoundDuringGenerateFileUrl() {
        String fileName = "test.txt";

        when(bucket.get(fileName)).thenReturn(null);

        assertThrows(ApplicationException.class, () ->
                fileStorageService.generateFileUrl(fileName));
    }
}