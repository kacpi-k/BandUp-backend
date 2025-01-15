package dev.kkoncki.bandup.commons.genre.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGenreForm {

    @NotBlank(message = "Genre name must not be blank")
    @Length(min = 2, message = "Genre name must be at least 2 characters long")
    private String name;
}
