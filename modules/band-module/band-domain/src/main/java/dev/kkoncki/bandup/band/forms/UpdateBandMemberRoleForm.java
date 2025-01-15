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
public class UpdateBandMemberRoleForm {
    @NotBlank(message = "Member ID must not be blank")
    @Size(min = 36, max = 36, message = "Member ID must have 36 characters.")
    private String memberId;

    @NotBlank(message = "New role must not be blank")
    @Pattern(regexp = "leader|member|guest", message = "Role must be 'leader', 'member', or 'guest'")
    private String newRole;
}
