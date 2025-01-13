package dev.kkoncki.bandup.user.management.instrument.category;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instrument_categories", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentCategoryEntity {

    @Id
    private String id;

    private String name;
}
