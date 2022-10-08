package ru.practicum.shareit.exceptions;

public class ValidationException extends IllegalStateException {
    public ValidationException(final String message) {
        super(message);
    }
}
