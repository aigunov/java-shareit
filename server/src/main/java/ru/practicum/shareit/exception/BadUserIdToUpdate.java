package ru.practicum.shareit.exception;

public class BadUserIdToUpdate extends RuntimeException {
    public BadUserIdToUpdate(String message) {
        super(message);
    }
}
