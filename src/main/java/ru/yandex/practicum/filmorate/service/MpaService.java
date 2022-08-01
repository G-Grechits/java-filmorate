package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.Collection;

@Slf4j
@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Collection<Mpa> getAllMpa() {
        log.info("Список всех рейтингов MPA получен.");
        return mpaStorage.getAllMpa();
    }

    public Mpa getMpaById(int id) {
        if (id <= 0 || id > 5) {
            throw new ObjectNotFoundException(String.format("ID рейтинга MPA не может равняться %d.", id));
        }
        log.info("Рейтинг MPA {} получен.", mpaStorage.getMpaById(id));
        return mpaStorage.getMpaById(id);
    }
}