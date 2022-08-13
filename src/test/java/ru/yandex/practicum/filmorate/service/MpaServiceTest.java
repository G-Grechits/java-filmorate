package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaServiceTest {
    private final MpaService mpaService;

    @Test
    void getAllMpa() {
        assertThat(mpaService.getAllMpa().size()).isEqualTo(5);
    }

    @Test
    void getMpaById() {
        Mpa mpaG = mpaService.getMpaById(1);
        Mpa mpaR = mpaService.getMpaById(4);

        assertThat(mpaG.getName()).isEqualTo("G");
        assertThat(mpaR.getName()).isEqualTo("R");
        assertThatThrownBy(() -> mpaService.getMpaById(6)).isInstanceOf(ObjectNotFoundException.class);
    }
}