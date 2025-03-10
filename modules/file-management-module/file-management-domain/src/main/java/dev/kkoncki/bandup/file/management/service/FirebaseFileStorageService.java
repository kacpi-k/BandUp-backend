package dev.kkoncki.bandup.file.management.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.file.management.File;
import dev.kkoncki.bandup.firebase.FirebaseProps;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class FirebaseFileStorageService implements FileStorageService {

    private final Storage storage;
    private final FirebaseProps firebaseProps;
    private final FileManagementService fileManagementService;

    public FirebaseFileStorageService(Storage storage, FirebaseProps firebaseProps, FileManagementService fileManagementService) {
        this.storage = storage;
        this.firebaseProps = firebaseProps;
        this.fileManagementService = fileManagementService;
    }

    private String generateUniqueFileName(String originalFileName, String fileId) {
        String extension = getFileExtension(originalFileName);

        return fileId + (extension.isEmpty() ? "" : "." + extension);
    }

    private String generateFileUUID() {
        return UUID.randomUUID().toString();
    }

    private String getFileExtension(String fileName) {
        int lastDodIndex = fileName.lastIndexOf(".");
        if (lastDodIndex > 0 && lastDodIndex < fileName.length() - 1) {
            return fileName.substring(lastDodIndex + 1);
        }
        return fileName;
    }

    private String removeFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < fileName.length() - 1) {
            return fileName.substring(0, lastDotIndex);
        }
        return fileName;
    }

    @Override
    public String uploadFile(byte[] file, String userId, String originalFileName, String contentType) {
        try {
            String fileId = generateFileUUID();

            String uniqueFileName = generateUniqueFileName(originalFileName, fileId);

            Bucket bucket = storage.get(firebaseProps.getBucket());
            if (bucket == null) {
                throw new ApplicationException(ErrorCode.BUCKET_NOT_FOUND);
            }

            bucket.create(uniqueFileName, file, contentType);

            String mediaUrl = generateFileUrl(uniqueFileName);

            File fileToDB = File.builder()
                    .id(fileId)
                    .userId(userId)
                    .extension(getFileExtension(originalFileName))
                    .size(file.length)
                    .url(mediaUrl)
                    .uploadedAt(Instant.now())
                    .build();

            fileManagementService.save(fileToDB);

            return mediaUrl;
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_TO_UPLOAD_FILE);
        }
    }


    @Override
    public byte[] downloadFile(String fileName) {
        try {
            Bucket bucket = storage.get(firebaseProps.getBucket());
            Blob blob = bucket.get(fileName);

            if (blob == null || !blob.exists()) {
                throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
            }

            return blob.getContent();
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_TO_DOWNLOAD_FILE);
        }
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            Bucket bucket = storage.get(firebaseProps.getBucket());
            Blob blob = bucket.get(fileName);

            if (blob == null  || !blob.exists()) {
                throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
            }

            blob.delete();

            String fileId = removeFileExtension(fileName);
            fileManagementService.delete(fileId);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_TO_DELETE_FILE);
        }
    }

    @Override
    public String generateFileUrl(String fileName) {
        try {
            Bucket bucket = storage.get(firebaseProps.getBucket());
            Blob blob = bucket.get(fileName);

            if (blob == null || !blob.exists()) {
                throw new ApplicationException(ErrorCode.FILE_NOT_FOUND);
            }

            return String.format("https://storage.googleapis.com/%s/%s", firebaseProps.getBucket(), fileName);
        } catch (Exception e) {
            throw new ApplicationException(ErrorCode.FAILED_TO_GENERATE_FILE_URL);
        }
    }
}
