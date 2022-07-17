package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
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
    private Set<Long> likes = new HashSet<>();
}