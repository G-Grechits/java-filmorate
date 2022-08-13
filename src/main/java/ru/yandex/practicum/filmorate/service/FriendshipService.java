package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FriendshipService {
    private final FriendshipStorage friendshipStorage;
    private final UserStorage userStorage;

    public FriendshipService(FriendshipStorage friendshipStorage, UserStorage userStorage) {
        this.friendshipStorage = friendshipStorage;
        this.userStorage = userStorage;
    }

    public Collection<User> getAllFriends(long id) {
        checkUserId(id);
        log.info("Количество друзей пользователя {}: {}.", userStorage.getUserById(id),
                friendshipStorage.getAllFriends(id).size());
        return friendshipStorage.getAllFriends(id);
    }

    public Collection<User> getCommonFriends(long id, long otherId) {
        checkUserId(id);
        checkUserId(otherId);
        Collection<User> userFriends = friendshipStorage.getAllFriends(id);
        Collection<User> otherUserFriends = friendshipStorage.getAllFriends(otherId);
        return userFriends.stream().filter(otherUserFriends::contains).collect(Collectors.toList());
    }

    public void addToFriends(long id, long friendId) {
        checkUserId(id);
        checkUserId(friendId);
        checkIdForMatch(id, friendId);
        friendshipStorage.addToFriends(id, friendId);
        log.info("Пользователь {} добавлен в друзья.", userStorage.getUserById(friendId));
    }

    public void removeFromFriends(long id, long friendId) {
        checkUserId(id);
        checkUserId(friendId);
        checkIdForMatch(id, friendId);
        friendshipStorage.removeFromFriends(id, friendId);
        log.info("Пользователь {} удалён из друзей.", userStorage.getUserById(friendId));
    }

    private void checkUserId(long id) {
        if (id <= 0 || userStorage.getAllUsers().stream().noneMatch(u -> u.getId() == id)) {
            throw new ObjectNotFoundException(String.format("пользователь с ID=%d не существует.", id));
        }
    }

    private void checkIdForMatch(long id, long otherId) {
        if (id == otherId) {
            throw new SameObjectException("пользователь не может добавить в друзья или удалить из друзей сам себя.");
        }
    }
}