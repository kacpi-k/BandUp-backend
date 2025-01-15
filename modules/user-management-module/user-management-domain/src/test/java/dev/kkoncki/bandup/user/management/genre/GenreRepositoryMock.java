package dev.kkoncki.bandup.user.management.genre;

import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.repository.GenreRepository;

import java.util.*;

public class GenreRepositoryMock implements GenreRepository {

    Map<String, Genre> mockDB = new HashMap<>();

    @Override
    public Optional<Genre> findById(String id) {
        return Optional.ofNullable(mockDB.get(id));
    }

    @Override
    public Genre save(Genre genre) {
        mockDB.put(genre.getId(), genre);
        return genre;
    }

    @Override
    public List<Genre> findAll() {
        return new ArrayList<>(mockDB.values());
    }
}
