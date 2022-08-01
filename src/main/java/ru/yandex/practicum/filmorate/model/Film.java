package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Film {
    private long id;
    @NotBlank(message = "Название не может быть пустым.")
    private String name;
    @Size(max = 200, message = "Максимальная длина описания фильма не может превышать 200 символов.")
    @NotNull(message = "Не указано описание фильма.")
    private String description;
    @NotNull(message = "Не указана дата релиза фильма.")
    private LocalDate releaseDate;
    @Positive(message = "Продолжительность фильма не может быть отрицательным числом или равняться 0.")
    @NotNull(message = "Не указана продолжительность фильма.")
    private int duration;
    @NotNull(message = "Не указан возрастной рейтинг фильма.")
    private Mpa mpa;
    private Set<Genre> genres;
    private Set<Long> likes;
}