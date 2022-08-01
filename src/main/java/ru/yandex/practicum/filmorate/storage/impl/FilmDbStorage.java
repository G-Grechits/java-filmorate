package ru.yandex.practicum.filmorate.storage.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreStorage;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreDbStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                "mpa.NAME AS MPA_NAME " +
                "FROM FILMS AS f " +
                "JOIN MPA_RATINGS AS mpa ON f.MPA_ID = mpa.MPA_ID";
        Collection<Film> films = jdbcTemplate.query(sql, this::mapRowToObject);
        for (Film film : films) {
            if (film != null) {
                film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
            }
        }
        return films;
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, f.DURATION, f.MPA_ID, " +
                "mpa.NAME AS MPA_NAME " +
                "FROM FILMS AS f " +
                "JOIN MPA_RATINGS AS mpa ON f.MPA_ID = mpa.MPA_ID " +
                "WHERE f.FILM_ID = ?";
        Film film = jdbcTemplate.queryForObject(sql, this::mapRowToObject, id);
        if (film != null) {
            film.setGenres(genreStorage.getGenresByFilmId(film.getId()));
        }
        return film;
    }

    @Override
    public Film createFilm(Film film) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("FILMS")
                .usingGeneratedKeyColumns("FILM_ID");
        Map<String, Object> filmData = new HashMap<>();
        filmData.put("NAME", film.getName());
        filmData.put("DESCRIPTION", film.getDescription());
        filmData.put("RELEASE_DATE", film.getReleaseDate());
        filmData.put("DURATION", film.getDuration());
        filmData.put("MPA_ID", film.getMpa().getId());
        film.setId(jdbcInsert.executeAndReturnKey(filmData).longValue());
        addFilmGenreInDb(film);
        return getFilmById(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        String sql = "UPDATE FILMS SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, MPA_ID = ? " +
                "WHERE FILM_ID = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getId());
        updateFilmGenreInDb(film);
        return getFilmById(film.getId());
    }

    @Override
    public Collection<Film> getPopularFilms(int count) {
        String sqlQuery = "SELECT f.film_id " +
                "FROM FILMS AS f " +
                "LEFT JOIN FILM_LIKES AS fl on f.FILM_ID = fl.FILM_ID " +
                "GROUP BY f.film_id " +
                "ORDER BY COUNT(fl.LIKE_ID) DESC " +
                "LIMIT ?";
        Collection<Long> popularFilmIds = jdbcTemplate.queryForList(sqlQuery, Long.class, count);
        return popularFilmIds.stream().map(this::getFilmById).collect(Collectors.toList());
    }

    private Film mapRowToObject(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("FILM_ID"))
                .name(rs.getString("NAME"))
                .description(rs.getString("DESCRIPTION"))
                .releaseDate(rs.getDate("RELEASE_DATE").toLocalDate())
                .duration(rs.getInt("DURATION"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("MPA_ID"))
                        .name(rs.getString("MPA_NAME"))
                        .build())
                .build();
    }

    private void updateFilmGenreInDb(Film film) {
        jdbcTemplate.update("DELETE FROM FILM_GENRES WHERE FILM_ID = ?", film.getId());
        addFilmGenreInDb(film);
    }

    private void addFilmGenreInDb(Film film) {
        String sql = "INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (?, ?)";
        if (film.getGenres() == null) {
            return;
        }
        for (Genre genre : film.getGenres()) {
            jdbcTemplate.update(sql, film.getId(), genre.getId());
        }
    }
}