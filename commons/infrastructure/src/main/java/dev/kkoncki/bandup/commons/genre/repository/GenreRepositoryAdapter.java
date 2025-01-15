package dev.kkoncki.bandup.commons.genre.repository;

import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.GenreEntity;
import dev.kkoncki.bandup.commons.genre.GenreMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class GenreRepositoryAdapter implements GenreRepository {
    private final JpaGenreRepository jpaGenreRepository;

    public GenreRepositoryAdapter(JpaGenreRepository jpaGenreRepository) {
        this.jpaGenreRepository = jpaGenreRepository;
    }

    @Override
    public Optional<Genre> findById(String id) {
        return jpaGenreRepository.findById(id).map(GenreMapper::toDomain);
    }

    @Override
    public Genre save(Genre genre) {
        GenreEntity entity = GenreMapper.toEntity(genre);
        return GenreMapper.toDomain(jpaGenreRepository.save(entity));
    }

    @Override
    public List<Genre> findAll() {
        return jpaGenreRepository.findAll().stream().map(GenreMapper::toDomain).toList();
    }
}
