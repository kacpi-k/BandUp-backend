package dev.kkoncki.bandup.user.management.genre;

public class GenreMapper {

    public static Genre toDomain(GenreEntity entity) {
        return Genre.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }

    public static GenreEntity toEntity(Genre genre) {
        return GenreEntity.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
