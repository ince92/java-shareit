package ru.practicum.shareit.exceptions;

public class EmailException extends IllegalStateException {
    public EmailException(final String message) {
        super(message);
    }
}
