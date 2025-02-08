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
public class Friendship {
    private String id;
    private String requesterId;
    private String addresseeId;
    private FriendshipStatus status;
    private Instant timestamp;
}
