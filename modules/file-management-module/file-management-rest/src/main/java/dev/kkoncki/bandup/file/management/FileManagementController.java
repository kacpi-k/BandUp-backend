package dev.kkoncki.bandup.file.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import dev.kkoncki.bandup.file.management.service.FileManagementService;
import dev.kkoncki.bandup.file.management.service.FileStorageService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/file")
public class FileManagementController {

    private final FileStorageService fileStorageService;
    private final FileManagementService fileManagementService;
    private final LoggedUser loggedUser;

    public FileManagementController(FileStorageService fileStorageService, FileManagementService fileManagementService, LoggedUser loggedUser) {
        this.fileStorageService = fileStorageService;
        this.fileManagementService = fileManagementService;
        this.loggedUser = loggedUser;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.uploadFile(file.getBytes(), loggedUser.getUserId(), file.getOriginalFilename(), file.getContentType());
    }

    @DeleteMapping("/delete/{fileName}")
    public void deleteFile(@PathVariable String fileName) {
        fileStorageService.deleteFile(fileName);
    }

    @GetMapping("/download/{fileName}")
    public byte[] downloadFile(@PathVariable String fileName) {
        return fileStorageService.downloadFile(fileName);
    }

    @GetMapping("/url/{fileName}")
    public String generateFileUrl(@PathVariable String fileName) {
        return fileStorageService.generateFileUrl(fileName);
    }

    @GetMapping("/{id}")
    public File get(@PathVariable String id) {
        return fileManagementService.get(id);
    }

    @GetMapping("all-by-user")
    public List<File> getAllByUserId() {
        return fileManagementService.getAllByUserId(loggedUser.getUserId());
    }
}
