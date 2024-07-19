package ru.practicum.shareit.exception;

public class NoSuchItemException extends RuntimeException {
    public NoSuchItemException(final String message) {
        super(message);
    }
}
