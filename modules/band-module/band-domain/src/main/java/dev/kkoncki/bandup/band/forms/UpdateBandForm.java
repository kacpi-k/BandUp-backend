package dev.kkoncki.bandup.band.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBandForm {
    @NotBlank(message = "Band ID must not be blank")
    private String id;

    @NotBlank(message = "Name must not be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @NotEmpty(message = "Genres list must not be empty")
    private List<String> genres;
}

