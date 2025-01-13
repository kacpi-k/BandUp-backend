package dev.kkoncki.bandup.user.management.genre.service;

import dev.kkoncki.bandup.user.management.genre.Genre;
import dev.kkoncki.bandup.user.management.genre.forms.CreateGenreForm;
import jakarta.validation.Valid;

import java.util.List;

public interface GenreService {
    Genre get(String id);
    Genre save(@Valid CreateGenreForm form);
    List<Genre> getAll();
}
