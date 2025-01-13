package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.user.management.instrument.SkillLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInstrument {
    private String id;
    private String userId;
    private String instrumentId;
    private SkillLevel skillLevel;
}
