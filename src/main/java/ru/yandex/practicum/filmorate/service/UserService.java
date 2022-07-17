package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private long autoId = 0L;

    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        log.info("Текущее количество пользователей: {}.", userStorage.getUsers().size());
        return userStorage.getUsers().values();
    }

    public User getUserById(long id) { /*в случае выбрасывания исключений текст в данном случае и ниже начинается
    с маленькой буквы не случайно: начальная часть текста ошибок передаётся при создании новых объектов типа
    ErrorResponse в методах обработки исключений класса ErrorHandler, а здесь передаётся уже пояснительная часть,
    следующая после двоеточия*/
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
        return userStorage.getUsers().get(id);
    }

    public User createUser(User user) {
        if (userStorage.getUsers().values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new ValidationException(String.format("пользователь с электронной почтой %s уже зарегистрирован.",
                    user.getEmail()));
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(++autoId);
        userStorage.addUser(user);
        log.info("Пользователь {} зарегистрирован.", user);
        return user;
    }

    public User updateUser(User user) {
        if (!userStorage.getUsers().containsKey(user.getId())) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", user.getId()));
        }
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("логин не может содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.info("Информация по пользователю {} обновлена.", user);
        return user;
    }

    public void addToFriends(long id, long friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", friendId));
        }
        userStorage.getUsers().get(id).getFriends().add(friendId);
        userStorage.getUsers().get(friendId).getFriends().add(id);
        log.info("Пользователь {} добавлен в друзья.", userStorage.getUsers().get(friendId));
    }

    public void removeFromFriends(long id, long friendId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
        if (!userStorage.getUsers().containsKey(friendId)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", friendId));
        }
        userStorage.getUsers().get(id).getFriends().remove(friendId);
        userStorage.getUsers().get(friendId).getFriends().remove(id);
        log.info("Пользователь {} удалён из друзей.", userStorage.getUsers().get(friendId));
    }

    public Collection<User> getAllFriends(long id) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
        log.info("Количество друзей пользователя {}: {}.", userStorage.getUsers().get(id),
                userStorage.getUsers().get(id).getFriends().size());
        return userStorage.getUsers().get(id).getFriends().stream()
                .map(friendId -> userStorage.getUsers().get(friendId))
                .collect(Collectors.toList());
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        if (!userStorage.getUsers().containsKey(id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
        if (!userStorage.getUsers().containsKey(otherId)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", otherId));
        }
        return userStorage.getUsers().get(id).getFriends().stream()
                .filter(friendId -> userStorage.getUsers().get(otherId).getFriends().contains(friendId))
                .map(friendId -> userStorage.getUsers().get(friendId))
                .collect(Collectors.toList());
    }
}