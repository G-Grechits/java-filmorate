package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;

@Component
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmLikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void putLikeToFilm(long filmId, long userId) {
        String sql = "INSERT INTO FILM_LIKES (FILM_ID, LIKE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sql, filmId, userId);
    }

    @Override
    public void removeLikeFromFilm(long filmId, long userId) {
        String sql = "DELETE FROM FILM_LIKES WHERE FILM_ID = ? AND LIKE_ID = ?";
        jdbcTemplate.update(sql, filmId, userId);
    }
}