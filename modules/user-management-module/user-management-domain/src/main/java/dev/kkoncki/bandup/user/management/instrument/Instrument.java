package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Instrument {
    private String id;
    private String name;
    private InstrumentCategory category;
}
