package dev.kkoncki.bandup.file.management;

public interface FileStorageService {
    String uploadFile(byte[] file, String userId, String originalFileName, String contentType);
    byte[] downloadFile(String fileName);
    void deleteFile(String fileName);
    String generateFileUrl(String fileName);
}
