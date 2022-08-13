package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

@Slf4j
@Service
public class FilmLikesService {
    private final FilmLikesStorage filmLikesStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public FilmLikesService(FilmLikesStorage filmLikesStorage, FilmStorage filmStorage, UserStorage userStorage) {
        this.filmLikesStorage = filmLikesStorage;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void putLikeToFilm(long filmId, long userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        filmLikesStorage.putLikeToFilm(filmId, userId);
        log.info("Пользователь {} поставил лайк фильму {}.", userStorage.getUserById(userId),
                filmStorage.getFilmById(filmId));
    }

    public void removeLikeFromFilm(long filmId, long userId) {
        checkFilmId(filmId);
        checkUserId(userId);
        filmLikesStorage.removeLikeFromFilm(filmId, userId);
        log.info("Пользователь {} убрал свой лайк у фильма {}.", userStorage.getUserById(userId),
                filmStorage.getFilmById(filmId));
    }

    private void checkFilmId(long id) {
        if (id <= 0 || filmStorage.getAllFilms().stream().noneMatch(f -> f.getId() == id)) {
            throw new ObjectNotFoundException(String.format("фильм с ID=%d не существует.", id));
        }
    }

    private void checkUserId(long id) {
        if (id <= 0 || userStorage.getAllUsers().stream().noneMatch(u -> u.getId() == id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
    }
}