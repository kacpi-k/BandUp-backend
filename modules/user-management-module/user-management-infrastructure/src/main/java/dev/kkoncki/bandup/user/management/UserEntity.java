package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.user.management.genre.GenreEntity;
import dev.kkoncki.bandup.user.management.instrument.user.instrument.UserInstrumentEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "users", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private Instant createdOn;

    private boolean isBlocked;

    private String bio;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserInstrumentEntity> instruments;

    @ManyToMany
    @JoinTable(
            name = "user_genres",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<GenreEntity> genres;
}
