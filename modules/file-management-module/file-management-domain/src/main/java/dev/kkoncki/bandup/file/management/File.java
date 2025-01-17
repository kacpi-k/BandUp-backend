package dev.kkoncki.bandup.file.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class File {
    private String id;
    private String userId;
    private String extension;
    private long size;
    private String url;
    private Instant uploadedAt;
}
