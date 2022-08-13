package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmServiceTest {
    private final FilmService filmService;
    private final MpaService mpaService;

    @Test
    void getAllFilms() {
        Film testFilm1 = new Film(0, "Служанка", "Корея под властью Японии, начало 20-ого века",
                LocalDate.of(2016, 6, 1), 167, mpaService.getMpaById(4), null);
        Film testFilm2 = new Film(0, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 188, mpaService.getMpaById(2), null);
        filmService.createFilm(testFilm1);
        filmService.createFilm(testFilm2);

        assertThat(filmService.getAllFilms().size()).isEqualTo(2);
    }

    @Test
    void getFilmById() {
        Film testFilm1 = new Film(0, "Служанка", "Корея под властью Японии, начало 20-ого века",
                LocalDate.of(2016, 6, 1), 167, mpaService.getMpaById(4), null);
        Film testFilm2 = new Film(0, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 188, mpaService.getMpaById(2), null);
        Film newFilm1 = filmService.createFilm(testFilm1);
        Film newFilm2 = filmService.createFilm(testFilm2);

        assertThat(filmService.getFilmById(1)).isEqualTo(newFilm1);
        assertThat(filmService.getFilmById(2)).isEqualTo(newFilm2);
        assertThatThrownBy(() -> filmService.getFilmById(-1)).isInstanceOf(ObjectNotFoundException.class);
        assertThatThrownBy(() -> filmService.getFilmById(3)).isInstanceOf(ObjectNotFoundException.class);
    }

    @Test
    void createFilm() {
        Film testFilm = new Film(0, "Служанка", "Корея под властью Японии, начало 20-ого века",
                LocalDate.of(2016, 6, 1), 167, mpaService.getMpaById(4), null);
        Film fakeFilm = new Film(0, "Служанка", "История о невероятных совпадениях",
                LocalDate.of(2016, 6, 1), 188, mpaService.getMpaById(2), null);
        Film newFilm = filmService.createFilm(testFilm);

        assertThat(filmService.getFilmById(1)).isEqualTo(newFilm);
        assertThatThrownBy(() -> filmService.createFilm(fakeFilm)).isInstanceOf(ValidationException.class);
    }

    @Test
    void updateFilm() {
        Film testFilm1 = new Film(0, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 10, mpaService.getMpaById(2), null);
        Film testFilm2 = new Film(1, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1999, 12, 8), 188, mpaService.getMpaById(2), null);
        Film filmWithWrongDate = new Film(1, "Магнолия", "История о невероятных совпадениях",
                LocalDate.of(1800, 12, 8), 188, mpaService.getMpaById(2), null);
        filmService.createFilm(testFilm1);

        assertThat(filmService.updateFilm(testFilm2).getDuration()).isEqualTo(188);
        assertThatThrownBy(() -> filmService.updateFilm(filmWithWrongDate)).isInstanceOf(ValidationException.class);
    }
}