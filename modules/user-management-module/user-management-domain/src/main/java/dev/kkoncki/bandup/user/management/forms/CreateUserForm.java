package dev.kkoncki.bandup.user.management.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserForm {

    @NotBlank(message = "First name must not be blank")
    @Length(min = 2, message = "First name must be at least 2 characters long")
    private String firstName;

    @NotBlank(message = "Last name must not be blank")
    @Length(min = 2, message = "Last name must be at least 2 characters long")
    private String lastName;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email is invalid")
    private String email;
}
