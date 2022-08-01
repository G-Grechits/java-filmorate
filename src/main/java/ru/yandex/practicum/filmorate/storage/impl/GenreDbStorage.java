package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Collection<Genre> getAllGenres() {
        return jdbcTemplate.query("SELECT * FROM GENRES ORDER BY GENRE_ID", this::mapRowToObject);
    }

    @Override
    public Genre getGenreById(int id) {
        return jdbcTemplate.queryForObject("SELECT * FROM GENRES WHERE GENRE_ID = ?", this::mapRowToObject, id);
    }

    @Override
    public Set<Genre> getGenresByFilmId(long id) {
        Collection<Integer> genreIds = jdbcTemplate.queryForList("SELECT GENRE_ID FROM FILM_GENRES WHERE FILM_ID = ?",
                Integer.class, id);
        return genreIds.stream().map(this::getGenreById).collect(Collectors.toSet());
    }

    private Genre mapRowToObject(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("GENRE_ID"))
                .name(rs.getString("NAME"))
                .build();
    }
}
