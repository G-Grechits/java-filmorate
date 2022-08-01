package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreServiceTest {
    private final GenreService genreService;

    @Test
    void getAllGenres() {
        assertThat(genreService.getAllGenres().size()).isEqualTo(6);
    }

    @Test
    void getGenreById() {
        Genre genreDrama = genreService.getGenreById(2);
        Genre genreThriller = genreService.getGenreById(4);

        assertThat(genreDrama.getName()).isEqualTo("Драма");
        assertThat(genreThriller.getName()).isEqualTo("Триллер");
        assertThatThrownBy(() -> genreService.getGenreById(7)).isInstanceOf(ObjectNotFoundException.class);
    }
}