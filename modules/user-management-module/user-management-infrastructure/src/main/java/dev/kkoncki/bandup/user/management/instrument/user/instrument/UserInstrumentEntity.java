package dev.kkoncki.bandup.user.management.instrument.user.instrument;

import dev.kkoncki.bandup.user.management.UserEntity;
import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;
import dev.kkoncki.bandup.user.management.instrument.SkillLevel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_instrument", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserInstrumentEntity {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "instrument_id", nullable = false)
    private InstrumentEntity instrument;

    @Enumerated(EnumType.STRING)
    private SkillLevel skillLevel;
}
