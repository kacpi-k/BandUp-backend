package dev.kkoncki.bandup.user.management.forms;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserLocationForm {

    @NotNull(message = "Latitude must not be null")
    private Double latitude;

    @NotNull(message = "Longitude must not be null")
    private Double longitude;

    private String city;

    private String country;
}
