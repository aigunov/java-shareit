package ru.practicum.shareit.exception;

public class ChangeBookingStatusTwice extends RuntimeException {
    public ChangeBookingStatusTwice(String message) {
        super(message);
    }
}
