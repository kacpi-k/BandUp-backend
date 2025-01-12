package dev.kkoncki.bandup.user.management.genre.service;

import dev.kkoncki.bandup.user.management.genre.Genre;
import dev.kkoncki.bandup.user.management.genre.forms.CreateGenreForm;
import dev.kkoncki.bandup.user.management.genre.repository.GenreRepository;

public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Genre save(CreateGenreForm form) {
        Genre genre = Genre.builder()
                .name(form.getName())
                .build();
        return genreRepository.save(genre);
    }
}
