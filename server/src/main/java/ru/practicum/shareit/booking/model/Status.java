package ru.practicum.shareit.booking.model;

import java.util.Optional;

public enum Status {
    WAITING, // новое бронирование, ожидает одобрения
    APPROVED, // бронирование подтверждено владельцем
    CURRENT, // текущие бронирования
    REJECTED, // отклонена бронь владельцем вещи
    ALL, // все аренды
    PAST, // завершенные бронирования
    FUTURE; // будущие бронирования

    public static Optional<Status> from(String stringState) {
        for (Status state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return java.util.Optional.of(state);
            }
        }
        return java.util.Optional.empty();
    }

}
