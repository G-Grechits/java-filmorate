package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);

    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}.", filmStorage.getAllFilms().size());
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        checkFilmId(id);
        return filmStorage.getFilmById(id);
    }

    public Film createFilm(Film film) {
        if (filmStorage.getAllFilms().stream().anyMatch(f -> f.getName().equals(film.getName())
                && f.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new ValidationException("фильм с названием «" + film.getName() + "» и датой релиза "
                    + film.getReleaseDate() + " уже существует.");
        }
        checkFilmReleaseDate(film);
        Film newFilm = filmStorage.createFilm(film);
        log.info("Фильм {} сохранён.", newFilm);
        return newFilm;
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        checkFilmReleaseDate(film);
        Film newFilm = filmStorage.updateFilm(film);
        log.info("Информация по фильму {} обновлена.", newFilm);
        return newFilm;
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getPopularFilms(count);
    }

    private void checkFilmId(long id) {
        if (id <= 0 || filmStorage.getAllFilms().stream().noneMatch(f -> f.getId() == id)) {
            throw new ObjectNotFoundException(String.format("фильм с ID=%d не существует.", id));
        }
    }

    private void checkFilmReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(birthdayOfCinema)) {
            throw new ValidationException("дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
    }
}