package dev.kkoncki.bandup.user.management.genre;

import dev.kkoncki.bandup.commons.ApplicationException;
import dev.kkoncki.bandup.commons.ErrorCode;
import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.forms.CreateGenreForm;
import dev.kkoncki.bandup.commons.genre.repository.GenreRepository;
import dev.kkoncki.bandup.commons.genre.service.GenreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenreServiceTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    private CreateGenreForm createGenreForm;
    private Genre genre;

    @BeforeEach
    void setUp() {
        createGenreForm = new CreateGenreForm("Rock");

        genre = Genre.builder()
                .id("genre-123")
                .name("Rock")
                .build();
    }

    @Test
    void shouldSaveGenreSuccessfully() {
        when(genreRepository.save(any(Genre.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Genre savedGenre = genreService.save(createGenreForm);

        assertNotNull(savedGenre);
        assertEquals("Rock", savedGenre.getName());

        verify(genreRepository, times(1)).save(any(Genre.class));
    }

    @Test
    void shouldGetGenreSuccessfully() {
        when(genreRepository.findById("genre-123")).thenReturn(Optional.of(genre));

        Genre fetchedGenre = genreService.get("genre-123");

        assertNotNull(fetchedGenre);
        assertEquals("genre-123", fetchedGenre.getId());
        assertEquals("Rock", fetchedGenre.getName());

        verify(genreRepository, times(1)).findById("genre-123");
    }

    @Test
    void shouldThrowExceptionWhenGenreNotFound() {
        when(genreRepository.findById("invalid-id")).thenReturn(Optional.empty());

        ApplicationException exception = assertThrows(ApplicationException.class,
                () -> genreService.get("invalid-id"));

        assertEquals(ErrorCode.GENRE_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void shouldReturnAllGenres() {
        List<Genre> genres = List.of(
                new Genre("genre-123", "Rock"),
                new Genre("genre-456", "Jazz"),
                new Genre("genre-789", "Pop")
        );

        when(genreRepository.findAll()).thenReturn(genres);

        List<Genre> result = genreService.getAll();

        assertEquals(3, result.size());
        assertEquals("Rock", result.get(0).getName());
        assertEquals("Jazz", result.get(1).getName());
        assertEquals("Pop", result.get(2).getName());

        verify(genreRepository, times(1)).findAll();
    }
}

