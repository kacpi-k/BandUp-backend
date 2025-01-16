package dev.kkoncki.bandup.file.management;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.firebase.FirebaseProps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Slf4j
@Service
public class FirebaseFileStorageService implements FileStorageService {

    private final Storage storage;
    private final FirebaseProps firebaseProps;

    public FirebaseFileStorageService(Storage storage, FirebaseProps firebaseProps) {
        this.storage = storage;
        this.firebaseProps = firebaseProps;
    }

    private String generateUniqueFileName(String userId, String originalFileName) {
        String extension = getFileExtension(originalFileName);
        String uniqueId = UUID.randomUUID().toString();

        return uniqueId + (extension.isEmpty() ? "" : "." + extension);
    }

    private String getFileExtension(String fileName) {
        int lastDodIndex = fileName.lastIndexOf(".");
        if (lastDodIndex > 0 && lastDodIndex < fileName.length() - 1) {
            return fileName.substring(lastDodIndex + 1);
        }
        return "";
    }

    @Override
    public String uploadFile(byte[] file, String userId, String originalFileName, String contentType) {
        try {
            String uniqueFileName = generateUniqueFileName(userId, originalFileName);

            log.info("Uploading file to bucket: {}", firebaseProps.getBucket());
            log.info("Generated file name: {}", uniqueFileName);
            log.info("Content type: {}", contentType);



            Bucket bucket = storage.get(firebaseProps.getBucket());
            if (bucket == null) {
                log.info("Bucket problem");
                throw new ApplicationException(ErrorCode.TEST);
            }
            log.info("Bucket: {}", bucket);
            Blob blob = bucket.create(uniqueFileName, file, contentType);

            return blob.getMediaLink();
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
