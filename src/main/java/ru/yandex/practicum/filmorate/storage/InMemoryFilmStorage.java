package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public Map<Long, Film> getFilms() {
        return films;
    }

    @Override
    public void addFilm(Film film) {
        films.put(film.getId(), film);
    }
}