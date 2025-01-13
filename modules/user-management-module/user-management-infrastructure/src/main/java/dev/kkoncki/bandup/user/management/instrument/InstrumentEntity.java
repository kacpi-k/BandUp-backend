package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategoryEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "instruments", schema = "public")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InstrumentEntity {

    @Id
    private String id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = true)
    private InstrumentCategoryEntity category;
}
