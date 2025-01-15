package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.commons.genre.GenreEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "band", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BandEntity {
    @Id
    private String id;

    private String name;

    private String description;

    @Column(name = "created_on", nullable = false, updatable = false)
    private Instant createdOn;

    @OneToMany(mappedBy = "band", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BandMemberEntity> members = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "band_genres",
            joinColumns = @JoinColumn(name = "band_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres = new ArrayList<>();
}
