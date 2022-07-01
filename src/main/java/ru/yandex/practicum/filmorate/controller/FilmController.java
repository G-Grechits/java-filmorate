package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private int autoId = 0;

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}", films.size());
        return films.values();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        if (films.values().stream().anyMatch(f -> f.getName().equals(film.getName())
                && f.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new ValidationException("Фильм с названием " + film.getName() + " и датой релиза "
                    + film.getReleaseDate() + " уже существует.");
        }
        if (film.getReleaseDate().isBefore(birthdayOfCinema)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        film.setId(++autoId);
        films.put(film.getId(), film);
        log.info("Фильм {} сохранён.", film);
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException(String.format("Фильм с ID=%d не существует.", film.getId()));
        }
        if (film.getReleaseDate().isBefore(birthdayOfCinema)) {
            throw new ValidationException("Дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
        films.put(film.getId(), film);
        log.info("Информация по фильму {} обновлена.", film);
        return film;
    }
}