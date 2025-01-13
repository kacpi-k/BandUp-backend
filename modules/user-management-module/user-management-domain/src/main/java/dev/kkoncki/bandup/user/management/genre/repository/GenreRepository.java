package dev.kkoncki.bandup.user.management.genre.repository;

import dev.kkoncki.bandup.user.management.genre.Genre;

import java.util.List;
import java.util.Optional;

public interface GenreRepository {
    Optional<Genre> findById(String genreId);
    Genre save(Genre genre);
    List<Genre> findAll();
}
