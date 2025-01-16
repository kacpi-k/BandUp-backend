package dev.kkoncki.bandup.file.management;

import dev.kkoncki.bandup.commons.LoggedUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/file")
public class FileController {

    private final FileStorageService fileStorageService;
    private final LoggedUser loggedUser;

    public FileController(FileStorageService fileStorageService, LoggedUser loggedUser) {
        this.fileStorageService = fileStorageService;
        this.loggedUser = loggedUser;
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        return fileStorageService.uploadFile(file.getBytes(), loggedUser.getUserId(), file.getOriginalFilename(), file.getContentType());
    }
}
