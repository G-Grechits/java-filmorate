package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmLikesService;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmLikesService filmLikesService;

    public FilmController(FilmService filmService, FilmLikesService filmLikesService) {
        this.filmService = filmService;
        this.filmLikesService = filmLikesService;
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@Positive @PathVariable long id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return filmService.getPopularFilms(count);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLikeToFilm(@Positive @PathVariable long id, @Positive @PathVariable long userId) {
        filmLikesService.putLikeToFilm(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLikeFromFilm(@Positive @PathVariable long id, @Positive @PathVariable long userId) {
        filmLikesService.removeLikeFromFilm(id, userId);
    }
}