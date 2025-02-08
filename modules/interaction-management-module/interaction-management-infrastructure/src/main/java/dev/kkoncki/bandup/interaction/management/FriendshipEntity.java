package dev.kkoncki.bandup.interaction.management;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "friendship")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FriendshipEntity {

    @Id
    private String id;

    @Column(name = "requester_id", nullable = false)
    private String requesterId;

    @Column(name = "addressee_id", nullable = false)
    private String addresseeId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    @Column(nullable = false)
    private Instant timestamp;
}
