package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@Slf4j
@Service
public class GenreService {
    private final GenreStorage genreStorage;

    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        log.info("Список всех жанров получен.");
        return genreStorage.getAllGenres();
    }

    public Genre getGenreById(int id) {
        if (id <= 0 || id > 6) {
            throw new ObjectNotFoundException(String.format("ID жанра не может равняться %d.", id));
        }
        log.info("Жанр {} получен.", genreStorage.getGenreById(id));
        return genreStorage.getGenreById(id);
    }
}