package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface FriendshipStorage {

    Collection<User> getAllFriends(long id);

    void addToFriends(long id, long friendId);

    void removeFromFriends(long id, long friendId);
}