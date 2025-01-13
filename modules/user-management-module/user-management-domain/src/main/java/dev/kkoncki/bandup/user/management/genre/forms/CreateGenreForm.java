package dev.kkoncki.bandup.user.management.genre.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGenreForm {

    @NotBlank(message = "Genre name must not be blank")
    @Length(min = 2, message = "Genre name must be at least 2 characters long")
    private String name;
}
