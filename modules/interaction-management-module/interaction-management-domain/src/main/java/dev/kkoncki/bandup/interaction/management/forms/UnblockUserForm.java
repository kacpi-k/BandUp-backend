package dev.kkoncki.bandup.interaction.management.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnblockUserForm {
    @NotBlank
    @Size(min = 36, max = 36)
    private String blockId;
}
