package dev.kkoncki.bandup.band;

import dev.kkoncki.bandup.commons.genre.GenreEntity;

import java.util.List;
import java.util.stream.Collectors;

public class BandMapper {

    public static Band toDomain(BandEntity entity) {
        return Band.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .createdOn(entity.getCreatedOn())
                .genres(entity.getGenres().stream().map(GenreEntity::getId).collect(Collectors.toList()))
                .build();
    }

    public static BandEntity toEntity(Band band, List<GenreEntity> genres) {
        return BandEntity.builder()
                .id(band.getId())
                .name(band.getName())
                .description(band.getDescription())
                .createdOn(band.getCreatedOn())
                .genres(genres)
                .build();
    }
}
