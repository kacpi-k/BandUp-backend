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
public class Comment {
    private String id;
    private String postId;
    private String userId;
    private String content;
    private Instant timestamp;
}
