package dev.kkoncki.bandup.band;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BandMember {
    private String id;
    private String bandId;
    private String userId;
    private String role;
    private Instant joinedOn;

    public boolean isLeader() {
        return "leader".equalsIgnoreCase(this.role);
    }
}
