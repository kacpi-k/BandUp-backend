package dev.kkoncki.bandup.user.management;

import dev.kkoncki.bandup.user.management.genre.Genre;
import dev.kkoncki.bandup.user.management.instrument.Instrument;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Instant createdOn;
    private boolean isBlocked;
    private Instrument instrument;
    private SkillLevel skillLevel;
    private String bio;
    private List<Genre> genres;
}
