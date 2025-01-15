package dev.kkoncki.bandup.commons.genre.repository;

import dev.kkoncki.bandup.commons.genre.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(String id);
    Genre save(Genre genre);
    List<Genre> findAll();
}
