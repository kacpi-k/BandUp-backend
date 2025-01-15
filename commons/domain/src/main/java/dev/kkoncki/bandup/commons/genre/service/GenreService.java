package dev.kkoncki.bandup.commons.genre.service;

import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.forms.CreateGenreForm;
import jakarta.validation.Valid;

import java.util.List;

public interface GenreService {
    Genre get(String id);
    Genre save(@Valid CreateGenreForm form);
    List<Genre> getAll();
}
