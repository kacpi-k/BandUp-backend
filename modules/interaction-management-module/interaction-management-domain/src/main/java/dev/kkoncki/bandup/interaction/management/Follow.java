package dev.kkoncki.bandup.interaction.management;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Follow {
    private String id;
    private String followerId;
    private String followedId;
    private Instant timestamp;
}
