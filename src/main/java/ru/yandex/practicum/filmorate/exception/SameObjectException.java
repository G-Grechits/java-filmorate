package ru.yandex.practicum.filmorate.exception;

public class SameObjectException extends RuntimeException {
    public SameObjectException(String message) {
        super(message);
    }
}