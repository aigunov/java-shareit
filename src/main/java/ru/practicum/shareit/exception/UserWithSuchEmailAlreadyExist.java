package ru.practicum.shareit.exception;

public class UserWithSuchEmailAlreadyExist extends RuntimeException {
    public UserWithSuchEmailAlreadyExist(final String message) {
        super(message);
    }
}
