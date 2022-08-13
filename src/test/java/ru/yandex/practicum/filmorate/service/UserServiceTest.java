package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserServiceTest {
    private final UserService userService;

    @Test
    void getAllUsers() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        userService.createUser(testUser1);
        userService.createUser(testUser2);

        assertThat(userService.getAllUsers().size()).isEqualTo(2);
    }

    @Test
    void getUserById() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);

        assertThat(userService.getUserById(1)).isEqualTo(newUser1);
        assertThat(userService.getUserById(2)).isEqualTo(newUser2);
        assertThatThrownBy(() -> userService.getUserById(-1)).isInstanceOf(ObjectNotFoundException.class);
        assertThatThrownBy(() -> userService.getUserById(3)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void createUser() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);
        List<User> users = (List<User>) userService.getAllUsers();

        assertThat(users.get(0)).isEqualTo(newUser1);
        assertThat(users.get(1)).isEqualTo(newUser2);
        assertThatThrownBy(() -> userService.createUser(newUser1)).isInstanceOf(ValidationException.class);
    }

    @Test
    void updateUser() {
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(1, "user1@mail.ru", "newUserName", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User userWithWrongLogin = new User(1, "user1@mail.ru", "user Name1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        userService.createUser(testUser1);

        assertThat(userService.updateUser(testUser2).getLogin()).isEqualTo("newUserName");
        assertThatThrownBy(() -> userService.updateUser(userWithWrongLogin)).isInstanceOf(ValidationException.class);
    }
}