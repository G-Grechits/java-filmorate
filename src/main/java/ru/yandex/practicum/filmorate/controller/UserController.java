package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FriendshipService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FriendshipService friendshipService;

    public UserController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping
    public Collection<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@Positive @PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getAllFriends(@Positive @PathVariable long id) {
        return friendshipService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@Positive @PathVariable long id, @Positive @PathVariable long otherId) {
        return friendshipService.getCommonFriends(id, otherId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addToFriends(@Positive @PathVariable long id, @Positive @PathVariable long friendId) {
        friendshipService.addToFriends(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFromFriends(@Positive @PathVariable long id, @Positive @PathVariable long friendId) {
        friendshipService.removeFromFriends(id, friendId);
    }
}