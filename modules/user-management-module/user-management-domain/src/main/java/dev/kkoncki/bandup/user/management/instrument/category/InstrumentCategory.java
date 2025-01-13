package dev.kkoncki.bandup.user.management.instrument.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentCategory {
    private String id;
    private String name;
}
