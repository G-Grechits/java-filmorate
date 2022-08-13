package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmLikesServiceTest {
    private final FilmLikesService filmLikesService;
    private final FilmService filmService;
    private final UserService userService;
    private final MpaService mpaService;

    @Test
    void putLikeToFilm() {
        Film testFilm1 = new Film(0, "Служанка", "Корея под властью Японии, начало 20-ого века",
                LocalDate.of(2016, 6, 1), 167, mpaService.getMpaById(4), null);
        Film testFilm2 = new Film(0, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 188, mpaService.getMpaById(2), null);
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        Film newFilm1 = filmService.createFilm(testFilm1);
        Film newFilm2 = filmService.createFilm(testFilm2);
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);

        filmLikesService.putLikeToFilm(newFilm1.getId(), newUser1.getId());
        List<Film> popularFilms1 = (List<Film>) filmService.getPopularFilms(1);
        assertThat(popularFilms1.get(0)).isEqualTo(newFilm1);

        filmLikesService.putLikeToFilm(newFilm2.getId(), newUser1.getId());
        filmLikesService.putLikeToFilm(newFilm2.getId(), newUser2.getId());
        List<Film> popularFilms2 = (List<Film>) filmService.getPopularFilms(1);
        assertThat(popularFilms2.get(0)).isEqualTo(newFilm2);
    }

    @Test
    void removeLikeFromFilm() {
        Film testFilm1 = new Film(0, "Служанка", "Корея под властью Японии, начало 20-ого века",
                LocalDate.of(2016, 6, 1), 167, mpaService.getMpaById(4), null);
        Film testFilm2 = new Film(0, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 188, mpaService.getMpaById(2), null);
        User testUser1 = new User(0, "user1@mail.ru", "userName1", "userLogin1",
                LocalDate.of(1990, 8, 30));
        User testUser2 = new User(0, "user2@mail.ru", "userName2", "userLogin2",
                LocalDate.of(1997, 5, 19));
        Film newFilm1 = filmService.createFilm(testFilm1);
        Film newFilm2 = filmService.createFilm(testFilm2);
        User newUser1 = userService.createUser(testUser1);
        User newUser2 = userService.createUser(testUser2);

        filmLikesService.putLikeToFilm(newFilm1.getId(), newUser1.getId());
        filmLikesService.putLikeToFilm(newFilm2.getId(), newUser1.getId());
        filmLikesService.putLikeToFilm(newFilm2.getId(), newUser2.getId());
        List<Film> popularFilms1 = (List<Film>) filmService.getPopularFilms(1);
        assertThat(popularFilms1.get(0)).isEqualTo(newFilm2);

        filmLikesService.removeLikeFromFilm(newFilm2.getId(), newUser1.getId());
        filmLikesService.removeLikeFromFilm(newFilm2.getId(), newUser2.getId());
        List<Film> popularFilms2 = (List<Film>) filmService.getPopularFilms(1);
        assertThat(popularFilms2.get(0)).isEqualTo(newFilm1);

        assertThatThrownBy(() -> filmLikesService.removeLikeFromFilm(3, newUser1.getId()))
                .isInstanceOf(ObjectNotFoundException.class);
        assertThatThrownBy(() -> filmLikesService.removeLikeFromFilm(newFilm1.getId(), 3))
                .isInstanceOf(ObjectNotFoundException.class);
    }
}