package dev.kkoncki.bandup.user.management.instrument.category;

import dev.kkoncki.bandup.user.management.instrument.InstrumentEntity;

public class InstrumentCategoryMapper {

    public static InstrumentCategory toDomain(InstrumentCategoryEntity entity) {
        return InstrumentCategory.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static InstrumentCategoryEntity toEntity(InstrumentCategory category) {
        return InstrumentCategoryEntity.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }
}
