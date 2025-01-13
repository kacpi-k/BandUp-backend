package dev.kkoncki.bandup.user.management.instrument.forms;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInstrumentForm {

    @NotBlank
    private String name;

    @NotNull(message = "ID cannot be null.")
    @Size(min = 36, max = 36, message = "ID must have 36 characters.")
    private String categoryId;
}
