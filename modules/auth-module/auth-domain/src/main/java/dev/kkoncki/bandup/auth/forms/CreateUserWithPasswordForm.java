package dev.kkoncki.bandup.auth.forms;

import dev.kkoncki.bandup.user.management.forms.CreateUserForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserWithPasswordForm extends CreateUserForm {

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
