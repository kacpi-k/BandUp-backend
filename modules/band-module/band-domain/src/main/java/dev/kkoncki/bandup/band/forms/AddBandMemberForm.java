package dev.kkoncki.bandup.band.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBandMemberForm {
    @NotBlank(message = "Band ID must not be blank")
    @Size(min = 36, max = 36, message = "Band ID must have 36 characters.")
    private String bandId;

    @NotBlank(message = "User ID must not be blank")
    @Size(min = 36, max = 36, message = "User ID must have 36 characters.")
    private String userId;

    @NotBlank(message = "Role must not be blank")
    @Pattern(regexp = "leader|member|guest", message = "Role must be 'leader', 'member', or 'guest'")
    private String role;
}

