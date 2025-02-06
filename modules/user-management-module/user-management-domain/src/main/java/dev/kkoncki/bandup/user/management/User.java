package dev.kkoncki.bandup.user.management;

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
    private List<String> instruments;
    private String bio;
    private List<String> genres;
    private String imageUrl;
    private Double latitude;
    private Double longitude;
    private String city;
    private String country;
}
