package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.SameObjectException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FriendshipServiceTest {
    private final FriendshipService friendshipService;
    private final UserService userService;

    @Test
    void getAllFriends() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);
        friendshipService.addToFriends(newUser1.getId(), newUser2.getId());

        assertThat(friendshipService.getAllFriends(newUser1.getId()).size()).isEqualTo(1);
    }

    @Test
    void getCommonFriends() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User testUser3 = new User(0, "user3@mail.ru", "userName3", "userLogin3",
                LocalDate.of(1992, 10, 12));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);
        User newUser3 = userService.createUser(testUser3);
        friendshipService.addToFriends(newUser1.getId(), newUser2.getId());
        friendshipService.addToFriends(newUser1.getId(), newUser3.getId());
        friendshipService.addToFriends(newUser2.getId(), newUser1.getId());
        friendshipService.addToFriends(newUser2.getId(), newUser3.getId());

        assertThat(friendshipService.getCommonFriends(newUser1.getId(), newUser2.getId()).size()).isEqualTo(1);
        assertThat(friendshipService.getCommonFriends(newUser1.getId(), newUser3.getId()).size()).isEqualTo(0);
    }

    @Test
    void addToFriends() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);

        assertThatThrownBy(() -> friendshipService.addToFriends(3, newUser2.getId()))
                .isInstanceOf(ObjectNotFoundException.class);
        assertThatThrownBy(() -> friendshipService.addToFriends(newUser1.getId(), 3))
                .isInstanceOf(ObjectNotFoundException.class);
        assertThatThrownBy(() -> friendshipService.addToFriends(newUser1.getId(), newUser1.getId()))
                .isInstanceOf(SameObjectException.class);

    }

    @Test
    void removeFromFriends() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User testUser3 = new User(0, "user3@mail.ru", "userName3", "userLogin3",
                LocalDate.of(1992, 10, 12));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);
        User newUser3 = userService.createUser(testUser3);

        friendshipService.addToFriends(newUser1.getId(), newUser2.getId());
        friendshipService.addToFriends(newUser1.getId(), newUser3.getId());
        assertThat(friendshipService.getAllFriends(newUser1.getId()).size()).isEqualTo(2);

        friendshipService.removeFromFriends(newUser1.getId(), newUser3.getId());
        assertThat(friendshipService.getAllFriends(newUser1.getId()).size()).isEqualTo(1);
    }
}