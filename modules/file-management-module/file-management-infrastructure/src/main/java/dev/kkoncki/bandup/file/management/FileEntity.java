package dev.kkoncki.bandup.file.management;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "file", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileEntity {

    @Id
    private String id;

    @Column(name = "user_id")
    private String userId;

    private String extension;

    private long size;

    private String url;

    @Column(name = "uploaded_at")
    private Instant uploadedAt;

}
