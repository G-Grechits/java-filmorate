package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final LocalDate birthdayOfCinema = LocalDate.of(1895, Month.DECEMBER, 28);
    private long autoId = 0L;

    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    /*в случае выбрасывания исключений текст в данном случае и ниже начинается с маленькой буквы не случайно: начальная
    часть текста ошибок передаётся при создании новых объектов типа ErrorResponse в методах обработки исключений класса
    ErrorHandler, а здесь передаётся уже пояснительная часть, следующая после двоеточия*/
    private void checkFilmId (long id) {
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("фильм с ID=%d не существует.", id));
        }
    }

    private void checkUserId (long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
    }

    private void checkFilmReleaseDate (Film film) {
        if (film.getReleaseDate().isBefore(birthdayOfCinema)) {
            throw new ValidationException("дата релиза фильма не может быть раньше 28 декабря 1895 года.");
        }
    }

    public Collection<Film> getAllFilms() {
        log.info("Текущее количество фильмов: {}.", filmStorage.getFilms().size());
        return filmStorage.getFilms().values();
    }

    public Film getFilmById(long id) {
        checkFilmId(id);
        return filmStorage.getFilms().get(id);
    }

    public Film createFilm(Film film) {
        if (filmStorage.getFilms().values().stream().anyMatch(f -> f.getName().equals(film.getName())
                && f.getReleaseDate().equals(film.getReleaseDate()))) {
            throw new ValidationException("фильм с названием «" + film.getName() + "» и датой релиза "
                    + film.getReleaseDate() + " уже существует.");
        }
        checkFilmReleaseDate(film);
        film.setId(++autoId);
        filmStorage.addFilm(film);
        log.info("Фильм {} сохранён.", film);
        return film;
    }

    public Film updateFilm(Film film) {
        checkFilmId(film.getId());
        checkFilmReleaseDate(film);
        filmStorage.addFilm(film);
        log.info("Информация по фильму {} обновлена.", film);
        return film;
    }

    public void putLikeToFilm(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.getFilms().get(id).getLikes().add(userId);
        log.info("Пользователь {} поставил лайк фильму {}.", userStorage.getUsers().get(userId),
                filmStorage.getFilms().get(id));
    }

    public void removeLikeFromFilm(long id, long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmStorage.getFilms().get(id).getLikes().remove(userId);
        log.info("Пользователь {} убрал свой лайк у фильма {}.", userStorage.getUsers().get(userId),
                filmStorage.getFilms().get(id));
    }

    public Collection<Film> getPopularFilms(int count) {
        return filmStorage.getFilms().values().stream()
                .sorted((f0, f1) -> f1.getLikes().size() - f0.getLikes().size())
                .limit(count).collect(Collectors.toList());
    }
}