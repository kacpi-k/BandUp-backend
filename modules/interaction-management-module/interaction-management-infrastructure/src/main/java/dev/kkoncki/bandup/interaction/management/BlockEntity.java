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
@Table(name = "block")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BlockEntity {

    @Id
    private String id;

    @Column(name = "blocker_id", nullable = false)
    private String blockerId;

    @Column(name = "blocked_id", nullable = false)
    private String blockedId;

    @Column(nullable = false)
    private Instant timestamp;
}
