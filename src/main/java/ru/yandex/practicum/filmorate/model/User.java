package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
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
}