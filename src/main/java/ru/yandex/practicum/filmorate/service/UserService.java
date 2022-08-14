package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}.", userStorage.getAllUsers().size());
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        checkUserId(id);
        return userStorage.getUserById(id);
    }

    public User createUser(User user) {
        if (userStorage.getAllUsers().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException(String.format("пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()));
        }
        checkUserLoginAndName(user);
        User newUser = userStorage.createUser(user);
        log.info("Пользователь {} зарегистрирован.", newUser);
        return newUser;
    }

    public User updateUser(User user) {
        checkUserId(user.getId());
        checkUserLoginAndName(user);
        User newUser = userStorage.updateUser(user);
        log.info("Информация по пользователю {} обновлена.", newUser);
        return newUser;
    }

    private void checkUserId(long id) {
        if (id <= 0 || userStorage.getAllUsers().stream().noneMatch(u -> u.getId() == id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
    }

    private void checkUserLoginAndName(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}