package dev.kkoncki.bandup.user.management.instrument.category.forms;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateInstrumentCategoryForm {

    @NotBlank(message = "Instrument category name must not be blank")
    @Length(min = 2, message = "Instrument category name must be at least 2 characters long")
    private String name;
}
