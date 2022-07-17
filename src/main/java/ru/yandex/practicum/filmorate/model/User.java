package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @Email(message = "Указан некорректный адрес электронной почты.")
    @NotNull(message = "Не указан адрес электронной почты.")
    private String email;
    @NotBlank(message = "Логин не может быть пустым.")
    private String login;
    private String name;
    @Past(message = "Дата рождения не может быть указана в будущем времени.")
    @NotNull(message = "Не указана дата рождения пользователя.")
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();
}