package dev.kkoncki.bandup.user.management.genre.service;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.user.management.genre.Genre;
import dev.kkoncki.bandup.user.management.genre.forms.CreateGenreForm;
import dev.kkoncki.bandup.user.management.genre.repository.GenreRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    private Genre getByIdOrThrowGenre(String genreId) {
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.GENRE_NOT_FOUND));
    }

    @Override
    public Genre get(String genreId) {
        return getByIdOrThrowGenre(genreId);
    }

    @Override
    public Genre save(CreateGenreForm form) {
        Genre genre = Genre.builder()
                .name(form.getName())
                .build();
        return genreRepository.save(genre);
    }

    @Override
    public List<Genre> getAll() {
        return genreRepository.findAll();
    }
}
