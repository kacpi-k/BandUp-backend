package dev.kkoncki.bandup.post.management;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "post", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostEntity {

    @Id
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String content;

    @Column(name = "media_url")
    private String mediaUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type")
    private MediaType mediaType;

    private Instant timestamp;

    @Column(name = "likes_count")
    private int likesCount;

    @Column(name = "comments_count")
    private int commentsCount;
}
