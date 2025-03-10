package dev.kkoncki.bandup.commons.genre;

import dev.kkoncki.bandup.commons.genre.forms.CreateGenreForm;
import dev.kkoncki.bandup.commons.genre.service.GenreService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genre")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable("id") String id) {
        return genreService.get(id);
    }

    @PostMapping("/save")
    public Genre save(@RequestBody CreateGenreForm form) {
        return genreService.save(form);
    }

    @GetMapping("/all")
    public List<Genre> getAll() {
        return genreService.getAll();
    }
}
