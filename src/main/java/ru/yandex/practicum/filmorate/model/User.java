package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class User {
    private int id;
    @Email(message = "Указан некорректный адрес электронной почты.")
    @NotNull(message = "Не указан адрес электронной почты.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть указана в будущем времени.")
    @NotNull(message = "Не указана дата рождения пользователя.")
    private LocalDate birthday;
}