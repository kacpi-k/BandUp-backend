package dev.kkoncki.bandup.user.management.genre;

import dev.kkoncki.bandup.commons.genre.Genre;
import dev.kkoncki.bandup.commons.genre.forms.CreateGenreForm;
import dev.kkoncki.bandup.commons.genre.repository.GenreRepository;
import dev.kkoncki.bandup.commons.genre.service.GenreService;
import dev.kkoncki.bandup.commons.genre.service.GenreServiceImpl;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GenreServiceTest {

    GenreRepository genreRepository = new GenreRepositoryMock();

    GenreService genreService = new GenreServiceImpl(genreRepository);

    private final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = validatorFactory.getValidator();

    private <T> void genericViolationSet(T form) {
        Set<ConstraintViolation<T>> violations = validator.validate(form);

        assertThrows(ConstraintViolationException.class, () -> {
            if (!violations.isEmpty()) {
                throw new ConstraintViolationException("Validation failed", violations);
            }
        });
    }

    @Test
    void getTest() {
        CreateGenreForm form = new CreateGenreForm("rock");
        Genre genre = genreService.save(form);

        Genre genreFound = genreService.get(genre.getId());

        assertEquals(genre.getId(), genreFound.getId());
        assertEquals(genre.getName(), genreFound.getName());
    }

    @Test
    void saveTest() {
        CreateGenreForm form = new CreateGenreForm("rock");

        Genre genre = genreService.save(form);

        assertNotNull(genre.getId());
        assertEquals("rock", genre.getName());
    }

    @Test
    void getAllTest() {
        CreateGenreForm form1 = new CreateGenreForm("rock");
        CreateGenreForm form2 = new CreateGenreForm("pop");

        Genre genre1 = genreService.save(form1);
        Genre genre2 = genreService.save(form2);

        List<Genre> genres = genreService.getAll();

        assertTrue(genres.contains(genre1));
        assertTrue(genres.contains(genre2));
        assertEquals(2, genres.size());
    }

    @Test
    void validateCreateGenreFormTest() {
        CreateGenreForm form = new CreateGenreForm("r");

        genericViolationSet(form);
    }
}
