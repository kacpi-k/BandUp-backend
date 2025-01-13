package dev.kkoncki.bandup.user.management.instrument.user.instrument.forms;

import dev.kkoncki.bandup.user.management.instrument.SkillLevel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInstrumentForm {

    @NotNull
    @Size(min = 36, max = 36, message = "ID must have 36 characters.")
    private String id;

    @NotNull
    @Size(min = 36, max = 36, message = "ID must have 36 characters.")
    private String instrumentId;

    @NotNull(message = "Skill level cannot be null.")
    private SkillLevel skillLevel;
}
