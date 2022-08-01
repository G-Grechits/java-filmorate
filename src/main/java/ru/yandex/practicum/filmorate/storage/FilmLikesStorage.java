package ru.yandex.practicum.filmorate.storage;

public interface FilmLikesStorage {

    void putLikeToFilm(long filmId, long userId);

    void removeLikeFromFilm(long filmId, long userId);
}