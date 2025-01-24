package dev.kkoncki.bandup.post.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Post {
    private String id;
    private String userId;
    private String content;
    private String mediaUrl;
    private MediaType mediaType;
    private Instant timestamp;
    private int likesCount;
    private int commentsCount;
}
