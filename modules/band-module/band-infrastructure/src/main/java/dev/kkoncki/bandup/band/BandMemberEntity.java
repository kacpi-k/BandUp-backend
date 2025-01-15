package dev.kkoncki.bandup.band;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "band_member", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BandMemberEntity {

    @Id
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "band_id", nullable = false)
    private BandEntity band;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(nullable = false)
    private String role;

    @Column(name = "joined_on", nullable = false, updatable = false)
    private Instant joinedOn;
}
