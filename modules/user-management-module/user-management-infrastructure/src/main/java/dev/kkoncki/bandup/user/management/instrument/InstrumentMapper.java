package dev.kkoncki.bandup.user.management.instrument;

import dev.kkoncki.bandup.user.management.instrument.category.InstrumentCategoryEntity;

public class InstrumentMapper {

    public static Instrument toDomain(InstrumentEntity entity) {
        return Instrument.builder()
                .id(entity.getId())
                .name(entity.getName())
                .categoryId(entity.getCategory() != null ? entity.getCategory().getId() : null)
                .build();
    }

    public static InstrumentEntity toEntity(Instrument instrument) {
        return InstrumentEntity.builder()
                .id(instrument.getId())
                .name(instrument.getName())
                .category(instrument.getCategoryId() != null ? InstrumentCategoryEntity.builder().id(instrument.getCategoryId()).build() : null)
                .build();
    }
}
