package dev.kkoncki.bandup.user.management.genre.service;

import dev.kkoncki.bandup.user.management.genre.Genre;
import dev.kkoncki.bandup.user.management.genre.forms.CreateGenreForm;
import jakarta.validation.Valid;

public interface GenreService {
    Genre save(@Valid CreateGenreForm form);
}
