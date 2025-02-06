package dev.kkoncki.bandup.post.management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "comment", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentEntity {

    @Id
    private String id;

    @Column(name = "post_id", nullable = false)
    private String postId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    private String content;

    private Instant timestamp;
}
