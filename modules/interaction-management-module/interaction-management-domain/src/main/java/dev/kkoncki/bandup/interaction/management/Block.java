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
public class Block {
    private String id;
    private String blockerId;
    private String blockedId;
    private Instant timestamp;
}
