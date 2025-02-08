package dev.kkoncki.bandup.interaction.management;

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
@Table(name = "follow")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FollowEntity {

    @Id
    private String id;

    @Column(name = "follower_id", nullable = false)
    private String followerId;

    @Column(name = "followed_id", nullable = false)
    private String followedId;

    @Column(nullable = false)
    private Instant timestamp;
}
